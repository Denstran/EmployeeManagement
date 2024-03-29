package com.manageemployee.employeemanagement.util;

import com.manageemployee.employeemanagement.util.enumType.TransferAction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractPaymentLogSpec {
    public static Specification<? extends BasePaymentEntity> isBetweenDates(String startDate, String endDate) {
        return ((root, query, criteriaBuilder) -> {
            Date start;
            Date end;
            try {
                start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
                end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            Predicate greaterThanOrEqualToStartDate =
                    criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfPayment"), start);
            Predicate lessThanOrEqualToEndDate =
                    criteriaBuilder.lessThanOrEqualTo(root.get("dateOfPayment"), end);

            return criteriaBuilder.and(greaterThanOrEqualToStartDate, lessThanOrEqualToEndDate);
        });
    }

    public static Specification<? extends BasePaymentEntity> isTransferActionEqualTo(String transferAction) {
        TransferAction transferActionEnum = TransferAction.valueOf(transferAction);
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("transferAction"), transferActionEnum));
    }
}
