package com.babjo.whatdaybot.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Room {
    @Id
    private String id;
    private boolean botRunning;

    public Room() {
    }

    public Room(String id, boolean botRunning) {
        this.id = id;
        this.botRunning = botRunning;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBotRunning() {
        return botRunning;
    }

    public void setBotRunning(boolean botRunning) {
        this.botRunning = botRunning;
    }
}
