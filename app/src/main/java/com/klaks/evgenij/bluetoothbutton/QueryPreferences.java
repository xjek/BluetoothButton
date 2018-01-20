package com.klaks.evgenij.bluetoothbutton;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class QueryPreferences {
    private static final String PREF_TOKEN = "token"; // токен для сервера

    public static void setToken(Context context, String token) {
        setString(context, PREF_TOKEN, token);
    }

    public static String getToken(Context context) {
        return getString(context, PREF_TOKEN);
    }

    private static void setString(Context context, String parsm, String value) {
        getPreference(context)
                .edit()
                .putString(parsm, value)
                .apply();
    }

    private static String getString(Context context, String param) {
        return getPreference(context).getString(param, "");
    }

    private static SharedPreferences getPreference(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
