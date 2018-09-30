package com.news.hackernews.Screens.Activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;


import com.news.hackernews.ApplicationShell.ShellActivity.BaseActivity;
import com.news.hackernews.ConstantHelper.ExtraUtils;
import com.news.hackernews.R;
import com.news.hackernews.Screens.Fragments.CommentsFragment;

import java.util.ArrayList;
import java.util.List;

public class HackerNewsDetailsActivity extends BaseActivity {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private String URL_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        URL_ID = getIntent().getStringExtra(ExtraUtils.INTENT_ID);

        activateViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setOnTabClickListener();
        setupViewPager(mViewPager);
    }

    private void activateViews() {
        mToolbar = findViewById(R.id.main_toolbar);
        mCToolbar = findViewById(R.id.main_collapsing);
        mViewPager = findViewById(R.id.main_viewpager);
        mTabLayout = findViewById(R.id.main_tabs);
        
        mTabLayout.setupWithViewPager(mViewPager, true);
    }

    private void setOnTabClickListener() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        //
                        break;
                    case 1:
                        //
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CommentsFragment(), "Comments".toUpperCase());
        adapter.addFragment(new CommentsFragment(), "article".toUpperCase());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}