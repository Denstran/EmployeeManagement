package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {
}
