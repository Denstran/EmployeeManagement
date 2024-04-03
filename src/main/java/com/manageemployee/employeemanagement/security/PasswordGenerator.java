package com.manageemployee.employeemanagement.security;

import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;


public class PasswordGenerator {
    private static final org.passay.PasswordGenerator passwordGenerator = new org.passay.PasswordGenerator();
    private static final CharacterCharacteristicsRule rules = new CharacterCharacteristicsRule();

    static {
        rules.getRules().add(new CharacterRule(EnglishCharacterData.UpperCase, 5));
        rules.getRules().add(new CharacterRule(EnglishCharacterData.LowerCase, 5));
        rules.getRules().add(new CharacterRule(EnglishCharacterData.Digit, 5));
        rules.getRules().add(new CharacterRule(EnglishCharacterData.Special, 5));
    }

    public static String generatePassword() {
        return passwordGenerator.generatePassword(20, rules.getRules());
    }
}
