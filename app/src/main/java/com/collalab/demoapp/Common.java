package com.collalab.demoapp;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by VietMac on 2017-03-24.
 */

public class Common {
    public static String getDateInString(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(calendar.getTime());
        }
        return "";
    }

    public static String getDateInStringHHMM(Date date) {
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return sdf.format(calendar.getTime());
        }
        return "";
    }

    private static void hideKeyBoard(Context context, View view) {
        if (context == null || view == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyBoard(Activity activity) {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus == null) return;
        hideKeyBoard(activity, currentFocus);
    }

}
