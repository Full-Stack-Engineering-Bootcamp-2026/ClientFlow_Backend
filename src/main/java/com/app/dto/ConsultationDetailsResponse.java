package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConsultationDetailsResponse {

    private Long id;

    private String diagnosis;

    private String clinicalNotes;
}