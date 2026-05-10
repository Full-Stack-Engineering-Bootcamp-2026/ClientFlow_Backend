package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PatientHistoryPatientResponse {

    private Long id;

    private String fullName;

    private String gender;

    private Integer age;

    private String bloodGroup;
}