package com.yechy.hfreservemask;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Created by cloud on 2020-03-05.
 */
public class OutputTextView extends TextView {
    public OutputTextView(Context context) {
        super(context);
    }

    public OutputTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OutputTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void outputMessage(String message) {
        CharSequence oldMessage = getText();
        String newMessage = oldMessage + "\n" + message;
        setText(newMessage);

        int offset = getLineCount() * getLineHeight();
        if (offset > getHeight()) {
            scrollTo(0, offset - getHeight() + getLineHeight() * 2);
        }
    }
}
