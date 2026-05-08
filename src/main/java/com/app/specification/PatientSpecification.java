package com.app.specification;

import com.app.entity.Patient;
import org.springframework.data.jpa.domain.Specification;

public class PatientSpecification {

    private PatientSpecification() {
    }

    public static Specification<Patient> searchByKeyword(String keyword) {

        return (root, query, cb) -> {

            String pattern = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("fullName")), pattern),
                    cb.like(cb.lower(root.get("mobile")), pattern)
            );
        };
    }
}