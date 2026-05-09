package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DoctorQueueStatsResponse {

    private long waiting;

    private long inProgress;

    private long completed;

    private long totalToday;
}