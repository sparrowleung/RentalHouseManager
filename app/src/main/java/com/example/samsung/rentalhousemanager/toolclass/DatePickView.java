package com.example.samsung.rentalhousemanager.toolclass;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by yuyang.liang on 2018/8/16.
 */

public class DatePickView extends LinearLayout implements NumberPicker.OnValueChangeListener {
    private final static String TAG = DatePickView.class.getSimpleName();
    private Context mContext;

    //视图控件
    private TextView mMinuteTip;
    private DatePickDialog mNpLeft, mNpMiddle, mNpRight;
    //框之间的距离
    private int mOffsetMargin = 12;
    //一组数据长度
    private final int DATA_SIZE = 5;
    //当前时间戳
    private long mTimeMillis;
    //回调监听
    private onSelectedChangedListener mSelectedListener;
    private SimpleDateFormat mDateFormat;

    public DatePickView(Context context) {
        super(context);
        mContext = context;
        generateView();
        initPicker();
    }

    public DatePickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        generateView();
        initPicker();
    }

    public DatePickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        generateView();
        initPicker();
    }


    private void generateView() {
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.setOrientation(VERTICAL);
        this.setGravity(Gravity.CENTER);

        LinearLayout container = new LinearLayout(mContext);
        container.setOrientation(HORIZONTAL);
        container.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setGravity(Gravity.CENTER);

        mNpLeft = new DatePickDialog(mContext);
        mNpRight = new DatePickDialog(mContext);
        mNpMiddle = new DatePickDialog(mContext);
        mMinuteTip = new TextView(mContext);
        mMinuteTip.setText(":");

        mNpLeft.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        mNpRight.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        mNpMiddle.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        mNpLeft.setLayoutParams(params);
        params.setMargins(dip2px(mOffsetMargin), 0, 0, 0);
        mNpMiddle.setLayoutParams(params);
        mNpRight.setLayoutParams(params);
        mMinuteTip.setLayoutParams(params);

        mNpLeft.setOnValueChangedListener(this);
        mNpRight.setOnValueChangedListener(this);
        mNpMiddle.setOnValueChangedListener(this);

        container.addView(mNpLeft);
        container.addView(mNpMiddle);
        container.addView(mMinuteTip);
        container.addView(mNpRight);
        this.addView(container);
        initTime();

    }

    public void initPicker() {
        mDateFormat = new SimpleDateFormat("yyyy.MM");
        mMinuteTip.setVisibility(GONE);

        mNpLeft.setMinValue(0);
        mNpLeft.setMaxValue(DATA_SIZE - 1);
        updateLeftValue(mTimeMillis);

        mNpMiddle.setMinValue(0);
        mNpMiddle.setMaxValue(DATA_SIZE - 1);
        updateMiddleValue(mTimeMillis);

        mNpRight.setMinValue(0);
        mNpRight.setMaxValue(DATA_SIZE - 1);
        updateRightValue(mTimeMillis);
        mNpRight.setVisibility(GONE);
    }

    //更新左侧控件 选择年份控件
    private void updateLeftValue(long timeMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String str[] = new String[DATA_SIZE];
        for (int i = 0; i < DATA_SIZE; i++) {
            Calendar calender = Calendar.getInstance();
            calender.setTimeInMillis(timeMillis);
            calender.add(Calendar.YEAR, (i - DATA_SIZE / 2));
            str[i] = dateFormat.format(calender.getTimeInMillis());
        }

        mNpLeft.setDisplayedValues(str);
        mNpLeft.setValue(DATA_SIZE / 2);
        mNpLeft.postInvalidate();
    }

    //更新中间控件，选择月份控件
    private void updateMiddleValue(long timeMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        String str[] = new String[DATA_SIZE];
        for (int i = 0; i < DATA_SIZE; i++) {
            Calendar calender = Calendar.getInstance();
            calender.setTimeInMillis(timeMillis);
            calender.add(Calendar.MONTH, (i - DATA_SIZE / 2));
            str[i] = dateFormat.format(calender.getTimeInMillis());
        }
        mNpMiddle.setDisplayedValues(str);
        mNpMiddle.setValue(DATA_SIZE / 2);
        mNpMiddle.postInvalidate();
    }

    //更新右侧控件，选择日期
    private void updateRightValue(long timeMillis) {
        String str[] = new String[DATA_SIZE];
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(timeMillis);

        int days = getMaxDayOfMonth(calender.get(Calendar.YEAR), calender.get(Calendar.MONTH) + 1);
        int currentDay = calender.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < DATA_SIZE; i++) {
            int temp = currentDay - (DATA_SIZE / 2 - i);
            if (temp > days) {
                temp -= days;
            }
            if (temp < 1) {
                temp += days;
            }
            str[i] = temp > 9 ? temp + "" : "0" + temp;
        }

        mNpRight.setDisplayedValues(str);
        mNpRight.setValue(DATA_SIZE / 2);
        mNpRight.postInvalidate();
    }

    public void initTime() {
        Calendar calendar = Calendar.getInstance();
        mTimeMillis = calendar.getTimeInMillis();
    }

    public int getMaxDayOfMonth(int year, int month) {
        int result = 0;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            result = 31;
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            result = 30;
        } else {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                result = 29;
            } else {
                result = 28;
            }
        }
        return result;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mTimeMillis);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int offset = newVal - oldVal;

        if (picker == mNpLeft) {
            calendar.add(Calendar.YEAR, offset);
            updateLeftValue(calendar.getTimeInMillis());
            mTimeMillis = calendar.getTimeInMillis();
        } else if (picker == mNpMiddle) {
            calendar.add(Calendar.MONTH, offset);
            if (calendar.get(Calendar.YEAR) != year) {
                calendar.set(Calendar.YEAR, year);
            }
            updateMiddleValue(calendar.getTimeInMillis());
            mTimeMillis = calendar.getTimeInMillis();
        } else if (picker == mNpRight) {
            int days = getMaxDayOfMonth(year, month + 1);
            if (day == 1 && offset < 0) {
                calendar.set(Calendar.DAY_OF_MONTH, days);
            } else if (day == days && offset > 0) {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
            } else {
                calendar.add(Calendar.DAY_OF_MONTH, offset);
            }

            if (calendar.get(Calendar.MONTH) != month) {
                calendar.set(Calendar.MONTH, month);
            }
            if (calendar.get(Calendar.YEAR) != year) {
                calendar.set(Calendar.YEAR, year);
            }
            updateRightValue(calendar.getTimeInMillis());
            mTimeMillis = calendar.getTimeInMillis();
        }

        if (mSelectedListener != null) {
            mSelectedListener.onSelected(this, mTimeMillis);
        }
    }

    public void setOnSelectedChangedListener(onSelectedChangedListener selectedListener) {
        this.mSelectedListener = selectedListener;
    }

    public void setTimeMillis(long timeMillis) {
        if (timeMillis != 0) {
            this.mTimeMillis = timeMillis;
            initPicker();
            this.postInvalidate();
        } else {
            initTime();
        }
    }

    private int dip2px(int dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (scale * dp + 0.5f);
    }

    public interface onSelectedChangedListener {
        void onSelected(DatePickView view, long timeMillis);
    }
}
