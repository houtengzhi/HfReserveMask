package com.yechy.hfreservemask.entry;

/**
 * Created by cloud on 2020-02-22.
 */
public class Pharmacy {
    private String name;
    private int code;

    private MaskInfo maskInfo;

    public Pharmacy(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public MaskInfo getMaskInfo() {
        return maskInfo;
    }

    public void setMaskInfo(MaskInfo maskInfo) {
        this.maskInfo = maskInfo;
    }

    @Override
    public String toString() {
        return "Pharmacy{" +
                "name='" + name + '\'' +
                ", code=" + code +
                '}';
    }
}
