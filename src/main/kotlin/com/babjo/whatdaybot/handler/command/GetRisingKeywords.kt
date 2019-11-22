package com.babjo.whatdaybot.handler.command

import com.babjo.whatdaybot.crawler.RisingKeywordCrawler
import com.linecorp.bot.model.message.TextMessage

class GetRisingKeywords(private val crawler: RisingKeywordCrawler) : Command {
    override fun execute() =
        crawler.let {
            TextMessage("인기검색어 ${it.latestRefreshTime}\n" +
                    (0..minOf(10, it.latestRisingKeywords.size - 1)).joinToString("\n") { i ->
                        "${i + 1}. ${it.latestRisingKeywords[i].text}: ${it.latestRisingKeywords[i].url}"
                    }
            )
        }
}
