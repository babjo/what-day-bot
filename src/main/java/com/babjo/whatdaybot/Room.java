package com.babjo.whatdaybot;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class Room {
    @Id
    private String id;
    private boolean botRunning;
}
