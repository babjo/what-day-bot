package com.babjo.whatdaybot.repository

import com.babjo.whatdaybot.model.Room
import org.springframework.data.jpa.repository.JpaRepository

interface RoomRepository : JpaRepository<Room, String>
