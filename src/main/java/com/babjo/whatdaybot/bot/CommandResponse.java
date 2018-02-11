package com.babjo.whatdaybot.bot;

import lombok.Value;

@Value
public class CommandResponse {
    public static final CommandResponse NULL_RESPONSE = new CommandResponse(null);
    private final String text;
}
