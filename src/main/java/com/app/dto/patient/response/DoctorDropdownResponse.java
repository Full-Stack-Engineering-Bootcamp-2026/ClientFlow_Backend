package com.app.dto.patient.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDropdownResponse {

    private Long doctorId;

    private String fullName;

    private String specialization;
}