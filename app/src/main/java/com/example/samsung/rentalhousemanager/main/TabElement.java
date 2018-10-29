package com.example.samsung.rentalhousemanager.main;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.util.Pair;

/**
 * Created by yuyang.liang on 2018/6/21.
 */

public class TabElement implements Parcelable {

    private final int mPriority;
    private final int mName;
    private final String mFragmentClassName;
    private final Bundle mBundle;

    public static <T extends Fragment> TabElement create(int prior, int name, Class<T> fragmentName) {
        return new TabElement(prior, name, fragmentName, null);
    }

    public static <T extends Fragment> TabElement create(int prior, int name, Class<T> fragmentName, Bundle bundle) {
        return new TabElement(prior, name, fragmentName, bundle);
    }

    private <T extends Fragment> TabElement(int prior, int name, Class<T> fragmentName, Bundle bundle) {
        mPriority = prior;
        mName = name;
        mFragmentClassName = fragmentName.getName();
        mBundle = bundle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mPriority);
        out.writeInt(mName);
        out.writeString(mFragmentClassName);
        out.writeBundle(mBundle);
    }

    private TabElement(Parcel in) {
        mPriority = in.readInt();
        mName = in.readInt();
        mFragmentClassName = in.readString();
        mBundle = in.readBundle();
    }

    public static final Parcelable.Creator<TabElement> CREATOR = new Parcelable.Creator<TabElement>() {
        @Override
        public TabElement createFromParcel(Parcel source) {
            return new TabElement((source));
        }

        public TabElement[] newArray(int size) {
            return new TabElement[size];
        }
    };

    public Pair<Integer, String> getFragment() {
        return Pair.create(mName, mFragmentClassName);
    }

    public Bundle getArguments() {
        return mBundle;
    }

    public void setupTab(TabLayout.Tab tab) {
        tab.setText(mName);
    }
}
