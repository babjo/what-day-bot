package com.babjo.whatdaybot

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.Id

data class Holiday(val name: String, val date: LocalDate)
data class RisingKeyword(val text: String, val url: String)

@Entity
data class Room(@Id var id: String, var botRunning: Boolean)
