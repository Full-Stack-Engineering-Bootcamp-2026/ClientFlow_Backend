package com.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PatientHistoryResponse {

    private PatientHistoryPatientResponse patient;

    private List<VisitHistoryResponse> visitHistory;
}