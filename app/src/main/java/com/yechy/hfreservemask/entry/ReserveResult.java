package com.yechy.hfreservemask.entry;

/**
 * Created by cloud on 2020-03-05.
 */
public class ReserveResult {
    private String msg;
    private boolean succeed;
    private int status;

    public ReserveResult(String msg, boolean succeed, int status) {
        this.msg = msg;
        this.succeed = succeed;
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSucceed() {
        return succeed;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ReserveResult{" +
                "msg='" + msg + '\'' +
                ", succeed=" + succeed +
                ", status=" + status +
                '}';
    }
}
