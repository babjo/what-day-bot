package com.babjo.whatdaybot;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Room {
    @Id
    private String id;
    private boolean botRunning;

    public Room(String id, boolean botRunning) {
        this.id = id;
        this.botRunning = botRunning;
    }
}
