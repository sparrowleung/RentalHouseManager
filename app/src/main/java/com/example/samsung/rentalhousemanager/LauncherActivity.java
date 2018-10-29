package com.example.samsung.rentalhousemanager;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.samsung.rentalhousemanager.disclaimer.DisclaimerActivity;
import com.example.samsung.rentalhousemanager.main.MainActivity;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by yuyang.liang on 2018/8/15.
 */

public class LauncherActivity extends AppCompatActivity {
    private final static String TAG = LauncherActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        Log.d(TAG, "onCreate");
        Bmob.initialize(this, "a6b90c7087109ed745b0a2b62d1963a6");

        BmobUser user = BmobUser.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(LauncherActivity.this, DisclaimerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
