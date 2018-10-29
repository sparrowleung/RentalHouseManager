package com.example.samsung.rentalhousemanager.server;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by yuyang.liang on 2018/8/2.
 */

public class RentalMoneyObject extends BmobObject {
    private String waterUse;
    private String electricUse;
    private String airUse;
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

    public void setRoomNum(String num) {
        roomNum = num;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setWaterUse(String water) {
        waterUse = water;
    }

    public String getWaterUse() {
        return waterUse;
    }

    public void setElectricUse(String electric) {
        electricUse = electric;
    }

    public String getElectricUse() {
        return electricUse;
    }

    public void setAirUse(String air) {
        airUse = air;
    }

    public String getAirUse() {
        return airUse;
    }

}
