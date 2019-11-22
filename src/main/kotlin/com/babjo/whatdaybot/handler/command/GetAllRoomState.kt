package com.babjo.whatdaybot.handler.command

import com.babjo.whatdaybot.repository.RoomRepository
import com.linecorp.bot.model.message.TextMessage

class GetAllRoomState(private val roomRepository: RoomRepository) : Command {
    override fun execute() =
        roomRepository
            .findAll()
            .let {
                TextMessage(it.joinToString { r -> "RoomId: ${r.id}, isRunning: ${r.botRunning}" })
            }
}
