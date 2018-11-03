package com.example.samsung.rentalhousemanager.roomdata;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.server.RentalMoneyObject;
import com.example.samsung.rentalhousemanager.server.ServerRequest;
import com.example.samsung.rentalhousemanager.server.onSaveResultsListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by yuyang.liang on 2018/7/17.
 */

public class RoomExpandListAdapter extends BaseExpandableListAdapter {
    private final static String TAG = RoomExpandListAdapter.class.getSimpleName();

    private Date mDate;
    private Context mContext;
    private IResponse mIResponse;
    private LayoutInflater mInflater;

    private List<Map<String, String>> mGroupList;
    private List<List<Map<String, String>>> mChildList;

    private static final int ITEM_TYPE_1 = 0;
    private static final int ITEM_TYPE_2 = 1;
    private static final int ITEM_TYPE_3 = 2;
    public static final int INPUT_ERROR = 3;
    public static final int INFO_SETUP_SUCCESS = 4;

    public RoomExpandListAdapter(Context context, IResponse response) {
        mContext = context;
        mIResponse = response;
        mGroupList = new ArrayList<>();
        mChildList = new ArrayList<>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setExpandListData(List<Map<String, String>> groupList, List<List<Map<String, String>>> childList, Date date) {
        mDate = date;
        mGroupList = groupList;
        mChildList = childList;
    }

    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = mInflater.inflate(R.layout.list_room_group, null);
            viewHolder = new ViewHolder();
            viewHolder.mGroupImage = (ImageView) view.findViewById(R.id.room_group_image);
            viewHolder.mGroupName = (TextView) view.findViewById(R.id.room_group_text);
            viewHolder.mGroupExpand = (ImageView) view.findViewById(R.id.room_group_expand);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.mGroupImage.setImageResource(R.drawable.information);
        if (isExpanded) {
            viewHolder.mGroupExpand.setImageResource(R.drawable.up);
        } else {
            viewHolder.mGroupExpand.setImageResource(R.drawable.down);
        }

        viewHolder.mGroupName.setText(mGroupList.get(groupPosition).get("group"));
        return view;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        switch (groupPosition) {
            case 0:
                return ITEM_TYPE_1;
            case 1:
                return ITEM_TYPE_2;
            case 2:
                return ITEM_TYPE_3;
        }
        return 0;
    }

    @Override
    public int getChildTypeCount() {
        return 3;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        int type = getChildType(groupPosition, childPosition);
        switch (type) {
            case ITEM_TYPE_1:
                if (view == null) {
                    viewHolder = new ViewHolder();
                    view = mInflater.inflate(R.layout.list_room_child_renter, null);
                    viewHolder.mRenterId = (TextView) view.findViewById(R.id.renter_id);
                    viewHolder.mRenterName = (TextView) view.findViewById(R.id.renter_name);
                    viewHolder.mRenterSex = (TextView) view.findViewById(R.id.renter_sex);
                    viewHolder.mRenterPlace = (TextView) view.findViewById(R.id.renter_place);
                    viewHolder.mRenterPhone = (TextView) view.findViewById(R.id.renter_phone);
                    viewHolder.mRenterJob = (TextView) view.findViewById(R.id.renter_job);
                    viewHolder.mRenterLiveNum = (TextView) view.findViewById(R.id.renter_number);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }

                if (mChildList.get(groupPosition) != null && mChildList.get(groupPosition).size() > 0) {
                    viewHolder.mRenterId.setText(mChildList.get(groupPosition).get(0).get("id"));
                    viewHolder.mRenterName.setText(mChildList.get(groupPosition).get(0).get("name"));
                    viewHolder.mRenterSex.setText(mChildList.get(groupPosition).get(0).get("sex"));
                    viewHolder.mRenterPlace.setText(mChildList.get(groupPosition).get(0).get("place"));
                    viewHolder.mRenterPhone.setText(mChildList.get(groupPosition).get(0).get("phone"));
                    viewHolder.mRenterJob.setText(mChildList.get(groupPosition).get(0).get("job"));
                    viewHolder.mRenterLiveNum.setText(mChildList.get(groupPosition).get(0).get("liveNum"));
                } else {
                    view = mInflater.inflate(R.layout.view_empty_message, null);
                }
                break;
            case ITEM_TYPE_2:
                if (view == null) {
                    viewHolder = new ViewHolder();
                    view = mInflater.inflate(R.layout.list_room_child_house, null);
                    viewHolder.mHouseAir = (TextView) view.findViewById(R.id.house_air);
                    viewHolder.mHouseTenancy = (TextView) view.findViewById(R.id.house_tenancy);
                    viewHolder.mHouseDeposit = (TextView) view.findViewById(R.id.house_deposit);
                    viewHolder.mHouseMonthly = (TextView) view.findViewById(R.id.house_monthly);
                    viewHolder.mHouseDate = (TextView) view.findViewById(R.id.house_date);
                    viewHolder.mHouseWater = (TextView) view.findViewById(R.id.house_water);
                    viewHolder.mHouseElectric = (TextView) view.findViewById(R.id.house_electric);
                    viewHolder.mHouseManage = (TextView) view.findViewById(R.id.house_manage);
                    viewHolder.mHouseOther = (TextView) view.findViewById(R.id.house_other);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }

                if (mChildList.get(groupPosition) != null && mChildList.get(groupPosition).size() > 0) {
                    viewHolder.mHouseAir.setText(mChildList.get(groupPosition).get(0).get("air"));
                    viewHolder.mHouseTenancy.setText(mChildList.get(groupPosition).get(0).get("tenancy"));
                    viewHolder.mHouseDeposit.setText(mChildList.get(groupPosition).get(0).get("deposit"));
                    viewHolder.mHouseMonthly.setText(mChildList.get(groupPosition).get(0).get("monthly"));
                    viewHolder.mHouseDate.setText(mChildList.get(groupPosition).get(0).get("date"));
                    viewHolder.mHouseWater.setText(mChildList.get(groupPosition).get(0).get("water"));
                    viewHolder.mHouseElectric.setText(mChildList.get(groupPosition).get(0).get("electric"));
                    viewHolder.mHouseManage.setText(mChildList.get(groupPosition).get(0).get("manage"));
                    viewHolder.mHouseOther.setText(mChildList.get(groupPosition).get(0).get("other"));
                } else {
                    view = mInflater.inflate(R.layout.view_empty_message, null);
                }
                break;
            case ITEM_TYPE_3:
                if (view == null) {
                    viewHolder = new ViewHolder();
                    view = mInflater.inflate(R.layout.list_room_child_money, null);
                    viewHolder.mHouseElectricMoney = (TextView) view.findViewById(R.id.room_electric_money);
                    viewHolder.mHouseElectricUse = (TextView) view.findViewById(R.id.room_electric);
                    viewHolder.mHouseWaterUse = (TextView) view.findViewById(R.id.room_water);
                    viewHolder.mHouseWaterMoney = (TextView) view.findViewById(R.id.room_water_money);
                    viewHolder.mSumMoney = (TextView) view.findViewById(R.id.room_sum_money);
                    viewHolder.mHouseAirUse = (TextView) view.findViewById(R.id.room_air);
                    viewHolder.mHouseAirMoney = (TextView) view.findViewById(R.id.room_air_money);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }

                if (mChildList.get(groupPosition) != null && mChildList.get(groupPosition).size() > 0) {
                    Float electricMoney = Float.parseFloat(mChildList.get(groupPosition).get(0).get("electricUse")) * Float.parseFloat(mChildList.get(groupPosition - 1).get(0).get("electric"));
                    viewHolder.mHouseElectricUse.setText(mChildList.get(groupPosition).get(0).get("electricUse"));
                    viewHolder.mHouseElectricMoney.setText(Float.toString(electricMoney));

                    Float airMoney = Float.parseFloat(mChildList.get(groupPosition).get(0).get("airUse")) * Float.parseFloat(mChildList.get(groupPosition - 1).get(0).get("air"));
                    viewHolder.mHouseAirUse.setText(mChildList.get(groupPosition).get(0).get("airUse"));
                    viewHolder.mHouseAirMoney.setText(Float.toString(airMoney));

                    Float waterMoney = Float.parseFloat(mChildList.get(groupPosition).get(0).get("waterUse")) * Float.parseFloat(mChildList.get(groupPosition - 1).get(0).get("water"));
                    viewHolder.mHouseWaterUse.setText(mChildList.get(groupPosition).get(0).get("waterUse"));
                    viewHolder.mHouseWaterMoney.setText(Float.toString(waterMoney));

                    Float sumMoney = waterMoney + electricMoney + airMoney
                            + Float.parseFloat(mChildList.get(groupPosition - 1).get(0).get("manage"))
                            + Float.parseFloat(mChildList.get(groupPosition - 1).get(0).get("other"))
                            + Float.parseFloat(mChildList.get(groupPosition - 1).get(0).get("monthly"));
                    viewHolder.mSumMoney.setText(Float.toString(sumMoney));
                } else {
                    view = mInflater.inflate(R.layout.view_money_empty, null);
                    Button button = (Button) view.findViewById(R.id.money_setting);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                            final View DialogView = mInflater.inflate(R.layout.view_money_setting, null);
                            dialog.setView(DialogView);
                            dialog.setNegativeButton(mContext.getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.setPositiveButton(mContext.getString(R.string.dialog_confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    EditText airUse = (EditText) DialogView.findViewById(R.id.money_airUse);
                                    EditText electUse = (EditText) DialogView.findViewById(R.id.money_electricUse);
                                    EditText waterUse = (EditText) DialogView.findViewById(R.id.money_waterUse);
                                    String air = airUse.getText().toString();
                                    String elect = electUse.getText().toString();
                                    String water = waterUse.getText().toString();
                                    if (air.isEmpty() || air.equals("")
                                            || elect.isEmpty() || elect.equals("")
                                            || water.isEmpty() || water.equals("")) {
                                        mIResponse.onFail(INPUT_ERROR);
                                    } else {
                                        RentalMoneyObject object = new RentalMoneyObject();
                                        object.setRoomNum(mChildList.get(1).get(0).get("num"));
                                        object.setElectricUse(elect);
                                        object.setWaterUse(water);
                                        object.setAirUse(air);
                                        object.setUploadAt(new BmobDate(mDate));
                                        object.setUserName(BmobUser.getCurrentUser().getUsername());
                                        ServerRequest.onSaveRequest(object, new onSaveResultsListener() {
                                            @Override
                                            public void onSuccess(String objectId) {
                                                mIResponse.onSuccess(INFO_SETUP_SUCCESS);
                                            }

                                            @Override
                                            public void onFail(int errorCode, String errorMessage) {
                                                mIResponse.onFail(INPUT_ERROR);
                                                Log.e(TAG, "Set MoneyMsg Fail - errorCode -> " + errorCode + " , errorMessage -> " + errorMessage);
                                            }
                                        });
                                    }
                                }
                            });

                            dialog.show();
                        }
                    });
                }
                break;
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class ViewHolder {
        ImageView mGroupImage;
        ImageView mGroupExpand;
        TextView mGroupName;

        TextView mRenterName;
        TextView mRenterSex;
        TextView mRenterPlace;
        TextView mRenterPhone;
        TextView mRenterJob;
        TextView mRenterLiveNum;
        TextView mRenterId;

        TextView mHouseTenancy;
        TextView mHouseDeposit;
        TextView mHouseMonthly;
        TextView mHouseDate;
        TextView mHouseWater;
        TextView mHouseElectric;
        TextView mHouseAir;
        TextView mHouseManage;
        TextView mHouseOther;

        TextView mHouseWaterUse;
        TextView mHouseWaterMoney;
        TextView mHouseElectricUse;
        TextView mHouseElectricMoney;
        TextView mHouseAirUse;
        TextView mHouseAirMoney;
        TextView mSumMoney;
    }
}