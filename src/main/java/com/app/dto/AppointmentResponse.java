package com.app.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private Long appointmentId;

    private String patientName;

    private String doctorName;

    private Integer queueNumber;

    private String queueLabel;

    private String status;

    private LocalDate appointmentDate;
}