package com.babjo.whatdaybot.handler.command

import com.babjo.whatdaybot.model.Room
import com.babjo.whatdaybot.repository.RoomRepository
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.MessageContent
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage

class TurnOnPushMessages(private var event: MessageEvent<MessageContent>,
                         private var roomRepository: RoomRepository) : Command {
    override fun execute(): Message {
        roomRepository.save(Room(event.source.senderId, true))
        return TextMessage("OK! START!")
    }
//
//    class Factory(val roomRepository: RoomRepository) : CommandFactory {
//        override fun create(event: MessageEvent<TextMessageContent>): Command = TurnOnPushMessages(event, roomRepository)
//    }
}