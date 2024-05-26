package com.manageemployee.employeemanagement.employee.repository;

import com.manageemployee.employeemanagement.companyBranch.model.CompanyBranch;
import com.manageemployee.employeemanagement.department.model.Department;
import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.vacation.RequestStatus;
import com.manageemployee.employeemanagement.employee.model.vacation.VacationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface VacationRepository extends JpaRepository<VacationRequest, Long> {
    List<VacationRequest> findByEmployee(Employee employee);

    @Query("SELECT CASE WHEN COUNT(vr) > 0 THEN true ELSE false END " +
            "FROM VacationRequest vr " +
            "WHERE vr.employee.id = :employeeId " +
            "AND vr.vacationStartDate <= :endDate " +
            "AND vr.vacationEndDate >= :startDate " +
            "AND vr.requestStatus != 'CANCELLED'")
    boolean existsByEmployeeIdAndVacationDatesNotCancelled(@Param("employeeId") Long employeeId,
                                                           @Param("startDate") LocalDate startDate,
                                                           @Param("endDate") LocalDate endDate);

    @Query("SELECT CASE WHEN COUNT(vr) > 0 THEN true ELSE false END " +
            "FROM VacationRequest vr " +
            "WHERE vr.employee.id = :employeeId " +
            "AND vr.vacationStartDate <= :endDate " +
            "AND vr.vacationEndDate >= :startDate " +
            "AND vr.id != :vacationId " +
            "AND vr.requestStatus != 'CANCELLED'")
    boolean existsByEmployeeIdAndVacationDatesExcludeVacationNotCancelled(@Param("employeeId") Long employeeId,
                                                                          @Param("startDate") LocalDate startDate,
                                                                          @Param("endDate") LocalDate endDate,
                                                                          @Param("vacationId") Long vacationId);

    List<VacationRequest> findByEmployeeAndRequestStatus(Employee employee, RequestStatus requestStatus);
    List<VacationRequest> findByEmployeeAndRequestStatusAndIdNot(Employee employee,
                                                                 RequestStatus requestStatus,
                                                                 Long vacationId);

    boolean existsByIdAndEmployee_Email(Long vacationId, String employeeEmail);

    void deleteAllByEmployee_Email(String employeeEmail);

    List<VacationRequest> findAllByEmployee_CompanyBranchAndEmployee_Position_Department(CompanyBranch companyBranch,
                                                                                         Department department);
}
