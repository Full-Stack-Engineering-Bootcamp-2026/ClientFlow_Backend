package com.app.dto.patient.response;

import com.app.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {

    private Long id;

    private String fullName;

    private String mobile;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String bloodGroup;

    private String address;

    private String medicalNotes;

    private String registeredBy;

    private LocalDateTime registeredAt;
}