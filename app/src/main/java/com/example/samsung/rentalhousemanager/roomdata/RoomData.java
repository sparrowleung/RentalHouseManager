package com.example.samsung.rentalhousemanager.roomdata;

import android.util.Log;

import com.example.samsung.rentalhousemanager.server.RentalMoneyObject;
import com.example.samsung.rentalhousemanager.server.RenterMessageObject;
import com.example.samsung.rentalhousemanager.server.RoomMessageObject;
import com.example.samsung.rentalhousemanager.server.ServerRequest;
import com.example.samsung.rentalhousemanager.server.onFindResultsListener;
import com.example.samsung.rentalhousemanager.server.onUpdateResultsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuyang.liang on 2018/7/17.
 */

public class RoomData {
    public static final String TAG = RoomData.class.getSimpleName();

    private List<Map<String, Object>> mFloorGroup = new ArrayList<>();
    private List<Map<String, Object>> mFloorChild = new ArrayList<>();
    private List<Map<String, String>> mRoomGroup = new ArrayList<>();
    private List<Map<String, String>> mRoomMessageChild;
    private List<Map<String, String>> mRenterMessageChild;
    private List<Map<String, String>> mRentalMoneyChild;

    public static final int UPDATE_ROOM_SUCCESS = 1;
    public static final int UPDATE_RENTER_SUCCESS = 2;
    public static final int UPDATE_RENTAL_SUCCESS = 3;

    private boolean mRoomRequest = false;
    private boolean mRenterRequest = false;
    private boolean mMoneyRequest = false;
    private int mRequestTime = 0;
    private IResponse mResponse;

    public RoomData(IResponse response) {
        mResponse = response;
    }

    List<Map<String, Object>> getFloorGroupList() {
        return mFloorGroup;
    }

    List<Map<String, Object>> getFloorChildList() {
        return mFloorChild;
    }

    List<Map<String, String>> getRoomGroupList() {
        return mRoomGroup;
    }

    List<Map<String, String>> getRoomMessageChildList(Map<String, Object> map) {
        mRoomMessageChild = new ArrayList<>();
        ServerRequest.onFindRequest("-uploadAt", 1, map, new onFindResultsListener<RoomMessageObject>() {
            @Override
            public void onSuccess(List<RoomMessageObject> list) {
                Map<String, String> message = new HashMap<String, String>();
                message.put("air", list.get(0).getRoomAir());
                message.put("num", list.get(0).getRoomNum());
                message.put("tenancy", list.get(0).getRoomTenancy());
                message.put("deposit", list.get(0).getRoomDeposit());
                message.put("monthly", list.get(0).getRoomMonthly());
                message.put("date", list.get(0).getRoomDate());
                message.put("water", list.get(0).getRoomWater());
                message.put("electric", list.get(0).getRoomElectric());
                message.put("manage", list.get(0).getRoomManage());
                message.put("other", list.get(0).getRoomOther());
                message.put("objectId", list.get(0).getObjectId());
                mRoomMessageChild.add(message);
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "RoomRequest Fail : errorCode -> " + errorCode + "; errorMessage -> " + errorMessage);
            }

            @Override
            public void onComplete(boolean normal) {
                Log.e(TAG, "RoomRequest normal -> " + normal);
                mRoomRequest = normal;
                getRequestState(1);
            }
        });
        return mRoomMessageChild;
    }

    List<Map<String, String>> getSelectDateRoomMessageChildList(Map<String, Object> equalMap, Map<String, Object> lessMap, Map<String, Object> greaterMap) {
        mRoomMessageChild = new ArrayList<>();
        ServerRequest.onFindRequest("-uploadAt", 1, equalMap, lessMap, greaterMap, new onFindResultsListener<RoomMessageObject>() {
            @Override
            public void onSuccess(List<RoomMessageObject> list) {
                Map<String, String> message = new HashMap<String, String>();
                message.put("air", list.get(0).getRoomAir());
                message.put("num", list.get(0).getRoomNum());
                message.put("tenancy", list.get(0).getRoomTenancy());
                message.put("deposit", list.get(0).getRoomDeposit());
                message.put("monthly", list.get(0).getRoomMonthly());
                message.put("date", list.get(0).getRoomDate());
                message.put("water", list.get(0).getRoomWater());
                message.put("electric", list.get(0).getRoomElectric());
                message.put("manage", list.get(0).getRoomManage());
                message.put("other", list.get(0).getRoomOther());
                message.put("objectId", list.get(0).getObjectId());
                mRoomMessageChild.add(message);
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "Room Request Fail : errorCode -> " + errorCode + "; errorMessage -> " + errorMessage);
            }

            @Override
            public void onComplete(boolean normal) {
                Log.e(TAG, "Room Request normal -> " + normal);
                mRoomRequest = normal;
                getRequestState(1);
            }
        });

        return mRoomMessageChild;
    }

    List<Map<String, String>> getRenterMessageChildList(Map<String, Object> map) {
        mRenterMessageChild = new ArrayList<>();
        ServerRequest.onFindRequest("-uploadAt", 1, map, new onFindResultsListener<RenterMessageObject>() {
            @Override
            public void onSuccess(List<RenterMessageObject> list) {
                Map<String, String> message = new HashMap<String, String>();
                message.put("id", list.get(0).getRenterId());
                message.put("name", list.get(0).getRenterName());
                message.put("sex", list.get(0).getRenterSex());
                message.put("place", list.get(0).getRenterPlace());
                message.put("phone", list.get(0).getRenterPhone());
                message.put("job", list.get(0).getRenterJob());
                message.put("liveNum", list.get(0).getRenterLiveNum());
                message.put("num", list.get(0).getRoomNum());
                message.put("objectId", list.get(0).getObjectId());
                mRenterMessageChild.add(message);
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "RenterRequest Fail : errorCode -> " + errorCode + "; errorMessage -> " + errorMessage);
            }

            @Override
            public void onComplete(boolean normal) {
                Log.e(TAG, "RenterRequest normal -> " + normal);
                mRenterRequest = normal;
                getRequestState(2);
            }
        });
        return mRenterMessageChild;
    }

    List<Map<String, String>> getSelectDateRenterMessageChildList(Map<String, Object> equalMap, Map<String, Object> lessMap, Map<String, Object> greaterMap) {
        mRenterMessageChild = new ArrayList<>();
        ServerRequest.onFindRequest("-uploadAt", 1, equalMap, lessMap, greaterMap, new onFindResultsListener<RenterMessageObject>() {
            @Override
            public void onSuccess(List<RenterMessageObject> list) {
                Map<String, String> message = new HashMap<String, String>();
                message.put("id", list.get(0).getRenterId());
                message.put("name", list.get(0).getRenterName());
                message.put("sex", list.get(0).getRenterSex());
                message.put("place", list.get(0).getRenterPlace());
                message.put("phone", list.get(0).getRenterPhone());
                message.put("job", list.get(0).getRenterJob());
                message.put("liveNum", list.get(0).getRenterLiveNum());
                message.put("num", list.get(0).getRoomNum());
                message.put("objectId", list.get(0).getObjectId());
                mRenterMessageChild.add(message);
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "Renter Request Fail : errorCode -> " + errorCode + "; errorMessage -> " + errorMessage);
            }

            @Override
            public void onComplete(boolean normal) {
                Log.e(TAG, "Renter Request normal -> " + normal);
                mRenterRequest = normal;
                getRequestState(2);
            }
        });

        return mRenterMessageChild;
    }

    List<Map<String, String>> getRenterMoneyChildList(Map<String, Object> map) {
        mRentalMoneyChild = new ArrayList<>();
        ServerRequest.onFindRequest("-uploadAt", 1, map, new onFindResultsListener<RentalMoneyObject>() {
            @Override
            public void onSuccess(List<RentalMoneyObject> list) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("waterUse", list.get(0).getWaterUse());
                map.put("electricUse", list.get(0).getElectricUse());
                map.put("airUse", list.get(0).getAirUse());
                map.put("objectId", list.get(0).getObjectId());
                mRentalMoneyChild.add(map);
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "MoneyRequest Fail : errorCode -> " + errorCode + "; errorMessage -> " + errorMessage);
            }

            @Override
            public void onComplete(boolean normal) {
                Log.e(TAG, "MoneyRequest normal -> " + normal);
                mMoneyRequest = normal;
                getRequestState(3);
            }
        });

        return mRentalMoneyChild;
    }

    List<Map<String, String>> getSelectDateRenterMoneyChildList(Map<String, Object> equalMap, Map<String, Object> lessMap, Map<String, Object> greaterMap) {
        mRentalMoneyChild = new ArrayList<>();
        ServerRequest.onFindRequest("-uploadAt", 1, equalMap, lessMap, greaterMap, new onFindResultsListener<RentalMoneyObject>() {
            @Override
            public void onSuccess(List<RentalMoneyObject> list) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("waterUse", list.get(0).getWaterUse());
                map.put("electricUse", list.get(0).getElectricUse());
                map.put("airUse", list.get(0).getAirUse());
                map.put("objectId", list.get(0).getObjectId());
                mRentalMoneyChild.add(map);
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "Money Request Fail : errorCode -> " + errorCode + "; errorMessage -> " + errorMessage);
            }

            @Override
            public void onComplete(boolean normal) {
                Log.e(TAG, "Money Request normal -> " + normal);
                mMoneyRequest = normal;
                getRequestState(3);
            }
        });

        return mRentalMoneyChild;
    }

    public void getRequestState(int time) {
        mRequestTime += time;
        if (mRequestTime == 6) {
            if (!mRoomRequest && !mRenterRequest) {
                mResponse.onComplete(false);
            } else {
                mResponse.onComplete(true);
            }
            mRequestTime = 0;
        }
    }

    public void updateRoomMessageInfo(List<Map<String, String>> list) {
        RoomMessageObject roomMessageObject = new RoomMessageObject();
        String objectId = "";
        for (Map.Entry<String, String> map : list.get(1).entrySet()) {
            if (!map.getKey().equals("objectId")) {
                roomMessageObject.setValue(map.getKey(), map.getValue());
            } else {
                objectId = map.getValue();
            }
        }
        ServerRequest.onUpdateRequest(roomMessageObject, objectId, new onUpdateResultsListener() {
            @Override
            public void onSuccess(String objectId) {
                Log.e(TAG, "updateRoomMessage Success: ObjectId -> " + objectId);
                mResponse.onSuccess(UPDATE_ROOM_SUCCESS);
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "updateRoomMessage Fail: errorCode -> " + errorCode + "; errorMessage -> " + errorMessage);
            }
        });
    }

    public void updateRenterMessageInfo(List<Map<String, String>> list) {
        RenterMessageObject renterMessageObject = new RenterMessageObject();
        String objectId = "";
        for (Map.Entry<String, String> map : list.get(0).entrySet()) {
            if (!map.getKey().equals("objectId")) {
                renterMessageObject.setValue(map.getKey(), map.getValue());
            } else {
                objectId = map.getValue();
            }
        }
        ServerRequest.onUpdateRequest(renterMessageObject, objectId, new onUpdateResultsListener() {
            @Override
            public void onSuccess(String objectId) {
                Log.e(TAG, "updateRenterMessage Success: ObjectId -> " + objectId);
                mResponse.onSuccess(UPDATE_RENTER_SUCCESS);
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "updateRenterMessage Fail: errorCode -> " + errorCode + "; errorMessage -> " + errorMessage);
            }
        });
    }

    public void updateMoneyMessageInfo(List<Map<String, String>> list) {
        RentalMoneyObject rentalMoneyObject = new RentalMoneyObject();
        String objectId = "";
        for (Map.Entry<String, String> map : list.get(0).entrySet()) {
            if (!map.getKey().equals("objectId")) {
                rentalMoneyObject.setValue(map.getKey(), map.getValue());
            } else {
                objectId = map.getValue();
            }
        }
        ServerRequest.onUpdateRequest(rentalMoneyObject, objectId, new onUpdateResultsListener() {
            @Override
            public void onSuccess(String objectId) {
                Log.e(TAG, "updateMoneyMessage Success: ObjectId -> " + objectId);
                mResponse.onSuccess(UPDATE_RENTAL_SUCCESS);
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "updateMoneyMessage Fail: errorCode -> " + errorCode + "; errorMessage -> " + errorMessage);
            }
        });
    }

    public void updateRoomInfo(List<Map<String, String>> mapList) {
        if (!mapList.isEmpty()) {
            updateRoomMessageInfo(mapList);
            updateRenterMessageInfo(mapList);
        }
    }

    public void updateMoneyInfo(List<Map<String, String>> mapList) {
        if (!mapList.isEmpty()) {
            updateMoneyMessageInfo(mapList);
        }
    }
}
