package com.xiekang.king.gift.tese;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiekang.king.gift.R;
import com.xiekang.king.gift.youxi.KaifuFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/6 0006.
 *
 */
public class TeseFragment extends Fragment {

    private Context mContext;
    private List<Fragment> fragmentList = new ArrayList<>();
    private MyFragmentPagerAdapter mFragmentPagerAdapter;
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private List<String> titles = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        loadFragment();
    }

    private void loadFragment() {
        fragmentList.add(BaodaFragment.newInstance());
        fragmentList.add(WeeklyFragment.newInstance());
        titles.add("暴打星期三");
        titles.add("新游周刊");
    }

    public static TeseFragment newInstance(){
        TeseFragment fragment = new TeseFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tese_fragment_layout, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tese_tab_layout);
        //在ViewPager里重写getPagerTitle才能显示
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());

        mViewPager = (ViewPager) view.findViewById(R.id.tese_view_pager);
        mFragmentPagerAdapter = new MyFragmentPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mFragmentPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        return view;
    }


    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
