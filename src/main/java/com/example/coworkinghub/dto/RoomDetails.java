package com.example.coworkinghub.dto;

import com.example.coworkinghub.model.Room;
import com.example.coworkinghub.model.Reservation;
import java.util.List;

public class RoomDetails {
    public Room room;
    public List<Reservation> reservations;
    public RoomDetails(Room room, List<Reservation> reservations){
        this.room = room;
        this.reservations = reservations;
    }
}
