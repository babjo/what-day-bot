package com.babjo.whatdaybot.handler.command

import com.babjo.whatdaybot.repository.RoomRepository
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage

class GetAllRoomState(private val roomRepository: RoomRepository) : Command {
    override fun execute(): Message {
        val rooms = roomRepository.findAll()
        return TextMessage(rooms.joinToString { r -> "RoomId: ${r.id}, isRunning: ${r.botRunning}" })
    }
//
//    class Factory(val roomRepository: RoomRepository) : CommandFactory {
//        override fun create(event: MessageEvent<TextMessageContent>): Command = GetAllRoomState(roomRepository)
//    }
}