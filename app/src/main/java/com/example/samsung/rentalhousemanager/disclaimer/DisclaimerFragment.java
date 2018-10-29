package com.example.samsung.rentalhousemanager.disclaimer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.baseclass.BaseFragment;

/**
 * Created by yuyang.liang on 2018/8/23.
 */

public class DisclaimerFragment extends BaseFragment {
    private final static String TAG = DisclaimerFragment.class.getSimpleName();

    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstanceState) {
        mRootView = layoutInflater.inflate(R.layout.fragment_welcome, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        Button nextButton = (Button) mRootView.findViewById(R.id.welcome_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisclaimerActivity activity = ((DisclaimerActivity) getActivity());
                if (activity != null) {
                    NewDisclaimerFragment fragment = new NewDisclaimerFragment();
                    activity.pushFragment(fragment);
                    activity.updateNextFragment(fragment.getClass().getName());
                }
            }
        });
    }


}
