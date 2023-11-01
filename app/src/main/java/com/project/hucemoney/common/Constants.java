package com.project.hucemoney.common;

import java.math.BigInteger;
import java.time.format.DateTimeFormatter;

public class Constants {
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9]+$";
    public static final String REGEX_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9\\s]).{8,}$";
    public static final String REGEX_SPECIAL_CHAR = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]";
    public static final BigInteger MAX_MONEY = new BigInteger("10000000000");
    public static final boolean TYPE_EXPENSE = false;
    public static final boolean TYPE_INCOME = true;
    public static final String TYPE_VERIFY_REGISTER = "register";
    public static final String TYPE_VERIFY_FORGOT_PASSWORD = "forgot_password";
    public static final String ACTION_ADD = "00";
    public static final String ACTION_EDIT = "01";
    public static final String ACTION_DELETE = "02";
    public static final String[] DAY_OF_WEEK = new String[]{"Chủ Nhật", "Thứ Hai", "Thứ Ba", "Thứ Tư", "Thứ Năm", "Thứ Sáu", "Thứ Bảy"};
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

}
