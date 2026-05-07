package com.app.dto.patient.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LiveQueueDashboardResponse {

    private QueueStatsResponse stats;

    private List<DoctorQueueCardResponse> doctorQueues;
}