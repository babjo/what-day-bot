package com.babjo.whatdaybot.repository;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class RisingKeywords {
    private final LocalDateTime time;
    private final List<String> keywords;
}
