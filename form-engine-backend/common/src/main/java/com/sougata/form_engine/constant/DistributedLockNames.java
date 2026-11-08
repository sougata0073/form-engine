package com.sougata.form_engine.constant;

import com.sougata.form_engine.constant.cache.CommonCacheNames;

import java.util.UUID;

public final class DistributedLockNames {

    public static String formMultipleResponse(UUID formId, UUID responderId) {
        return "formMultipleResponseLock" + CommonCacheNames.SEPARATOR + formId + CommonCacheNames.SEPARATOR + responderId;
    }

    public static String formDetailsDbLoad(UUID formId) {
        return "formDetailsDbLoadLock" + CommonCacheNames.SEPARATOR + formId;
    }

    public static String formDefaultSettingsSave(UUID userId) {
        return "formDefaultSettingsLock" + CommonCacheNames.SEPARATOR + userId;
    }
}
