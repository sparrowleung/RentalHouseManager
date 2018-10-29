package com.example.samsung.rentalhousemanager.baseclass;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.toolclass.ActionLinkManager;

/**
 * Created by yuyang.liang on 2018/7/10.
 */

public class BaseFragment extends android.support.v4.app.Fragment {

    private static final String _TAG = BaseFragment.class.getSimpleName();
    protected Context mContext;
    protected String mTitle;
    protected View mCustomActionBarView;
    protected View mNaviView;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        if (getActivity() == null) {
            return;
        }

        mContext = getActivity().getApplicationContext();
    }

    public void updateActionBar() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        int displayOptions = actionBar.getDisplayOptions();
        if ((displayOptions & ActionBar.DISPLAY_SHOW_CUSTOM) > 0) {
            if (mCustomActionBarView != null) {
                actionBar.setCustomView(mCustomActionBarView);
            }

            View view = actionBar.getCustomView();
            TextView textView = view.findViewById(R.id.jpTitleTextView);
            textView.setText(mTitle);
            if (view != null) {
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                view.setLayoutParams(lp);
            }
        }
    }

    protected void performActionLink(String actionLink) {
        Activity activity = getActivity();

        if (activity == null || activity.isFinishing()) {
            return;
        }

        ActionLinkManager.performContextActionLink(activity, actionLink);
    }

    protected void performActionLink(String actionLink, Bundle bundle) {
        Activity activity = getActivity();

        if (activity == null || activity.isFinishing()) {
            return;
        }

        ActionLinkManager.performContextActionLink(activity, actionLink, bundle);
    }

}
