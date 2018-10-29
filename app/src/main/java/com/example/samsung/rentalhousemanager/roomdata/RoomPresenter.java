package com.example.samsung.rentalhousemanager.roomdata;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuyang.liang on 2018/7/17.
 */

public abstract class RoomPresenter {

    static public RoomPresenter create(Context context, IResponse roomView, Map<String, Object> equalMap,
                                       Map<String, Object> lessMap, Map<String, Object> greatMap) {
        return new RoomPresenterImpl(context, roomView, equalMap, lessMap, greatMap);
    }

    public abstract List<Map<String, String>> getGroupList();

    public abstract List<List<Map<String, String>>> getChildList();

    public abstract void notifyRoomInfo(RoomDetailViewModel model);

    public abstract void notifyMoneyInfo(RoomDetailViewModel model);

    public abstract void onCreate(Bundle saveInstanceState);

    public abstract void onDestroy();

    public abstract boolean isNormal();
}

class RoomPresenterImpl extends RoomPresenter {
    private final static String TAG = RoomPresenterImpl.class.getSimpleName();

    private Context mContext;
    private RoomData mRoomData;
    private Map<String, Object> mLessMap = new HashMap<>();
    private Map<String, Object> mEqualMap = new HashMap<>();
    private Map<String, Object> mGreaterMap = new HashMap<>();
    private List<Map<String, String>> mGroupList;
    private List<List<Map<String, String>>> mChildList;

    RoomPresenterImpl(Context context, IResponse roomView, Map<String, Object> equalMap,
                      Map<String, Object> lessMap, Map<String, Object> greatMap) {
        mContext = context;
        mRoomData = new RoomData(roomView);
        mLessMap = lessMap;
        mEqualMap = equalMap;
        mGreaterMap = greatMap;
    }

    public void setGroupList() {
        for (int i = 0; i < 3; i++) {
            Map<String, String> map = new HashMap<>();
            switch (i) {
                case 0:
                    map.put("group", "租客信息");
                    break;
                case 1:
                    map.put("group", "房子信息");
                    break;
                case 2:
                    map.put("group", "费用信息");
                    break;
            }
            mGroupList.add(map);
        }
    }

    public void setChildList() {
        mChildList.add(mRoomData.getSelectDateRenterMessageChildList(mEqualMap, mLessMap, null));
        mChildList.add(mRoomData.getSelectDateRoomMessageChildList(mEqualMap, mLessMap, null));
        mChildList.add(mRoomData.getSelectDateRenterMoneyChildList(mEqualMap, mLessMap, mGreaterMap));
    }

    @Override
    public void notifyRoomInfo(RoomDetailViewModel model) {
        model.getNotifyRoomInfo(mRoomData);
    }

    @Override
    public void notifyMoneyInfo(RoomDetailViewModel model) {
        model.getNotifyMoneyInfo(mRoomData);
    }

    @Override
    public List<Map<String, String>> getGroupList() {
        return mGroupList;
    }

    @Override
    public List<List<Map<String, String>>> getChildList() {
        return mChildList;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        setGroupList();
        setChildList();
    }

    @Override
    public void onDestroy() {
        if (!mGroupList.isEmpty()) {
            mGroupList.clear();
        }
        if (!mChildList.isEmpty()) {
            mChildList.clear();
        }
        if (!mEqualMap.isEmpty()) {
            mEqualMap.clear();
        }
        if (!mLessMap.isEmpty()) {
            mLessMap.clear();
        }
        if (!mGreaterMap.isEmpty()) {
            mGreaterMap.clear();
        }
    }

    @Override
    public boolean isNormal() {
        return false;
    }
}
