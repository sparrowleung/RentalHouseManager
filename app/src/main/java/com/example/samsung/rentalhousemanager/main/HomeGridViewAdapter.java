package com.example.samsung.rentalhousemanager.main;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.samsung.rentalhousemanager.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yuyang.liang on 2018/7/16.
 */

public class HomeGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private int groupPosition;
    private int mFourRoom;
    private int mIncrement;
    private LayoutInflater mInflater;
    private List<String> mFloorList;
    private List<List<Map<String, Integer>>> mGridMap;

    HomeGridViewAdapter(Context context) {
        mContext = context;
        mGridMap = new ArrayList<>();
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setGridMap(List<List<Map<String, Integer>>> gridMap, int groupPosition, List<String> floorList, int fourChange) {
        mGridMap = gridMap;
        mIncrement = 0;
        this.mFloorList = floorList;
        this.mFourRoom = fourChange;
        this.groupPosition = groupPosition;
    }

    @Override
    public int getCount() {
        return mGridMap.get(groupPosition).size();
    }

    @Override
    public Object getItem(int position) {
        return mGridMap.get(groupPosition).get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.view_grid_view, null);
            viewHolder.gridImageView = (ImageView) view.findViewById(R.id.image_gridview);
            viewHolder.gridTextView = (TextView) view.findViewById(R.id.text_gridview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String roomNum = String.valueOf(mGridMap.get(groupPosition).get(position).get("child") + mIncrement);
        if (roomNum.contains("4")) {
            switch (mFourRoom) {
                case 3:
                    break;
                case 4:
                    mIncrement += 1;
                    roomNum = String.valueOf(mGridMap.get(groupPosition).get(position).get("child") + mIncrement);
                    break;
                case 5:
                    roomNum = roomNum.replace("4", "3A");
                    break;
            }
        }

        if (mGridMap.get(groupPosition).get(position).get("child") < 10) {
            viewHolder.gridTextView.setText(new StringBuffer(mFloorList.get(groupPosition)).append("0").append(roomNum));
        } else {
            viewHolder.gridTextView.setText(new StringBuffer(mFloorList.get(groupPosition)).append(roomNum));
        }
        return view;
    }


    static class ViewHolder {
        ImageView gridImageView;
        TextView gridTextView;
    }
}
