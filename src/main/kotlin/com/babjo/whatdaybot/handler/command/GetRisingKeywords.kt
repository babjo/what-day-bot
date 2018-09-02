package com.babjo.whatdaybot.handler.command

import com.babjo.whatdaybot.crawler.PeriodicRisingKeywordCrawler
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage

class GetRisingKeywords(private val periodicRisingKeywordCrawler: PeriodicRisingKeywordCrawler) : Command {
    override fun execute(): Message {
        val keywords = periodicRisingKeywordCrawler.latestRisingKeywords
        val time = periodicRisingKeywordCrawler.latestRefreshTime

        return TextMessage("인기검색어 $time\n" + (0..10)
                .joinToString("\n") { i -> "${i + 1}. ${keywords!![i].text}: ${keywords[i].url}" })
    }
}