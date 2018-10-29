package com.example.samsung.rentalhousemanager.disclaimer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuyang.liang on 2018/8/24.
 */

public class NewDisclaimerModel {

    private String account;
    private String password;
    private String verifyCode;
    private String floorNum;
    private String floorRoom;

    public void setFloorNum(String num) {
        floorNum = num;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorRoom(String room) {
        floorRoom = room;
    }

    public String getFloorRoom() {
        return floorRoom;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setVerifyCode(String code) {
        this.verifyCode = code;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

}
