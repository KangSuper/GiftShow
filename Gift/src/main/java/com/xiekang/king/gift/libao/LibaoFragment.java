package com.xiekang.king.gift.libao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiekang.king.gift.DetailsActivity;
import com.xiekang.king.gift.JavaBean.LibaoInfo;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by King on 2016/8/15.
 */
public class LibaoFragment extends Fragment implements ICallBack {


    public final String urlString = "http://www.1688wan.com/majax.action?method=getGiftList";
    public final String headString = "http://www.1688wan.com/";
    private Context mContext;
    private ListView mListView;
    private FragmentManager fragmentManager;
    private LibaoAdapter mLibaoAdapter;
    private List<LibaoInfo> giftList;
    private int page = 1;
    private PullToRefreshListView refreshView;
    private InfoCallBack infoCallBack;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //加载数据完成之后，通知刷新控件结束刷新动作
            refreshView.onRefreshComplete();
            mLibaoAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        if (mContext instanceof MainActivity) {
            infoCallBack = (InfoCallBack) mContext;
        }
        fragmentManager = getFragmentManager();
        giftList = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("pageno", page);
        HttpUtils.load(urlString).post(params).callBack(this, 1);

    }

    public static LibaoFragment newInstance() {
        LibaoFragment fragment = new LibaoFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //加载礼包的listView
        View view = inflater.inflate(R.layout.libao_list_layout, container, false);
        //加载广告视图
        View topView = inflater.inflate(R.layout.ad_top_layout, null);
        //获取ListView控件
        refreshView = (PullToRefreshListView) view.findViewById(R.id.libao_list_view);
        //设置模式
        refreshView.setMode(PullToRefreshBase.Mode.BOTH);
        //设置上拉下拉监听
        refreshView.setOnRefreshListener(refreshListener2);
        //
        refreshView.setLastUpdatedLabel(getTime());
        //将RefreshView转换成ListView
        mListView = refreshView.getRefreshableView();
        //
        mListView.addHeaderView(topView);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ad_frame_layout, new AdFragment().newInstance());
        fragmentTransaction.commit();


        mLibaoAdapter = new LibaoAdapter();
        mListView.setAdapter(mLibaoAdapter);

        return view;

    }

    private PullToRefreshBase.OnRefreshListener2 refreshListener2 = new PullToRefreshBase.OnRefreshListener2() {
        //下拉
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {
            refreshView.setLastUpdatedLabel(getTime());
            page = 1;
            Map<String, Object> params = new HashMap<>();
            params.put("pageno", page);
            giftList.clear();
            HttpUtils.load(urlString).post(params).callBack(new ICallBack() {
                @Override
                public void successJson(String result, int requestCode) {
                    if (requestCode == 1) {
                        getLibaoInfo(result);
                    }
                    mHandler.sendEmptyMessageDelayed(1, 100);
                }

                @Override
                public void successBitmap(Bitmap bitmap, int requestCode) {
                }
            }, 1);
        }

        //上拉
        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            refreshView.setLastUpdatedLabel(getTime());
            page += 2;
            Map<String, Object> params = new HashMap<>();
            params.put("pageno", page);
            HttpUtils.load(urlString).post(params).callBack(new ICallBack() {
                @Override
                public void successJson(String result, int requestCode) {
                    if (requestCode == 1) {
                        getLibaoInfo(result);
                        mHandler.sendEmptyMessageDelayed(1, 100);
                    }
                }

                @Override
                public void successBitmap(Bitmap bitmap, int requestCode) {

                }
            }, 1);
        }
    };

    @Override
    public void successJson(String result, int requestCode) {
        if (requestCode == 1) {
            getLibaoInfo(result);
        }
    }

    @Override
    public void successBitmap(Bitmap bitmap, int requestCode) {

    }


    private void getLibaoInfo(String result) {
        try {
            JSONObject jsonObj = new JSONObject(result);
            JSONArray jsonArray = jsonObj.getJSONArray("list");
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String giftname = jsonObject.getString("giftname");
                String gname = jsonObject.getString("gname");
                String iconurl = headString + jsonObject.getString("iconurl");
                int number = jsonObject.getInt("number");
                String addtime = jsonObject.getString("addtime");
                String id = jsonObject.getString("id");
                int exchanges = jsonObject.getInt("exchanges");
                int type = jsonObject.getInt("type");
                int integral = jsonObject.getInt("integral");
                int isintegral = jsonObject.getInt("isintegral");
                String ptype = jsonObject.getString("ptype");
                String operators = jsonObject.getString("operators");
                int flag = jsonObject.getInt("flag");
                giftList.add(new LibaoInfo(id, iconurl, giftname, number, exchanges, type, gname, integral, isintegral, addtime, ptype, operators, flag));
            }
            mLibaoAdapter.notifyDataSetChanged();
            infoCallBack.dataCount(giftList.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class LibaoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return giftList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.libao_item_list, parent, false);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.imageView.setImageResource(R.drawable.applogo);

            final LibaoInfo libaoInfo = giftList.get(position);
            String gname = libaoInfo.getGname();
            String giftname = libaoInfo.getGiftname();
            String addtime = libaoInfo.getAddtime();
            int number = libaoInfo.getNumber();
            final String iconurl = libaoInfo.getIconurl();
            if (number == 0) {
                viewHolder.getBtn.setText("淘号");
                viewHolder.getBtn.setBackgroundColor(Color.GRAY);
            } else {
                viewHolder.getBtn.setText("立即领取");
                viewHolder.getBtn.setBackgroundColor(Color.rgb(251, 67, 62));
            }
            viewHolder.gnameTxt.setText(gname);
            viewHolder.giftnameTxt.setText(giftname);
            viewHolder.numberTxt.setText(number + "");
            viewHolder.addtimeTxt.setText(addtime);
            Bitmap bitmap = LruCacheTool.readCache(iconurl);
            if (bitmap != null) {
                viewHolder.imageView.setImageBitmap(bitmap);
            } else {
                viewHolder.imageView.setTag(iconurl);
                BitmapUtils.load(iconurl).compress(4900).callBack(new ICallBack() {
                    @Override
                    public void successJson(String result, int requestCode) {

                    }

                    @Override
                    public void successBitmap(Bitmap bitmap, int requestCode) {
                        if (requestCode == 2) {
                            LruCacheTool.writeCache(iconurl, bitmap);
                            if (viewHolder.imageView.getTag() == iconurl) {
                                viewHolder.imageView.setImageBitmap(bitmap);
                            }
                        }
                    }
                }, 2);
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = libaoInfo.getId();
                    Intent intent = new Intent();
                    intent.putExtra("id", id);
                    intent.setClass(mContext, DetailsActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }

        class ViewHolder {
            public TextView gnameTxt;
            public TextView giftnameTxt;
            public TextView numberTxt;
            public TextView addtimeTxt;
            public Button getBtn;
            public ImageView imageView;

            public ViewHolder(View view) {
                view.setTag(this);
                imageView = (ImageView) view.findViewById(R.id.libao_item_image_view);
                gnameTxt = (TextView) view.findViewById(R.id.libao_item_gname_txt);
                giftnameTxt = (TextView) view.findViewById(R.id.libao_item_giftname_txt);
                numberTxt = (TextView) view.findViewById(R.id.libao_item_number_txt);
                addtimeTxt = (TextView) view.findViewById(R.id.libao_item_addtime_txt);
                getBtn = (Button) view.findViewById(R.id.libao_item_get_btn);
            }
        }


    }

    private String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }


}
