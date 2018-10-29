package com.example.samsung.rentalhousemanager.roomsetting;

import android.databinding.BindingAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.samsung.rentalhousemanager.R;

/**
 * Created by yuyang.liang on 2018/8/3.
 */

public class RoomDataSettingBinding {

    @BindingAdapter({"txtChangedListener"})
    public static void setTextChangedListener(final EditText editText, final OnTextChangedCallback changedCallback) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changedCallback.onTextChanged(editText, s.toString());
            }
        });
    }

    @BindingAdapter({"onClickListener"})
    public static void setClickListener(Button button, final IOnClickCallBack onClickCallBack) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.setting_cancel:
                        onClickCallBack.clickCancel();
                        break;
                    case R.id.setting_upload:
                        onClickCallBack.clickUpload();
                        break;
                }
            }
        });
    }

}
