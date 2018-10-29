package com.example.samsung.rentalhousemanager.roomdata;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.baseclass.BaseActivity;

/**
 * Created by yuyang.liang on 2018/7/17.
 */

public class RoomDetailActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle saveInstance) {
        super.onCreate(saveInstance);
        setContentView(R.layout.activity_room);

        setActionBar();
        Fragment fragment = new RoomDetailFragment();
        Bundle bundle = getIntent().getExtras();
        fragment.setArguments(bundle);
        attachFragmentAsSingle(fragment);
    }
}