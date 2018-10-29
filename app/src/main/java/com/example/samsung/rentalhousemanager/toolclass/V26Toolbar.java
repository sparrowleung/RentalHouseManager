package com.example.samsung.rentalhousemanager.toolclass;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.StringRes;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.support.v7.appcompat.R;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by yuyang.liang on 2018/6/19.
 */

public class V26Toolbar extends Toolbar {
    private final static String TAG = "V26Toolbar";

    private TextView mTitleTextView;
    private CharSequence mTitleText;
    private int mTitleTextColor;
    private int mTitleTextAppearance;

    public V26Toolbar(Context context) {
        super(context);
        resolveAttribute(context, null, R.attr.toolbarStyle);
    }

    public V26Toolbar(Context context, AttributeSet attr) {
        super(context, attr);
        resolveAttribute(context, attr, R.attr.toolbarStyle);
    }

    public V26Toolbar(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
        resolveAttribute(context, attr, defStyleAttr);
    }

    private void resolveAttribute(Context context, AttributeSet attr, int defStyleAttr) {
        context = getContext();
        final TintTypedArray tintArray = TintTypedArray.obtainStyledAttributes(context, attr, R.styleable.Toolbar, defStyleAttr, 0);
        final int titleTextAppearance = tintArray.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);

        if (titleTextAppearance != 0) {
            setTitleTextAppearance(context, titleTextAppearance);
        }

        if (mTitleTextColor != 0) {
            setTitleTextColor(mTitleTextColor);
        }

        tintArray.recycle();
        post(new Runnable() {
            @Override
            public void run() {
                if (getLayoutParams() instanceof LayoutParams) {
                    ((LayoutParams) getLayoutParams()).gravity = Gravity.CENTER;
                }
            }
        });
    }

    @Override
    public CharSequence getTitle() {
        return mTitleText;
    }

    @Override
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            if (mTitleTextView == null) {
                final Context context = getContext();
                mTitleTextView = new TextView(context);
                mTitleTextView.setSingleLine();
                mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                if (mTitleTextAppearance != 0) {
                    mTitleTextView.setTextAppearance(context, mTitleTextAppearance);
                }
                if (mTitleTextColor != 0) {
                    mTitleTextView.setTextColor(mTitleTextColor);
                }
            } else if (mTitleTextView.getParent() != this) {
                addCenterView(mTitleTextView);
            }
            if (mTitleTextView != null) {
                mTitleTextView.setGravity(Gravity.CENTER);
                mTitleTextView.setText(title);
            }
            mTitleText = title;
        }
    }

    private void addCenterView(View textView) {
        ViewGroup.LayoutParams vgl = textView.getLayoutParams();
        LayoutParams lp;
        if (vgl == null) {
            lp = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vgl)) {
            lp = generateLayoutParams(vgl);
        } else {
            lp = (LayoutParams) vgl;
        }
        addView(textView, lp);
    }

    @Override
    public void setTitleTextAppearance(Context context, @StringRes int resId) {
        mTitleTextAppearance = resId;
        if (mTitleTextView != null) {
            mTitleTextView.setTextAppearance(context, resId);
        }
    }

    @Override
    public void setTitleTextColor(@ColorInt int color) {
        mTitleTextColor = color;
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(color);
        }
    }


}
