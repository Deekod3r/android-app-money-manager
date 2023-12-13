package com.project.hucemoney.common;

import com.project.hucemoney.R;

import java.math.BigInteger;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
    public static final Map<String, Integer> FLAGS_REGION = new HashMap<String, Integer>() {{
        put("AUD", R.drawable.ic_flags_australian);
        put("CAD", R.drawable.ic_flags_canada);
        put("CHF", R.drawable.ic_flags_switzerland);
        put("CNY", R.drawable.ic_flags_china);
        put("DKK", R.drawable.ic_flags_denmark);
        put("EUR", R.drawable.ic_flags_euro);
        put("GBP", R.drawable.ic_flags_england);
        put("HKD", R.drawable.ic_flags_hongkong);
        put("INR", R.drawable.ic_flags_indian);
        put("JPY", R.drawable.ic_flags_japan);
        put("KWD", R.drawable.ic_flags_kuwait);
        put("KRW", R.drawable.ic_flags_korea);
        put("MYR", R.drawable.ic_flags_malaysia);
        put("NOK", R.drawable.ic_flags_norway);
        put("RUB", R.drawable.ic_flags_russia);
        put("SAR", R.drawable.ic_flags_saudi);
        put("SEK", R.drawable.ic_flags_sweden);
        put("SGD", R.drawable.ic_flags_singapore);
        put("THB", R.drawable.ic_flags_thailand);
        put("USD", R.drawable.ic_flags_america);
    }};

    public static float BHXH = 0.08f;
    public static float BHYT = 0.015f;
    public static float BHTN = 0.01f;
    public static long BH_V1 = 1160000;
    public static long BH_V2 = 1280000;
    public static long BH_V3 = 1400000;
    public static long BH_V4 = 1520000;

}
