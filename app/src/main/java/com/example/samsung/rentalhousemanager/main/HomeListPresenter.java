package com.example.samsung.rentalhousemanager.main;

import android.os.Bundle;
import android.util.Log;

import com.example.samsung.rentalhousemanager.server.FloorObject;
import com.example.samsung.rentalhousemanager.server.ServerRequest;
import com.example.samsung.rentalhousemanager.server.onFindResultsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;


/**
 * Created by yuyang.liang on 2018/7/16.
 */

public abstract class HomeListPresenter {
    static public HomeListPresenter create(IHomeView homeView) {
        return new HomeListPresenterImpl(homeView);
    }

    public abstract void onCreate(Bundle saveInstanceState, Bundle bundle);

    public abstract void onDestroy();

    public abstract void onFinished();
}

class HomeListPresenterImpl extends HomeListPresenter {
    private final static String TAG = HomeListPresenterImpl.class.getSimpleName();

    private Integer mFloorNum;
    private Integer mFloorRoom;
    public static Integer mFourRoom;
    public static Integer mFourFloor;

    private IHomeView mHomeView;

    HomeListPresenterImpl(IHomeView homeView) {
        mHomeView = homeView;
    }

    private void setFloor() {
        List<Map<String, Integer>> mFloorList = new ArrayList<>();
        mFloorList.clear();
        for (int i = 1; i <= mFloorNum; i++) {
            Map<String, Integer> map = new HashMap<>();
            map.put("group", i);
            mFloorList.add(map);
        }
        mHomeView.setFloor(mFloorList);
    }

    private void setChild() {
        List<List<Map<String, Integer>>> mChildList = new ArrayList<>();
        mChildList.clear();
        for (int j = 1; j <= mFloorNum; j++) {
            List<Map<String, Integer>> childList = new ArrayList<>();
            for (int i = 1; i <= mFloorRoom; i++) {
                Map<String, Integer> map = new HashMap<>();
                map.put("child", i);
                childList.add(map);
            }
            mChildList.add(childList);
        }
        mHomeView.setChildList(mChildList);
    }

    private void requestFromServer() {
        String userName = BmobUser.getCurrentUser().getUsername();
        Map<String, Object> map = new HashMap<>();
        map.put("userName", userName);

        ServerRequest.onFindRequest("-userName", 1, map, new onFindResultsListener<FloorObject>() {
            @Override
            public void onSuccess(List<FloorObject> list) {
                mFloorNum = list.get(0).getFloorNum();
                mFloorRoom = list.get(0).getRoomNum();
                mFourFloor = list.get(0).getFourFloor();
                mFourRoom = list.get(0).getFourRoom();

                Log.e(TAG, "Request FloorNum -> " + mFloorNum + " , FloorRoom -> " + mFloorRoom + " , FourFloor -> " + mFourFloor+ " , FourFloor -> " + mFourRoom);
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                Log.e(TAG, "Request FloorData errorCode -> " + errorCode + " , errorMessage -> " + errorMessage);
            }

            @Override
            public void onComplete(boolean normal) {
                if (normal) {
                    setFloor();
                    setChild();
                    mHomeView.setExpandList();
                } else {
                    mHomeView.getMsgFail();
                }
            }
        });
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public void onCreate(Bundle saveInstanceState, Bundle bundle) {
        if (bundle != null) {
            mFloorNum = bundle.getInt("floorNum");
            mFloorRoom = bundle.getInt("floorRoom");
            mFourFloor = bundle.getInt("fourFloor");
            mFourRoom = bundle.getInt("fourRoom");

            Log.e(TAG, " FloorNum -> " + mFloorNum + " , FloorRoom -> " + mFloorRoom + " , FourFloor -> " + mFourFloor+ " , FourFloor -> " + mFourRoom);
            setChild();
            setFloor();
            mHomeView.setExpandList();
        } else {
            requestFromServer();
        }
    }

    @Override
    public void onDestroy() {}

    @Override
    public void onFinished() {}
}
