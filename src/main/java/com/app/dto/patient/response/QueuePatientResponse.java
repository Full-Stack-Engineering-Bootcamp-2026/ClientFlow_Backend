package com.app.dto.patient.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueuePatientResponse {

    private Long appointmentId;

    private Integer queueNumber;

    private String patientName;

    private String mobile;

    private String status;
}