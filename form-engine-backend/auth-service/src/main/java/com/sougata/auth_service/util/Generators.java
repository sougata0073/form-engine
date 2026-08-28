package com.sougata.auth_service.util;

import com.github.f4b6a3.tsid.TsidCreator;

public class Generators {

    public static String getRandomUsername() {
        return "USER_" + TsidCreator.getTsid();
    }

}
