package com.babjo.whatdaybot.repository;

import com.babjo.whatdaybot.model.RisingKeywords;

public interface RisingKeywordRepository {
    RisingKeywords findLatestRisingKeywords();
}
