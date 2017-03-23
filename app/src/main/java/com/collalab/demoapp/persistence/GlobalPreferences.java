package com.collalab.demoapp.persistence;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by laptop88 on 12/28/2016.
 */

public class GlobalPreferences {
    SharedPreferences sharedPreferences;
    private Context context;
    private static GlobalPreferences mSpInstance;

    private static final String PREFERENCE_NAME = "sonha_app_persistence";

    private GlobalPreferences(Context context) {
        this.context = context;
    }

    public static synchronized GlobalPreferences getInstance(Context context) {
        if (mSpInstance == null) {
            mSpInstance = new GlobalPreferences(context);
        }
        return mSpInstance;
    }

    public SharedPreferences getmPrefs() {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getmPrefs().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void saveString(String key, String value) {
        SharedPreferences.Editor editor = getmPrefs().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = getmPrefs().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return getmPrefs().getBoolean(key, false);
    }

    public String getString(String key) {
        return getmPrefs().getString(key, null);
    }

    public int getInt(String key) {
        return getmPrefs().getInt(key, -1);
    }
}
