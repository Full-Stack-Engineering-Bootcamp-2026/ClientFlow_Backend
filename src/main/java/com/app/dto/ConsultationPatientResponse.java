package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConsultationPatientResponse {

    private Long id;

    private String name;

    private Integer age;

    private String gender;
}