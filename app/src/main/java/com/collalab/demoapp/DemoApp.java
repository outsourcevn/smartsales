package com.collalab.demoapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by VietMac on 2017-03-23.
 */

public class DemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface getFont(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if (tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }
}
