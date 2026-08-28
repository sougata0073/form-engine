package com.sougata.form_data_service.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtil {

    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    private static final String URL_PATTERN =
            "^(https?://)?((localhost)|(([a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}))(:\\d+)?(/\\S*)?$";

    public static boolean isEmail(String s) {
        return s.matches(EMAIL_PATTERN);
    }

    public static boolean isUrl(String s) {
        return s.matches(URL_PATTERN);
    }

    public static String upperToCamelCase(String value, String separator, boolean lowercaseFirst) {
        String result = Arrays.stream(value.split(separator))
                .map(s -> ch.qos.logback.core.util.StringUtil.capitalizeFirstLetter(s.toLowerCase()))
                .collect(Collectors.joining());

        if (lowercaseFirst) {
            return ch.qos.logback.core.util.StringUtil.lowercaseFirstLetter(result);
        }

        return result;
    }

    public static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }
}
