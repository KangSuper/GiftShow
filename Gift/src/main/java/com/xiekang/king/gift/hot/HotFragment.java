package com.xiekang.king.gift.hot;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiekang.king.gift.JavaBean.HotGridInfo;
import com.xiekang.king.gift.JavaBean.HotListInfo;
import com.xiekang.king.gift.R;
import com.xiekang.king.gift.YouxiDetailsActivity;
import com.xiekang.king.gift.utils.BitmapUtils;
import com.xiekang.king.gift.utils.HttpUtils;
import com.xiekang.king.gift.utils.ICallBack;
import com.xiekang.king.gift.utils.LruCacheTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class HotFragment extends Fragment implements ICallBack{

    private String urlString = "http://www.1688wan.com//majax.action?method=hotpushForAndroid";
    public static final String headString = "http://www.1688wan.com/";
    private Context mContext;
    private ListView mListView;
    private GridView mGridView;
    private List<HotListInfo> hotListInfoList = new ArrayList<>();
    private List<HotGridInfo> hotGridInfoList = new ArrayList<>();
    private MyAdapter myAdapter;
    private MyGridAdapter myGridAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        HttpUtils.load(urlString).callBack(this,15);
        super.onCreate(savedInstanceState);
        mContext = getContext();

    }

    public static HotFragment newInstance(){
        HotFragment fragment = new HotFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_list_view, container, false);
        mListView = (ListView) view.findViewById(R.id.hot_list_view);
        myAdapter = new MyAdapter();
        mListView.setAdapter(myAdapter);
        mGridView = (GridView) view.findViewById(R.id.hot_grid_view);
        myGridAdapter = new MyGridAdapter();
        mGridView.setAdapter(myGridAdapter);
        return view;
    }

    @Override
    public void successJson(String result, int requestCode) {
        if (requestCode == 15){
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject jsonObject = jsonObj.getJSONObject("info");
                JSONArray push1 = jsonObject.getJSONArray("push1");
                for (int i = 0; i < push1.length(); i++) {
                    Gson gson = new Gson();
                    HotListInfo hotListInfo = gson.fromJson(push1.get(i).toString(), HotListInfo.class);
                    Log.d("androidxx", "successJson: hostListInfo:"+hotListInfo.toString());
                    hotListInfoList.add(hotListInfo);
                }
                myAdapter.notifyDataSetChanged();
                JSONArray push2 = jsonObject.getJSONArray("push2");
                for (int i = 0; i < push2.length(); i++) {
                    Gson gson = new Gson();
                    HotGridInfo hotGridInfo = gson.fromJson(push2.get(i).toString(), HotGridInfo.class);
                    hotGridInfoList.add(hotGridInfo);
                }
                myGridAdapter.notifyDataSetChanged();
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
            return hotListInfoList == null ? 0:hotListInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return hotListInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHodler viewHodler;
            if (convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.hot_list_item,parent,false);
                viewHodler = new ViewHodler(convertView);
            }else {
                viewHodler = (ViewHodler) convertView.getTag();
            }

            final HotListInfo hotListInfo = hotListInfoList.get(position);
            viewHodler.name.setText(hotListInfo.getName());
            viewHodler.typename.setText(hotListInfo.getTypename());
            viewHodler.appSize.setText(hotListInfo.getSize());
            viewHodler.clicks.setText(hotListInfo.getClicks());
            final String logoUrl = headString + hotListInfo.getLogo();
            viewHodler.imageView.setTag(logoUrl);
            Bitmap bitmap = LruCacheTool.readCache(logoUrl);
            if (bitmap == null){
                BitmapUtils.load(logoUrl).callBack(new ICallBack() {
                    @Override
                    public void successJson(String result, int requestCode) {

                    }

                    @Override
                    public void successBitmap(Bitmap bitmap, int requestCode) {
                        if (requestCode == 16 && viewHodler.imageView.getTag().equals(logoUrl)){
                            viewHodler.imageView.setImageBitmap(bitmap);
                            LruCacheTool.writeCache(logoUrl,bitmap);
                        }
                    }
                },16);
            }else {
                if (viewHodler.imageView.getTag().equals(logoUrl)){
                    viewHodler.imageView.setImageBitmap(bitmap);
                }
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("id",hotListInfo.getAppid());
                    intent.setClass(mContext, YouxiDetailsActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }

         class ViewHodler {
            public ImageView imageView;
            public TextView name;
            public TextView typename;
            public TextView appSize;
            public TextView clicks;
            public ViewHodler(View convertView) {
                convertView.setTag(this);
                imageView = (ImageView) convertView.findViewById(R.id.hot_list_logo_image);
                name = (TextView) convertView.findViewById(R.id.hot_list_name_txt);
                typename = (TextView) convertView.findViewById(R.id.hot_list_typename_txt);
                appSize = (TextView) convertView.findViewById(R.id.hot_list_size_txt);
                clicks = (TextView) convertView.findViewById(R.id.hot_list_clicks_txt);
            }
        }

    }

    class MyGridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return hotGridInfoList == null ? 0:hotGridInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return hotGridInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHodler viewHodler;
            if (convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.hot_grid_item,parent,false);
                viewHodler = new ViewHodler(convertView);
            }else {
                viewHodler = (ViewHodler) convertView.getTag();
            }
            final HotGridInfo hotGridInfo = hotGridInfoList.get(position);
            viewHodler.name.setText(hotGridInfo.getName());
            final String logoUrl = headString + hotGridInfo.getLogo();
            viewHodler.imageView.setTag(logoUrl);
            Bitmap bitmap = LruCacheTool.readCache(logoUrl);
            if (bitmap == null){
                BitmapUtils.load(logoUrl).compress(22500).callBack(new ICallBack() {
                    @Override
                    public void successJson(String result, int requestCode) {

                    }

                    @Override
                    public void successBitmap(Bitmap bitmap, int requestCode) {
                        if (requestCode == 17 && viewHodler.imageView.getTag().equals(logoUrl)){
                            viewHodler.imageView.setImageBitmap(bitmap);
                            LruCacheTool.writeCache(logoUrl,bitmap);
                        }
                    }
                },17);
            }else {
                if (viewHodler.imageView.getTag().equals(logoUrl)){
                    viewHodler.imageView.setImageBitmap(bitmap);
                }
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("id",hotGridInfo.getAppid());
                    intent.setClass(mContext, YouxiDetailsActivity.class);
                    startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHodler {
            public ImageView imageView;
            public TextView name;

            public ViewHodler(View convertView) {
                convertView.setTag(this);
                imageView = (ImageView) convertView.findViewById(R.id.hot_grid_image_view);
                name = (TextView) convertView.findViewById(R.id.hot_grid_name_txt);
            }
        }
    }



}
