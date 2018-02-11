package com.babjo.whatdaybot.bot;

import lombok.Value;

@Value
public class Command {
    private String from;
    private String text;
}
