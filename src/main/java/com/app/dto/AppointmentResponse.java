package com.app.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AppointmentResponse {

    private Long id;
    private String doctorName;
    private Integer queueNumber;
    private String status;
    private LocalDate appointmentDate;
}