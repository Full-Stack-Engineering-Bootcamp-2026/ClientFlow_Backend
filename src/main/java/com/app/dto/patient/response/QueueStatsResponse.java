package com.app.dto.patient.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueStatsResponse {

    private Long totalWaitingPatients;

    private Long activeDoctors;

    private Long urgentCases;

    private Long averageWaitTime;
}