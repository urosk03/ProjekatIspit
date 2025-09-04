package com.example.coworkinghub.controller;

import com.example.coworkinghub.dto.RoomRequest;
import com.example.coworkinghub.dto.RoomDetails;
import com.example.coworkinghub.model.Room;
import com.example.coworkinghub.model.Reservation;
import com.example.coworkinghub.repository.ReservationRepository;
import com.example.coworkinghub.service.ReservationService;
import com.example.coworkinghub.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    public RoomController(RoomService roomService, ReservationService reservationService,
                          ReservationRepository reservationRepository) {
        this.roomService = roomService;
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    // Listanje + pretraga + sortiranje (capacity/popularity)
    @GetMapping
    public List<Room> list(@RequestParam(required = false) String q,
                           @RequestParam(required = false, defaultValue = "capacity") String sort) {
        List<Room> rooms = roomService.search(q);
        Map<Long, Long> pop = reservationService.popularity();
        return roomService.sortBy(rooms, sort, pop);
    }

    // Detalji sa "kalendarom" (listom rezervacija) po ID-u
    @GetMapping("/{id}")
    public RoomDetails detailsById(@PathVariable Long id) {
        Room room = roomService.search(null).stream()
                .filter(r -> r.getId().equals(id))
                .findFirst().orElseThrow();
        List<Reservation> reservations = reservationRepository.findByRoomId(id);
        return new RoomDetails(room, reservations);
    }

    // Detalji po nazivu
    @GetMapping("/by-name/{name}")
    public RoomDetails detailsByName(@PathVariable String name) {
        Room room = roomService.search(name).stream()
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst().orElseThrow();
        List<Reservation> reservations = reservationRepository.findByRoomId(room.getId());
        return new RoomDetails(room, reservations);
    }

    // Manager CRUD
    @PostMapping
    public Room create(@Valid @RequestBody RoomRequest req) {
        Room r = new Room();
        r.setName(req.name);
        r.setDescription(req.description);
        r.setCapacity(req.capacity);
        r.setWorkingHours(req.workingHours);
        return roomService.create(r);
    }

    @PutMapping("/{id}")
    public Room update(@PathVariable Long id, @Valid @RequestBody RoomRequest req) {
        Room r = new Room();
        r.setName(req.name);
        r.setDescription(req.description);
        r.setCapacity(req.capacity);
        r.setWorkingHours(req.workingHours);
        return roomService.update(id, r);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roomService.delete(id);
    }
}
