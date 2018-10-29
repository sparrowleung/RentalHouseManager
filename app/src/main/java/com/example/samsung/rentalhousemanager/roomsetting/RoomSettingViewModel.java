package com.example.samsung.rentalhousemanager.roomsetting;

import android.databinding.BaseObservable;
import android.os.Bundle;
import android.widget.EditText;

import com.example.samsung.rentalhousemanager.roomdata.IResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by yuyang.liang on 2018/8/3.
 */

public class RoomSettingViewModel extends BaseObservable {
    private final static String TAG = RoomSettingViewModel.class.getSimpleName();

    private String mRoomNum;
    private Date mDateString;
    private RoomDataModel dataModel;
    private RoomSettingData roomSettingData;
    private List<Map<String, Object>> mapList = new ArrayList<>();
    public final PublishSubject<String> clickEventPublisher = PublishSubject.create();


    public RoomSettingViewModel(IResponse iResponse, Date date, String roomNum) {
        roomSettingData = new RoomSettingData(iResponse);
        dataModel = new RoomDataModel();
        mDateString = date;
        mRoomNum = roomNum;
    }

    public final OnTextChangedCallback onTextChangedCallback = new OnTextChangedCallback() {
        @Override
        public void onTextChanged(EditText editText, String text) {
            dataModel.set(editText, text);
        }
    };

    public final IOnClickCallBack onClickCallBack = new IOnClickCallBack() {
        @Override
        public void clickCancel() {
            if (!mapList.isEmpty()) {
                mapList.clear();
            }
            clickEventPublisher.onNext("cancel");
        }

        @Override
        public void clickUpload() {
            clickEventPublisher.onNext("upload");
            dataModel.setDateText(mDateString);
            mapList = dataModel.getAll(mRoomNum);
            if (!mapList.isEmpty()) {
                roomSettingData.UpLoadData(mapList);
            }
        }
    };

}
