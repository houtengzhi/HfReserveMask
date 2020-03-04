package com.yechy.hfreservemask.entry;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cloud on 2020-02-22.
 */
public class UserSendData {
    private String name;
    private String cardNum;
    private String phoneNum;
    private int reservationNum = 5;
    private String pharmacyName;
    private String pharmacyCode;
    private String pharmacyPhase;
    private String pharmacyPhaseName;
    private String captcha;

    private String timestamp;
    private String hash;

    public UserSendData() {
    }

    public UserSendData(User user, String pharmacyName,
                        String pharmacyCode, String pharmacyPhase, String pharmacyPhaseName,
                        String captcha) {
        this.name = user.getName();
        this.cardNum = user.getCardNum();
        this.phoneNum = user.getPhoneNum();
        this.pharmacyName = pharmacyName;
        this.pharmacyCode = pharmacyCode;
        this.pharmacyPhase = pharmacyPhase;
        this.pharmacyPhaseName = pharmacyPhaseName;
        this.captcha = captcha;
    }

    public UserSendData(String name, String cardNum, String phoneNum, String pharmacyName,
                        String pharmacyCode, String pharmacyPhase, String pharmacyPhaseName,
                        String captcha) {
        this.name = name;
        this.cardNum = cardNum;
        this.phoneNum = phoneNum;
        this.pharmacyName = pharmacyName;
        this.pharmacyCode = pharmacyCode;
        this.pharmacyPhase = pharmacyPhase;
        this.pharmacyPhaseName = pharmacyPhaseName;
        this.captcha = captcha;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getReservationNum() {
        return reservationNum;
    }

    public void setReservationNum(int reservationNum) {
        this.reservationNum = reservationNum;
    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getPharmacyCode() {
        return pharmacyCode;
    }

    public void setPharmacyCode(String pharmacyCode) {
        this.pharmacyCode = pharmacyCode;
    }

    public String getPharmacyPhase() {
        return pharmacyPhase;
    }

    public void setPharmacyPhase(String pharmacyPhase) {
        this.pharmacyPhase = pharmacyPhase;
    }

    public String getPharmacyPhaseName() {
        return pharmacyPhaseName;
    }

    public void setPharmacyPhaseName(String pharmacyPhaseName) {
        this.pharmacyPhaseName = pharmacyPhaseName;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @Override
    public String toString() {
        return "UserSendData{" +
                "name='" + name + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", reservationNum=" + reservationNum +
                ", pharmacyName='" + pharmacyName + '\'' +
                ", pharmacyCode='" + pharmacyCode + '\'' +
                ", pharmacyPhase='" + pharmacyPhase + '\'' +
                ", pharmacyPhaseName='" + pharmacyPhaseName + '\'' +
                ", captcha='" + captcha + '\'' +
                '}';
    }

    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("cardNo", cardNum);
            jsonObject.put("phone", phoneNum);
            jsonObject.put("reservationNum", reservationNum);
            jsonObject.put("pharmacyName", pharmacyName);
            jsonObject.put("pharmacyCode", pharmacyCode);
            jsonObject.put("pharmacyPhase", pharmacyPhase);
            jsonObject.put("pharmacyPhaseName", pharmacyPhaseName);
            jsonObject.put("captcha", captcha);
            jsonObject.put("timestamp", timestamp);
            jsonObject.put("hash", hash);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
