package com.example.coworkinghub.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ReservationRequest {
    @NotNull public Long roomId;
    @NotNull public Long userId;
    @NotNull public LocalDateTime startTime;
    @NotNull public LocalDateTime endTime;
}
