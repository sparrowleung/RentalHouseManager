package com.example.samsung.rentalhousemanager.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.baseclass.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yuyang.liang on 2018/7/6.
 */

public class HomeFragment extends BaseFragment implements IHomeView {
    private static String TAG = HomeFragment.class.getSimpleName();

    private View mRootView;
    private TextView failText;
    private ExpandableListView mExpandListView;
    private HomeExpandAdapter mHomeExpandAdapter;
    private HomeListPresenter mPresenter;

    private List<Map<String, Integer>> mFloorList = new ArrayList<>();
    private List<List<Map<String, Integer>>> mChildList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        mTitle = getResources().getString(R.string.app_name);
        updateActionBar();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);

        failText = mRootView.findViewById(R.id.home_failText);
        mExpandListView = mRootView.findViewById(R.id.home_expandlist);
        mHomeExpandAdapter = new HomeExpandAdapter(getContext(), getActivity());

        mPresenter = HomeListPresenter.create(this);
        mPresenter.onCreate(saveInstanceState, getArguments());
    }

    @Override
    public void setFloor(List<Map<String, Integer>> floorList) {
        mFloorList = floorList;
    }

    @Override
    public void setChildList(List<List<Map<String, Integer>>> childList) {
        mChildList = childList;
    }

    @Override
    public void setExpandList() {
        failText.setVisibility(View.GONE);
        mExpandListView.setDivider(null);
        mExpandListView.setGroupIndicator(null);
        mExpandListView.setChildIndicator(null);

        mExpandListView.setAdapter(mHomeExpandAdapter);
        mHomeExpandAdapter.setupHomeMap(mFloorList, mChildList, HomeListPresenterImpl.mFourFloor, HomeListPresenterImpl.mFourRoom);
        mHomeExpandAdapter.notifyDataSetChanged();
        mExpandListView.invalidate();
        mExpandListView.requestLayout();
    }

    @Override
    public void getMsgFail() {
        failText.setVisibility(View.VISIBLE);
    }
}
