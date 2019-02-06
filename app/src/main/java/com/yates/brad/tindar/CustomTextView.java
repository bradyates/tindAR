/**
 * CustomTextView.java
 * Creating a custom Text View that uses a specific font file.
 *
 * @author  Brad Yates & Kennedey Oddom Sok
 * @version 1.0
 * @since   11/19/2018
 */

package com.yates.brad.tindar;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {
    public CustomTextView(Context context) {
        super(context);
        setFont();
    }
    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/ProximaNovaSoft-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}