package com.example.samsung.rentalhousemanager.main;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.toolclass.CustomExpandableListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yuyang.liang on 2018/7/4.
 */

public class HomeDrawerAdapter extends BaseExpandableListAdapter {

    private static final int VIEW_TYPE_ITEM = 1;
    private static final int VIEW_TYPE_DIVIDER = 2;

    private List<Map<String, Object>> mDrawerMap = null;
    private LayoutInflater mInflater = null;
    private Context mContext = null;
    private Activity mActivity = null;

    public HomeDrawerAdapter(Context context) {
        mContext = context;
        mDrawerMap = new ArrayList<>();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setUpDrawerMap(List<Map<String, Object>> drawerMap) {
        mDrawerMap = drawerMap;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    @Override
    public int getGroupCount() {
        return mDrawerMap.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Map<String, Object> groupMap = mDrawerMap.get(groupPosition);

        if (groupMap != null && groupMap.containsKey("type") && groupMap.get("type").equals("group") && groupMap.containsKey("child")) {
            List<Map<String, Object>> childMap = (List<Map<String, Object>>) groupMap.get("child");
            return childMap.size();
        }

        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDrawerMap.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Map<String, Object> groupMap = mDrawerMap.get(groupPosition);

        if (groupMap != null && groupMap.containsKey("type") && groupMap.get("type").equals("group") && groupMap.containsKey("child")) {
            List<Map<String, Object>> childList = (List<Map<String, Object>>) groupMap.get("child");
            Map<String, Object> childMap = childList.get(childPosition);
            return childMap;
        }
        return null;
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
        final ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.list_drawer_group, null);
            viewHolder.divider = view.findViewById(R.id.divider);
            viewHolder.drawerItem = (ViewGroup) view.findViewById(R.id.drawer_item);
            viewHolder.drawerArrow = (ImageView) view.findViewById(R.id.drawer_arrow);
            viewHolder.drawerIcon = (ImageView) view.findViewById(R.id.drawer_icon);
            viewHolder.drawerText = (TextView) view.findViewById(R.id.drawer_text);
            viewHolder.newTextView = (TextView) view.findViewById(R.id.newTextView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        switch (getViewType(groupPosition)) {
            case VIEW_TYPE_DIVIDER:
                viewHolder.divider.setVisibility(View.VISIBLE);
                viewHolder.drawerItem.setVisibility(View.GONE);
                break;
            case VIEW_TYPE_ITEM:
                viewHolder.divider.setVisibility(View.GONE);
                viewHolder.drawerItem.setVisibility(View.VISIBLE);
                Map<String, Object> groupMap = (Map<String, Object>) getGroup(groupPosition);
                if (groupMap != null) {
                    String type = (String) groupMap.get("type");
                    int name = (int) groupMap.get("name");
                    int icon = (int) groupMap.get("icon");
                    Glide.with(mContext).load(icon).into(viewHolder.drawerIcon);

                    switch (type) {
                        case "group":
                            viewHolder.drawerArrow.setVisibility(View.VISIBLE);
                            viewHolder.drawerText.setText(name);
                            viewHolder.drawerArrow.setImageResource(R.drawable.down);
                            break;
                        case "single":
                            viewHolder.drawerArrow.setVisibility(View.GONE);
                            viewHolder.drawerText.setText(name);
                            break;
                        case "main":
                            viewHolder.drawerArrow.setVisibility(View.GONE);
                            viewHolder.drawerText.setText(name);
                            break;
                        default:
                            break;
                    }
                }
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;

        if (view == null) {
            view = mInflater.inflate(R.layout.list_drawer_child, null);
            viewHolder = new ViewHolder();
            viewHolder.drawerText = (TextView) view.findViewById(R.id.drawer_text);
            viewHolder.expandableList = getExpandableListView(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ViewHolder {
        View divider;
        ViewGroup drawerItem;
        ImageView drawerIcon;
        ImageView drawerArrow;
        TextView drawerText;
        TextView newTextView;
        CustomExpandableListView expandableList;
    }

    public int getViewType(int groupPosition) {
        Map<String, Object> groupMap = (Map<String, Object>) getGroup(groupPosition);
        if (groupMap != null) {
            String type = (String) groupMap.get("type");
            if (TextUtils.equals(type, "divider")) {
                return VIEW_TYPE_DIVIDER;
            }
        }
        return VIEW_TYPE_ITEM;
    }

    private CustomExpandableListView getExpandableListView(View view) {
        final CustomExpandableListView mExpandableListView = (CustomExpandableListView) view.findViewById(R.id.drawer_listview_child);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setChildIndicator(null);
        mExpandableListView.setDividerHeight(0);

        mExpandableListView.invalidate();
        mExpandableListView.requestLayout();

        final MainActivity mainActivity = (MainActivity) mActivity;
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (mainActivity != null) {
                    mainActivity.requestResize(mExpandableListView.getCount());
                }
            }
        });

        mExpandableListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CustomExpandableListView listView = (CustomExpandableListView) v;
                if (hasFocus && listView != null) {
                    listView.expandGroup(0);
                }
            }
        });

        return mExpandableListView;
    }
}
