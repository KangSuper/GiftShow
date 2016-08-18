package com.xiekang.king.gift.youxi;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiekang.king.gift.JavaBean.KaifuInfo;
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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/6 0006.
 * 开服页面
 */
public class KaifuFragment extends Fragment implements ICallBack {

    private Context context;
    private String urlString = "http://www.1688wan.com/majax.action?method=getJtkaifu";
    public final String headString = "http://www.1688wan.com/";
    private String TAG = "kaifu";
    private List<KaifuInfo> kaifuInfoList = new ArrayList<>();
    private LinkedHashSet<String> timeSet = new LinkedHashSet<>();
    private ArrayList<String> timeList;
    private Map<String, List<KaifuInfo>> mapData = new HashMap<>();
    private ExpandableListView expandList;
    private Context mContext;
    private MyAdapter mAdapter;
    private InfoCallBack infoCallBack ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getContext();
        HttpUtils.load(urlString).callBack(this, 7);
        mContext = getContext();
        super.onCreate(savedInstanceState);
        if (context instanceof MainActivity){
            infoCallBack = (InfoCallBack) context;
        }
    }

    public static KaifuFragment newInstance() {
        KaifuFragment fragment = new KaifuFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kaifu_fragment_layout, container, false);
        expandList = (ExpandableListView) view.findViewById(R.id.kaifu_expand_list);
        mAdapter = new MyAdapter();
        expandList.setAdapter(mAdapter);
        return view;

    }

    @Override
    public void successJson(String result, int requestCode) {
        if (requestCode == 7) {
            getInfo(result);
            timeList = new ArrayList<>(timeSet);
            for (int i = 0; i < timeList.size(); i++) {
                //注意
                ArrayList<KaifuInfo> kaifuInfos = new ArrayList<>();
                ;
                String time = timeList.get(i);
                for (int j = 0; j < kaifuInfoList.size(); j++) {
                    KaifuInfo kaifuInfo = kaifuInfoList.get(j);
                    String addtime = kaifuInfo.getAddtime();
                    if (addtime.equals(time)) {
                        kaifuInfos.add(kaifuInfo);
                    }
                }
                mapData.put(timeList.get(i), kaifuInfos);
            }
            for (int i = 0; i < timeList.size(); i++) {
                expandList.expandGroup(i);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void successBitmap(Bitmap bitmap, int requestCode) {

    }


    class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return timeList == null ? 0 : timeList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            String time = timeList.get(groupPosition);
            List<KaifuInfo> kaifuInfos = mapData.get(time);
            return kaifuInfos == null ? 0 : kaifuInfos.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.kaifu_time_itme, parent, false);
                TextView addTime = (TextView) convertView.findViewById(R.id.kaifu_addtime_txt);
                String time = timeList.get(groupPosition);
                addTime.setText(time);
            }
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String time = timeList.get(groupPosition);
            List<KaifuInfo> kaifuInfos = mapData.get(time);
            final KaifuInfo kaifuInfo = kaifuInfos.get(childPosition);
            final ViewHodler viewHodler;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.kaifu_item_view, parent, false);
                viewHodler = new ViewHodler(convertView);
            } else {
                viewHodler = (ViewHodler) convertView.getTag();
            }

            viewHodler.gname.setText(kaifuInfo.getGname());
            viewHodler.startTime.setText(kaifuInfo.getLinkurl());
            viewHodler.area.setText(kaifuInfo.getArea());
            viewHodler.operators.setText(kaifuInfo.getOperators());
            final String iconurl = kaifuInfo.getIconurl();
            viewHodler.imageView.setTag(iconurl);
            Bitmap bitmap = LruCacheTool.readCache(iconurl);
            if (bitmap == null) {
                BitmapUtils.load(iconurl).compress(6400).callBack(new ICallBack() {
                    @Override
                    public void successJson(String result, int requestCode) {

                    }

                    @Override
                    public void successBitmap(Bitmap bitmap, int requestCode) {
                        if (requestCode == 8 && viewHodler.imageView.getTag() == iconurl) {
                            viewHodler.imageView.setImageBitmap(bitmap);
                            LruCacheTool.writeCache(iconurl, bitmap);
                        }
                    }
                }, 8);
            } else {
                if (viewHodler.imageView.getTag() == iconurl) {
                    viewHodler.imageView.setImageBitmap(bitmap);
                }
            }
            viewHodler.checkBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("id",kaifuInfo.getId());
                    intent.setClass(context, YouxiDetailsActivity.class);
                    startActivity(intent);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("id",kaifuInfo.getId());
                    String id = kaifuInfo.getId();
                    Log.d(TAG, "onClick: id:"+id);
                    intent.setClass(context, YouxiDetailsActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


        class ViewHodler {
            public ImageView imageView;
            public TextView gname;
            public TextView startTime;
            public TextView area;
            public TextView operators;
            public Button checkBtn;
            public ViewHodler(View convertView) {
                convertView.setTag(this);
                imageView = (ImageView) convertView.findViewById(R.id.kaifu_image_view);
                gname = (TextView) convertView.findViewById(R.id.kaifu_gname_txt);
                startTime = (TextView) convertView.findViewById(R.id.kaifu_starttime_txt);
                area = (TextView) convertView.findViewById(R.id.kaifu_area_txt);
                operators = (TextView) convertView.findViewById(R.id.kaifu_operators_txt);
                checkBtn = (Button) convertView.findViewById(R.id.kaifu_check_btn);
            }
        }
    }


    private void getInfo(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonArray = jsonObj.getJSONArray("info");
            Log.d(TAG, "getLibaoInfo: jsonArray:" + jsonArray.toString());
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d(TAG, "getLibaoInfo: jsonObject:" + jsonObject.toString());
                String gname = jsonObject.getString("gname");
                String iconurl = headString + jsonObject.getString("iconurl");
                String addtime = jsonObject.getString("addtime");
                String id = jsonObject.getString("gid");
                String operators = jsonObject.getString("operators");
                String linkurl = jsonObject.getString("linkurl");
                String area = jsonObject.getString("area");
                KaifuInfo kaifuInfo = new KaifuInfo(id, iconurl, gname, linkurl, operators, addtime, area);
                timeSet.add(addtime);
                kaifuInfoList.add(kaifuInfo);
            }
            infoCallBack.dataCount(kaifuInfoList.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
