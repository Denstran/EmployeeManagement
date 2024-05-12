package com.manageemployee.employeemanagement.employee.service;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.vacation.RequestStatus;
import com.manageemployee.employeemanagement.employee.model.vacation.VacationRequest;
import com.manageemployee.employeemanagement.employee.repository.VacationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class VacationService {
    private final VacationRepository repository;

    @Autowired
    public VacationService(VacationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public VacationRequest createRequest(VacationRequest vacationRequest) {
        if (vacationRequest == null) throw new NullPointerException("Передано null значение!");

        vacationRequest.createRequest();
        return repository.save(vacationRequest);
    }

    @Transactional
    public VacationRequest updateVacation(VacationRequest vacationRequest) {
        if (vacationRequest == null) throw new NullPointerException("Передано null значение!");

        vacationRequest.updateVacation();
        return repository.save(vacationRequest);
    }

    // Метод для тестирования
    @Transactional
    public VacationRequest saveRequest(VacationRequest vacationRequest) {
        if (vacationRequest == null) throw new NullPointerException("Передано null значение!");

        return repository.save(vacationRequest);
    }

    @Transactional
    public void deleteById(Long vacationId) {
        repository.deleteById(vacationId);
    }

    @Transactional
    public void deleteAllByEmployeeEmail(String employeeEmail) {
        repository.deleteAllByEmployee_Email(employeeEmail);
    }

    @Transactional
    public void saveAll(List<VacationRequest> vacationRequests) {
        repository.saveAll(vacationRequests);
    }

    @Transactional
    public void approveVacation(VacationRequest vacationRequest) {
        if (vacationRequest == null) throw new NullPointerException("Передано null значение!");
        vacationRequest.approveVacation();
        repository.save(vacationRequest);
    }

    @Transactional
    public void cancelVacation(VacationRequest vacationRequest, Employee headOfDepartment) {
        if (vacationRequest == null) throw new NullPointerException("Передано null значение!");
        vacationRequest.cancelVacation(headOfDepartment);
        repository.save(vacationRequest);
    }

    public VacationRequest getVacationById(Long vacationId) {
        return repository.findById(vacationId).orElseThrow(() ->
                new IllegalArgumentException("Выбранный отпуск не существует!"));
    }

    public List<VacationRequest> getByHeadOfDepartment(Employee headOfDepartment) {
        return repository.findAllByEmployee_CompanyBranchAndEmployee_Position_Department(
                headOfDepartment.getCompanyBranch(), headOfDepartment.getPosition().getDepartment());
    }

    public List<VacationRequest> getAllVacations() {
        return repository.findAll();
    }

    public List<VacationRequest> getEmployeeVacations(Employee employee) {
        return repository.findByEmployee(employee);
    }

    public boolean existsByEmployeeIdAndVacationDates(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return repository.existsByEmployeeIdAndVacationDates(employeeId, startDate, endDate);
    }

    public boolean existsByEmployeeIdAndVacationDatesExcludeVacation(Long employeeId, LocalDate startDate,
                                                                    LocalDate endDate, Long vacationId) {
        return repository.existsByEmployeeIdAndVacationDatesExcludeVacation(employeeId, startDate, endDate, vacationId);
    }

    public long getVacationDaysOfProcessingRequestByEmployee(Employee employee) {
        List<VacationRequest> vacationRequests = repository.findByEmployeeAndRequestStatus(employee,
                RequestStatus.IN_PROCESS);
        return vacationRequests.stream().mapToLong(VacationRequest::getVacationDays).sum();
    }

    public long getVacationDaysOfProcessingRequestByEmployeeExcludeCurrentVacation(Employee employee, Long vacationId) {
        log.info("ID OF EXCLUDED VACATION: {}", vacationId);
        List<VacationRequest> vacationRequests = repository.findByEmployeeAndRequestStatusAndIdNot(
                employee, RequestStatus.IN_PROCESS, vacationId);
        log.info("VACATIONS FOUND: {}", vacationRequests);
        return vacationRequests.stream().mapToLong(VacationRequest::getVacationDays).sum();
    }

    public boolean existsByVacationIdAndEmployeeMail(Long vacationId, String employeeMail) {
        return repository.existsByIdAndEmployee_Email(vacationId, employeeMail);
    }
}
