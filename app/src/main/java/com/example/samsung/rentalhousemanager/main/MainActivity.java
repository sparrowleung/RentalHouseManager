package com.example.samsung.rentalhousemanager.main;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.databinding.DataBindingUtil;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.guillotine.GuillotineAnimation;
import com.example.guillotine.GuillotineListener;
import com.example.guillotine.GuillotineViewInterpolator;
import com.example.samsung.rentalhousemanager.R;
import com.example.samsung.rentalhousemanager.baseclass.BaseActivity;
import com.example.samsung.rentalhousemanager.databinding.ActivityMainBinding;
import com.example.samsung.rentalhousemanager.toolclass.ActionLinkManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import cn.bmob.v3.BmobUser;

public class MainActivity extends BaseActivity implements IMainView, GuillotineListener {
    private final static String TAG = MainActivity.class.getSimpleName();

    public static final String KEY_QUERY_TAB = "tab";

    private int mOffset;
    private Bundle mBundle = new Bundle();

    private Toolbar mToolbar;
    private View mProfileHeader;
    private View guillotineView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private AppBarLayout mBarLayout;
    private NavigationView mNaviView;
    private ExpandableListView mDrawerListView;

    private MainPresenter mMainPresenter;
    private TabPagerAdapter mPagerAdapter;
    private HomeDrawerAdapter mDrawerAdapter;
    private ActivityMainBinding mMainBinding;

    public enum Main {
        Home("home", MainType.HOME),
        NewsTips("newstips", MainType.NewsTips);

        public final String mName;
        public final MainType mType;

        Main(String name, MainType mainType) {
            mName = name;
            mType = mainType;
        }

        public boolean contains(@NonNull String name) {
            if (name.isEmpty()) {
                return false;
            }
            for (Main tab : values()) {
                if (tab.mName.equals(name)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBundle = getIntent().getExtras();
        getWindow().setBackgroundDrawable(null);
        setViewId();

        mBarLayout.addOnOffsetChangedListener(onOffsetChangedListener);
        setDrawerProfile(mDrawerListView);
        mToolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(mToolbar);

        mMainPresenter = MainPresenter.create(this, this, mBundle);
        mMainPresenter.onCreate(savedInstanceState);

        setViewPager();
        setupTabLayout();
        setupDrawer(mDrawerListView);
        setupActionBar();
        setTabPadding(mTabLayout);

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 298, this.getResources().getDisplayMetrics());
        mDrawerLayout.addView(guillotineView, new DrawerLayout.LayoutParams(width, DrawerLayout.LayoutParams.WRAP_CONTENT));

        ImageView imageView = guillotineView.findViewById(R.id.drawerIcon);
        imageView.setImageResource(R.drawable.navi_icon_90);
        TextView textView = guillotineView.findViewById(R.id.jpTitleTextView);
        textView.setText(getString(R.string.app_name));
        View closeView = guillotineView.findViewById(R.id.drawer_layout);

        ExpandableListView guillotine_list = guillotineView.findViewById(R.id.drawer_list);
        setDrawerProfile(guillotine_list);
        setupDrawer(guillotine_list);
        guillotine_list.setAdapter(mDrawerAdapter);

        new GuillotineAnimation.AnimationBuilder(mDrawerIcon, closeView, guillotineView)
                .setStartDelay(250)
                .setActionBarView(mToolbar)
                .setIsCloseOnBegin(true)
                .setListener(this)
                .build();
    }

    @Override
    public void setupActionBar() {
        setupActionBarTitle();
    }

    @Override
    public void setupTabLayout() {
        List<TabElement> tabType = mMainPresenter.getTabs();
        mPagerAdapter.setTabElement(tabType);
        mTabLayout.removeAllTabs();

        for (TabElement tab : tabType) {
            TabLayout.Tab tb = mTabLayout.newTab();
            tab.setupTab(tb);
            mTabLayout.addTab(tb);
        }

        setMainTextFontStyle(mTabLayout, mViewPager.getCurrentItem());
    }

    @Override
    public void setupDrawer(ExpandableListView expandableListView) {
        setProfileHeader();

        List<Map<String, Object>> drawerMap = mMainPresenter.getDrawer();
        mDrawerAdapter.setUpDrawerMap(drawerMap);
        mDrawerAdapter.notifyDataSetChanged();
        expandableListView.invalidate();
        expandableListView.requestLayout();

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Map<String, Object> groupMap = (Map<String, Object>) mDrawerAdapter.getGroup(groupPosition);
                String type = (String) groupMap.get("type");

                switch (type) {
                    case "group":
                        break;
                    case "single":
                        if (groupMap.containsKey("actionLink")) {
                            String actionLink = (String) groupMap.get("actionLink");
                            ActionLinkManager.performContextActionLink(MainActivity.this, actionLink);
                        }
                        mDrawerLayout.closeDrawers();
                        break;
                    case "main":
                        closeDrawer();
                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Map<String, Object> childMap = (Map<String, Object>) mDrawerAdapter.getChild(groupPosition, childPosition);
                if (childMap != null && childMap.containsKey("actionLink")) {
                    String actionLink = (String) childMap.get("actionLink");
                    if (actionLink != null) {
                        ActionLinkManager.performContextActionLink(MainActivity.this, actionLink);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void openDrawer() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            mDrawerListView.requestFocus();
        }
    }

    @Override
    public void closeDrawer() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean isFinished() {
        return isFinishing();
    }

    public void setViewId() {
        mBarLayout = mMainBinding.appBarLayout;
        mDrawerLayout = mMainBinding.drawerLayout;
        mToolbar = mMainBinding.toolbar;
        mTabLayout = mMainBinding.tapLayout;
        mDrawerListView = mMainBinding.drawerList;
        mViewPager = mMainBinding.viewPager;
        mNaviView = mMainBinding.naviView;
        guillotineView = LayoutInflater.from(this).inflate(R.layout.view_guillotine, mDrawerLayout, false);
    }

    private void setDrawerProfile(ExpandableListView listView) {
        listView.setGroupIndicator(null);
        listView.setChildIndicator(null);
        listView.setDividerHeight(0);

        mProfileHeader = getLayoutInflater().inflate(R.layout.view_navigation_header, null);
        setProfileHeader();
        listView.addHeaderView(mProfileHeader, null, false);
    }

    private void setProfileHeader() {
        ImageView imageView = (ImageView) mNaviView.findViewById(R.id.drawerIcon);
        imageView.setImageResource(R.drawable.navi_icon_90);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
            }
        });
        TextView textView = (TextView) mNaviView.findViewById(R.id.username);
        if (BmobUser.getCurrentUser() != null && textView != null) {
            textView.setText(BmobUser.getCurrentUser().getUsername());
        }

        TextView textView1 = (TextView) guillotineView.findViewById(R.id.username);
        if (BmobUser.getCurrentUser() != null && textView1 != null) {
            textView1.setText(BmobUser.getCurrentUser().getUsername());
        }

    }

    private void setViewPager() {
        mViewPager.setOffscreenPageLimit(100);
        mDrawerAdapter = new HomeDrawerAdapter(this);
        mDrawerAdapter.setActivity(this);
        mDrawerListView.setAdapter(mDrawerAdapter);

        mPagerAdapter = new TabPagerAdapter(MainActivity.this, getSupportFragmentManager(), mViewPager);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mTabLayout.setFocusable(false);
        }
        mViewPager.addOnPageChangeListener(mPageChangeListener);
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setMainTextFontStyle(mTabLayout, position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void setMainTextFontStyle(TabLayout tabLayout, int position) {
        if (tabLayout == null || position < 0 || tabLayout.getTabCount() <= position) {
            return;
        }

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                ViewGroup mainView = (ViewGroup) tabLayout.getChildAt(0);
                ViewGroup tabView = mainView != null ? (ViewGroup) mainView.getChildAt(tab.getPosition()) : null;
                if (tabView != null) {
                    for (int j = 0; j < tabView.getChildCount(); j++) {
                        View tabViewChild = tabView.getChildAt(j);
                        if (tabViewChild instanceof TextView) {
                            TextView tabTextView = (TextView) tabViewChild;
                            if (position == i) {
                                tabTextView.setTypeface(Typeface.create("sec-roboto-condensed", Typeface.BOLD));
                                tabTextView.setTextColor(Color.parseColor("#51b0d3"));
                                tabView.requestFocus();
                            } else {
                                tabTextView.setTypeface(Typeface.create("sec-roboto-condensed", Typeface.NORMAL));
                                tabTextView.setTextColor(Color.parseColor("#969696"));
                            }
                        }
                    }
                }
            }
        }
    }

    public void requestResize(int delta) {
        if (mDrawerListView != null) {
            int target = mDrawerListView.getLastVisiblePosition() + delta >= mDrawerListView.getCount() - 1 ?
                    mDrawerListView.getCount() - 1 : mDrawerListView.getLastVisiblePosition() + delta;
            mDrawerListView.smoothScrollToPosition(target);
        }
    }

    private AppBarLayout.OnOffsetChangedListener onOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            mOffset = verticalOffset;
        }
    };

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mDrawerIcon.requestFocus();
            }
            return;
        }

        if (guillotineView.getVisibility() == View.VISIBLE) {
            guillotineView.setVisibility(View.GONE);
            return;
        }

        if (mPagerAdapter.getCurrentTabType() != MainType.HOME) {
            goTab(MainType.HOME);
            return;
        }

        super.onBackPressed();
    }

    private View mDrawerIcon;

    private void setupActionBarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            ViewGroup customActionbarView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.view_action_bar, null);
            mDrawerIcon = customActionbarView.findViewById(R.id.drawer_layout);
            ImageView imageView = mDrawerIcon.findViewById(R.id.drawerIcon);
            imageView.setBackgroundResource(R.drawable.navi_icon);

            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(customActionbarView, lp);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setTabPadding(TabLayout tabLayout) {
        if (tabLayout != null && tabLayout.getChildAt(0) instanceof LinearLayout) {
            int tabMinWidth = 0;
            LinearLayout slidingTabStrip = (LinearLayout) tabLayout.getChildAt(0);
            for (int i = 0; i < slidingTabStrip.getChildCount(); i++) {
                View tabView = slidingTabStrip.getChildAt(i);
                tabView.setMinimumWidth(tabMinWidth);
            }
            slidingTabStrip.setPaddingRelative((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getApplicationContext().getResources().getDisplayMetrics()), slidingTabStrip.getPaddingTop(), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getApplicationContext().getResources().getDisplayMetrics()), slidingTabStrip.getPaddingBottom());
        }
    }

    public boolean goTab(MainType type) {
        List<MainType> list = new ArrayList<>();
        for (MainType mainType : MainType.values()) {
            list.add(mainType);
        }
        if (list.contains(type)) {
            int index = list.indexOf(type);
            TabLayout.Tab tab = mTabLayout.getTabAt(index);
            if (tab != null) {
                ViewGroup mainView = (ViewGroup) mTabLayout.getChildAt(0);
                ViewGroup tabView = mainView != null ? (ViewGroup) mainView.getChildAt(tab.getPosition()) : null;
                if (tabView != null) {
                    tabView.requestFocus();
                }
            }

            mViewPager.setCurrentItem(index);
            return true;
        }
        return false;
    }

    @Override
    public void onGuillotineViewOpen() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
    }

    @Override
    public void onGuillotineViewClose() {

    }

    static class TabPagerAdapter extends FragmentStatePagerAdapter {
        private final ArrayList<TabElement> mTabElement = new ArrayList<>(7);
        private final SparseArray<WeakReference<android.support.v4.app.Fragment>> mWeakFragment = new SparseArray<>();
        private ViewPager mViewPager;
        private Activity mActivity;

        TabPagerAdapter(Activity activity, FragmentManager fm, ViewPager viewPager) {
            super(fm);
            mViewPager = viewPager;
            mActivity = activity;
        }

        void clearTabs() {
            mTabElement.clear();
        }

        void onDestroy() {
            clearTabs();
            mActivity = null;
        }

        void saveInstance(Bundle bundle) {
            if (bundle != null) {
                bundle.putParcelable(this.getClass().getName(), saveState());
            }
        }

        void restoreInstance(Bundle bundle) {
            if (bundle != null && bundle.containsKey(this.getClass().getName())) {
                restoreState(bundle.getParcelable(this.getClass().getName()), null);
            }
        }

        void setTabElement(List<TabElement> elementList) {
            mViewPager.setAdapter(this);
            clearTabs();
            if (elementList != null && !elementList.isEmpty()) {
                ListIterator<TabElement> iterator = elementList.listIterator();
                while (iterator.hasNext()) {
                    int index = iterator.nextIndex();
                    TabElement tab = iterator.next();
                    mTabElement.add(tab);
                }
            }
            mViewPager.setAdapter(this);
        }

        @Nullable
        MainType getCurrentTabType() {
            int index = mViewPager.getCurrentItem();
            List<MainType> mainTypeList = new ArrayList<>();
            for (MainType type : MainType.values()) {
                mainTypeList.add(type);
            }
            if (index >= 0 && index < getCount()) {
                return mainTypeList.get(index);
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            TabElement tabElement = mTabElement.get(position);
            String fragClass = tabElement.getFragment().second;
            Bundle bundle = tabElement.getArguments();

            if (mActivity.getIntent().getExtras() != null && mActivity.getIntent().getExtras().containsKey(KEY_QUERY_TAB)) {
                bundle = bundle == null ? new Bundle() : bundle;
                bundle.putAll(mActivity.getIntent().getExtras());
            }

            return Fragment.instantiate(mActivity, fragClass, bundle);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment frag = (Fragment) super.instantiateItem(container, position);
            mWeakFragment.put(position, new WeakReference<Fragment>(frag));
            return frag;
        }

        @Override
        public int getCount() {
            return mTabElement.size();
        }

        public Fragment getWeakItem(int position) {
            startUpdate(mViewPager);
            Fragment ret = (Fragment) super.instantiateItem(mViewPager, position);
            finishUpdate(mViewPager);
            return ret;
        }
    }

}
