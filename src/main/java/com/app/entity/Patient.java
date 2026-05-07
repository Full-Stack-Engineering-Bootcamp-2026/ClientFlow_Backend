package com.app.entity;

import com.app.entity.base.CreatedOnly;
import com.app.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = false)
@Entity
@Table(
    name = "patient",
    indexes = @Index(name = "idx_patient_mobile", columnList = "mobile")
)
@AttributeOverride(name = "createdAt", column = @Column(name = "registered_at", nullable = false, updatable = false))
public class Patient extends CreatedOnly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "mobile", nullable = false, unique = true, length = 15)
    private String mobile;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "blood_group", length = 5)
    private String bloodGroup;

    @Column(name = "address", length = 300)
    private String address;

    @Column(name = "medical_notes", columnDefinition = "TEXT")
    private String medicalNotes;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registered_by", nullable = false)
    private Staff registeredBy;
}