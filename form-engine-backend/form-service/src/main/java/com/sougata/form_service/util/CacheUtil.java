package com.sougata.form_service.util;

import com.sougata.form_service.constant.cacheNames.CommonCacheNames;

public class CacheUtil {

    public static String buildKey(Object... names) {
        StringBuilder str = new StringBuilder(CommonCacheNames.PREFIX + CommonCacheNames.SEPARATOR);

        for (int i = 0; i < names.length; i++) {
            var name = names[i];

            if (i == names.length - 1) {
                str.append(name.toString());
            } else {
                str.append(name.toString()).append(CommonCacheNames.SEPARATOR);
            }
        }

        return str.toString();
    }

}
