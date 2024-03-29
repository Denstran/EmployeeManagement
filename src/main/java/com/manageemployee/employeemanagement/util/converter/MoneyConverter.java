package com.manageemployee.employeemanagement.util.converter;

import com.manageemployee.employeemanagement.util.Money;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter for converting Money type to database column and vice versa
 */
@Converter
public class MoneyConverter implements AttributeConverter<Money, String> {

    /**
     * Method for converting Money object to database column
     * @param money - object for converting
     * @return string representation of Money object
     */
    @Override
    public String convertToDatabaseColumn(Money money) {
        return money.toString();
    }

    /**
     * Method for converting row from database column to Money object
     * @param s - row form database
     * @return Money object
     */
    @Override
    public Money convertToEntityAttribute(String s) {
        return Money.getMoneyFromString(s);
    }
}
