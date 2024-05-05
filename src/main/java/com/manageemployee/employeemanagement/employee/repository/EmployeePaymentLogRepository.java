package com.manageemployee.employeemanagement.employee.repository;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.employee.EmployeePaymentLog;
import com.manageemployee.employeemanagement.util.enumType.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface EmployeePaymentLogRepository extends JpaRepository<EmployeePaymentLog, Long>,
        JpaSpecificationExecutor<EmployeePaymentLog> {
    List<EmployeePaymentLog> findByDateOfPaymentBetweenAndEmployeeAndPaymentTypeIn(Date beginning, Date end,
                                                                                   Employee employee,
                                                                                   List<PaymentType> paymentTypes);
}
