package com.example.samsung.rentalhousemanager.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.roomdata.RoomDetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yuyang.liang on 2018/7/12.
 */

public class HomeExpandAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private Activity mActivity;
    private Integer mFourFloor;
    private Integer mFourRoom;
    private List<String> mFloorList;
    private List<Map<String, Integer>> mGroupMap;
    private List<List<Map<String, Integer>>> mChildMap;

    private LayoutInflater mInflater;

    HomeExpandAdapter(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
        mGroupMap = new ArrayList<>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void setupHomeMap(List<Map<String, Integer>> groupMap, List<List<Map<String, Integer>>> childMap, Integer fourFloor, Integer fourRoom) {
        mGroupMap = groupMap;
        mChildMap = childMap;
        mFourFloor = fourFloor;
        mFourRoom = fourRoom;
        mFloorList = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return mGroupMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupMap.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return mChildMap.get(groupPosition).get(childPosition);
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
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.list_home_group, null);
            viewHolder.floorText = (TextView) view.findViewById(R.id.item_text);
            viewHolder.expandImage = (ImageView) view.findViewById(R.id.item_expand);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (isExpanded) {
            viewHolder.expandImage.setImageResource(R.drawable.up);
        } else {
            viewHolder.expandImage.setImageResource(R.drawable.down);
        }

        String floor = String.valueOf(mGroupMap.get(groupPosition).get("group"));
        if (floor.contains("4")) {
            switch (mFourFloor) {
                case 1:
                    break;
                case 2:
                    floor = floor.replace("4", "3A");
                    break;
            }
        }

        viewHolder.floorText.setText(floor);
        mFloorList.add(groupPosition, floor);
        return view;
    }

    private int mIncrement;

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        mIncrement = 0;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.list_home_child, null);
            viewHolder.roomList = (HomeGridView) view.findViewById(R.id.home_gridview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        HomeGridViewAdapter homeGridViewAdapter = new HomeGridViewAdapter(mContext);
        homeGridViewAdapter.setGridMap(mChildMap, groupPosition, mFloorList, mFourRoom);

        viewHolder.roomList.setAdapter(homeGridViewAdapter);
        viewHolder.roomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, RoomDetailActivity.class);
                intent.putExtra("roomNum", HomeGridViewAdapter.roomChaangeList.get(position));
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        ImageView expandImage;
        TextView floorText;
        HomeGridView roomList;
    }

}
