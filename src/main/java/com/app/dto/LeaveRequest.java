package com.app.dto;

import java.time.LocalDate;

import com.app.enums.DayOfWeek;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LeaveRequest {


     @NotNull
    private Long doctorId;

    @NotNull
    private LocalDate date;

    private String reason;
    
}