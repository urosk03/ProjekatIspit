package com.example.coworkinghub.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class RoomRequest {
    @NotBlank public String name;
    public String description;
    @Min(1)  public int capacity;
    @NotBlank public String workingHours; // "08:00-20:00"
}
