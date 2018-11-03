package com.example.samsung.rentalhousemanager.message;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.roomdata.IResponse;
import com.example.samsung.rentalhousemanager.toolclass.RHToast;
import com.example.samsung.rentalhousemanager.toolclass.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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

    public abstract void onStop();
}

class MsgSendPresenterImpl extends MsgSendPresenter {
    private static final String TAG = MsgSendPresenterImpl.class.getSimpleName();

    private int index;
    private Context mContext;
    private Map<String, Object> lessMap;
    private Map<String, Object> greatMap;

    private ArrayList<Integer> failList = new ArrayList<>();
    private List<List<Map<String, String>>> mRentalAllMsg = new ArrayList<>();

    private IResponse mResponse;
    private MsgSendData mMsgSendData;
    private MsgSendStateBroadcastReceiver sendStateReceiver;
    private MsgReceiveStateBroadcastReceiver receiveStateReceiver;
    private CompositeDisposable mSendDisposable = new CompositeDisposable();

    MsgSendPresenterImpl(Context context, IResponse response) {
        mContext = context;
        mResponse = response;
    }

    @Override
    public void sendMsgForRental(final List<String> sumDeposit) {
        final String SMS_SEND_ACTION = "SMS_SEND";
        final String SMS_DELIVERED_ACTION = "SMS_DELIVERED";

        index = 0;
        sendStateReceiver = new MsgSendStateBroadcastReceiver();
        mContext.registerReceiver(sendStateReceiver, new IntentFilter(SMS_SEND_ACTION));

        receiveStateReceiver = new MsgReceiveStateBroadcastReceiver();
        mContext.registerReceiver(receiveStateReceiver, new IntentFilter(SMS_DELIVERED_ACTION));

        mSendDisposable.add(Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                for (int i = 0; i < 2; i++) {
                    PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, i, new Intent(SMS_SEND_ACTION), 0);
                    PendingIntent receiveIntent = PendingIntent.getBroadcast(mContext, i + 1, new Intent(SMS_DELIVERED_ACTION), 0);

                    SmsManager smsManager = SmsManager.getDefault();
                    String str = String.format("尊敬的%s房客:\n上个月您的用电量为%s度、用水量为%s吨，共计房租%s元。收到通知后，可通过微信转账/现金支付。\n祝您生活愉快。",
                            mRentalAllMsg.get(0).get(0).get("roomNum"),
                            mRentalAllMsg.get(1).get(0).get("electricUse"),
                            mRentalAllMsg.get(1).get(0).get("waterUse"),
                            sumDeposit.get(0));

                    ArrayList<String> msgMulti = smsManager.divideMessage(str);
                    ArrayList<PendingIntent> sentIntents = new ArrayList<>();
                    ArrayList<PendingIntent> receiveIntents = new ArrayList<>();

                    for (int j = 0; j < msgMulti.size(); j++) {
                        sentIntents.add(sentIntent);
                        receiveIntents.add(receiveIntent);
                    }
                    smsManager.sendMultipartTextMessage("18520971032", null, msgMulti, sentIntents, receiveIntents);
                }
                e.onNext(true);
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            RHToast.makeText(mContext, mContext.getString(R.string.msg_sending), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, "SendMsg Message -> " + throwable.getMessage());
                    }
                }));
    }

    @Override
    public List<List<Map<String, String>>> getRenterAllMsg() {
        mMsgSendData = new MsgSendData(mResponse, lessMap, greatMap);
        return mMsgSendData.requestAllMsg();
    }

    @Override
    public String getDateForNow() {
        new TimeUtil();
        String date = TimeUtil.date;
        lessMap = new HashMap<>();
        greatMap = new HashMap<>();
        lessMap.put("uploadAt", TimeUtil.dateFinish);
        greatMap.put("uploadAt", TimeUtil.dateStart);
        return date;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {}

    @Override
    public void onStop() {
        if (sendStateReceiver != null) {
            mContext.unregisterReceiver(sendStateReceiver);
        }
        if (receiveStateReceiver != null) {
            mContext.unregisterReceiver(receiveStateReceiver);
        }
        if (mSendDisposable != null) {
            mSendDisposable.clear();
        }
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

    class MsgReceiveStateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Log.e(TAG, "Receive Success");
                    break;
                default:
                    Log.e(TAG, "Receive Fail");
                    break;
            }
        }
    }

    class MsgSendStateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    index++;
                    Log.e(TAG, "Send Success");
                    break;
                default:
                    index++;
                    failList.add(index);
                    Log.e(TAG, "Send Fail");
                    break;
            }

            if (index / 2 == 2) {
                mSendDisposable.clear();
                if (failList.isEmpty()) {
                    RHToast.makeText(mContext, mContext.getString(R.string.msg_finish), Toast.LENGTH_SHORT).show();
                } else {
                    StringBuilder strBuilder = new StringBuilder();
                    for (int i = 0; i < failList.size(); i += 2) {
                        strBuilder.append(mRentalAllMsg.get(0).get(i).get("roomNum")).append("/");
                    }
                    RHToast.makeText(mContext, strBuilder + "发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
