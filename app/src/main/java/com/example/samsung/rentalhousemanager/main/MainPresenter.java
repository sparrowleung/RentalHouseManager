package com.example.samsung.rentalhousemanager.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.samsung.rentalhousemanager.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuyang.liang on 2018/7/11.
 */

public abstract class MainPresenter {


    static public MainPresenter create(IMainView mainView, Activity activity, Bundle bundle) {
        return new MainPresenterImpl(mainView, activity, bundle);
    }

    public abstract List<TabElement> getTabs();

    public abstract List<Map<String, Object>> getDrawer();

    public abstract void onCreate(Bundle saveInstanceState);

    public abstract void onResume();

    public abstract void onPause();

    public abstract void onStop();

    public abstract void onDestroy();

    public abstract void onFinish();
}

class MainPresenterImpl extends MainPresenter {
    private static final String TAG = "MainPresenterImpl";

    private Bundle mBundle;
    private Activity mActivity;
    private IMainView mMainView;

    private List<TabElement> mTabs = new ArrayList<>();
    private List<Map<String, Object>> mDrawers = new ArrayList<>();

    MainPresenterImpl(IMainView mainView, Activity activity, Bundle bundle) {
        mMainView = mainView;
        mActivity = activity;
        mBundle = bundle;
    }

    void setupTabs() {
        int index = 0;
        mTabs.clear();
        for (MainType type : MainType.values()) {
            if (MainType.getMainClass(type).getSimpleName().equals("HomeFragment")) {
                mTabs.add(TabElement.create(index++, MainType.getName(type), MainType.getMainClass(type), mBundle));
            } else {
                mTabs.add(TabElement.create(index++, MainType.getName(type), MainType.getMainClass(type), null));
            }
        }
    }

    void setDrawer() {
        mDrawers.clear();
        Map<String, Object> divider = new HashMap<>();
        divider.put("type", "divider");
        mDrawers.add(divider);

        mDrawers.addAll(getGeneralDrawerList());

        mDrawers.add(divider);

        int order = 0;
        for (MainType type : MainType.values()) {
            mDrawers.add(MainType.getDrawerItemMap(type, order++));
        }

        mDrawers.add(divider);
    }

    List<Map<String, Object>> getGeneralDrawerList() {
        List<Map<String, Object>> generalList = new ArrayList<>();
        Map<String, Object> noticeMap = new HashMap<>();
        noticeMap.put("name", R.string.notice);
        noticeMap.put("type", "single");
        noticeMap.put("actionLink", "voc://view/notice");
        noticeMap.put("icon", R.drawable.message);
        generalList.add(noticeMap);

        Map<String, Object> settingMap = new HashMap<>();
        settingMap.put("name", R.string.setting);
        settingMap.put("type", "single");
        settingMap.put("actionLink", "voc://view/setting");
        settingMap.put("icon", R.drawable.setting);
        generalList.add(settingMap);

        return generalList;
    }

    @Override
    public List<TabElement> getTabs() {
        return mTabs;
    }

    @Override
    public List<Map<String, Object>> getDrawer() {
        return mDrawers;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        Log.d(TAG, "onCreate");
        setupTabs();
        setDrawer();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        if (mDrawers != null) {
            mDrawers.clear();
        }

        if (mTabs != null) {
            mTabs.clear();
        }

        mActivity = null;
        mMainView = null;
    }

    @Override
    public void onFinish() {
        Log.d(TAG, "onFinish");
    }
}
