package com.example.samsung.rentalhousemanager.roomsetting;

import android.util.Log;
import android.util.Pair;
import android.widget.EditText;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.roomdata.IResponse;
import com.example.samsung.rentalhousemanager.server.RentalMoneyObject;
import com.example.samsung.rentalhousemanager.server.RenterMessageObject;
import com.example.samsung.rentalhousemanager.server.RoomMessageObject;
import com.example.samsung.rentalhousemanager.server.ServerRequest;
import com.example.samsung.rentalhousemanager.server.onSaveResultsListener;

import java.util.List;
import java.util.Map;

/**
 * Created by yuyang.liang on 2018/8/3.
 */

public class RoomSettingData {
    public final static String TAG = RoomSettingData.class.getSimpleName();

    public IResponse iResponse;

    public RoomSettingData(IResponse iResponse) {
        this.iResponse = iResponse;
    }

    public void UpLoadData(List<Map<String, Object>> mapList) {
        for (int i = 0; i < 3; i++) {
            switch (i) {
                case 0:
                    RenterMessageObject messageObject = new RenterMessageObject();
                    for (Map.Entry<String, Object> map : mapList.get(i).entrySet()) {
                        messageObject.setValue(map.getKey(), map.getValue());
                    }
                    ServerRequest.onSaveRequest(messageObject, new onSaveResultsListener() {
                        @Override
                        public void onSuccess(String objectId) {
                            Log.e(TAG, "Success : Type -> 0, ObjectId -> " + objectId);
                            iResponse.onComplete(true);
                        }

                        @Override
                        public void onFail(int errorCode, String errorMessage) {
                            Log.e(TAG, "Fail : Type -> 0, errorCode -> " + errorCode + ", errorMessage -> " + errorMessage);
                            iResponse.onComplete(false);
                        }
                    });
                    break;
                case 1:
                    RoomMessageObject roomMessageObject = new RoomMessageObject();
                    for (Map.Entry<String, Object> map : mapList.get(i).entrySet()) {
                        roomMessageObject.setValue(map.getKey(), map.getValue());
                    }
                    ServerRequest.onSaveRequest(roomMessageObject, new onSaveResultsListener() {
                        @Override
                        public void onSuccess(String objectId) {
                            Log.e(TAG, "Success : Type -> 1, ObjectId -> " + objectId);
                            iResponse.onComplete(true);
                        }

                        @Override
                        public void onFail(int errorCode, String errorMessage) {
                            Log.e(TAG, "Fail : Type -> 1, errorCode -> " + errorCode + ", errorMessage -> " + errorMessage);
                            iResponse.onComplete(false);
                        }
                    });
                    break;
                case 2:
                    RentalMoneyObject moneyObject = new RentalMoneyObject();
                    for (Map.Entry<String, Object> map : mapList.get(i).entrySet()) {
                        moneyObject.setValue(map.getKey(), map.getValue());
                    }
                    ServerRequest.onSaveRequest(moneyObject, new onSaveResultsListener() {
                        @Override
                        public void onSuccess(String objectId) {
                            Log.e(TAG, "Success : Type -> 2, ObjectId -> " + objectId);
                            iResponse.onComplete(true);
                        }

                        @Override
                        public void onFail(int errorCode, String errorMessage) {
                            Log.e(TAG, "Fail : Type -> 2, errorCode -> " + errorCode + ", errorMessage -> " + errorMessage);
                            iResponse.onComplete(false);
                        }
                    });
                    break;
            }
        }
    }

}
