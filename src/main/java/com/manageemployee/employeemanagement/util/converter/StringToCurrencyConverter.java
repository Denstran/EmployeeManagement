package com.manageemployee.employeemanagement.util.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Currency;

/**
 * Class for converting Strings to Currency objects
 */
@Component
public class StringToCurrencyConverter implements Converter<String, Currency> {
    @Override
    public Currency convert(String source) {
        try {
            return Currency.getInstance(source);
        }catch (IllegalArgumentException e) {
            return null;
        }
    }
}
