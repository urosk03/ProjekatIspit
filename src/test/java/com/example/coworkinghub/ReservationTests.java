package com.example.coworkinghub;

import com.example.coworkinghub.model.*;
import com.example.coworkinghub.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReservationTests {

    @Autowired RoomRepository roomRepo;
    @Autowired UserRepository userRepo;
    @Autowired ReservationRepository resRepo;
    @Autowired PasswordEncoder encoder;

    @Test
    void testOverlapRejected() {
        Room room = new Room();
        room.setName("Test");
        room.setCapacity(5);
        room.setWorkingHours("08:00-20:00");
        roomRepo.save(room);

        User u = new User();
        u.setUsername("u1");
        u.setPassword(encoder.encode("p"));
        u.setRole(Role.MEMBER);
        userRepo.save(u);

        Reservation a = new Reservation();
        a.setRoom(room); a.setUser(u);
        a.setStartTime(LocalDateTime.of(2025,1,1,10,0));
        a.setEndTime(LocalDateTime.of(2025,1,1,12,0));
        a.setStatus(ReservationStatus.APPROVED);
        resRepo.save(a);

        boolean overlaps = resRepo.overlaps(room,
                LocalDateTime.of(2025,1,1,11,0),
                LocalDateTime.of(2025,1,1,13,0));
        assertTrue(overlaps, "Treba da prepozna preklapanje");
    }

    @Test
    void testApproveBlocksNext() {
        Room room = new Room();
        room.setName("X");
        room.setCapacity(3);
        room.setWorkingHours("08:00-20:00");
        roomRepo.save(room);

        User u = new User();
        u.setUsername("u2");
        u.setPassword(encoder.encode("p"));
        u.setRole(Role.MEMBER);
        userRepo.save(u);

        Reservation p = new Reservation();
        p.setRoom(room); p.setUser(u);
        p.setStartTime(LocalDateTime.of(2025,1,1,14,0));
        p.setEndTime(LocalDateTime.of(2025,1,1,16,0));
        p.setStatus(ReservationStatus.APPROVED);
        resRepo.save(p);

        boolean overlaps = resRepo.overlaps(room,
                LocalDateTime.of(2025,1,1,15,0),
                LocalDateTime.of(2025,1,1,17,0));
        assertTrue(overlaps);
    }
}
