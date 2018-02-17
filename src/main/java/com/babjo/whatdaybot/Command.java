package com.babjo.whatdaybot;

import java.util.function.Function;

import lombok.Value;

@Value
public class Command {
    private final String name;
    private final String description;
    private final Function<String, String> function;
}
