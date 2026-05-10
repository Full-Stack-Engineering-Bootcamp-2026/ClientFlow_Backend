package com.app.dao;

import com.app.entity.Staff;
import com.app.exception.BadRequestException;
import com.app.exception.ResourceNotFoundException;
import com.app.exception.UnauthorizedException;
import com.app.repository.StaffRepository;
import com.app.specification.DoctorSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StaffDaoImpl implements StaffDao {

 private final StaffRepository staffRepository;

 @Override
 public Staff getById(Long id) {

 return staffRepository.findById(id)
 .orElseThrow(() ->
 new ResourceNotFoundException("Doctor not found")
 );
 }

 @Override
 public Staff getByEmail(String email) {

 return staffRepository.findByEmail(email)
 .orElseThrow(() ->
 new UnauthorizedException("User not found")
 );
 }

    @Override
    public Staff findByEmail(String email) {

        return staffRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UnauthorizedException("User not found")
                );
    }

    @Override
    public long getTotalStaffCount() {

 return staffRepository.count();
 }

 @Override
 public long getActiveDoctorCount() {

 return staffRepository
 .countByRole_NameAndIsActiveTrue("DOCTOR");
 }

 @Override
 public long countByRoleId(Long roleId) {

 return staffRepository.countByRoleId(roleId);
 }

 @Override
 public Staff save(Staff staff) {

 return staffRepository.save(staff);
 }

 @Override
 public boolean existsByEmail(String email) {

 return staffRepository.existsByEmail(email);
 }

 @Override
 public List<Staff> getActiveDoctors() {

 return staffRepository
 .findByRole_NameAndIsActiveTrue("DOCTOR");
 }

 @Override
 public Staff update(Staff staff) {

 return staffRepository.save(staff);
 }

 @Override
 public Page<Staff> getAllStaff(Pageable pageable) {

 return staffRepository.findAll(pageable);
 }

 @Override
 public Page<Staff> getAllStaff(
 Specification<Staff> specification,
 Pageable pageable
 ) {

 return staffRepository.findAll(specification, pageable);
 }

 @Override
 public List<Staff> getAllActiveDoctors() {

 try {

 Specification<Staff> specification =
 DoctorSpecification.activeDoctors();

 return staffRepository.findAll(specification);

 } catch (DataAccessException ex) {

 log.error(
 "Database error while fetching active doctors: {}",
 ex.getMessage()
 );

 throw new BadRequestException(
 "Unable to fetch doctors"
 );
 }
 }
}
