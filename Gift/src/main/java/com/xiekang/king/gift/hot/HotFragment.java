package com.xiekang.king.gift.hot;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ListView;

import com.xiekang.king.gift.R;
import com.xiekang.king.gift.utils.HttpUtils;
import com.xiekang.king.gift.utils.ICallBack;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class HotFragment extends Fragment implements ICallBack{

    private String urlString = "http://www.1688wan.com//majax.action?method=hotpushForAndroid";
    public static final String headString = "http://www.1688wan.com/";
    private Context mContext;
    private ListView mListView;
    private GridLayout mGridView;

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
        mGridView = (GridLayout) view.findViewById(R.id.hot_grid_view);
        return view;
    }

    @Override
    public void successJson(String result, int requestCode) {
        if (requestCode == 15){

        }
    }

    @Override
    public void successBitmap(Bitmap bitmap, int requestCode) {

    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

}
