package com.xiekang.king.gift.youxi;

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

import com.xiekang.king.gift.JavaBean.KaifuInfo;
import com.xiekang.king.gift.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/16
 * 开服页面
 */
public class YouxiFragment extends Fragment {

    private Context mContext;
    private ViewPager mViewPager;
    private KaiceFragment kaiceFragment;
    private KaifuFragment kaifuFragment;
    private List<Fragment> fragmentList = new ArrayList<>();
    private MyFragmentPagerAdapter mFragmentPagerAdapter;
    private TabLayout tabLayout;
    private List<String> titles = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        loadFragment();
    }

    private void loadFragment() {
        kaiceFragment = KaiceFragment.newInstance();
        kaifuFragment = KaifuFragment.newInstance();
        fragmentList.add(kaifuFragment);
        fragmentList.add(kaiceFragment);
        titles.add("                 开服                 ");
        titles.add("                 开测                 ");
    }

    public static YouxiFragment newInstance() {
        YouxiFragment fragment = new YouxiFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.youxi_list_layout, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.youxi_tab_layout);
        //在ViewPager里重写getPagerTitle才能显示
        tabLayout.addTab(tabLayout.newTab().setText("开服"));
        tabLayout.addTab(tabLayout.newTab().setText("开测"));

        mViewPager = (ViewPager) view.findViewById(R.id.youxi_view_pager);
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
