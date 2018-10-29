package com.example.samsung.rentalhousemanager.disclaimer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.baseclass.BaseActivity;
import com.mob.MobSDK;

/**
 * Created by yuyang.liang on 2018/8/23.
 */

public class DisclaimerActivity extends BaseActivity {

    private final static String TAG = DisclaimerActivity.class.getSimpleName();

    private String mCurrentFragment = DisclaimerFragment.class.getName();
    static final String CURRENT_FRAGMENT_TAG = "DisclaimerCurrent";
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_disclaimer);
        MobSDK.init(getBaseContext(), "27be57111f3e3", "55b53de6f067db03655b82f9a44b95ae");
        mFragmentManager = getSupportFragmentManager();
        pushFragment(Fragment.instantiate(getBaseContext(), mCurrentFragment));
    }

    public void pushFragment(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.container, fragment, CURRENT_FRAGMENT_TAG);
        transaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
    }

    public void updateNextFragment(@NonNull String name) {
        mCurrentFragment = name;
    }
}
