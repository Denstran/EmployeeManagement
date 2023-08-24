package com.manageemployee.employeemanagement.converter.dtoMappers;

import com.manageemployee.employeemanagement.dto.PaymentTypeDTO;
import com.manageemployee.employeemanagement.model.PaymentType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentTypeMapper extends AbstractMapper<PaymentType, PaymentTypeDTO> {

    @Autowired
    public PaymentTypeMapper(ModelMapper mapper) {
        super(PaymentType.class, PaymentTypeDTO.class, mapper);
    }

}
