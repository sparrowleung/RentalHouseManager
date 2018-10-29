package com.example.samsung.rentalhousemanager.message;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.baseclass.BaseFragment;
import com.example.samsung.rentalhousemanager.roomdata.IResponse;
import com.example.samsung.rentalhousemanager.toolclass.RHToast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yuyang.liang on 2018/7/9.
 */

public class MsgSendFragment extends BaseFragment implements IResponse {
    private static String TAG = MsgSendFragment.class.getSimpleName();

    private View mRootView;
    private TextView dateView;
    private Button mSendMsgButton;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    private List<String> sumDeposit = new ArrayList<>();
    private List<List<Map<String, String>>> mDataList = new ArrayList<>();
    private MsgSendPresenter mMsgSendPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mRootView = inflater.inflate(R.layout.fragment_msg_send, container, false);

        mSendMsgButton = mRootView.findViewById(R.id.msg_send);
        mRecyclerView = mRootView.findViewById(R.id.msg_listView);
        mProgressBar = mRootView.findViewById(R.id.msg_progressBar);
        dateView = mRootView.findViewById(R.id.month_Now);

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        mSendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgSendConfirmDialog();
            }
        });
    }

    @Override
    public void onFail(String type) {
        mProgressBar.setVisibility(View.GONE);
        RHToast.makeText(mContext, "获取住户信息错误，请重试", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(String type) {
        switch (type) {
            case "MsgSend" :
                RHToast.makeText(mContext, "获取住户信息成功", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                createListHeater();
                RentalAllMsgListAdapter listAdapter= new RentalAllMsgListAdapter(mContext, mDataList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                mRecyclerView.setAdapter(listAdapter);
                break;
        }
    }

    @Override
    public void onComplete(boolean normal) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisit) {
        super.setUserVisibleHint(isVisit);
        if (getUserVisibleHint()) {
            requestDataFromServer();
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        } else {
            if (mProgressBar != null) {
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public void msgSendConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("确认框");
        builder.setMessage("是否确认上述信息无误？");
        builder.setNegativeButton("再检查一下", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认无误", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMsgSendPresenter.sendMsgForRental(sumDeposit);
                RHToast.makeText(mContext, "Message Send", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }

    public void requestDataFromServer() {
        mMsgSendPresenter = MsgSendPresenter.create(mContext, this);
        mMsgSendPresenter.onCreate(null);
        mDataList = mMsgSendPresenter.getRenterAllMsg();
        dateView.setText(mMsgSendPresenter.getDateForNow());

        if (mRecyclerView != null) {
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    public void createListHeater() {
        FrameLayout frameLayout = mRootView.findViewById(R.id.container);
        View view = LayoutInflater.from(frameLayout.getContext()).inflate(R.layout.listitem_msg_send, null);
        TextView textView = view.findViewById(R.id.list_item_roomNum);
        textView.setText("房号");
        TextView textView1 = view.findViewById(R.id.list_item_renterName);
        textView1.setText(getString(R.string.customer_name));
        TextView textView2 = view.findViewById(R.id.list_item_renterPhone);
        textView2.setText("联系电话");
        TextView textView3 = view.findViewById(R.id.list_item_sumMoney);
        textView3.setText("总租金");
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameLayout.addView(view, lp);
    }

    class RentalAllMsgListAdapter extends RecyclerView.Adapter<RentalAllMsgListAdapter.RentalAllMsgViewHolder> {
        private List<Map<String, String>> mRenterList = new ArrayList<>();
        private List<Map<String, String>> mRentalList = new ArrayList<>();
        private List<Map<String, String>> mRoomList = new ArrayList<>();
        private Context mContext;

        private String waterUse;
        private String electricUse;
        private String airUse;
        private String air;
        private String water;
        private String electric;
        private String monthlyMoney;
        private String manageMoney;
        private String otherMoney;
        private float sumMoney;

        public RentalAllMsgListAdapter(Context context, List<List<Map<String, String>>> dataList) {
            mContext = context;
            mRoomList = dataList.get(2);
            mRenterList = dataList.get(0);
            mRentalList = dataList.get(1);
        }

        class RentalAllMsgViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;
            private TextView textView1;
            private TextView textView2;
            private TextView textView3;

            public RentalAllMsgViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.list_item_roomNum);
                textView3 = itemView.findViewById(R.id.list_item_sumMoney);
                textView1 = itemView.findViewById(R.id.list_item_renterName);
                textView2 = itemView.findViewById(R.id.list_item_renterPhone);
            }
        }

        @Override
        public RentalAllMsgViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_msg_send, viewGroup, false);
            return new RentalAllMsgViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RentalAllMsgViewHolder rentalAllMsgViewHolder, int i) {

            rentalAllMsgViewHolder.textView1.setText(mRenterList.get(i).get("name"));
            rentalAllMsgViewHolder.textView.setText(mRenterList.get(i).get("roomNum"));
            rentalAllMsgViewHolder.textView2.setText(mRenterList.get(i).get("phoneNum"));

            airUse = mRentalList.get(i).get("airUse");
            waterUse = mRentalList.get(i).get("waterUse");
            electricUse = mRentalList.get(i).get("electricUse");

            air = mRoomList.get(i).get("air");
            water = mRoomList.get(i).get("water");
            electric = mRoomList.get(i).get("electric");

            otherMoney = mRoomList.get(i).get("other");
            manageMoney = mRoomList.get(i).get("manage");
            monthlyMoney = mRoomList.get(i).get("monthly");

            sumMoney  = Float.parseFloat(waterUse) * Float.parseFloat(water) +
                    Float.parseFloat(electricUse) * Float.parseFloat(electric) +
                    Float.parseFloat(airUse) * Float.parseFloat(air) +
                    Float.parseFloat(monthlyMoney) + Float.parseFloat(manageMoney) + Float.parseFloat(otherMoney);

            DecimalFormat decimalFormat = new DecimalFormat(".00");
            String sumDepositStr = decimalFormat.format(sumMoney);
            sumDeposit.add(sumDepositStr);
            rentalAllMsgViewHolder.textView3.setText(sumDepositStr);
        }

        @Override
        public int getItemCount() {
            return mRentalList.size();
        }
    }
}
