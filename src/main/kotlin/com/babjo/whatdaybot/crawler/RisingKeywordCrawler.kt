package com.babjo.whatdaybot.crawler

import com.babjo.whatdaybot.RisingKeyword
import com.babjo.whatdaybot.naver.openapi.URLShortenerClient
import io.reactivex.Single
import mu.KotlinLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.time.Clock
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class RisingKeywordCrawler(
    private val clock: Clock,
    private val timeoutSeconds: Long,
    private val urlShortenerClient: URLShortenerClient
) {

    private val logger = KotlinLogging.logger {}
    lateinit var latestRefreshTime: LocalDateTime
    lateinit var latestRisingKeywords: List<RisingKeyword>

    fun refresh() {
        val target = getTargetTime(LocalDateTime.now(clock))

        doCrawling(target)
            .flatMap { Single.merge(it.map(::shortenUrl)).toList() }
            .timeout(timeoutSeconds, TimeUnit.SECONDS)
            .subscribe { keywords, t ->
                if (t != null) {
                    logger.error("Failed to refresh", t)
                } else {
                    latestRisingKeywords = keywords
                    latestRefreshTime = target
                }
            }
    }

    private fun getTargetTime(now: LocalDateTime) =
        now.let {
            var sec = it.second
            sec %= 30
            it.plusSeconds((-sec).toLong())
        }

    private fun shortenUrl(keyword: RisingKeyword): Single<RisingKeyword> =
        urlShortenerClient
            .shorten(keyword.url)
            .map { RisingKeyword(keyword.text, it.result.url) }
            .onErrorReturn { keyword }

    private fun doCrawling(dateTime: LocalDateTime): Single<List<RisingKeyword>> =
        Single.fromCallable {
            Jsoup
                .connect(
                    "https://datalab.naver.com/keyword/realtimeList.naver?datetime=" +
                            dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                )
                .get()
                .select(".select_date")[0]
                .select(".title")
                .map(Element::text)
                .map {
                    RisingKeyword(
                        it,
                        "https://search.naver.com/search.naver?query=${encodeURIComponent(it)}"
                    )
                }
        }

    private fun encodeURIComponent(s: String) = try {
        URLEncoder.encode(s, "UTF-8")
            .replace("\\+".toRegex(), "%20")
            .replace("\\%21".toRegex(), "!")
            .replace("\\%27".toRegex(), "'")
            .replace("\\%28".toRegex(), "(")
            .replace("\\%29".toRegex(), ")")
            .replace("\\%7E".toRegex(), "~")
    } catch (e: UnsupportedEncodingException) {
        s
    }
}
