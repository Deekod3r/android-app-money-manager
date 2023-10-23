package com.project.hucemoney.utils;

import com.project.hucemoney.common.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isValidEmail(String input) {
        if (isNullOrEmpty(input)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches();
        }
    }

    public static boolean isValidPassword(String input) {
        if (isNullOrEmpty(input) || input.length() < 8) {
            return false;
        } else {
            Pattern pattern = Pattern.compile(Constants.REGEX_PASSWORD);
            Matcher matcher = pattern.matcher(input);
            return matcher.matches();
        }
    }

    public static boolean containsSpecialCharacter(String input) {
        if (isNullOrEmpty(input)) {
            return false;
        } else {
            Pattern pattern = Pattern.compile(Constants.REGEX_SPECIAL_CHAR);
            Matcher matcher = pattern.matcher(input);
            return matcher.find();
        }
    }

}
