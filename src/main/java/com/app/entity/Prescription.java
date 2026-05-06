package com.app.entity;

import com.app.entity.base.CreatedOnly;
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
@Table(name = "prescription")
public class Prescription extends CreatedOnly {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "consultation_id", nullable = false, unique = true)
    private Consultation consultation;

    @Column(name = "general_instructions", columnDefinition = "TEXT")
    private String generalInstructions;

    @Column(name = "follow_up_notes", length = 500)
    private String followUpNotes;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Builder.Default
    @Column(name = "is_locked", nullable = false)
    private Boolean isLocked = false;
}