package com.app.specification;

import com.app.entity.Staff;
import org.springframework.data.jpa.domain.Specification;

public class DoctorSpecification {

    private DoctorSpecification() {
    }

    public static Specification<Staff> activeDoctors() {

        return (root, query, cb) -> {

            query.orderBy(
                    cb.asc(root.get("fullName"))
            );

            return cb.and(
                    cb.equal(root.get("isActive"), true),
                    cb.equal(root.get("role").get("name"), "DOCTOR")
            );
        };
    }
}