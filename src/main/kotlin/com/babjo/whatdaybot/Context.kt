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
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.Arrays.asList
import java.util.concurrent.Executors

@Configuration
class Context {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    fun dataSourceProperties() = DataSourceProperties()


    @Bean
    fun dataSource(properties: DataSourceProperties) =
        properties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()!!


    @Bean
    fun lineMessageScheduler(
        clock: Clock, roomRepository: RoomRepository,
        client: LineMessagingClient
    ) = LineMessageScheduler(clock, Random(), client, roomRepository)


    @Bean
    fun commandFactory(
        roomRepository: RoomRepository, clock: Clock,
        periodicRisingKeywordCrawler: PeriodicRisingKeywordCrawler,
        holidays: List<Holiday>
    ) = CommandFactory()
        .addCreationRule(TextPatternRule("RoomState") { GetAllRoomState(roomRepository) })
        .addCreationRule(TextPatternRule("모레 무슨 요일\\?") { GetDayAfterTomorrow(clock) })
        .addCreationRule(TextPatternRule("(미워|미웡)") { ReturnSimpleText("미워하지마") })
        .addCreationRule(TextPatternRule("월요송") { GetMondaySong() })

        .addCreationRule(TextPatternRule("핫해") { GetRisingKeywords(periodicRisingKeywordCrawler) })
        .addCreationRule(TextPatternRule("(월급좀|월급\\?)") { GetNextSalaryDate(clock, holidays) })

        .addCreationRule(TextPatternRule("start") { TurnOnPushMessages(it, roomRepository) })
        .addCreationRule(TextPatternRule("stop") { TurnOffPushMessages(it, roomRepository) })

        .addCreationRule(TextPatternRule("(오늘|금일) 무슨 요일\\?") { GetToday(clock) })
        .addCreationRule(TextPatternRule("내일 무슨 요일\\?") { GetTomorrow(clock) })
        .addCreationRule(TextPatternRule("어제 무슨 요일\\?") { GetYesterday(clock) })


    @Bean
    fun clock() = Clock.system(ZoneId.of("UTC+09:00"))

    @Bean
    @ConfigurationProperties(prefix = "naver.openapi")
    fun urlShortenerClient() = URLShortenerClient()

    @Bean
    fun periodicRisingKeywordCrawler(
        clock: Clock,
        urlShortenerClient: URLShortenerClient
    ) = PeriodicRisingKeywordCrawler(
        clock,
        Executors.newScheduledThreadPool(1),
        RisingKeywordCrawler(),
        urlShortenerClient
    ).also {
        it.start()
    }

    @Bean
    fun holidays() = asList(
        Holiday("추석(연휴)", LocalDate.of(2018, 9, 23)),
        Holiday("추석(연휴)", LocalDate.of(2018, 9, 24)),
        Holiday("추석(연휴)", LocalDate.of(2018, 9, 25)),
        Holiday("추석(연휴)", LocalDate.of(2018, 9, 26)),
        Holiday("개천절", LocalDate.of(2018, 10, 3)),
        Holiday("한글날", LocalDate.of(2018, 10, 9)),
        Holiday("크리스마스", LocalDate.of(2018, 12, 25)),
        Holiday("신정", LocalDate.of(2019, 1, 1)),
        Holiday("설날", LocalDate.of(2019, 2, 4)),
        Holiday("설날", LocalDate.of(2019, 2, 5)),
        Holiday("설날", LocalDate.of(2019, 2, 6)),
        Holiday("삼일절", LocalDate.of(2019, 3, 1)),
        Holiday("어린이날", LocalDate.of(2019, 5, 5)),
        Holiday("대체공휴일", LocalDate.of(2019, 5, 6)),
        Holiday("부처님오신날", LocalDate.of(2019, 5, 12)),
        Holiday("현충일", LocalDate.of(2019, 6, 6)),
        Holiday("광복절", LocalDate.of(2019, 8, 15)),
        Holiday("추석", LocalDate.of(2019, 9, 12)),
        Holiday("추석", LocalDate.of(2019, 9, 13)),
        Holiday("추석", LocalDate.of(2019, 9, 14)),
        Holiday("개천절", LocalDate.of(2019, 10, 3)),
        Holiday("한글날", LocalDate.of(2019, 10, 9)),
        Holiday("크리스마스", LocalDate.of(2019, 12, 25))
    )!!
}