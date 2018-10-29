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
import com.example.samsung.rentalhousemanager.databinding.FragmentForgetBinding;
import com.example.samsung.rentalhousemanager.toolclass.RHToast;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by yuyang.liang on 2018/9/6.
 */

public class DisclaimerForgetCodeFragment extends BaseFragment {
    private final static String TAG = DisclaimerForgetCodeFragment.class.getSimpleName();

    private NewDisclaimerViewModel mViewModel;
    private FragmentForgetBinding mForgetBinding;
    private DisclaimerActivity mDisclaimerActivity;
    private NewDisclaimerFragment mDisclaimerFragment;

    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstanceState) {
        mForgetBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_forget, container, false);

        mViewModel = new NewDisclaimerViewModel(getContext());
        mForgetBinding.setForgetViewModel(mViewModel);

        mDisclaimerActivity = ((DisclaimerActivity) getActivity());
        mDisclaimerFragment = new NewDisclaimerFragment();
        return mForgetBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mDisposable.add(mViewModel.mPublishSubject
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        switch (integer) {
                            case NewDisclaimerViewModel.TYPE_FORGET_SUCCESS:
                                Log.e(TAG, "Forget Success and Jump to Login Page");
                                RHToast.makeText(getContext(), "Modify Password Success", Toast.LENGTH_SHORT).show();
                                mDisclaimerActivity.pushFragment(mDisclaimerFragment);
                                mDisclaimerActivity.updateNextFragment(mDisclaimerFragment.getClass().getName());
                                break;
                            case NewDisclaimerViewModel.TYPE_FORGET_FAIL:
                                Log.e(TAG, "Forget Fail");
                                RHToast.makeText(getContext(), "Modify Password Fail & Try Again", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }));
    }
}
