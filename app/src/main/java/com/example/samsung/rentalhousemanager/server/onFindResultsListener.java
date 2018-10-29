package com.example.samsung.rentalhousemanager.server;

import android.util.Log;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by yuyang.liang on 2018/7/31.
 */

public abstract class onFindResultsListener<T> extends FindListener<T> {

    public onFindResultsListener(){}

    public void done(List<T> var, BmobException var2) {
        if (var2 == null) {
            onSuccess(var);
            onComplete(true);
        } else {
            onFail(var2.getErrorCode(), var2.getMessage());
            onComplete(false);
        }
    }

    public abstract void onSuccess(List<T> list);
    public abstract void onFail(int errorCode, String errorMessage);
    public abstract void onComplete(boolean normal);
}
