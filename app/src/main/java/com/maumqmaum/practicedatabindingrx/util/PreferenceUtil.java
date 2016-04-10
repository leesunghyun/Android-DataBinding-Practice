package com.maumqmaum.practicedatabindingrx.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;

import com.google.gson.Gson;
import com.maumqmaum.practicedatabindingrx.App;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PreferenceUtil {

    public static final String KEY_TWITTER_ACCESS_TOKEN = "key_twitter_access_token";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            KEY_TWITTER_ACCESS_TOKEN
    })
    public @interface PreferenceKeys {
    }

    private SharedPreferences preference;

    private Gson gson;

    public PreferenceUtil() {
        preference = App.getInstance().getSharedPreferences("pref", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void putString(@PreferenceKeys String key, String value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value).apply();
    }

    public void putInt(@PreferenceKeys String key, int value) {
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(key, value).apply();
    }

    public void putObject(@PreferenceKeys String key, Object object) {
        SharedPreferences.Editor editor = preference.edit();
        String json = gson.toJson(object);
        editor.putString(key, json).apply();
    }


    public int getInt(@PreferenceKeys String key) {
        return preference.getInt(key, -1);
    }

    public <T> T getObejct(@PreferenceKeys String key, Class<T> object) {
        String json = preference.getString(key, "");
        return gson.fromJson(json, object);
    }
}
