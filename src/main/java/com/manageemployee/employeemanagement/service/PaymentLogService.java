package com.manageemployee.employeemanagement.service;

import com.manageemployee.employeemanagement.model.PaymentLog;
import com.manageemployee.employeemanagement.repository.PaymentLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentLogService {
    private final PaymentLogRepository paymentLogRepository;

    @Autowired
    public PaymentLogService(PaymentLogRepository paymentLogRepository) {
        this.paymentLogRepository = paymentLogRepository;
    }

    public PaymentLog createOrUpdatePaymentLog(PaymentLog paymentLog) {
        return paymentLogRepository.saveAndFlush(paymentLog);
    }

    public PaymentLog getPaymentLogById(Long id) {
        return paymentLogRepository.findById(id).orElse(null);
    }

    public List<PaymentLog> getAllPaymentLogs() {
        return paymentLogRepository.findAll();
    }

    public void deletePaymentLogById(Long id) {
        paymentLogRepository.deleteById(id);
    }
}
