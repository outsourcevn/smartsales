package com.collalab.demoapp.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.collalab.demoapp.DemoApp;

public class AwesomeFontTextView extends TextView {
    public AwesomeFontTextView(Context context) {
        this(context, null, 0);
    }

    public AwesomeFontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AwesomeFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Typeface awesomeFont = DemoApp.getFont("fonts/fontawesome-webfont.ttf", context);
        setTypeface(awesomeFont);
        isInEditMode();
    }

}
