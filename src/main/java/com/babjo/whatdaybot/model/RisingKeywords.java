package com.babjo.whatdaybot.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class RisingKeywords {
    private final LocalDateTime time;
    private final List<RisingKeyword> keywords;
}
