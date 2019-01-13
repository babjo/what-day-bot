package com.babjo.whatdaybot.handler.command

import com.babjo.whatdaybot.Room
import com.babjo.whatdaybot.repository.RoomRepository
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage

class TurnOffPushMessages(
    private val event: MessageEvent<MessageContent>,
    private var roomRepository: RoomRepository
) : Command {
    override fun execute(): Message {
        roomRepository.save(Room(event.source.senderId, false))
        return TextMessage("OK! STOP!")
    }
}