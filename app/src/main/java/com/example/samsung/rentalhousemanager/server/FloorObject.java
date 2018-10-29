package com.example.samsung.rentalhousemanager.server;

import cn.bmob.v3.BmobObject;

/**
 * Created by yuyang.liang on 2018/7/31.
 */

public class FloorObject extends BmobObject {
    private Integer floorNum;
    private Integer roomNum;
    private Integer fourFloor;
    private Integer fourRoom;
    private String userName;

    public void setUserName(String user) {
        userName = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public Integer getFloorNum() {
        return floorNum;
    }

    public Integer getRoomNum() {
        return roomNum;
    }

    public void setFourFloor(Integer four) {
        fourFloor = four;
    }

    public Integer getFourFloor() {
        return fourFloor;
    }

    public void setFourRoom(Integer four) {
        fourRoom = four;
    }

    public Integer getFourRoom() {
        return fourRoom;
    }

}
