package com.babjo.whatdaybot

import com.linecorp.bot.client.LineMessagingClient
import com.linecorp.bot.model.PushMessage
import com.linecorp.bot.model.message.TextMessage
import com.linecorp.bot.model.response.BotApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@RestController
class HealthCheckController {
    @GetMapping("/health")
    fun `do`() = "OK"
}

@RestController
class BotController(@Autowired private val client: LineMessagingClient) {

    @PostMapping("/push")
    fun push(@RequestBody req: PushRequest): Mono<PushResponse> =
        client
            .pushMessage(PushMessage(req.to, TextMessage(req.text)))
            .toMono()
            .map { PushResponse(it) }

}

data class PushRequest(val to: String?, val text: String?)
data class PushResponse(val botApiResponse: BotApiResponse)
