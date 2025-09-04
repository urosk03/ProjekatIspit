package com.example.coworkinghub.service;

import com.example.coworkinghub.model.*;
import com.example.coworkinghub.repository.ReservationRepository;
import com.example.coworkinghub.repository.RoomRepository;
import com.example.coworkinghub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReservationService {
    private final ReservationRepository resRepo;
    private final RoomRepository roomRepo;
    private final UserRepository userRepo;

    private static final Duration MAX_DURATION = Duration.ofHours(4);
    private static final Duration BUFFER = Duration.ofMinutes(15);

    public ReservationService(ReservationRepository resRepo, RoomRepository roomRepo, UserRepository userRepo) {
        this.resRepo = resRepo;
        this.roomRepo = roomRepo;
        this.userRepo = userRepo;
    }

    public Reservation create(Long roomId, Long userId, LocalDateTime start, LocalDateTime end) {
        Room room = roomRepo.findById(roomId).orElseThrow();
        User user = userRepo.findById(userId).orElseThrow();

        if (Duration.between(start, end).compareTo(MAX_DURATION) > 0) {
            throw new IllegalArgumentException("Trajanje ne sme preći 4 sata.");
        }

        ensureWithinWorkingHours(room.getWorkingHours(), start, end);

        LocalDateTime s = start.minus(BUFFER);
        LocalDateTime e = end.plus(BUFFER);

        if (resRepo.overlaps(room, s, e)) {
            throw new IllegalStateException("Termin se preklapa sa postojećim.");
        }

        Reservation r = new Reservation();
        r.setRoom(room);
        r.setUser(user);
        r.setStartTime(start);
        r.setEndTime(end);
        r.setStatus(ReservationStatus.PENDING);
        return resRepo.save(r);
    }

    public Reservation approve(Long id) {
        Reservation r = resRepo.findById(id).orElseThrow();
        if (resRepo.overlaps(r.getRoom(), r.getStartTime().minus(BUFFER), r.getEndTime().plus(BUFFER))) {
            throw new IllegalStateException("Preklapanje pri odobravanju.");
        }
        r.setStatus(ReservationStatus.APPROVED);
        return resRepo.save(r);
    }

    public Reservation reject(Long id, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Razlog odbijanja je obavezan.");
        }
        Reservation r = resRepo.findById(id).orElseThrow();
        r.setStatus(ReservationStatus.REJECTED);
        r.setRejectReason(reason);
        return resRepo.save(r);
    }

    public Map<Long, Long> popularity() {
        Map<Long, Long> map = new HashMap<>();
        for (Object[] row : resRepo.popularityByRoom()) {
            Long roomId = (Long) row[0];
            Long count  = (Long) row[1];
            map.put(roomId, count);
        }
        return map;
    }

    private void ensureWithinWorkingHours(String workingHours, LocalDateTime start, LocalDateTime end) {
        String[] parts = workingHours.split("-");
        LocalTime open  = LocalTime.parse(parts[0]);
        LocalTime close = LocalTime.parse(parts[1]);
        if (start.toLocalTime().isBefore(open) || end.toLocalTime().isAfter(close)) {
            throw new IllegalArgumentException("Termin mora biti unutar radnog vremena sale.");
        }
    }
}
