package com.xiekang.king.gift.libao;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiekang.king.gift.R;
import com.xiekang.king.gift.utils.LruCacheTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15
 *
 */

public class AdFragment extends Fragment {

    private Message message;
    private ViewPager mViewPager;
    private LinearLayout mIndicatorLayout;
    private int childCount;
    public static final String urlString = "http://www.1688wan.com/majax.action?method=getGiftList";
    public static final String headString = "http://www.1688wan.com/";
    private List<String> adUrlList = new ArrayList<>();
    private Context mContext;
    private MyPagerAdapter myPagerAdapter;



    public static AdFragment newInstance() {
        AdFragment fragment = new AdFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        message = new Message();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //获取布局文件
        View view = inflater.inflate(R.layout.top_view_pager, container, false);
        //获取viewpager
        mViewPager = (ViewPager) view.findViewById(R.id.top_view_pager);
        //获取指示器
        mIndicatorLayout = (LinearLayout) view.findViewById(R.id.indicator_layout);
        //初始化适配器
        myPagerAdapter = new MyPagerAdapter();
        //为ViewPager指定适配器
        mViewPager.setAdapter(myPagerAdapter);
        //为ViewPager添加滚动监听，实现指示器的同步
        mViewPager.addOnPageChangeListener(pageChangeListener);
        childCount = mIndicatorLayout.getChildCount();
        controlIndicator(0);
        return view;
    }


    /**
     * 初始化指示器
     *
     * @param index
     */
    private void controlIndicator(int index) {
        ImageView view = (ImageView) mIndicatorLayout.getChildAt(index);
        //初始化所有的ImageView的enable属性为false
        for (int i = 0; i < childCount; i++) {
            ImageView childView = (ImageView) mIndicatorLayout.getChildAt(i);
            childView.setEnabled(false);
        }
        view.setEnabled(true);
    }


    /**
     * ViewPager的适配器
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {

            return adUrlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            //设置默认资源
            imageView.setImageResource(R.mipmap.ic_launcher);
            String url = adUrlList.get(position);
            //获取缓存里的图片资源
            Bitmap bitmap = LruCacheTool.readCache(url);
            //当缓存里有时直接设置，否则开启线程下载
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setTag(url);
            }
            container.addView(imageView);
            return imageView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    /**
     * 对Viewpager的监听，来使指示器同步
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            controlIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



}
