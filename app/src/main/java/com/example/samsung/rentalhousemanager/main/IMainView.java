package com.example.samsung.rentalhousemanager.main;

/**
 * Created by yuyang.liang on 2018/7/11.
 */

public interface IMainView {
    void setupActionBar();

    void setupTabLayout();

    void setupDrawer();

    void openDrawer();

    void closeDrawer();

    boolean isFinished();
}
