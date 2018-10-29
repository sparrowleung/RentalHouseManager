package com.example.samsung.rentalhousemanager.roomsetting;

import android.util.Log;
import android.widget.EditText;

import com.example.samsung.rentalhousemanager.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by yuyang.liang on 2018/8/3.
 */

public class RoomDataModel {
    private final static String TAG = RoomDataModel.class.getSimpleName();

    private String Name;
    private String Sex;
    private String Place;
    private String Phone;
    private String Job;
    private String LiveNum;
    private String Id;
    private String renterObjectId;

    private String RoomNum;
    private String Monthly;
    private String Deposit;
    private String Tenancy;
    private String InDate;
    private String Manage;
    private String Other;
    private String roomObjectId;

    private String Electric;
    private String ElectricUse;
    private String Water;
    private String WaterUse;
    private String Air;
    private String AirUse;
    private String useObjectId;

    private Date DateText;

    public RoomDataModel() {
    }

    public RoomDataModel getInstance() {
        return new RoomDataModel();
    }

    private static class Holder {
        private static RoomDataModel INSTANCE = new RoomDataModel();
    }

    public void set(EditText editText, String text) {
        switch (editText.getId()) {
            case R.id.setting_name:
                setName(text);
                break;
            case R.id.setting_sex:
                setSex(text);
                break;
            case R.id.setting_place:
                setPlace(text);
                break;
            case R.id.setting_phone:
                setPhone(text);
                break;
            case R.id.setting_job:
                setJob(text);
                break;
            case R.id.setting_number:
                setLiveNum(text);
                break;
            case R.id.setting_id:
                setId(text);
                break;
            case R.id.setting_monthly:
                setMonthly(text);
                break;
            case R.id.setting_deposit:
                setDeposit(text);
                break;
            case R.id.setting_tenancy:
                setTenancy(text);
                break;
            case R.id.setting_date:
                setInDate(text);
                break;
            case R.id.setting_manage:
                setManage(text);
                break;
            case R.id.setting_other:
                setOther(text);
                break;
            case R.id.setting_electric:
                setElectric(text);
                break;
            case R.id.setting_electricUse:
                setElectricUse(text);
                break;
            case R.id.setting_water:
                setWater(text);
                break;
            case R.id.setting_waterUse:
                setWaterUse(text);
                break;
            case R.id.setting_air:
                setAir(text);
                break;
            case R.id.setting_airUse:
                setAirUse(text);
                break;
        }
    }

    public List<Map<String, Object>> getAll(String roomNum) {
        String userName = BmobUser.getCurrentUser().getUsername();
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("renterName", getName());
        map1.put("renterSex", getSex());
        map1.put("renterPlace", getPlace());
        map1.put("renterPhone", getPhone());
        map1.put("renterJob", getJob());
        map1.put("renterLiveNum", getLiveNum());
        map1.put("renterId", getId());
        map1.put("roomNum", roomNum);
        map1.put("uploadAt", getDateText());
        map1.put("userName", userName);
        mapList.add(map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("roomNum", roomNum);
        map2.put("roomMonthly", getMonthly());
        map2.put("roomDeposit", getDeposit());
        map2.put("roomTenancy", getTenancy());
        map2.put("roomDate", getInDate());
        map2.put("roomManage", getManage());
        map2.put("roomOther", getOther());
        map2.put("roomElectric", getElectric());
        map2.put("roomWater", getWater());
        map2.put("roomAir", getAir());
        map2.put("uploadAt", getDateText());
        map2.put("userName", userName);
        mapList.add(map2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("roomNum", roomNum);
        map3.put("electricUse", getElectricUse());
        map3.put("waterUse", getWaterUse());
        map3.put("airUse", getAirUse());
        map3.put("uploadAt", getDateText());
        map3.put("userName", userName);
        mapList.add(map3);

        return mapList;
    }

    public List<Map<String, String>> getNotifyRoomInfo() {
        String userName = BmobUser.getCurrentUser().getUsername();
        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("renterName", getName());
        map1.put("renterSex", getSex());
        map1.put("renterPlace", getPlace());
        map1.put("renterPhone", getPhone());
        map1.put("renterJob", getJob());
        map1.put("renterLiveNum", getLiveNum());
        map1.put("renterId", getId());
        map1.put("roomNum", getRoomNum());
        map1.put("objectId", getRenterObjectId());
        map1.put("userName", userName);
        mapList.add(map1);

        Map<String, String> map2 = new HashMap<>();
        map2.put("roomNum", getRoomNum());
        map2.put("roomMonthly", getMonthly());
        map2.put("roomDeposit", getDeposit());
        map2.put("roomTenancy", getTenancy());
        map2.put("roomDate", getInDate());
        map2.put("roomManage", getManage());
        map2.put("roomOther", getOther());
        map2.put("roomElectric", getElectric());
        map2.put("roomWater", getWater());
        map2.put("roomAir", getAir());
        map2.put("objectId", getRoomObjectId());
        map2.put("userName", userName);
        mapList.add(map2);

        return mapList;
    }

    public List<Map<String, String>> getNotifyMoneyInfo() {
        String userName = BmobUser.getCurrentUser().getUsername();
        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("roomNum", getRoomNum());
        map.put("electricUse", getElectricUse());
        map.put("waterUse", getWaterUse());
        map.put("airUse", getAirUse());
        map.put("objectId", getUseObjectId());
        map.put("userName", userName);
        mapList.add(map);

        return mapList;
    }

    public RoomDataModel getSingleton() {
        return Holder.INSTANCE;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getSex() {
        return Sex;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getPlace() {
        return Place;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPhone() {
        return Phone;
    }

    public void setJob(String job) {
        Job = job;
    }

    public String getJob() {
        return Job;
    }

    public void setLiveNum(String liveNum) {
        LiveNum = liveNum;
    }

    public String getLiveNum() {
        return LiveNum;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public void setRoomNum(String roomNum) {
        RoomNum = roomNum;
    }

    public String getRoomNum() {
        return RoomNum;
    }

    public void setMonthly(String monthly) {
        Monthly = monthly;
    }

    public String getMonthly() {
        return Monthly;
    }

    public void setDeposit(String deposit) {
        Deposit = deposit;
    }

    public String getDeposit() {
        return Deposit;
    }

    public void setTenancy(String tenancy) {
        Tenancy = tenancy;
    }

    public String getTenancy() {
        return Tenancy;
    }

    public void setInDate(String inDate) {
        InDate = inDate;
    }

    public String getInDate() {
        return InDate;
    }

    public void setManage(String manage) {
        Manage = manage;
    }

    public String getManage() {
        return Manage;
    }

    public void setOther(String other) {
        Other = other;
    }

    public String getOther() {
        return Other;
    }

    public void setElectric(String electric) {
        Electric = electric;
    }

    public String getElectric() {
        return Electric;
    }

    public void setElectricUse(String electricUse) {
        ElectricUse = electricUse;
    }

    public String getElectricUse() {
        return ElectricUse;
    }

    public void setWater(String water) {
        Water = water;
    }

    public String getWater() {
        return Water;
    }

    public void setWaterUse(String waterUse) {
        WaterUse = waterUse;
    }

    public String getWaterUse() {
        return WaterUse;
    }

    public void setAir(String air) {
        Air = air;
    }

    public String getAir() {
        return Air;
    }

    public void setAirUse(String airUse) {
        AirUse = airUse;
    }

    public String getAirUse() {
        return AirUse;
    }

    public void setRenterObjectId(String id) {
        renterObjectId = id;
    }

    public String getRenterObjectId() {
        return renterObjectId;
    }

    public void setRoomObjectId(String id) {
        roomObjectId = id;
    }

    public String getRoomObjectId() {
        return roomObjectId;
    }

    public void setUseObjectId(String id) {
        useObjectId = id;
    }

    public String getUseObjectId() {
        return useObjectId;
    }

    public void setDateText(Date dateText) {
        DateText = dateText;
    }

    public Date getDateText() {
        return DateText;
    }

}
