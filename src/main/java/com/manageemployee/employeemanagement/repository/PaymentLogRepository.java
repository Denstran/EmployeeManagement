package com.manageemployee.employeemanagement.repository;

import com.manageemployee.employeemanagement.model.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentLogRepository extends JpaRepository<PaymentLog, Long> {
}
