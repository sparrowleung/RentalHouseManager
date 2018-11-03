package com.example.samsung.rentalhousemanager.baseclass;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.toolclass.V26Toolbar;

/**
 * Created by yuyang.liang on 2018/6/19.
 */

public class BaseActivity extends AppCompatActivity {
    private final String TAG = BaseActivity.class.getSimpleName();

    protected V26Toolbar mToolbar;
    protected Handler mHandler= new Handler(Looper.getMainLooper());
    private boolean isSaveState = false;

    protected void setActionBar() {
        mToolbar = (V26Toolbar) findViewById(R.id.toolbar);

        if (mToolbar != null) {
            mToolbar.setContentInsetsAbsolute(0, 0);
            setSupportActionBar(mToolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            ViewGroup mCustomActionBarView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.view_action_bar, null);
            View mDrawerIcon = mCustomActionBarView.findViewById(R.id.drawer_layout);
            ImageView imageView = mDrawerIcon.findViewById(R.id.drawerIcon);
            mDrawerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            imageView.setBackgroundResource(R.drawable.leftcircle);
            actionBar.setCustomView(mCustomActionBarView, lp);
        }
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    public void requestFocusOnActionBar(){
        if (mToolbar != null) {
            mToolbar.requestFocus();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    protected void attachFragmentAsSingle(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        String tag = fragment.getClass().getSimpleName() + "_" + fragment.hashCode();
        transaction.replace(R.id.container, fragment, tag);
        transaction.commitAllowingStateLoss();
        manager.executePendingTransactions();
    }

    public void popFragment() {
        FragmentManager manager = getSupportFragmentManager();
        try {
            manager.popBackStackImmediate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setStateBarVisibility(this);
    }

    @Override
    public void onConfigurationChanged(Configuration configuration){
        super.onConfigurationChanged(configuration);
        setStateBarVisibility(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        isSaveState = false;

    }

    public void setStateBarVisibility(Activity activity) {
        if (activity != null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
    }
}
