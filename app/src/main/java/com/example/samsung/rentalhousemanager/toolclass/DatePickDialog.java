package com.example.samsung.rentalhousemanager.toolclass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

/**
 * Created by yuyang.liang on 2018/8/15.
 */

public class DatePickDialog extends NumberPicker {
    private final static String TAG = DatePickDialog.class.getSimpleName();

    private Context mContext;
    private NumberPicker mNumberPicker;

    public DatePickDialog(Context context) {
        super(context);
        mContext = context;
        mNumberPicker = this;
    }

    public DatePickDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mNumberPicker = this;
    }

    public DatePickDialog(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mNumberPicker = this;
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    public void updateView(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            ((EditText) view).setTextColor(Color.parseColor("#6495ED"));
        }
    }

    private int mScrollState;
    private int[] mSelectorIndices;
    private int mCurrentScrollOffset;
    private int mRight, mLeft, mBottom;
    private int mSelectorElementHeight;
    private int mTopSelectionDividerTop;
    private int mBottomSelectionDividerBottom;

    private boolean mHasSelectorWheel;
    private boolean mHideWheelUntilFocused;

    private EditText mInputText;
    private Paint mSelectorWheelPaint;
    private Drawable mSelectionDivider;
    private SparseArray<String> mSelectorIndexToStringCache;

    private void getNumberValue() {
        mLeft = super.getLeft();
        mRight = super.getRight();
        mBottom = super.getBottom();

        Field[] mNumberPickerFields = NumberPicker.class.getDeclaredFields();
        for (Field field : mNumberPickerFields) {
            field.setAccessible(true);
            if (field.getName().equals("mSelectorWheelPaint")) {
                try {
                    mSelectorWheelPaint = (Paint) field.get(mNumberPicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        for (Field field : mNumberPickerFields) {
            field.setAccessible(true);
            if (field.getName().equals("mSelectorElementHeight")) {
                try {
                    mSelectorElementHeight = (int) field.get(mNumberPicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        for (Field field : mNumberPickerFields) {
            field.setAccessible(true);
            if (field.getName().equals("mCurrentScrollOffset")) {
                try {
                    mCurrentScrollOffset = (int) field.get(mNumberPicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        for (Field field : mNumberPickerFields) {
            field.setAccessible(true);
            if (field.getName().equals("mInputText")) {
                try {
                    mInputText = (EditText) field.get(mNumberPicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        for (Field field : mNumberPickerFields) {
            field.setAccessible(true);
            if (field.getName().equals("mSelectorIndexToStringCache")) {
                try {
                    mSelectorIndexToStringCache = (SparseArray<String>) field.get(mNumberPicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        for (Field field : mNumberPickerFields) {
            field.setAccessible(true);
            if (field.getName().equals("mSelectorIndices")) {
                try {
                    mSelectorIndices = (int[]) field.get(mNumberPicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        for (Field field : mNumberPickerFields) {
            field.setAccessible(true);
            if (field.getName().equals("mHasSelectorWheel")) {
                try {
                    mHasSelectorWheel = (boolean) field.get(mNumberPicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        for (Field field : mNumberPickerFields) {
            field.setAccessible(true);
            if (field.getName().equals("mHideWheelUntilFocused")) {
                try {
                    mHideWheelUntilFocused = (boolean) field.get(mNumberPicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        for (Field field : mNumberPickerFields) {
            field.setAccessible(true);
            if (field.getName().equals("mScrollState")) {
                try {
                    mScrollState = (int) field.get(mNumberPicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        for (Field field : mNumberPickerFields) {
            field.setAccessible(true);
            if (field.getName().equals("mTopSelectionDividerTop")) {
                try {
                    mTopSelectionDividerTop = (int) field.get(mNumberPicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        for (Field field : mNumberPickerFields) {
            field.setAccessible(true);
            if (field.getName().equals("mBottomSelectionDividerBottom")) {
                try {
                    mBottomSelectionDividerBottom = (int) field.get(mNumberPicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        for (Field field : mNumberPickerFields) {
            field.setAccessible(true);
            if (field.getName().equals("mSelectionDivider")) {
                try {
                    mSelectionDivider = (Drawable) field.get(mNumberPicker);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getNumberValue();
        mSelectorWheelPaint.setColor(Color.BLUE);
        if (!mHasSelectorWheel) {
            Log.e(TAG, "super");
            super.onDraw(canvas);
            return;
        }

        final boolean showSelectorWheel = mHideWheelUntilFocused ? hasFocus() : true;
        float x = (mRight - mLeft) / 2;
        float y = mCurrentScrollOffset;
        int[] selectorIndices = mSelectorIndices;

        for (int i = 0; i < selectorIndices.length; i++) {
            int selectorIndex = selectorIndices[i];
            String scrollSelectorValue = mSelectorIndexToStringCache.get(selectorIndex);

            if (i != 1) {
                mSelectorWheelPaint.setColor(Color.BLACK);
                mSelectorWheelPaint.setTextSize(sp2px(14));
            } else {
                mSelectorWheelPaint.setColor(Color.parseColor("#6495ED"));
                mSelectorWheelPaint.setTextSize(sp2px(16));
            }

            if ((showSelectorWheel && i != 1) || (i == 1 && mInputText.getVisibility() != VISIBLE)) {
                Rect rect = new Rect();
                mSelectorWheelPaint.getTextBounds(scrollSelectorValue, 0, scrollSelectorValue.length(), rect);
                canvas.drawText(scrollSelectorValue, x, y, mSelectorWheelPaint);
            }
            y += mSelectorElementHeight;
        }

        if (showSelectorWheel && mSelectionDivider != null) {
            mSelectionDivider = new ColorDrawable(Color.parseColor("#a0c4c4c4"));
            int topOfTopDivider = mTopSelectionDividerTop;
            int bottomOfTopDivider = topOfTopDivider + 2;
            mSelectionDivider.setBounds(0, topOfTopDivider, mRight, bottomOfTopDivider);
            mSelectionDivider.draw(canvas);

            int bottomOfBottomDivider = mBottomSelectionDividerBottom;
            int topOfBottomDivider = bottomOfBottomDivider - 2;
            mSelectionDivider.setBounds(0, topOfBottomDivider, mRight, bottomOfBottomDivider);
            mSelectionDivider.draw(canvas);
        }
    }

    private int sp2px(int sp) {
        float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (scale * sp + 0.5f);
    }
}
