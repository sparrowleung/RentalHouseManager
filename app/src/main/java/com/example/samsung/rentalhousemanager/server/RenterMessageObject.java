package com.example.samsung.rentalhousemanager.server;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by yuyang.liang on 2018/7/31.
 */

public class RenterMessageObject extends BmobObject {

    private String renterName;
    private String renterSex;
    private String renterPlace;
    private String renterPhone;
    private String renterJob;
    private String renterLiveNum;
    private String renterId;
    private String roomNum;
    private String userName;
    private BmobDate uploadAt;

    public void setUploadAt(BmobDate date) {
        uploadAt = date;
    }

    public BmobDate getUploadAt() {
        return uploadAt;
    }

    public void setUserName(String user) {
        userName = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setRenterName(String name) {
        renterName = name;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterSex(String sex) {
        renterSex = sex;
    }

    public String getRenterSex() {
        return renterSex;
    }

    public void setRenterPlace(String place) {
        renterPlace = place;
    }

    public String getRenterPlace() {
        return renterPlace;
    }

    public void setRenterPhone(String phone) {
        renterPhone = phone;
    }

    public String getRenterPhone() {
        return renterPhone;
    }

    public void setRenterJob(String job) {
        renterJob = job;
    }

    public String getRenterJob() {
        return renterJob;
    }

    public void setRenterLiveNum(String liveNum) {
        renterLiveNum = liveNum;
    }

    public String getRenterLiveNum() {
        return renterLiveNum;
    }

    public void setRenterId(String id) {
        renterId = id;
    }

    public String getRenterId() {
        return renterId;
    }

    public void setRoomNum(String room) {
        roomNum = room;
    }

    public String getRoomNum() {
        return roomNum;
    }
}
