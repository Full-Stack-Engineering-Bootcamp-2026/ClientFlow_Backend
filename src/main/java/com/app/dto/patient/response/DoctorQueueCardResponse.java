package com.app.dto.patient.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorQueueCardResponse {

    private Long doctorId;

    private String doctorName;

    private String specialization;

    private String queueState;

    private QueuePatientResponse servingNow;

    private QueuePatientResponse nextUp;

    private QueuePatientResponse lastServed;

    private Integer waitingCount;

    private List<QueuePatientResponse> waitingPatients;
}