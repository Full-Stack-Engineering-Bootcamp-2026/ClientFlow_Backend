package com.app.dto.patient.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentBookingResponse {

    private Long appointmentId;

    private String patientName;

    private String doctorName;

    private LocalDate appointmentDate;

    private Integer queueNumber;

    private String queueLabel;

    private String status;
}