package com.collalab.demoapp.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * RichTextView is a TextView which lets you customize the styling of parts of your text via
 * Spannables, but without the hassle of having to deal directly with Spannable themselves.
 * <p>
 * The idea behind a BabushkaText is that it is made up of {@code SpanStyle}s. Each SpanStyle represents a
 * section of the final text displayed by this TextView, and each SpanStyle may be styled independently
 * from the other Pieces. When you put it all together, the final results is still a a single
 * TextView, but with a a very different graphic output.
 */
public class RichTextView extends TextView {

    // some default params
    private static int DEFAULT_ABSOLUTE_TEXT_SIZE;
    private static float DEFAULT_RELATIVE_TEXT_SIZE = 1;

    private List<SpanStyle> mSpanStyles;

    /**
     * Create a new instance of a this class
     *
     * @param context
     */
    public RichTextView(Context context) {
        super(context);
        init();
    }

    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSpanStyles = new ArrayList<>();
        RichTextView.DEFAULT_ABSOLUTE_TEXT_SIZE = (int) getTextSize();
    }

    /**
     * Use this method to add a {@link SpanStyle} to a BabushkaText.
     * Each {@link SpanStyle } is added sequentially, so the
     * order you call this method matters.
     *
     * @param aSpanStyle the SpanStyle
     */
    public void addPiece(SpanStyle aSpanStyle) {
        mSpanStyles.add(aSpanStyle);
    }

    /**
     * Adds a SpanStyle at this specific location. The underlying data structure is a
     * {@link java.util.List}, so expect the same type of behaviour.
     *
     * @param aSpanStyle the SpanStyle to add.
     * @param location   the index at which to add.
     */
    public void addPiece(SpanStyle aSpanStyle, int location) {
        mSpanStyles.add(location, aSpanStyle);
    }

    /**
     * Replaces the SpanStyle at the specified location with this new SpanStyle. The underlying data
     * structure is a {@link java.util.List}, so expect the same type of behaviour.
     *
     * @param newSpanStyle the SpanStyle to insert.
     * @param location     the index at which to insert.
     */
    public void replacePieceAt(int location, SpanStyle newSpanStyle) {
        mSpanStyles.set(location, newSpanStyle);
    }

    /**
     * Removes the SpanStyle at this specified location. The underlying data structure is a
     * {@link java.util.List}, so expect the same type of behaviour.
     *
     * @param location the index of the SpanStyle to remove
     */
    public void removePiece(int location) {
        mSpanStyles.remove(location);
    }

    /**
     * Get a specific {@link SpanStyle} in position index.
     *
     * @param location position of SpanStyle (0 based)
     * @return SpanStyle o null if invalid index
     */
    public SpanStyle getPiece(int location) {
        if (location >= 0 && location < mSpanStyles.size()) {
            return mSpanStyles.get(location);
        }

        return null;
    }

    /**
     * Call this method when you're done adding {@link SpanStyle}s
     * and want this TextView to display the final, styled version of it's String contents.
     * <p>
     * You MUST also call this method whenever you make a modification to the text of a SpanStyle that
     * has already been displayed.
     */
    public void display() {

        // generate the final string based on the pieces
        StringBuilder builder = new StringBuilder();
        for (SpanStyle aSpanStyle : mSpanStyles) {
            builder.append(aSpanStyle.text);
        }

        // apply spans
        int cursor = 0;
        SpannableString finalString = new SpannableString(builder.toString());
        for (SpanStyle aSpanStyle : mSpanStyles) {
            applySpannablesTo(aSpanStyle, finalString, cursor, cursor + aSpanStyle.text.length());
            cursor += aSpanStyle.text.length();
        }

        // set the styled text
        setText(finalString);
    }

    private void applySpannablesTo(SpanStyle aSpanStyle, SpannableString finalString, int start, int end) {

        if (aSpanStyle.subscript) {
            finalString.setSpan(new SubscriptSpan(), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (aSpanStyle.superscript) {
            finalString.setSpan(new SuperscriptSpan(), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (aSpanStyle.strike) {
            finalString.setSpan(new StrikethroughSpan(), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        if (aSpanStyle.underline) {
            finalString.setSpan(new UnderlineSpan(), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // style
        finalString.setSpan(new StyleSpan(aSpanStyle.style), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // absolute text size
        finalString.setSpan(new AbsoluteSizeSpan(aSpanStyle.textSize), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // relative text size
        finalString.setSpan(new RelativeSizeSpan(aSpanStyle.textSizeRelative), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // text color
        finalString.setSpan(new ForegroundColorSpan(aSpanStyle.textColor), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // background color
        if (aSpanStyle.backgroundColor != -1) {
            finalString.setSpan(new BackgroundColorSpan(aSpanStyle.backgroundColor), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * Resets the styling of this view and sets it's content to an empty String.
     */
    public void reset() {
        mSpanStyles = new ArrayList<>();
        setText("");
    }

    /**
     * Change text color of all pieces of textview.
     */
    public void changeTextColor(int textColor) {
        for (SpanStyle mSpanStyle : mSpanStyles) {
            mSpanStyle.setTextColor(textColor);
        }
        display();
    }

    /**
     * A SpanStyle represents a part of the text that you want to style. Say for example you want this
     * BabushkaText to display "Hello World" such that "Hello" is displayed in Bold and "World" is
     * displayed in Italics. Since these have different styles, they are both separate Pieces.
     * <p>
     * You create a SpanStyle by using it's {@link SpanStyle.Builder}
     */
    public static class SpanStyle {

        private String text;
        private int textColor;
        private final int textSize;
        private final int backgroundColor;
        private final float textSizeRelative;
        private final int style;
        private final boolean underline;
        private final boolean superscript;
        private final boolean strike;
        private final boolean subscript;

        public SpanStyle(Builder builder) {
            this.text = builder.text;
            this.textSize = builder.textSize;
            this.textColor = builder.textColor;
            this.backgroundColor = builder.backgroundColor;
            this.textSizeRelative = builder.textSizeRelative;
            this.style = builder.style;
            this.underline = builder.underline;
            this.superscript = builder.superscript;
            this.subscript = builder.subscript;
            this.strike = builder.strike;
        }

        /**
         * Sets the text of this SpanStyle. If you're creating a new SpanStyle, you should do so using it's
         * {@link SpanStyle.Builder}.
         * <p>
         * Use this method if you want to modify the text of an existing SpanStyle that is already
         * displayed. After doing so, you MUST call {@code display()} for the changes to show up.
         *
         * @param text the text to display
         */
        public void setText(String text) {
            this.text = text;
        }


        /**
         * Sets the text color of this SpanStyle. If you're creating a new SpanStyle, you should do so using it's
         * {@link SpanStyle.Builder}.
         * <p>
         * Use this method if you want to change the text color of an existing SpanStyle that is already
         * displayed. After doing so, you MUST call {@code display()} for the changes to show up.
         *
         * @param textColor of text (it is NOT android Color resources ID, use getResources().getColor(R.color.colorId) for it)
         */
        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        /**
         * Builder of Pieces
         */
        public static class Builder {

            // required
            private final String text;

            // optional
            private int textSize = DEFAULT_ABSOLUTE_TEXT_SIZE;
            private int textColor = Color.BLACK;
            private int backgroundColor = -1;
            private float textSizeRelative = DEFAULT_RELATIVE_TEXT_SIZE;
            private int style = Typeface.NORMAL;
            private boolean underline = false;
            private boolean strike = false;
            private boolean superscript = false;
            private boolean subscript = false;

            /**
             * Creates a new Builder for this SpanStyle.
             *
             * @param text the text of this SpanStyle
             */
            public Builder(String text) {
                this.text = text;
            }

            /**
             * Sets the absolute text size.
             *
             * @param textSize text size in pixels
             * @return a Builder
             */
            public Builder textSize(int textSize) {
                this.textSize = textSize;
                return this;
            }

            /**
             * Sets the text color.
             *
             * @param textColor the color
             * @return a Builder
             */
            public Builder textColor(int textColor) {
                this.textColor = textColor;
                return this;
            }

            /**
             * Sets the background color.
             *
             * @param backgroundColor the color
             * @return a Builder
             */
            public Builder backgroundColor(int backgroundColor) {
                this.backgroundColor = backgroundColor;
                return this;
            }

            /**
             * Sets the relative text size.
             *
             * @param textSizeRelative relative text size
             * @return a Builder
             */
            public Builder textSizeRelative(float textSizeRelative) {
                this.textSizeRelative = textSizeRelative;
                return this;
            }

            /**
             * Sets a style to this SpanStyle.
             *
             * @param style see {@link android.graphics.Typeface}
             * @return a Builder
             */
            public Builder style(int style) {
                this.style = style;
                return this;
            }

            /**
             * Underlines this SpanStyle.
             *
             * @return a Builder
             */
            public Builder underline() {
                this.underline = true;
                return this;
            }

            /**
             * Strikes this SpanStyle.
             *
             * @return a Builder
             */
            public Builder strike() {
                this.strike = true;
                return this;
            }

            /**
             * Sets this SpanStyle as a superscript.
             *
             * @return a Builder
             */
            public Builder superscript() {
                this.superscript = true;
                return this;
            }

            /**
             * Sets this SpanStyle as a subscript.
             *
             * @return a Builder
             */
            public Builder subscript() {
                this.subscript = true;
                return this;
            }

            /**
             * Creates a {@link SpanStyle} with the customized
             * parameters.
             *
             * @return a SpanStyle
             */
            public SpanStyle build() {
                return new SpanStyle(this);
            }
        }
    }

}

