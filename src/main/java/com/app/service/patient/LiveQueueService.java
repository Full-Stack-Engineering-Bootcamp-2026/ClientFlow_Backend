package com.app.service.patient;

import com.app.dto.patient.response.LiveQueueDashboardResponse;

public interface LiveQueueService {

    LiveQueueDashboardResponse getLiveQueue();
}