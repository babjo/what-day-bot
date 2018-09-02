package com.babjo.whatdaybot.crawler

import com.babjo.whatdaybot.model.RisingKeyword
import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RisingKeywordCrawler {

    private val logger = LoggerFactory.getLogger(RisingKeywordCrawler::class.java)

    @Throws(IOException::class)
    fun doCrawling(dateTime: LocalDateTime): List<RisingKeyword> {
        val doc = Jsoup.connect("https://datalab.naver.com/keyword/realtimeList.naver?datetime=" + dateTime
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))).get()
        val element = doc.select(".select_date")[0]
        val keywords = element.select(".title")

        return keywords
                .map { keyword -> keyword.text() }
                .map { text -> RisingKeyword(text, "https://search.naver.com/search.naver?query=${encodeURIComponent(text)}") }
    }

    private fun encodeURIComponent(s: String): String {
        return try {
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
}