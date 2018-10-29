package com.example.samsung.rentalhousemanager.message;

import android.util.Log;

import com.example.samsung.rentalhousemanager.roomdata.IResponse;
import com.example.samsung.rentalhousemanager.server.RentalMoneyObject;
import com.example.samsung.rentalhousemanager.server.RenterMessageObject;
import com.example.samsung.rentalhousemanager.server.RoomMessageObject;
import com.example.samsung.rentalhousemanager.server.ServerRequest;
import com.example.samsung.rentalhousemanager.server.onFindResultsListener;
import com.example.samsung.rentalhousemanager.toolclass.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;

/**
 * Created by yuyang.liang on 2018/10/11.
 */

public class MsgSendData {
    private final static String TAG = MsgSendData.class.getSimpleName();

    private IResponse mResponse;
    private int finishFlag = 0;
    private Map<String, Object> equalMap = new HashMap<>();
    private Map<String, Object> lessMap = new HashMap<>();
    private Map<String, Object> greatMap = new HashMap<>();

    public MsgSendData(IResponse response, Map<String, Object> map, Map<String, Object> map1) {
        lessMap = map;
        greatMap = map1;
        mResponse = response;
        equalMap.put("userName", BmobUser.getCurrentUser().getUsername());
    }

    public List<Map<String,String>> requestForRenterMsg() {
        final List<Map<String,String>> renterMsgList = new ArrayList<>();
        ServerRequest.onFindRequest("roomNum,-uploadAt", 50, equalMap, lessMap, null, new onFindResultsListener<RenterMessageObject>() {
            @Override
            public void onSuccess(List<RenterMessageObject> list) {
                String num = "";
                for (RenterMessageObject object : list) {
                    if (!num.equals(object.getRoomNum())) {
                        Map<String, String> msgRenter = new HashMap<String, String>();
                        msgRenter.put("roomNum", object.getRoomNum());
                        msgRenter.put("name", object.getRenterName());
                        msgRenter.put("phoneNum", object.getRenterPhone());
                        renterMsgList.add(msgRenter);
                        num = object.getRoomNum();
                    }
                }
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "RenterMsg error : errorCode -> " + errorCode + " ; errorMessage -> " + errorMessage);
            }

            @Override
            public void onComplete(boolean normal) {
                if (normal) {
                    getProgress(1);
                } else {
                    mResponse.onFail("MsgSend");
                }
            }
        });
        return renterMsgList;
    }

    public List<Map<String,String>> requestForRentalMsg() {
        final List<Map<String,String>> rentalMsgList = new ArrayList<>();
        ServerRequest.onFindRequest("roomNum,-uploadAt", 50, equalMap, lessMap, greatMap, new onFindResultsListener<RentalMoneyObject>() {
            @Override
            public void onSuccess(List<RentalMoneyObject> list) {
                String num = "";
                for (RentalMoneyObject object : list) {
                    if (!num.equals(object.getRoomNum())) {
                        Map<String, String> msgRental = new HashMap<String, String>();
                        msgRental.put("waterUse", object.getWaterUse());
                        msgRental.put("electricUse", object.getElectricUse());
                        msgRental.put("airUse", object.getAirUse());
                        rentalMsgList.add(msgRental);
                        num = object.getRoomNum();
                    }
                }
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "RentalMsg error : errorCode -> " + errorCode + " ; errorMessage -> " + errorMessage);
            }

            @Override
            public void onComplete(boolean normal) {
                if (normal) {
                    getProgress(2);
                } else {
                    mResponse.onFail("MsgSend");
                }
            }
        });
        return rentalMsgList;
    }

    public List<Map<String,String>> requestForRoomMsg() {
        final List<Map<String,String>> roomMsgList = new ArrayList<>();
        ServerRequest.onFindRequest("roomNum,-uploadAt", 50, equalMap, lessMap, null, new onFindResultsListener<RoomMessageObject>() {
            @Override
            public void onSuccess(List<RoomMessageObject> list) {
                String num = "";
                for (RoomMessageObject object : list) {
                    if (!num.equals(object.getRoomNum())) {
                        Map<String, String> msgRental = new HashMap<String, String>();
                        msgRental.put("air", object.getRoomAir());
                        msgRental.put("monthly", object.getRoomMonthly());
                        msgRental.put("water", object.getRoomWater());
                        msgRental.put("electric", object.getRoomElectric());
                        msgRental.put("manage", object.getRoomManage());
                        msgRental.put("other", object.getRoomOther());
                        roomMsgList.add(msgRental);
                        num = object.getRoomNum();
                    }
                }
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "RoomMsg error : errorCode -> " + errorCode + " ; errorMessage -> " + errorMessage);
            }

            @Override
            public void onComplete(boolean normal) {
                if (normal) {
                    getProgress(3);
                } else {
                    mResponse.onFail("MsgSend");
                }
            }
        });
        return roomMsgList;
    }

    public void getProgress(int flag) {
        finishFlag += flag;
        if (finishFlag == 6) {
            mResponse.onSuccess("MsgSend");
        }
    }

}
