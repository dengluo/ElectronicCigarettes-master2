package com.bestmafen.smablelib.util;

import android.text.TextUtils;

import com.bestmafen.easeblelib.util.L;

import java.util.Locale;
import java.util.TimeZone;

public class SmaBleUtils {
    public static TimeZone getDefaultTimeZone() {
        return TimeZone.getTimeZone("GMT+0");
    }

    public static byte getLanguageCode() {
        byte code = 0x01;
        String language = Locale.getDefault().getLanguage();
        if (language.equalsIgnoreCase("zh")) {
            code = 0x00;
        } else if (language.equalsIgnoreCase("tr")) {
            code = 0x02;
        } else if (language.equalsIgnoreCase("ru")) {
            code = 0x04;
        } else if (language.equalsIgnoreCase("es")) {
            code = 0x05;
        }
        L.d(language + "," + code);
        return code;
    }

    public static String addressPlus1(String address) {
        if (TextUtils.isEmpty(address)) {
            return "";
        }

        String s = address.replace(":", "");
        long value = Long.parseLong(s, 16) + 1;
        s = Long.toHexString(value);

        int l = s.length() / 2;
        StringBuilder sb = new StringBuilder(s);
        for (int i = 1; i < l; i++) {
            sb.insert(i * 2 + (i - 1), ":");
        }

        return sb.toString().toUpperCase();
    }
}
