package com.yechy.hfreservemask.entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cloud on 2020-02-22.
 */
public class User {
    private String name;
    private String cardNum;
    private String phoneNum;
    private int reservationNum = 5;
    private List<Pharmacy> pharmacies;

    public User(String name, String cardNum, String phoneNum, List<Pharmacy> pharmacies) {
        this.name = name;
        this.cardNum = cardNum;
        this.phoneNum = phoneNum;
        this.pharmacies = pharmacies;
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

    public List<Pharmacy> getPharmacies() {
        return pharmacies;
    }

    public void setPharmacies(List<Pharmacy> pharmacies) {
        this.pharmacies = pharmacies;
    }

    public List<UserSendData> toSendData() {
        List<UserSendData> userSendDataList = new ArrayList<>();
        for (Pharmacy pharmacy : pharmacies) {
            UserSendData sendData = new UserSendData();
        }

        return userSendDataList;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
