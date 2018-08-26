package com.babjo.whatdaybot.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HealthCheckController {
    @GetMapping("/health")
    fun `do`(): String = "OK"
}