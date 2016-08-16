package com.xiekang.king.gift;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiekang.king.gift.JavaBean.YxiInfo;
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

public class YouxiDetailsActivity extends AppCompatActivity implements ICallBack {

    private String urlString = "http://www.1688wan.com/majax.action?method=getAppInfo";
    public final String headString = "http://www.1688wan.com";
    private ImageView mLogoImg;
    private TextView mTypeTxt;
    private TextView mSizeTxt;
    private ViewPager mViewPager;
    private TextView mDescription;
    private Button mDownBtn;
    private YxiInfo yxiInfo;
    private ImageButton mBackImageBtn;
    private TextView mTitleTxt;
    private TextView mNameTxt;
    private List<String> imageList = new ArrayList<>();
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActionBar();
        setContentView(R.layout.activity_youxi_details);
        getIntentFromFragment();
        initView();
    }

    private void initActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowCustomEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.yxdetails_bar_view, null,false);
        supportActionBar.setCustomView(view);
        mBackImageBtn = (ImageButton) view.findViewById(R.id.yx_back_image_view);
        mBackImageBtn.setOnClickListener(backClickListener);
        mTitleTxt = (TextView) view.findViewById(R.id.yx_title_text_view);
    }

    //后退键的监听
    private View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void initView() {
        mLogoImg = (ImageView) findViewById(R.id.yxdetails_logo_image);
        mTypeTxt = (TextView) findViewById(R.id.yxdetails_typename_txt);
        mSizeTxt = (TextView) findViewById(R.id.yxdetails_size_txt);
        mViewPager = (ViewPager) findViewById(R.id.yxdetails_pager_view);
        mDescription = (TextView) findViewById(R.id.yxdetails_description_txt);
        mDownBtn = (Button) findViewById(R.id.yxdetails_down_btn);
        mNameTxt = (TextView) findViewById(R.id.yxdetails_name_txt);

        mAdapter = new MyAdapter();
        mViewPager.setAdapter(mAdapter);
    }

    private void loadData() {
        String typename = yxiInfo.getTypename();
        String appSize = yxiInfo.getAppSize();
        String description = yxiInfo.getDescription();
        String name = yxiInfo.getName();
        String download_addr = yxiInfo.getDownload_addr();
        final String logoUrl = yxiInfo.getLogo();
        mNameTxt.setText(name);
        mTypeTxt.setText(typename);
        mTitleTxt.setText(name);
        if (appSize.equals("")) {
            mSizeTxt.setText("未知");
        } else {
            mSizeTxt.setText(appSize);
        }
        mDescription.setText(description);
        Bitmap bitmap = LruCacheTool.readCache(logoUrl);
        mLogoImg.setTag(logoUrl);
        if (bitmap == null) {
            BitmapUtils.load(logoUrl).compress(6400).callBack(new ICallBack() {
                @Override
                public void successJson(String result, int requestCode) {

                }

                @Override
                public void successBitmap(Bitmap bitmap, int requestCode) {
                    if (requestCode == 12 && mLogoImg.getTag().equals(logoUrl)) {
                        LruCacheTool.writeCache(logoUrl, bitmap);
                        mLogoImg.setImageBitmap(bitmap);
                    }
                }
            }, 12);
        }else {
            if (mLogoImg.getTag().equals(logoUrl)){
                mLogoImg.setImageBitmap(bitmap);
            }
        }
    }

    private void getIntentFromFragment() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        HttpUtils.load(urlString).post(params).callBack(this, 11);
    }


    @Override
    public void successJson(String result, int requestCode) {
        if (requestCode == 11) {
            getInfo(result);
            loadData();
        }
    }

    @Override
    public void successBitmap(Bitmap bitmap, int requestCode) {

    }

    private void getInfo(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONObject jsonObject = jsonObj.getJSONObject("app");
            String name = jsonObject.getString("name");
            String typename = jsonObject.getString("typename");
            String description = jsonObject.getString("description");
            String logo = headString + jsonObject.getString("logo");
            String download_addr = jsonObject.getString("download_addr");
            String appSize = jsonObject.getString("appsize");
            JSONArray jsonArray = jsonObj.getJSONArray("img");
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String address = headString + object.getString("address");
                imageList.add(address);
            }
            yxiInfo = new YxiInfo(name, logo, download_addr, description, typename, appSize, imageList);
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageList == null ? 0 : imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ImageView imageView = new ImageView(YouxiDetailsActivity.this);
            final String imgUrl = imageList.get(position);
            imageView.setImageResource(R.drawable.applogo);
            Bitmap bitmap = LruCacheTool.readCache(imgUrl);
            imageView.setTag(imgUrl);
            if (bitmap == null) {
                BitmapUtils.load(imgUrl).callBack(new ICallBack() {
                    @Override
                    public void successJson(String result, int requestCode) {

                    }

                    @Override
                    public void successBitmap(Bitmap bitmap, int requestCode) {
                        if (requestCode == 13 && imageView.getTag().equals(imgUrl)) {
                            LruCacheTool.writeCache(imgUrl, bitmap);
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                }, 13);
            } else {
                if (imageView.getTag().equals(imgUrl)) {
                    imageView.setImageBitmap(bitmap);
                }
            }
            container.addView(imageView);
            return imageView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);

        }
    }

}
