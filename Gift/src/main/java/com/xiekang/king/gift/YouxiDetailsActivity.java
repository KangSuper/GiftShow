package com.xiekang.king.gift;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiekang.king.gift.JavaBean.YxiInfo;
import com.xiekang.king.gift.utils.BitmapUtils;
import com.xiekang.king.gift.utils.HttpUtils;
import com.xiekang.king.gift.utils.ICallBack;
import com.xiekang.king.gift.utils.LruCacheTool;
import com.xiekang.king.gift.utils.MyThreadPool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ImageButton mShareImageBtn;
    private NotificationManager notificationManager;
    private Notification.Builder builder;
    private final File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//    private ExecutorService executorService;
    private int notificationNum;


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                install();
            }
        }
    };
    private String TAG = "downPicture";
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youxi_details);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        executorService = Executors.newFixedThreadPool(3);
        initActionBar();
        getIntentFromFragment();
        initView();
    }

    private void initActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowCustomEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.details_bar_view, null, false);
        supportActionBar.setCustomView(view);
        mBackImageBtn = (ImageButton) view.findViewById(R.id.back_image_view);
        mBackImageBtn.setOnClickListener(backClickListener);
        mTitleTxt = (TextView) view.findViewById(R.id.title_text_view);
        mShareImageBtn = (ImageButton) view.findViewById(R.id.share_image_view);
        mShareImageBtn.setVisibility(View.INVISIBLE);
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
        } else {
            if (mLogoImg.getTag().equals(logoUrl)) {
                mLogoImg.setImageBitmap(bitmap);
            }
        }

        if (download_addr.equals("")) {
            mDownBtn.setText("暂无下载");
            mDownBtn.setBackgroundColor(Color.GRAY);
            mDownBtn.setEnabled(false);
        } else {
            mDownBtn.setText("立即下载");
            mDownBtn.setBackgroundColor(Color.rgb(251, 67, 62));
            mDownBtn.setEnabled(true);
        }
        //下载按钮的监听
        mDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPath = getPath(yxiInfo.getDownload_addr());
                File file = new File(externalStoragePublicDirectory +mPath);
                if (file.exists()) {
                    Toast.makeText(YouxiDetailsActivity.this, "已经下载", Toast.LENGTH_SHORT).show();
                    install();
                    return;
                }
                showNotification();
                Toast.makeText(YouxiDetailsActivity.this, "正在下载", Toast.LENGTH_SHORT).show();
                MyThreadPool.executorService.execute(new DownRunnable(yxiInfo.getDownload_addr()));
                mDownBtn.setText("正在下载");
                mDownBtn.setBackgroundColor(Color.GRAY);
                mDownBtn.setClickable(false);
            }
        });
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

    private String getPath(String address){
        int index = address.lastIndexOf("/");
         return address.substring(index);
    }

    class DownRunnable implements Runnable {

        private String downLoad_addr;

        public DownRunnable(String downLoad_addr) {
            this.downLoad_addr = downLoad_addr;
        }

        @Override
        public void run() {
            InputStream inputStream;
            FileOutputStream fileOutputStream;
            try {
                URL url = new URL(downLoad_addr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int contentLength = connection.getContentLength();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    fileOutputStream = new FileOutputStream(externalStoragePublicDirectory + mPath);
                    inputStream = connection.getInputStream();
                    int len;
                    int downSize = 0;
                    byte buffer[] = new byte[1024];
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                        Log.d(TAG, "run: len:"+len);
                        downSize = downSize + len;
                        builder.setProgress(contentLength,downSize,false);
                        builder.setContentText("已下载"+downSize/1000+"KB/"+contentLength/1000+"KB");
                        notificationManager.notify(notificationNum,builder.getNotification());
                    }
                    fileOutputStream.flush();
                    builder.setContentTitle("提示");
                    builder.setContentText(yxiInfo.getName()+"下载完成");
                    notificationManager.notify(notificationNum,builder.getNotification());
                    Message message = mHandler.obtainMessage();
                    message.what = 1;
                    message.sendToTarget();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showNotification() {
        builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.applogo);
        builder.setContentTitle(yxiInfo.getName()+"....正在下载...");
        Notification notification = builder.getNotification();
        notificationNum = new Random().nextInt(Integer.MAX_VALUE);
        notificationManager.notify(notificationNum,notification);
    }

    private void install(){
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(externalStoragePublicDirectory+mPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        startActivity(intent);
    }


}
