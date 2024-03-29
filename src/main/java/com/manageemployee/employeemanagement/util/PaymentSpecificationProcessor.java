package com.manageemployee.employeemanagement.util;

import org.springframework.data.jpa.domain.Specification;

public class PaymentSpecificationProcessor {
    public static <T extends BasePaymentEntity> Specification<T> processDateParams(String startDate, String endDate,
                                                                                   Specification<T> spec) {
        if (!AreDatesValid(startDate, endDate)) return spec;

        return spec.and(AbstractPaymentLogSpec.isBetweenDates(startDate, endDate));
    }

    public static <T extends BasePaymentEntity> Specification<T> processAction(String transferAction,
                                                                               Specification<T> spec) {
        if (!isTransferActionValid(transferAction)) return spec;

        return spec.and(AbstractPaymentLogSpec.isTransferActionEqualTo(transferAction));
    }

    private static boolean isTransferActionValid(String transferAction) {
        return transferAction != null && !transferAction.isEmpty() && !transferAction.equals("ALL");
    }

    private static boolean AreDatesValid(String startDate, String endDate) {
        return startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty();
    }
}
