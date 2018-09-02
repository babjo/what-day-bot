package com.babjo.whatdaybot

import com.babjo.whatdaybot.crawler.PeriodicRisingKeywordCrawler
import com.babjo.whatdaybot.crawler.RisingKeywordCrawler
import com.babjo.whatdaybot.handler.command.*
import com.babjo.whatdaybot.handler.command.factory.CommandFactory
import com.babjo.whatdaybot.handler.command.factory.TextPatternRule
import com.babjo.whatdaybot.naver.openapi.URLShortenerClient
import com.babjo.whatdaybot.repository.RoomRepository
import com.linecorp.bot.client.LineMessagingClient
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.time.Clock
import java.time.ZoneId
import java.util.*
import java.util.concurrent.Executors
import javax.sql.DataSource

@Configuration
class Context {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun dataSource(properties: DataSourceProperties): DataSource {
        return properties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
    }

    @Bean
    fun lineMessageScheduler(clock: Clock, roomRepository: RoomRepository,
                             client: LineMessagingClient): LineMessageScheduler {
        return LineMessageScheduler(clock, Random(), client, roomRepository)
    }

    @Bean
    fun commandFactory(roomRepository: RoomRepository, clock: Clock, periodicRisingKeywordCrawler: PeriodicRisingKeywordCrawler): CommandFactory {
        val factory = CommandFactory()

        factory.addCreationRule(TextPatternRule("RoomState") { GetAllRoomState(roomRepository) })
        factory.addCreationRule(TextPatternRule("모레 무슨 요일\\?") { GetDayAfterTomorrow(clock) })
        factory.addCreationRule(TextPatternRule("(미워|미웡)") { ReturnSimpleText("미워하지마") })
        factory.addCreationRule(TextPatternRule("월요송") { GetMondaySong() })

        factory.addCreationRule(TextPatternRule("핫해") { GetRisingKeywords(periodicRisingKeywordCrawler) })
        factory.addCreationRule(TextPatternRule("(월급좀|월급\\?)") { GetNextSalaryDate(clock) })

        factory.addCreationRule(TextPatternRule("start") { TurnOnPushMessages(it, roomRepository) })
        factory.addCreationRule(TextPatternRule("stop") { TurnOffPushMessages(it, roomRepository) })

        factory.addCreationRule(TextPatternRule("(오늘|금일) 무슨 요일\\?") { GetToday(clock) })
        factory.addCreationRule(TextPatternRule("내일 무슨 요일\\?") { GetTomorrow(clock) })
        factory.addCreationRule(TextPatternRule("어제 무슨 요일\\?") { GetYesterday(clock) })
        return factory
    }

    @Bean
    fun clock(): Clock {
        return Clock.system(ZoneId.of("UTC+09:00"))
    }

    @Bean
    @ConfigurationProperties(prefix = "naver.openapi")
    fun urlShortenerClient(): URLShortenerClient {
        return URLShortenerClient()
    }

    @Bean
    fun periodicRisingKeywordCrawler(clock: Clock,
                                     urlShortenerClient: URLShortenerClient): PeriodicRisingKeywordCrawler {
        val crawler = PeriodicRisingKeywordCrawler(clock, Executors.newScheduledThreadPool(1), RisingKeywordCrawler(), urlShortenerClient)
        crawler.start()
        return crawler
    }
}