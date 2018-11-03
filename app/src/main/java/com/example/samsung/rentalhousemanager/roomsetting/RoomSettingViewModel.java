package com.example.samsung.rentalhousemanager.roomsetting;

import android.databinding.BaseObservable;
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
    public final PublishSubject<Integer> clickEventPublisher = PublishSubject.create();

    public final static int ROOM_SETTING_CANCEL = 1;
    public final static int ROOM_SETTING_UPLOAD = 2;


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
            clickEventPublisher.onNext(ROOM_SETTING_CANCEL);
        }

        @Override
        public void clickUpload() {
            clickEventPublisher.onNext(ROOM_SETTING_UPLOAD);
            dataModel.setDateText(mDateString);
            mapList = dataModel.getAll(mRoomNum);
            if (!mapList.isEmpty()) {
                roomSettingData.UpLoadData(mapList);
            }
        }
    };

}
