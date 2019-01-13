package com.babjo.whatdaybot.crawler

import com.babjo.whatdaybot.RisingKeyword
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RisingKeywordCrawler {

    @Throws(IOException::class)
    fun doCrawling(dateTime: LocalDateTime) = Jsoup
        .connect(
            "https://datalab.naver.com/keyword/realtimeList.naver?datetime=" +
                    dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        )
        .get()
        .select(".select_date")[0]
        .select(".title")
        .map(Element::text)
        .map { RisingKeyword(it, "https://search.naver.com/search.naver?query=${encodeURIComponent(it)}") }


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