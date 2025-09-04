package com.example.coworkinghub.controller;

import com.example.coworkinghub.dto.ReservationRequest;
import com.example.coworkinghub.model.Reservation;
import com.example.coworkinghub.model.ReservationStatus;
import com.example.coworkinghub.repository.ReservationRepository;
import com.example.coworkinghub.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationRepository resRepo;

    public ReservationController(ReservationService reservationService,
                                 ReservationRepository resRepo) {
        this.reservationService = reservationService;
        this.resRepo = resRepo;
    }

    @PostMapping
    public Reservation create(@Valid @RequestBody ReservationRequest req) {
        return reservationService.create(req.roomId, req.userId, req.startTime, req.endTime);
    }

    @GetMapping
    public List<Reservation> list(@RequestParam(required = false) ReservationStatus status,
                                  @RequestParam(required = false) Long roomId) {
        List<Reservation> base = status == null ? resRepo.findAll() : resRepo.findByStatus(status);
        if (roomId == null) return base;
        return base.stream().filter(r -> r.getRoom().getId().equals(roomId)).toList();
    }

    @PutMapping("/{id}/approve")
    public Reservation approve(@PathVariable Long id) {
        return reservationService.approve(id);
    }

    @PutMapping("/{id}/reject")
    public Reservation reject(@PathVariable Long id, @RequestParam String reason) {
        return reservationService.reject(id, reason);
    }
}
