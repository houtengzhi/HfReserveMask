package com.yechy.hfreservemask.entry;

import androidx.annotation.NonNull;

/**
 * Created by cloud on 2020-02-22.
 */
public class MaskInfo {
    private int remain;
    private String text;
    private String value;

    public MaskInfo(int remain, String text, String value) {
        this.remain = remain;
        this.text = text;
        this.value = value;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MaskInfo{" +
                "remain=" + remain +
                ", text='" + text + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
