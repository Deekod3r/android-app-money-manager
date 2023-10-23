package com.project.hucemoney.common;

import java.math.BigInteger;

public class Constants {
    public static final String REGEX_USERNAME = "^[a-zA-Z0-9]+$";
    public static final String REGEX_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9\\s]).{8,}$";
    public static final String REGEX_SPECIAL_CHAR = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]";
    public static final BigInteger MAX_MONEY = new BigInteger("10000000000");
    public static final Boolean TYPE_EXPENSE = false;
    public static final Boolean TYPE_INCOME = true;
}
