package com.app.entity;

import com.app.entity.base.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(callSuper = false)
@Entity
@Table(
    name = "staff",
    indexes = {
        @Index(name = "idx_staff_role_id",   columnList = "role_id"),
        @Index(name = "idx_staff_is_active", columnList = "is_active")
    }
)
public class Staff extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "employee_id", nullable = false, unique = true, length = 20)
    private String employeeId;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "official_role", length = 100)
    private String officialRole;

    @Column(name = "specialization", length = 100)
    private String specialization;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "profile_photo_url")
private String profilePhotoUrl;

@Column(name = "profile_photo_key")
private String profilePhotoKey;
}