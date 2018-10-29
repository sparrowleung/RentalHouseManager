package com.example.samsung.rentalhousemanager.server;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by yuyang.liang on 2018/8/1.
 */

public class RoomMessageObject extends BmobObject {

    private String roomNum;
    private String roomTenancy;
    private String roomDeposit;
    private String roomMonthly;
    private String roomDate;
    private String roomWater;
    private String roomElectric;
    private String roomAir;
    private String roomManage;
    private String roomOther;
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

    public void setRoomNum(String houseNum) {
        roomNum = houseNum;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomTenancy(String tenancy) {
        roomTenancy = tenancy;
    }

    public String getRoomTenancy() {
        return roomTenancy;
    }

    public void setRoomDeposit(String deposit) {
        roomDeposit = deposit;
    }

    public String getRoomDeposit() {
        return roomDeposit;
    }

    public void setRoomMonthly(String monthly) {
        roomMonthly = monthly;
    }

    public String getRoomMonthly() {
        return roomMonthly;
    }

    public void setRoomDate(String date) {
        roomDate = date;
    }

    public String getRoomDate() {
        return roomDate;
    }

    public void setRoomWater(String water) {
        roomWater = water;
    }

    public String getRoomWater() {
        return roomWater;
    }

    public void setRoomElectric(String electric) {
        roomElectric = electric;
    }

    public String getRoomElectric() {
        return roomElectric;
    }

    public void setRoomAir(String air) {
        roomAir = air;
    }

    public String getRoomAir() {
        return roomAir;
    }

    public void setRoomManage(String manage) {
        roomManage = manage;
    }

    public String getRoomManage() {
        return roomManage;
    }

    public void setRoomOther(String other) {
        roomOther = other;
    }

    public String getRoomOther() {
        return roomOther;
    }
}
