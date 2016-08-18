package com.xiekang.king.gift;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiekang.king.gift.JavaBean.WeeklyChildInfo;
import com.xiekang.king.gift.JavaBean.WeeklyInfo;
import com.xiekang.king.gift.customview.CircleImageView;
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

public class WeeklyActivity extends AppCompatActivity {

    private String urlString = "http://www.1688wan.com/majax.action?method=getWeekllChid";
    public final String headString = "http://www.1688wan.com/";
    private ImageButton mBackImageBtn;
    private TextView mTitleTxt;
    private ImageButton mShareImageBtn;
    private ListView mListView;
    private CircleImageView mAuthorImg;
    private ImageView mTopImg;
    private TextView mTopDesc;
    private List<WeeklyChildInfo> weeklyChildInfoList = new ArrayList<>();
    private MyAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);
        initActionBar();
        initView();
        getIntentFromFragment();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.weekly_child_list_view);
        View view = LayoutInflater.from(this).inflate(R.layout.weekly_child_top_view,null);
        mListView.addHeaderView(view);

        //查找头部布局控件
        mAuthorImg = (CircleImageView) view.findViewById(R.id.weekly_top_author_img);
        mTopImg = (ImageView) view.findViewById(R.id.weekly_top_img_view);
        mTopDesc = (TextView) view.findViewById(R.id.weekly_top_desc_txt);

        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
    }


    private void initActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowCustomEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.details_bar_view, null,false);
        supportActionBar.setCustomView(view);
        mBackImageBtn = (ImageButton) view.findViewById(R.id.back_image_view);
        mBackImageBtn.setOnClickListener(backClickListener);
        mTitleTxt = (TextView) view.findViewById(R.id.title_text_view);
        mShareImageBtn = (ImageButton) view.findViewById(R.id.share_image_view);
    }

    //后退键的监听
    private View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void getIntentFromFragment(){
        Intent intent = getIntent();
        WeeklyInfo weeklyInfo = (WeeklyInfo) intent.getSerializableExtra("weeklyInfo");
        mTopDesc.setText(weeklyInfo.getDescs());
        mTitleTxt.setText(weeklyInfo.getName());
        final String authorimg = headString + weeklyInfo.getAuthorimg();
        final String iconurl = headString + weeklyInfo.getIconurl();

        mTopImg.setTag(iconurl);
        Bitmap bitmap = LruCacheTool.readCache(iconurl);
        if (bitmap == null){
            BitmapUtils.load(iconurl).callBack(new ICallBack() {
                @Override
                public void successJson(String result, int requestCode) {

                }

                @Override
                public void successBitmap(Bitmap bitmap, int requestCode) {
                    if (requestCode == 30 && mTopImg.getTag().equals(iconurl)){
                        mTopImg.setImageBitmap(bitmap);
                        LruCacheTool.writeCache(iconurl,bitmap);
                    }
                }
            },30);
        }else {
            if (mTopImg.getTag().equals(iconurl)){
                mTopImg.setImageBitmap(bitmap);
            }
        }

        mAuthorImg.setTag(authorimg);
        Bitmap bitmap1 = LruCacheTool.readCache(authorimg);
        if (bitmap1 == null){
            BitmapUtils.load(authorimg).callBack(new ICallBack() {
                @Override
                public void successJson(String result, int requestCode) {

                }

                @Override
                public void successBitmap(Bitmap bitmap, int requestCode) {
                    if (requestCode == 31 && mAuthorImg.getTag().equals(authorimg)){
                        mAuthorImg.setImageBitmap(bitmap);
                        LruCacheTool.writeCache(authorimg,bitmap);
                    }
                }
            },31);
        }else {
            if (mAuthorImg.getTag().equals(authorimg)){
                mAuthorImg.setImageBitmap(bitmap1);
            }
        }
        String id = weeklyInfo.getId();
        Map<String,Object> params = new HashMap<>();
        params.put("id",id);
        HttpUtils.load(urlString).post(params).callBack(new ICallBack() {
            @Override
            public void successJson(String result, int requestCode) {
                if (requestCode == 32){
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String string = jsonArray.get(i).toString();
                            Gson gson = new Gson();
                            WeeklyChildInfo weeklyChildInfo = gson.fromJson(string, WeeklyChildInfo.class);
                            weeklyChildInfoList.add(weeklyChildInfo);
                        }
                        mAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void successBitmap(Bitmap bitmap, int requestCode) {

            }
        },32);
    }


    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return weeklyChildInfoList == null? 0:weeklyChildInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return weeklyChildInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHodler viewHodler ;
            if (convertView == null){
                convertView = LayoutInflater.from(WeeklyActivity.this).inflate(R.layout.weekly_child_item_list,parent,false);
                viewHodler = new ViewHodler(convertView);
            }else {
                viewHodler = (ViewHodler) convertView.getTag();
            }
            final WeeklyChildInfo weeklyChildInfo = weeklyChildInfoList.get(position);
            viewHodler.appname.setText(weeklyChildInfo.getAppname());
            viewHodler.appsize.setText(weeklyChildInfo.getAppsize());
            viewHodler.typename.setText(weeklyChildInfo.getTypename());
            viewHodler.descs.setText(weeklyChildInfo.getDescs());
            final String iconurl = headString + weeklyChildInfo.getIconurl();
            viewHodler.imageView.setTag(iconurl);
            Bitmap bitmap = LruCacheTool.readCache(iconurl);
            if (bitmap == null){
                BitmapUtils.load(iconurl).compress(14400).callBack(new ICallBack() {
                    @Override
                    public void successJson(String result, int requestCode) {

                    }

                    @Override
                    public void successBitmap(Bitmap bitmap, int requestCode) {
                        if (requestCode == 33 && viewHodler.imageView.getTag().equals(iconurl)){
                            viewHodler.imageView.setImageBitmap(bitmap);
                            LruCacheTool.writeCache(iconurl,bitmap);
                        }
                    }
                },33);
            }else {
                if (viewHodler.imageView.getTag().equals(iconurl)){
                    viewHodler.imageView.setImageBitmap(bitmap);
                }
            }
            viewHodler.downBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("id",weeklyChildInfo.getAppid());
                    intent.setClass(WeeklyActivity.this, YouxiDetailsActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }


        class ViewHodler {
            public ImageView imageView;
            public TextView appname;
            public TextView typename;
            public TextView appsize;
            public Button downBtn;
            public TextView descs;
            public ViewHodler(View convertView) {
                convertView.setTag(this);
                imageView = (ImageView) convertView.findViewById(R.id.weekly_child_list_image_view);
                appname = (TextView) convertView.findViewById(R.id.weekly_child_list_appname_txt);
                typename = (TextView) convertView.findViewById(R.id.weekly_child_list_typename_txt);
                appsize = (TextView) convertView.findViewById(R.id.weekly_child_list_size_txt);
                descs = (TextView) convertView.findViewById(R.id.weekly_child_list_descs_txt);
                downBtn = (Button) convertView.findViewById(R.id.weekly_child_list_check_btn);
            }
        }
    }
}
