package com.example.samsung.rentalhousemanager.roomsetting;

import android.app.Activity;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.baseclass.BaseFragment;
import com.example.samsung.rentalhousemanager.databinding.FragmentSettingBinding;
import com.example.samsung.rentalhousemanager.databinding.ViewRoomSettingBinding;
import com.example.samsung.rentalhousemanager.roomdata.IResponse;
import com.example.samsung.rentalhousemanager.toolclass.RHToast;

import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static com.example.samsung.rentalhousemanager.roomsetting.RoomSettingViewModel.ROOM_SETTING_CANCEL;
import static com.example.samsung.rentalhousemanager.roomsetting.RoomSettingViewModel.ROOM_SETTING_UPLOAD;


/**
 * Created by yuyang.liang on 2018/8/3.
 */

public class RoomSettingFragment extends BaseFragment implements IResponse {
    private final static String TAG = RoomSettingFragment.class.getSimpleName();

    private AlertDialog mDialog;
    private RoomSettingViewModel mRoomViewModel;
    private ViewRoomSettingBinding mViewRoomSettingBinding;
    private FragmentSettingBinding mFragmentSettingBinding;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstanceState) {
        mFragmentSettingBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_setting, container, false);
        mViewRoomSettingBinding = DataBindingUtil.inflate(layoutInflater, R.layout.view_room_setting, container, false);
        mFragmentSettingBinding.settingContainer.addView(mViewRoomSettingBinding.getRoot());

        String roomNum = getArguments().getString("roomNum");
        mRoomViewModel = new RoomSettingViewModel(this, (Date) getArguments().get("uploadAt"), roomNum);
        mViewRoomSettingBinding.setSettingViewModel(mRoomViewModel);

        mTitle = new StringBuffer().append(roomNum).append(getResources().getString(R.string.room_setting)).toString();
        updateActionBar();
        return mFragmentSettingBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getString(R.string.room_setting_cancel));
        dialog.setMessage(getString(R.string.room_setting_cancel_txt));
        dialog.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog.dismiss();
            }
        });
        dialog.setPositiveButton(getString(R.string.dialog_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Activity activity = getActivity();
                if (activity != null && !activity.isDestroyed() && !activity.isFinishing()) {
                    activity.finish();
                }
            }
        });
        mDialog = dialog.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        mDisposable.add(mRoomViewModel.clickEventPublisher
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        switch (integer) {
                            case ROOM_SETTING_CANCEL:
                                mDialog.show();
                                break;
                            case ROOM_SETTING_UPLOAD:
                                mFragmentSettingBinding.settingProgress.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }));


    }

    @Override
    public void onDestroy() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onFail(int type) {

    }

    @Override
    public void onSuccess(int type) {

    }

    @Override
    public void onComplete(boolean normal) {
        mFragmentSettingBinding.settingProgress.setVisibility(View.GONE);
        if (normal) {
            RHToast.makeText(getContext(), "Upload Success", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        } else {
            RHToast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
