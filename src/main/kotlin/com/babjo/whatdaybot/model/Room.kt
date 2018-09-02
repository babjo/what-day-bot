package com.babjo.whatdaybot.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Room(@Id var id: String, var botRunning: Boolean)