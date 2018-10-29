package com.example.samsung.rentalhousemanager.roomdata;

/**
 * Created by yuyang.liang on 2018/8/2.
 */

public interface IResponse {
    void onFail(String type);
    void onSuccess(String type);
    void onComplete(boolean normal);
}
