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
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiekang.king.gift.JavaBean.BdaChlidInfo;
import com.xiekang.king.gift.JavaBean.BdaInfo;
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

public class BaodaActivity extends AppCompatActivity implements ICallBack{

    public String urlString = "http://www.1688wan.com/majax.action?method=bdxqschild";
    public final String headString = "http://www.1688wan.com";
    private ImageButton mBackImageBtn;
    private TextView mTitleTxt;
    private ImageButton mShareImageBtn;
    private ImageView mLogoImg;
    private TextView mAddTime;
    private TextView mDesesTxt;
    private GridView mGridView;
    private List<BdaChlidInfo> bdaChlidInfoList = new ArrayList<>();
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baoda);

        initActionBar();
        initView();
        getIntentFromFragment();
    }

    private void initView() {
        mLogoImg = (ImageView) findViewById(R.id.baoda_details_logo_img);
        mLogoImg.setImageResource(R.drawable.applogo);
        mAddTime = (TextView) findViewById(R.id.baoda_details_addtime_txt);
        mDesesTxt = (TextView) findViewById(R.id.baoda_details_descs_txt);
        mGridView = (GridView) findViewById(R.id.baoda_details_grid_view);
        mAdapter = new MyAdapter();
        mGridView.setAdapter(mAdapter);
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

    private void getIntentFromFragment() {
        Intent intent = getIntent();
        BdaInfo bdaInfo = (BdaInfo) intent.getSerializableExtra("bdaInfo");
        mAddTime.setText(bdaInfo.getAddtime());
        mDesesTxt.setText(bdaInfo.getDescs());
        mTitleTxt.setText(bdaInfo.getName());
        final String iconurl = headString + bdaInfo.getIconurl();
        mLogoImg.setTag(iconurl);
        Bitmap bitmap = LruCacheTool.readCache(iconurl);
        if (bitmap == null){
            BitmapUtils.load(iconurl).callBack(new ICallBack() {
                @Override
                public void successJson(String result, int requestCode) {

                }

                @Override
                public void successBitmap(Bitmap bitmap, int requestCode) {
                    if (requestCode == 25 && mLogoImg.getTag().equals(iconurl)){
                        mLogoImg.setImageBitmap(bitmap);
                        LruCacheTool.writeCache(iconurl,bitmap);
                    }
                }
            },25);
        }else {
            if (mLogoImg.getTag().equals(iconurl)){
                mLogoImg.setImageBitmap(bitmap);
            }
        }

        String sid = bdaInfo.getSid();
        Map<String,Object> params = new HashMap<>();
        params.put("id",sid);
        HttpUtils.load(urlString).post(params).callBack(this,26);
    }

    @Override
    public void successJson(String result, int requestCode) {
        if (requestCode == 26){
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    Gson gson = new Gson();
                    BdaChlidInfo bdaChlidInfo = gson.fromJson(jsonArray.get(i).toString(), BdaChlidInfo.class);
                    bdaChlidInfoList.add(bdaChlidInfo);
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


    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return bdaChlidInfoList == null ? 0:bdaChlidInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return bdaChlidInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHodler viewHodler;
            if (convertView == null){
                convertView = LayoutInflater.from(BaodaActivity.this).inflate(R.layout.baoda_grid_item,parent,false);
                viewHodler = new ViewHodler(convertView);
            }else {
                viewHodler = (ViewHodler) convertView.getTag();
            }
            final BdaChlidInfo bdaChlidInfo = bdaChlidInfoList.get(position);
            viewHodler.imageView.setImageResource(R.drawable.applogo);
            viewHodler.name.setText(bdaChlidInfo.getAppname());
            final String appicon = headString + bdaChlidInfo.getAppicon();
            viewHodler.imageView.setTag(appicon);
            Bitmap bitmap = LruCacheTool.readCache(appicon);
            if (bitmap == null){
                BitmapUtils.load(appicon).compress(10000).callBack(new ICallBack() {
                    @Override
                    public void successJson(String result, int requestCode) {

                    }

                    @Override
                    public void successBitmap(Bitmap bitmap, int requestCode) {
                        if (requestCode == 27 && viewHodler.imageView.getTag().equals(appicon)){
                            viewHodler.imageView.setImageBitmap(bitmap);
                            LruCacheTool.writeCache(appicon,bitmap);
                        }
                    }
                },27);
            }else {
                if (viewHodler.imageView.getTag().equals(appicon)){
                    viewHodler.imageView.setImageBitmap(bitmap);
                }
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("id",bdaChlidInfo.getAppid());
                    intent.setClass(BaodaActivity.this, YouxiDetailsActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }


        class ViewHodler {
            public ImageView imageView;
            public TextView name;
            private Button downBtn;
            public ViewHodler(View convertView) {
                convertView.setTag(this);
                imageView = (ImageView) convertView.findViewById(R.id.baoda_grid_image_view);
                name = (TextView) convertView.findViewById(R.id.baoda_grid_name_txt);
                downBtn = (Button) convertView.findViewById(R.id.baoda_grid_down_btn);
            }
        }
    }
}
