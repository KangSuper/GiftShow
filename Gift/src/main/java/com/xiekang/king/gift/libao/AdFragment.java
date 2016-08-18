package com.xiekang.king.gift.libao;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiekang.king.gift.JavaBean.AdInfro;
import com.xiekang.king.gift.R;
import com.xiekang.king.gift.utils.BitmapUtils;
import com.xiekang.king.gift.utils.HttpUtils;
import com.xiekang.king.gift.utils.ICallBack;
import com.xiekang.king.gift.utils.LruCacheTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/15
 */

public class AdFragment extends Fragment implements ICallBack {

    private ViewPager mViewPager;
    private LinearLayout mIndicatorLayout;
    private int childCount;
    public static final String urlString = "http://www.1688wan.com/majax.action?method=getGiftList";
    public static final String headString = "http://www.1688wan.com/";
    private Context mContext;
    private MyPagerAdapter myPagerAdapter;
    private List<AdInfro> adInfroList = new ArrayList<>();

    private Handler mHandler = new Handler() {
        int i = 0;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            i++;
            mViewPager.setCurrentItem(i % 5);
            //回传消息实现循环滚动
            mHandler.sendEmptyMessageDelayed(1, 3000);
        }
    };

    public static AdFragment newInstance() {
        AdFragment fragment = new AdFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        Map<String, Object> params = new HashMap<>();
        //开启ViewPager的下载
        params.put("pageno", 1);
        HttpUtils.load(urlString).post(params).callBack(this, 3);
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

    @Override
    public void successJson(String result, int requestCode) {
        if (requestCode == 3) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray jsonArray = jsonObj.getJSONArray("ad");
                int length = jsonArray.length();
                for (int i = 0; i < length; i++) {
                    if (i == 0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        int flag = jsonObject.getInt("flag");
                        String addtime = jsonObject.getString("addtime");
                        String linkurl = jsonObject.getString("linkurl");
                        String giftid = jsonObject.getString("giftid");
                        String appName = jsonObject.getString("appName");
                        String appLogo = jsonObject.getString("appLogo");
                        int appId = jsonObject.getInt("appId");
                        String iconurl = headString + jsonObject.getString("iconurl");
                        adInfroList.add(new AdInfro(id, title, flag, iconurl, addtime, linkurl, giftid, appName, appLogo, appId));
                    }else {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String title = jsonObject.getString("title");
                        int flag = jsonObject.getInt("flag");
                        String addtime = jsonObject.getString("addtime");
                        String giftid = jsonObject.getString("giftid");
                        String appName = jsonObject.getString("appName");
                        String appLogo = jsonObject.getString("appLogo");
                        int appId = jsonObject.getInt("appId");
                        String iconurl = headString + jsonObject.getString("iconurl");
                        adInfroList.add(new AdInfro(id, title, flag, iconurl, addtime, null, giftid, appName, appLogo, appId));
                    }
                }
                myPagerAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mHandler.sendEmptyMessageDelayed(1, 3000);
    }

    @Override
    public void successBitmap(Bitmap bitmap, int requestCode) {

    }


    /**
     * ViewPager的适配器
     */
    class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {

            return adInfroList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ImageView imageView = new ImageView(mContext);
            //设置默认资源
            imageView.setImageResource(R.drawable.applogo);

            AdInfro adInfro = adInfroList.get(position);
            final String iconurl = adInfro.getIconurl();
            //获取缓存里的图片资源
            Bitmap bitmap = LruCacheTool.readCache(iconurl);
            //当缓存里有时直接设置，否则开启线程下载
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setTag(iconurl);
                BitmapUtils.load(iconurl).callBack(new ICallBack() {
                    @Override
                    public void successJson(String result, int requestCode) {

                    }

                    @Override
                    public void successBitmap(Bitmap bitmap, int requestCode) {
                        if (imageView.getTag() == iconurl) {
                            imageView.setImageBitmap(bitmap);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            LruCacheTool.writeCache(urlString, bitmap);
                        }

                    }
                }, 4);
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
