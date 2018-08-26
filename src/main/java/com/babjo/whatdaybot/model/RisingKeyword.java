package com.babjo.whatdaybot.model;

import com.google.common.base.Objects;

public class RisingKeyword {
    private final String text;
    private final String url;

    public RisingKeyword(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        RisingKeyword that = (RisingKeyword) o;
        return Objects.equal(text, that.text) &&
               Objects.equal(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text, url);
    }
}
