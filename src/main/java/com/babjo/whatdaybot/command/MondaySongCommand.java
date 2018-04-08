package com.babjo.whatdaybot.command;

import java.util.regex.Pattern;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.VideoMessage;

public class MondaySongCommand implements Command {

    private final Pattern pattern = Pattern.compile("월요송");

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public Message execute(MessageEvent<MessageContent> event) {
        return new VideoMessage("https://vt.media.tumblr.com/tumblr_p4nukupwiG1x76q2h.mp4",
                                "https://78.media.tumblr.com/3677c5e4ba151cc86d19999e0d2e8855/tumblr_p4nuzyVKwJ1x76q2ho1_r1_1280.png");
    }
}
