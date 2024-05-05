package com.manageemployee.employeemanagement.employee.model.vacation;

import com.manageemployee.employeemanagement.employee.model.employee.Employee;
import com.manageemployee.employeemanagement.employee.model.event.vacationEvent.VacationRequestCreated;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "VACATION_REQUEST")
@Getter
@Setter
@ToString
public class VacationRequest extends AbstractAggregateRoot<VacationRequest> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "EMPLOYEE_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_EMPLOYEE_VACATION"))
    @ToString.Exclude
    private Employee employee;

    @Column(name = "VACATION_START_DATE", nullable = false)
    @Temporal(value = TemporalType.DATE)
    @FutureOrPresent
    private LocalDate vacationStartDate;

    @Column(name = "VACATION_END_DATE", nullable = false)
    @Temporal(value = TemporalType.DATE)
    @Future
    private LocalDate vacationEndDate;

    @Column(name = "REQUEST_STATUS", nullable = false)
    private RequestStatus requestStatus;

    public void createRequest() {
        List<String> employeeContacts = List.of(employee.getPhoneNumber(), employee.getEmail());
        requestStatus = RequestStatus.IN_PROCESS;

        registerEvent(new VacationRequestCreated(
                employee.getCompanyBranch(), employee.getPosition().getDepartment(),
                vacationStartDate, vacationEndDate,
                employeeContacts, employee.getName()));
    }
}
