package com.babjo.whatdaybot.crawler

import com.babjo.whatdaybot.RisingKeyword
import com.babjo.whatdaybot.naver.openapi.URLShortenerClient
import org.slf4j.LoggerFactory
import java.io.IOException
import java.time.Clock
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class PeriodicRisingKeywordCrawler(
    private val clock: Clock,
    private val executorService: ScheduledExecutorService,
    private val risingKeywordCrawler: RisingKeywordCrawler,
    private val urlShortenerClient: URLShortenerClient
) {

    private val logger = LoggerFactory.getLogger(PeriodicRisingKeywordCrawler::class.java)
    lateinit var latestRefreshTime: LocalDateTime
    lateinit var latestRisingKeywords: List<RisingKeyword>

    fun start() {
        executorService.scheduleAtFixedRate(this::refresh, 0, 60, TimeUnit.SECONDS)
    }

    private fun refresh() {
        try {
            CompletableFuture
                .runAsync(this::doRefresh)
                .get(30, TimeUnit.SECONDS)
        } catch (e: Exception) {
            logger.error("Failed to refresh", e)
        }
    }

    private fun doRefresh() {
        try {
            targetDate().also {
                latestRisingKeywords = it
                    .let(risingKeywordCrawler::doCrawling)
                    .map(this::shortenUrl)
                latestRefreshTime = it
            }
        } catch (e: IOException) {
            logger.error("Failed to refresh", e)
        }
    }

    private fun targetDate() =
        LocalDateTime
            .now(clock)
            .let {
                var sec = it.second
                sec %= 30
                it.plusSeconds((-sec).toLong())
            }

    private fun shortenUrl(keyword: RisingKeyword) =
        RisingKeyword(keyword.text, urlShortenerClient.shorten(keyword.url))
}
