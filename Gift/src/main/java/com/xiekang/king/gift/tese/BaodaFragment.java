package com.xiekang.king.gift.tese;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiekang.king.gift.BaodaActivity;
import com.xiekang.king.gift.JavaBean.BdaInfo;
import com.xiekang.king.gift.MainActivity;
import com.xiekang.king.gift.R;
import com.xiekang.king.gift.utils.BitmapUtils;
import com.xiekang.king.gift.utils.HttpUtils;
import com.xiekang.king.gift.utils.ICallBack;
import com.xiekang.king.gift.utils.InfoCallBack;
import com.xiekang.king.gift.utils.LruCacheTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by King on 2016/8/17.
 *
 */
public class BaodaFragment extends Fragment implements ICallBack{

    public final String urlString = "http://www.1688wan.com/majax.action?method=bdxqs&pageNo=0";
    public final String headString = "http://www.1688wan.com/";
    private Context mContext;
    private PullToRefreshListView mRefreshView;
    private ListView mListView;
    private List<BdaInfo> bdaInfoList = new ArrayList<>();
    private MyAdapter mAdapter;
    private InfoCallBack infoCallBack;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        HttpUtils.load(urlString).callBack(this,19);
        if (mContext instanceof MainActivity){
            infoCallBack = (InfoCallBack) mContext;
        }
    }

    public static BaodaFragment newInstance(){
        BaodaFragment fragment = new BaodaFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.baoda_list_view, container, false);
        mRefreshView = (PullToRefreshListView) view.findViewById(R.id.baoda_refresh_list);
        mRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
        //获取ListView
        mListView = mRefreshView.getRefreshableView();
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
        return view;

    }

    @Override
    public void successJson(String result, int requestCode) {
        if (requestCode == 19){
            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray jsonArray = jsonObj.getJSONArray("list");
                int length = jsonArray.length();
                for (int i = 0; i < length; i++) {
                    Gson gson = new Gson();
                    BdaInfo bdaInfo = gson.fromJson(jsonArray.get(i).toString(), BdaInfo.class);
                    bdaInfoList.add(bdaInfo);
                }
                infoCallBack.dataCount(bdaInfoList.size());
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
            return bdaInfoList == null ? 0:bdaInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return bdaInfoList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHodler viewHodler;
            if (convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.baoda_item_list,parent,false);
                viewHodler = new ViewHodler(convertView);
            }else {
                viewHodler = (ViewHodler) convertView.getTag();
            }
            final BdaInfo bdaInfo = bdaInfoList.get(position);
            viewHodler.addTime.setText(bdaInfo.getAddtime());
            viewHodler.name.setText(bdaInfo.getName());
            viewHodler.imageView.setImageResource(R.drawable.applogo);
            final String iconurl = headString + bdaInfo.getIconurl();
            Bitmap bitmap = LruCacheTool.readCache(iconurl);
            viewHodler.imageView.setTag(iconurl);
            if (bitmap == null){
                BitmapUtils.load(iconurl).callBack(new ICallBack() {
                    @Override
                    public void successJson(String result, int requestCode) {

                    }

                    @Override
                    public void successBitmap(Bitmap bitmap, int requestCode) {
                        if (requestCode == 20 && viewHodler.imageView.getTag().equals(iconurl)){
                            viewHodler.imageView.setImageBitmap(bitmap);
                            LruCacheTool.writeCache(iconurl,bitmap);
                        }
                    }
                },20);
            }else {
                if (viewHodler.imageView.getTag().equals(iconurl)){
                    viewHodler.imageView.setImageBitmap(bitmap);
                }
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("bdaInfo",bdaInfo);
                    intent.setClass(mContext, BaodaActivity.class);
                    startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHodler {
            public ImageView imageView;
            public TextView name;
            public TextView addTime;
            public ViewHodler(View convertView) {
                convertView.setTag(this);
                imageView = (ImageView) convertView.findViewById(R.id.baoda_item_image_view);
                name = (TextView) convertView.findViewById(R.id.baoda_item_name_txt);
                addTime = (TextView) convertView.findViewById(R.id.baoda_item_time_txt);
            }
        }
    }

}
