package com.babjo.whatdaybot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babjo.whatdaybot.Room;

public interface RoomRepository extends JpaRepository<Room, String> {
}
