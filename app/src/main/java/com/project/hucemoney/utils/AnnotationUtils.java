package com.project.hucemoney.utils;

import androidx.room.TypeConverter;

import com.project.hucemoney.common.Constants;

import java.time.LocalDate;

public class AnnotationUtils {
    @TypeConverter
    public static LocalDate fromTimestamp(String value) {
        return value == null ? null : LocalDate.parse(value);
    }

    @TypeConverter
    public static String dateToTimestamp(LocalDate date) {
        return date == null ? null : date.toString();
    }
}
