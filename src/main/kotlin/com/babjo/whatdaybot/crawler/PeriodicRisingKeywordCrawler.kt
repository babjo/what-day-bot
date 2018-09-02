package com.babjo.whatdaybot.crawler

import com.babjo.whatdaybot.model.RisingKeyword
import com.babjo.whatdaybot.naver.openapi.URLShortenerClient
import org.slf4j.LoggerFactory
import java.io.IOException
import java.time.Clock
import java.time.LocalDateTime
import java.util.concurrent.*

class PeriodicRisingKeywordCrawler(private val clock: Clock,
                                   private val executorService: ScheduledExecutorService,
                                   private val risingKeywordCrawler: RisingKeywordCrawler,
                                   private val urlShortenerClient: URLShortenerClient) {

    private val logger = LoggerFactory.getLogger(PeriodicRisingKeywordCrawler::class.java)
    var latestRefreshTime: LocalDateTime? = null
    var latestRisingKeywords: List<RisingKeyword>? = null

    fun start() {
        executorService.scheduleAtFixedRate({ this.refresh() }, 0, 60, TimeUnit.SECONDS)
    }

    private fun refresh() {
        try {
            CompletableFuture.runAsync {
                try {
                    var dateTime = LocalDateTime.now(clock)
                    var sec = dateTime.second
                    sec %= 30
                    dateTime = dateTime.plusSeconds((-sec).toLong())

                    val keywords = risingKeywordCrawler.doCrawling(dateTime)

                    latestRefreshTime = dateTime
                    latestRisingKeywords = keywords.map { keyword ->
                        RisingKeyword(keyword.text, urlShortenerClient.shorten(keyword.url))
                    }
                } catch (e: IOException) {
                    logger.error("Fail to refresh", e)
                }
            }.get(30, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            logger.error("Fail to refresh", e)
        } catch (e: ExecutionException) {
            logger.error("Fail to refresh", e)
        } catch (e: TimeoutException) {
            logger.error("Fail to refresh", e)
        }

    }
}