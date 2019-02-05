package com.babjo.whatdaybot

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    @GetMapping("/health")
    fun `do`() = "OK"
}