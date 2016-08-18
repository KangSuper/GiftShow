package com.xiekang.king.gift.youxi;


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

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiekang.king.gift.JavaBean.KaiceInfo;
import com.xiekang.king.gift.MainActivity;
import com.xiekang.king.gift.R;
import com.xiekang.king.gift.YouxiDetailsActivity;
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
 * Created by Administrator on 2016/8/6 0006.
 */
public class KaiceFragment extends Fragment implements ICallBack{

    private String urlString = "http://www.1688wan.com/majax.action?method=getWebfutureTest";
    public final String headString = "http://www.1688wan.com/";
    private Context context;
    private PullToRefreshListView refreshListView;
    private ListView mListView;
    private List<KaiceInfo> kaiceInfoList = new ArrayList<>();
    private MyAdapter mAdapter;
    private InfoCallBack infoCallBack;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getContext();
        HttpUtils.load(urlString).callBack(this, 9);
        super.onCreate(savedInstanceState);
        if (context instanceof MainActivity){
            infoCallBack = (InfoCallBack) context;
        }
    }

    public static KaiceFragment newInstance() {
        KaiceFragment fragment = new KaiceFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kaice_fragment_layout, container, false);
        refreshListView = (PullToRefreshListView) view.findViewById(R.id.kaice_list_view);
        mListView = refreshListView.getRefreshableView();
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void successJson(String result, int requestCode) {
        if (requestCode == 9) {
            getInfo(result);
        }
    }

    @Override
    public void successBitmap(Bitmap bitmap, int requestCode) {

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return kaiceInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return kaiceInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHodler viewHodler;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.kaice_item_view, parent, false);
                viewHodler = new ViewHodler(convertView);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }

            final KaiceInfo kaiceInfo = kaiceInfoList.get(position);
            String gname = kaiceInfo.getGname();
            String addtime = kaiceInfo.getAddtime();
            final String iconurl = kaiceInfo.getIconurl();
            String operators = kaiceInfo.getOperators();
            viewHodler.addTime.setText(addtime);
            viewHodler.gname.setText(gname);
            viewHodler.operators.setText(operators);
            viewHodler.imageView.setTag(iconurl);
            Bitmap bitmap = LruCacheTool.readCache(iconurl);
            if (bitmap == null) {
                BitmapUtils.load(iconurl).compress(6400).callBack(new ICallBack() {
                    @Override
                    public void successJson(String result, int requestCode) {

                    }

                    @Override
                    public void successBitmap(Bitmap bitmap, int requestCode) {
                        if (requestCode == 10 && viewHodler.imageView.getTag() == iconurl) {
                            viewHodler.imageView.setImageBitmap(bitmap);
                            LruCacheTool.writeCache(iconurl, bitmap);
                        }
                    }
                }, 10);
            } else {
                if (viewHodler.imageView.getTag() == iconurl) {
                    viewHodler.imageView.setImageBitmap(bitmap);
                }
            }
            viewHodler.checkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("id",kaiceInfo.getId());
                    intent.setClass(context, YouxiDetailsActivity.class);
                    startActivity(intent);
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("id",kaiceInfo.getId());
                    intent.setClass(context, YouxiDetailsActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }

        class ViewHodler {
            public ImageView imageView;
            public TextView gname;
            public TextView addTime;
            public TextView operators;
            public Button checkBtn;
            public ViewHodler(View convertView) {
                convertView.setTag(this);
                imageView = (ImageView) convertView.findViewById(R.id.kaice_image_view);
                gname = (TextView) convertView.findViewById(R.id.kaice_gname_txt);
                addTime = (TextView) convertView.findViewById(R.id.kaice_addtime_txt);
                operators = (TextView) convertView.findViewById(R.id.kaice_operators_txt);
                checkBtn = (Button) convertView.findViewById(R.id.kaice_check_btn);
            }
        }
    }




    private void getInfo(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonArray = jsonObj.getJSONArray("info");
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String gname = jsonObject.getString("gname");
                String iconurl = headString + jsonObject.getString("iconurl");
                String addtime = jsonObject.getString("addtime");
                String id = jsonObject.getString("gid");
                String operators = jsonObject.getString("operators");
                kaiceInfoList.add(new KaiceInfo(id, iconurl, gname, operators, addtime));
            }
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
