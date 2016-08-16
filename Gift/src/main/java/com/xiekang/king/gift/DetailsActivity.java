package com.xiekang.king.gift;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiekang.king.gift.JavaBean.LibaoDetails;
import com.xiekang.king.gift.customview.CircleImageView;
import com.xiekang.king.gift.utils.BitmapUtils;
import com.xiekang.king.gift.utils.HttpUtils;
import com.xiekang.king.gift.utils.ICallBack;

import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity implements ICallBack{

    private CircleImageView circleImageView;
    private TextView mTimeTxt;
    private TextView mNumberTxt;
    private TextView mExplainTxt;
    private TextView mDescsTxt;
    private String urlString = "http://www.1688wan.com/majax.action?method=getGiftInfo";
    public static final String headString = "http://www.1688wan.com/";
    private String TAG = "details";
    private Button mGetBtn;
    private ImageButton mBackImageBtn;
    private TextView mTitleTxt;
    private ImageButton mShareImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initActionBar();
        getIntentFromFragment();
        initView();
    }

    private void initActionBar(){
        //设置可自定义ActionBar
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowCustomEnabled(true);
        //获取view
        View view = LayoutInflater.from(this).inflate(R.layout.details_bar_view,null);

        mBackImageBtn = (ImageButton) view.findViewById(R.id.back_image_view);
        mBackImageBtn.setOnClickListener(backClickListener);


        mTitleTxt = (TextView) view.findViewById(R.id.title_text_view);
        mShareImageBtn = (ImageButton) view.findViewById(R.id.share_image_view);
        supportActionBar.setCustomView(view);
    }

    //后退键的监听
    private View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };



    private void getIntentFromFragment(){
        //获取传值
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        Map<String,Object> params = new HashMap<>();
        params.put("id",id);
        HttpUtils.load(urlString).post(params).callBack(this,5);
    }




    private void initView() {
        circleImageView = (CircleImageView) findViewById(R.id.details_circle_view);
        mTimeTxt = (TextView) findViewById(R.id.details_time_txt);
        mNumberTxt = (TextView) findViewById(R.id.details_least_number);
        mExplainTxt = (TextView) findViewById(R.id.details_explains_text);
        mDescsTxt = (TextView) findViewById(R.id.details_descs_text);
        mGetBtn = (Button) findViewById(R.id.details_get_btn);
    }

    @Override
    public void successJson(String result, int requestCode) {
        if (requestCode == 5){
            Log.d(TAG, "successJson: result:"+result);
           setView(result);
        }
    }

    @Override
    public void successBitmap(Bitmap bitmap, int requestCode) {

    }

    private void setView(String result){
        Gson gson = new Gson();
        LibaoDetails libaoDetails = gson.fromJson(result, LibaoDetails.class);
        String addtime = libaoDetails.getInfo().getAddtime();
        int exchanges = libaoDetails.getInfo().getExchanges();
        String explains = libaoDetails.getInfo().getExplains();
        String descs = libaoDetails.getInfo().getDescs();
        String giftname = libaoDetails.getInfo().getGiftname();
        String iconurl = headString+libaoDetails.getInfo().getIconurl();
        String gname = libaoDetails.getInfo().getGname();
        mTitleTxt.setText(gname+"-"+giftname);
        BitmapUtils.load(iconurl).compress(12100).callBack(new ICallBack() {
            @Override
            public void successJson(String result, int requestCode) {

            }

            @Override
            public void successBitmap(Bitmap bitmap, int requestCode) {
                if (requestCode == 6){
                    circleImageView.setImageBitmap(bitmap);
                }
            }
        },6);
        mTimeTxt.setText(addtime);
        mNumberTxt.setText(exchanges+"");
        mExplainTxt.setText(explains);
        mDescsTxt.setText(descs);
        if (exchanges == 0){
            mGetBtn.setText("马上淘号");
        }else {
//            mGetBtn.setText("");
        }
    }

}
