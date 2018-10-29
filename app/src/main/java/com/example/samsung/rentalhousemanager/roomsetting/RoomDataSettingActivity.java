package com.example.samsung.rentalhousemanager.roomsetting;

import android.os.Bundle;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.baseclass.BaseActivity;

/**
 * Created by yuyang.liang on 2018/8/2.
 */

public class RoomDataSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_setting);
        setActionBar();

        RoomSettingFragment fragment = new RoomSettingFragment();
        fragment.setArguments(getIntent().getExtras());
        attachFragmentAsSingle(fragment);
    }

}
