package com.app.dto.patient.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientSearchResponse {

    private Long patientId;

    private String fullName;

    private String mobile;

    private boolean hasHistory;

    private String patientType;
}