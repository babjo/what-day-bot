package com.babjo.whatdaybot.handler.command

import com.linecorp.bot.model.message.VideoMessage
import java.net.URI

class GetMondaySong : Command {
    override fun execute() =
        VideoMessage(
            URI("https://vt.media.tumblr.com/tumblr_p4nukupwiG1x76q2h.mp4"),
            URI("https://78.media.tumblr.com/3677c5e4ba151cc86d19999e0d2e8855/tumblr_p4nuzyVKwJ1x76q2ho1_r1_1280.png")
        )
}
