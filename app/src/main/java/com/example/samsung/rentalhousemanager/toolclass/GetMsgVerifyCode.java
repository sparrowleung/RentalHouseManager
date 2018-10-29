package com.example.samsung.rentalhousemanager.toolclass;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.samsung.rentalhousemanager.disclaimer.NewDisclaimerViewModel;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by yuyang.liang on 2018/9/6.
 */

public class GetMsgVerifyCode {

    private final static String TAG = GetMsgVerifyCode.class.getSimpleName();

    private EventHandler eventHandler = new EventHandler() {
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;

                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {

                        } else {
                            ((Throwable) data).printStackTrace();
                        }
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {

                        } else {
                            ((Throwable) data).printStackTrace();
                        }
                    }
                    return false;
                }
            }).sendMessage(msg);
        }
    };

    public GetMsgVerifyCode(String phone) {
        SMSSDK.getVerificationCode("86", phone);
        SMSSDK.registerEventHandler(eventHandler);
    }

    public GetMsgVerifyCode(String phone, String code) {
        SMSSDK.submitVerificationCode("86", phone, code);
        SMSSDK.registerEventHandler(eventHandler);
    }


}
