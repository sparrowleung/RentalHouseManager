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
import com.example.samsung.rentalhousemanager.databinding.FragmentRegisterBinding;
import com.example.samsung.rentalhousemanager.toolclass.RHToast;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by yuyang.liang on 2018/8/29.
 */

public class DisclaimerRegisterFragment extends BaseFragment {
    private final static String TAG = DisclaimerRegisterFragment.class.getSimpleName();

    private CompositeDisposable mDisposable = new CompositeDisposable();

    private NewDisclaimerViewModel mViewModel;
    private DisclaimerActivity mDisclaimerActivity;
    private FragmentRegisterBinding mRegisterBinding;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstanceState) {
        mRegisterBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_register, container, false);

        mDisclaimerActivity = ((DisclaimerActivity) getActivity());
        mViewModel = new NewDisclaimerViewModel(getContext());
        mRegisterBinding.setRegisterViewModel(mViewModel);
        return mRegisterBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mDisposable.add(mViewModel.mPublishSubject
        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                switch (integer) {
                    case NewDisclaimerViewModel.TYPE_MESSAGE_EMPTY:
                        RHToast.makeText(getContext(), "Msg Empty", Toast.LENGTH_SHORT).show();
                        break;
                    case NewDisclaimerViewModel.TYPE_REGISTER_FAIL:
                        RHToast.makeText(getContext(), "Register Fail & Try Again", Toast.LENGTH_SHORT).show();
                        break;
                    case NewDisclaimerViewModel.TYPE_LOGIN:
                        Log.e(TAG, "Login Click CallBack");
                        DisclaimerFloorSetFragment floorSetFragment = new DisclaimerFloorSetFragment();
                        mDisclaimerActivity.pushFragment(floorSetFragment);
                        mDisclaimerActivity.updateNextFragment(floorSetFragment.getClass().getName());
                        break;
                    case NewDisclaimerViewModel.TYPE_LOGIN_FAIL:
                        RHToast.makeText(getContext(), "Register Fail & Try Again", Toast.LENGTH_SHORT).show();
                        NewDisclaimerFragment fragment = new NewDisclaimerFragment();
                        mDisclaimerActivity.pushFragment(fragment);
                        mDisclaimerActivity.updateNextFragment(fragment.getClass().getName());
                        break;
                }
            }
        }));
    }

}
