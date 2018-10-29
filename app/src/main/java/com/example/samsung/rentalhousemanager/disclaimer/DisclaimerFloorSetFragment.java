package com.example.samsung.rentalhousemanager.disclaimer;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.baseclass.BaseFragment;
import com.example.samsung.rentalhousemanager.databinding.FragmentFloorBinding;
import com.example.samsung.rentalhousemanager.toolclass.ActionLinkManager;
import com.example.samsung.rentalhousemanager.toolclass.RHToast;


import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by yuyang.liang on 2018/9/6.
 */

public class DisclaimerFloorSetFragment extends BaseFragment {
    private final static String TAG = DisclaimerFloorSetFragment.class.getSimpleName();

    private Integer floorNum;
    private Integer floorRoom;
    private Integer fourFloor;
    private Integer fourRoom;

    private FragmentFloorBinding mSetFloorBinding;
    private NewDisclaimerViewModel mViewModel;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstanceState) {
        mSetFloorBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_floor, container, false);

        mViewModel = new NewDisclaimerViewModel(getContext());
        mSetFloorBinding.setFloorViewModel(mViewModel);
        return mSetFloorBinding.getRoot();
    }

    @Override
    public void onStart() {

        super.onStart();
        mDisposable.add(
                mViewModel.mPublishSubject
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                switch (integer) {
                                    case NewDisclaimerViewModel.TYPE_MESSAGE_EMPTY:
                                        RHToast.makeText(getContext(), "Msg Empty & Input Data", Toast.LENGTH_SHORT).show();
                                        break;
                                    case NewDisclaimerViewModel.TYPE_SETFLOOR_FINISH:
                                        if (floorNum != null && floorRoom!= null && fourFloor != null && fourRoom != null) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("floorNum", floorNum);
                                            bundle.putInt("floorRoom", floorRoom);
                                            bundle.putInt("fourFloor", fourFloor);
                                            bundle.putInt("fourRoom", fourRoom);
                                            ActionLinkManager.performContextActionLink(getContext(), "voc://view/main", bundle);
                                            if (getActivity() != null && !getActivity().isFinishing()) {
                                                getActivity().finish();
                                            }
                                        }
                                        break;
                                }
                            }
                        }));

        mDisposable.add(
                mViewModel.mPubSubject
                        .subscribe(new Consumer<Map<String, Integer>>() {
                            @Override
                            public void accept(Map<String, Integer> stringIntegerMap) throws Exception {
                                floorNum = stringIntegerMap.get("floorNum");
                                floorRoom = stringIntegerMap.get("floorRoom");
                                fourFloor = stringIntegerMap.get("fourFloor");
                                fourRoom = stringIntegerMap.get("fourRoom");
                            }
                        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
