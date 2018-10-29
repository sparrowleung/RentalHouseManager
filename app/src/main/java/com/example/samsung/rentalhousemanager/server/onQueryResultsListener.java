package com.example.samsung.rentalhousemanager.server;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by yuyang.liang on 2018/7/31.
 */

public abstract class onQueryResultsListener<T> extends QueryListener<T> {

    public onQueryResultsListener(){}

    public void done(T var, BmobException var2) {
        if (var2 == null) {
            onSuccess(var);
            onComplete(true);
        } else {
            onFail(var2.getErrorCode(), var2.getMessage());
            onComplete(false);
        }
    }

    public abstract void onSuccess(T t);
    public abstract void onFail(int errorCode, String errorMessage);
    public abstract void onComplete(boolean normal);
}
