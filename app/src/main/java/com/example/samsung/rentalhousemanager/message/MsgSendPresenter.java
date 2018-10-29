package com.example.samsung.rentalhousemanager.message;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.samsung.rentalhousemanager.roomdata.IResponse;
import com.example.samsung.rentalhousemanager.toolclass.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by yuyang.liang on 2018/10/11.
 */

abstract class MsgSendPresenter {

    static public MsgSendPresenter create(Context context, IResponse response) {
        return new MsgSendPresenterImpl(context, response);
    }

    public abstract String getDateForNow();

    public abstract List<List<Map<String, String>>> getRenterAllMsg();

    public abstract void sendMsgForRental(List<String> sumList);

    public abstract void onCreate(Bundle saveInstanceState);

    public abstract void onDestroy();
}

class MsgSendPresenterImpl extends MsgSendPresenter {
    private static final String TAG = MsgSendPresenterImpl.class.getSimpleName();

    private Context mContext;
    private IResponse mResponse;
    private Map<String, Object> lessMap;
    private Map<String, Object> greatMap;
    private MsgSendData mMsgSendData;
    private List<List<Map<String, String>>> mRentalAllMsg = new ArrayList<>();

    MsgSendPresenterImpl(Context context, IResponse response) {
        mContext = context;
        mResponse = response;
    }

    @Override
    public void sendMsgForRental(List<String> sumDeposit) {
        for (int i = 0; i < mRentalAllMsg.get(1).size(); i++) {
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, new Intent(), 0);
            SmsManager smsManager = SmsManager.getDefault();
            String str = String.format("尊敬的%s房客，您在本月使用了%s度电，%s顿水，房租合计%s元。收到通知后，可以通过微信转账/现金支付。祝你有美好的一天。",
                    mRentalAllMsg.get(0).get(i).get("roomNum"),
                    mRentalAllMsg.get(1).get(i).get("electricUse"),
                    mRentalAllMsg.get(1).get(i).get("waterUse"),
                    sumDeposit.get(i));
            smsManager.sendTextMessage(mRentalAllMsg.get(0).get(i).get("phoneNum"), null, str, pendingIntent, null);
        }
    }

    @Override
    public List<List<Map<String, String>>> getRenterAllMsg() {
        mMsgSendData = new MsgSendData(mResponse, lessMap, greatMap);
        mRentalAllMsg.add(0, mMsgSendData.requestForRenterMsg());
        mRentalAllMsg.add(1, mMsgSendData.requestForRentalMsg());
        mRentalAllMsg.add(2, mMsgSendData.requestForRoomMsg());

        return mRentalAllMsg;
    }

    @Override
    public String getDateForNow() {
        new TimeUtil();
        String date = TimeUtil.date;
        lessMap = new HashMap<>();
        greatMap = new HashMap<>();
        lessMap.put("uploadAt", TimeUtil.dateFinish);
        greatMap.put("uploadAt", TimeUtil.dateStart);
        Log.e(TAG, "start -> " + TimeUtil.dateStart + " Finish -> " + TimeUtil.dateFinish);
        return date;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
    }

    @Override
    public void onDestroy() {
        if (!mRentalAllMsg.isEmpty()) {
            mRentalAllMsg.clear();
            mRentalAllMsg = null;
        }
        if (mMsgSendData != null) {
            mMsgSendData = null;
        }
    }
}
