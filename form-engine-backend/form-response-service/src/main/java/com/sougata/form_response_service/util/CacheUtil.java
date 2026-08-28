package com.sougata.form_response_service.util;

import com.sougata.form_engine.constant.cache.CommonCacheNames;

public class CacheUtil {

    public static String buildKey(Object... names) {
        StringBuilder str = new StringBuilder(CommonCacheNames.FORM_RESPONSE_SERVICE_PREFIX + CommonCacheNames.SEPARATOR);

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
