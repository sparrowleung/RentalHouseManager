package com.example.samsung.rentalhousemanager.main;

import android.widget.ExpandableListView;

/**
 * Created by yuyang.liang on 2018/7/11.
 */

public interface IMainView {
    void setupActionBar();

    void setupTabLayout();

    void setupDrawer(ExpandableListView listView);

    void openDrawer();

    void closeDrawer();

    boolean isFinished();
}
