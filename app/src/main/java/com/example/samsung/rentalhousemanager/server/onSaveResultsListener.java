package com.example.samsung.rentalhousemanager.server;


/**
 * Created by yuyang.liang on 2018/7/31.
 */

public abstract class onSaveResultsListener {

    public onSaveResultsListener() {}


    public abstract void onSuccess(String objectId);
    public abstract void onFail(int errorCode, String errorMessage);
}
