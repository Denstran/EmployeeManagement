package com.manageemployee.employeemanagement.model.events.departmentEvents;

import com.manageemployee.employeemanagement.model.DepartmentInfoPaymentLog;
import lombok.Data;

@Data
public class DepartmentInfoRegistered {
    private final DepartmentInfoPaymentLog departmentInfoPaymentLog;

}
