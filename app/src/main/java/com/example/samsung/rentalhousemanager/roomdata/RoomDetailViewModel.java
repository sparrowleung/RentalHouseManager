package com.example.samsung.rentalhousemanager.roomdata;

import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.widget.EditText;

import com.example.samsung.rentalhousemanager.roomsetting.OnTextChangedCallback;
import com.example.samsung.rentalhousemanager.roomsetting.RoomDataModel;

import java.util.List;
import java.util.Map;


/**
 * Created by yuyang.liang on 2018/8/13.
 */

public class RoomDetailViewModel extends BaseObservable {
    private final static String TAG = RoomDetailViewModel.class.getSimpleName();

    final public ObservableField<String> mId = new ObservableField<>("");
    final public ObservableField<String> mSex = new ObservableField<>("");
    final public ObservableField<String> mJob = new ObservableField<>("");
    final public ObservableField<String> mName = new ObservableField<>("");
    final public ObservableField<String> mPlace = new ObservableField<>("");
    final public ObservableField<String> mPhone = new ObservableField<>("");
    final public ObservableField<String> mLiveNum = new ObservableField<>("");

    final public ObservableField<String> mAir = new ObservableField<>("");
    final public ObservableField<String> mWater = new ObservableField<>("");
    final public ObservableField<String> mOther = new ObservableField<>("");
    final public ObservableField<String> mInDate = new ObservableField<>("");
    final public ObservableField<String> mManage = new ObservableField<>("");
    final public ObservableField<String> mRoomNum = new ObservableField<>("123");
    final public ObservableField<String> mDeposit = new ObservableField<>("");
    final public ObservableField<String> mMonthly = new ObservableField<>("");
    final public ObservableField<String> mTenancy = new ObservableField<>("");
    final public ObservableField<String> mElectric = new ObservableField<>("");

    final public ObservableField<String> mAirUse = new ObservableField<>("");
    final public ObservableField<String> mWaterUse = new ObservableField<>("");
    final public ObservableField<String> mElectricUse = new ObservableField<>("");

    private List<List<Map<String, String>>> hintList;
    private RoomDataModel mRoomDataModel;

    public RoomDetailViewModel(List<List<Map<String, String>>> list) {
        this.hintList = list;
        mRoomDataModel = new RoomDataModel();
        if (hintList.get(0).size() > 0) {
            mPhone.set(hintList.get(0).get(0).get("phone"));
            mId.set("身份证:" + hintList.get(0).get(0).get("id"));
            mSex.set("性别:" + hintList.get(0).get(0).get("sex"));
            mJob.set("职业:" + hintList.get(0).get(0).get("job"));
            mName.set("姓名:" + hintList.get(0).get(0).get("name"));
            mPlace.set("户籍:" + hintList.get(0).get(0).get("place"));
            mLiveNum.set("居住人数:" + hintList.get(0).get(0).get("liveNum"));
            mRoomDataModel.setRenterObjectId(hintList.get(0).get(0).get("objectId"));
        }

        if (hintList.get(1).size() > 0) {
            mAir.set(hintList.get(1).get(0).get("air"));
            mWater.set(hintList.get(1).get(0).get("water"));
            mTenancy.set(hintList.get(1).get(0).get("tenancy"));
            mElectric.set(hintList.get(1).get(0).get("electric"));
            mOther.set("其他:" + hintList.get(1).get(0).get("other"));
            mRoomNum.set("房号:" + hintList.get(1).get(0).get("num"));
            mInDate.set("入住:" + hintList.get(1).get(0).get("date"));
            mManage.set("管理:" + hintList.get(1).get(0).get("manage"));
            mDeposit.set("押金:" + hintList.get(1).get(0).get("deposit"));
            mMonthly.set("租金:" + hintList.get(1).get(0).get("monthly"));
            mRoomDataModel.setRoomObjectId(hintList.get(1).get(0).get("objectId"));
        }

        if (hintList.get(2).size() > 0) {
            mAirUse.set(hintList.get(2).get(0).get("airUse"));
            mWaterUse.set(hintList.get(2).get(0).get("waterUse"));
            mElectricUse.set(hintList.get(2).get(0).get("electricUse"));
            mRoomDataModel.setUseObjectId(hintList.get(2).get(0).get("objectId"));
        }
    }

    public final OnTextChangedCallback onTextChangedCallback = new OnTextChangedCallback() {
        @Override
        public void onTextChanged(EditText editText, String text) {
            mRoomDataModel.set(editText, text);
        }
    };

    public void getNotifyRoomInfo(RoomData roomData) {
        roomData.updateRoomInfo(mRoomDataModel.getNotifyRoomInfo());
    }

    public void getNotifyMoneyInfo(RoomData roomData) {
        roomData.updateMoneyInfo(mRoomDataModel.getNotifyMoneyInfo());
    }
}
