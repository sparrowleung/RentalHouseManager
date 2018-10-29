package com.example.samsung.rentalhousemanager.disclaimer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.baseclass.BaseFragment;
import com.example.samsung.rentalhousemanager.databinding.FragmentWelcomeNextBinding;
import com.example.samsung.rentalhousemanager.toolclass.ActionLinkManager;
import com.example.samsung.rentalhousemanager.toolclass.RHToast;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;


/**
 * Created by yuyang.liang on 2018/8/24.
 */

public class NewDisclaimerFragment extends BaseFragment {
    private final static String TAG = NewDisclaimerFragment.class.getSimpleName();

    private DisclaimerActivity mDisclaimerActivity;
    private FragmentWelcomeNextBinding mWelcomeBinding;
    private NewDisclaimerViewModel mDisclaimerViewModel;
    private DisclaimerRegisterFragment mRegisterFragment;
    private DisclaimerForgetCodeFragment mForgetFragment;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstanceState) {
        mWelcomeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_welcome_next, container, false);
        mDisclaimerViewModel = new NewDisclaimerViewModel(getContext());
        mWelcomeBinding.setWelcomeViewModel(mDisclaimerViewModel);

        mDisclaimerActivity = ((DisclaimerActivity) getActivity());
        mRegisterFragment = new DisclaimerRegisterFragment();
        mForgetFragment = new DisclaimerForgetCodeFragment();
        return mWelcomeBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mCompositeDisposable.add(
                mDisclaimerViewModel.mPublishSubject
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                switch (integer) {
                                    case NewDisclaimerViewModel.TYPE_LOGIN:
                                        Log.e(TAG, "Login Click CallBack");
                                        ActionLinkManager.performContextActionLink(getContext(), "voc://view/main");
                                        if (getActivity() != null && !getActivity().isFinishing()) {
                                            getActivity().finish();
                                        }
                                        break;
                                    case NewDisclaimerViewModel.TYPE_FORGET:
                                        Log.e(TAG, "Forget Click CallBack");
                                        mDisclaimerActivity.pushFragment(mForgetFragment);
                                        mDisclaimerActivity.updateNextFragment(mForgetFragment.getClass().getName());
                                        break;
                                    case NewDisclaimerViewModel.TYPE_REGISTER:
                                        Log.e(TAG, "Register Click CallBack");
                                        mDisclaimerActivity.pushFragment(mRegisterFragment);
                                        mDisclaimerActivity.updateNextFragment(mRegisterFragment.getClass().getName());
                                        break;
                                    case NewDisclaimerViewModel.TYPE_LOGIN_FAIL:
                                        RHToast.makeText(getContext(), "Account Message is error", Toast.LENGTH_SHORT).show();
                                        break;
                                    case NewDisclaimerViewModel.TYPE_MESSAGE_EMPTY:
                                        RHToast.makeText(getContext(), "Please Input Message", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        })
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }
}
