package com.example.samsung.rentalhousemanager.roomdata;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.baseclass.BaseFragment;
import com.example.samsung.rentalhousemanager.databinding.ViewInfoNotifyBinding;
import com.example.samsung.rentalhousemanager.databinding.ViewMonthlyMoneyBinding;
import com.example.samsung.rentalhousemanager.roomsetting.RoomDataSettingActivity;
import com.example.samsung.rentalhousemanager.toolclass.DatePickView;
import com.example.samsung.rentalhousemanager.toolclass.RHToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;

/**
 * Created by yuyang.liang on 2018/7/17.
 */

public class RoomDetailFragment extends BaseFragment implements IRoomView, IResponse, DatePickView.onSelectedChangedListener {
    private final static String TAG = RoomDetailFragment.class.getSimpleName();

    private View mRootView;
    private View mEmptyView;
    private View mDatePickView;

    private TextView mDateText;
    private String mRoomNumberText;
    private AlertDialog mDialogInfo;
    private AlertDialog mDialogMoney;
    private AlertDialog mDialogDatePick;

    private Button mSettingButton;
    private Button mNotifyInfoButton;
    private Button mNotifyMoneyButton;
    private ImageButton mDatePickerButton;

    private ProgressBar mProgressBar;
    private RelativeLayout mContainer;
    private ExpandableListView mExpandList;
    private ViewInfoNotifyBinding mInfoDataBinding;
    private ViewMonthlyMoneyBinding mMoneyDataBinding;

    private RoomPresenter mPresenter;
    private RoomDetailViewModel mRoomDetailViewModel;

    private String mDateString = "";
    private Date mSelectDate = new Date();
    private Date mSelectDateNight = new Date();

    private boolean isGoToSetting = false;
    private Map<String, Object> mLessMap = new HashMap<>();
    private Map<String, Object> mEqualMap = new HashMap<>();
    private Map<String, Object> mGreaterMap = new HashMap<>();
    private List<Map<String, String>> mGroupList = new ArrayList<>();
    private List<List<Map<String, String>>> mChildList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstanceState) {
        mRootView = layoutInflater.inflate(R.layout.fragment_room, container, false);
        mInfoDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.view_info_notify, container, false);
        mMoneyDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.view_monthly_money, container, false);

        mDatePickView = LayoutInflater.from(getContext()).inflate(R.layout.view_numberpicker, null);
        DatePickView datePick = mDatePickView.findViewById(R.id.view_timePick);
        datePick.setOnSelectedChangedListener(this);
        datePick.initPicker();

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        mRoomNumberText = getArguments().getString("roomNum");
        mTitle = "RentalHouseDetail";
        updateActionBar();
        initView();

        setInfoDialog();
        setMoneyDialog();
        setTimePickDialog();
    }

    public void initView() {
        getCurrentTime();

        TextView roomNum = (TextView) mRootView.findViewById(R.id.room_num);
        roomNum.setText(mRoomNumberText);

        mExpandList = (ExpandableListView) mRootView.findViewById(R.id.room_message_list);
        mContainer = (RelativeLayout) mRootView.findViewById(R.id.room_view);
        mProgressBar = (ProgressBar) mRootView.findViewById(R.id.room_progressbar);

        mNotifyInfoButton = (Button) mRootView.findViewById(R.id.room_notify_info);
        mNotifyMoneyButton = (Button) mRootView.findViewById(R.id.room_notify_money);

        mDateText = (TextView) mRootView.findViewById(R.id.room_time);
        mDatePickerButton = (ImageButton) mRootView.findViewById(R.id.room_timePick);

        mDateText.setVisibility(View.VISIBLE);
        mDateText.setText(mDateString);

        mDatePickerButton.setVisibility(View.VISIBLE);
        mDatePickerButton.setBackground(getResources().getDrawable(R.drawable.calendarr));
        mDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogDatePick.show();
            }
        });

        mEqualMap.put("roomNum", mRoomNumberText);
        mEqualMap.put("userName", BmobUser.getCurrentUser().getUsername());
        if (mSelectDate != null) {
            mGreaterMap.put("uploadAt", mSelectDate);
        }
        if (mSelectDateNight != null) {
            mLessMap.put("uploadAtNight", mSelectDateNight);
        }

        getExpandDataFromPresenter();
    }

    public void getExpandDataFromPresenter() {
        mProgressBar.setVisibility(View.VISIBLE);

        mPresenter = RoomPresenter.create(getContext(), this, mEqualMap, mLessMap, mGreaterMap);
        mPresenter.onCreate(null);

        setExpandChildList();
        setExpandGroupList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isGoToSetting) {
            mContainer.removeView(mEmptyView);
            getExpandDataFromPresenter();
            isGoToSetting = false;
        }
    }

    @Override
    public void onFail(int type) {
        switch (type) {
            case RoomExpandListAdapter.INPUT_ERROR:
                RHToast.makeText(getContext(), "Msg Error & Try Again", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onSuccess(int type) {
        switch (type) {
            case RoomData.UPDATE_ROOM_SUCCESS:
            case RoomData.UPDATE_RENTER_SUCCESS:
                RHToast.makeText(getContext(), "Update " + type + " Success", Toast.LENGTH_SHORT).show();
                getExpandDataFromPresenter();
                if (mDialogInfo != null && mDialogInfo.isShowing()) {
                    mDialogInfo.dismiss();
                }
                break;
            case RoomData.UPDATE_RENTAL_SUCCESS:
                RHToast.makeText(getContext(), "Update " + type + " Success", Toast.LENGTH_SHORT).show();
                getExpandDataFromPresenter();
                if (mDialogMoney != null && mDialogMoney.isShowing()) {
                    mDialogMoney.dismiss();
                }
                break;
            case RoomExpandListAdapter.INFO_SETUP_SUCCESS:
                RHToast.makeText(getContext(), "Add MoneyMsg Success", Toast.LENGTH_SHORT).show();
                getExpandDataFromPresenter();
                break;
        }
    }

    @Override
    public void onComplete(boolean normal) {
        mProgressBar.setVisibility(View.GONE);

        if (normal) {
            mContainer.removeView(mEmptyView);
            mExpandList.setVisibility(View.VISIBLE);
            setExpandList();

            mRoomDetailViewModel = new RoomDetailViewModel(mChildList);
            mInfoDataBinding.setDetailViewModel(mRoomDetailViewModel);
            mMoneyDataBinding.setMoneyViewModel(mRoomDetailViewModel);

            mNotifyInfoButton.setVisibility(View.VISIBLE);
            mNotifyInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogInfo.show();
                }
            });

            mNotifyMoneyButton.setVisibility(View.VISIBLE);
            mNotifyMoneyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogMoney.show();
                }
            });

        } else {
            mExpandList.setVisibility(View.GONE);
            mEmptyView = LayoutInflater.from(getContext()).inflate(R.layout.view_empty_message, null);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mContainer.addView(mEmptyView, layoutParams);
            mSettingButton = mEmptyView.findViewById(R.id.room_setting);
            toRoomDataSetting();
        }
    }

    @Override
    public void setExpandGroupList() {
        mGroupList = mPresenter.getGroupList();
    }

    @Override
    public void setExpandChildList() {
        mChildList = mPresenter.getChildList();
    }

    @Override
    public void setExpandList() {
        mExpandList.setGroupIndicator(null);
        mExpandList.setChildIndicator(null);
        mExpandList.setDivider(null);

        RoomExpandListAdapter roomExpandListAdapter = new RoomExpandListAdapter(getContext(), this);
        roomExpandListAdapter.setExpandListData(mGroupList, mChildList, mSelectDate);

        mExpandList.setAdapter(roomExpandListAdapter);
    }

    public void toRoomDataSetting() {
        mSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isGoToSetting = true;
                Intent intent = new Intent(getActivity(), RoomDataSettingActivity.class);
                intent.putExtra("roomNum", mRoomNumberText);
                intent.putExtra("uploadAt", mSelectDate);
                getActivity().startActivity(intent);
            }
        });
    }

    public void setMoneyDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setView(mMoneyDataBinding.getRoot());
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.notifyMoneyInfo(mRoomDetailViewModel);
            }
        });

        mDialogMoney = dialog.create();
    }

    public void setTimePickDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setView(mDatePickView);
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDateText.setText(mDateString);
                translateBmobDateFormat(mDateString);
                if (mSelectDate != null) {
                    mGreaterMap.put("uploadAt", mSelectDate);
                }
                if (mSelectDateNight != null) {
                    mLessMap.put("uploadAtNight", mSelectDateNight);
                }
                getExpandDataFromPresenter();
            }
        });

        mDialogDatePick = dialog.create();
    }

    public void setInfoDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setView(mInfoDataBinding.getRoot());

        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.notifyRoomInfo(mRoomDetailViewModel);
            }
        });

        mDialogInfo = dialog.create();
    }

    public void getCurrentTime() {
        long dateTime = System.currentTimeMillis();
        Date date = new Date(dateTime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        mDateString = format.format(date);
        translateBmobDateFormat(mDateString);
    }

    @Override
    public void onSelected(DatePickView view, long timeMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        mDateString = dateFormat.format(timeMillis);
    }

    public void translateBmobDateFormat(String dateText) {
        String dateNightText = new StringBuilder(dateText).append("-01 23:59:59").toString();
        dateText = new StringBuilder(dateText).append("-01 00:00:00").toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            mSelectDateNight = dateFormat.parse(dateNightText);
            mSelectDate = dateFormat.parse(dateText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (mDialogInfo != null && mDialogInfo.isShowing()) {
            mDialogInfo.dismiss();
        }

        if (mDialogMoney != null && mDialogMoney.isShowing()) {
            mDialogMoney.dismiss();
        }

        if (!mGroupList.isEmpty()) {
            mGroupList.clear();
        }

        if (!mChildList.isEmpty()) {
            mChildList.clear();
        }

        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
        super.onDestroy();
    }
}