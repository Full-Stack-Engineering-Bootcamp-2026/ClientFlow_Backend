package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class VisitHistoryResponse {

    private Long consultationId;

    private LocalDate appointmentDate;

    private String diagnosis;

    private String doctorName;

    private String status;
}