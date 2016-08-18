package com.xiekang.king.gift;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xiekang.king.gift.JavaBean.SearchInfo;
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

public class SearchActivity extends AppCompatActivity {

    public final String urlString = "http://www.1688wan.com/majax.action?method=searchGift";
    public final String headString = "http://www.1688wan.com/";
    private ListView mListView;
    private MyAdapter mAdapter;
    private List<SearchInfo> searchInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        HttpUtils.load(urlString + "&key=").callBack(new ICallBack() {
            @Override
            public void successJson(String result, int requestCode) {
                if (requestCode == 38) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String string = jsonArray.get(i).toString();
                            Gson gson = new Gson();
                            SearchInfo searchInfo = gson.fromJson(string, SearchInfo.class);
                            searchInfoList.add(searchInfo);
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
        }, 38);
        initView();
        initActionBar();
    }

    private void initActionBar() {

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowCustomEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.search_custom_bar, null);
        ImageButton backImg = (ImageButton) view.findViewById(R.id.search_custom_back_image);
        final EditText editText = (EditText) view.findViewById(R.id.search_custom_edit_text);
        TextView doTxt = (TextView) view.findViewById(R.id.search_custom_do_txt);
        supportActionBar.setCustomView(view);
        //后退的监听
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索的监听
        doTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable text = editText.getText();
                Map<String,Object> params = new HashMap<String, Object>();
                params.put("key",text);
                HttpUtils.load(urlString).post(params).callBack(new ICallBack() {
                    @Override
                    public void successJson(String result, int requestCode) {
                        if (requestCode == 39){
                            try {
                                searchInfoList.clear();
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray jsonArray = jsonObject.getJSONArray("list");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String string = jsonArray.get(i).toString();
                                    Gson gson = new Gson();
                                    SearchInfo searchInfo = gson.fromJson(string, SearchInfo.class);
                                    searchInfoList.add(searchInfo);
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
                },39);
            }
        });
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.search_list_view);
        mAdapter = new MyAdapter();
        mListView.setAdapter(mAdapter);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return searchInfoList == null ? 0 : searchInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return searchInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.libao_item_list, parent, false);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.imageView.setImageResource(R.drawable.applogo);
            final SearchInfo searchInfo = searchInfoList.get(position);
            String gname = searchInfo.getGname();
            String giftname = searchInfo.getGiftname();
            String addtime = searchInfo.getAddtime();
            int number = searchInfo.getNumber();
            final String iconurl = headString + searchInfo.getIconurl();
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
            viewHolder.imageView.setTag(iconurl);
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
                        if (requestCode == 37) {
                            LruCacheTool.writeCache(iconurl, bitmap);
                            if (viewHolder.imageView.getTag() == iconurl) {
                                viewHolder.imageView.setImageBitmap(bitmap);
                            }
                        }
                    }
                }, 37);
            }

            viewHolder.getBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = searchInfo.getId();
                    Intent intent = new Intent();
                    intent.putExtra("id", id);
                    intent.setClass(SearchActivity.this, DetailsActivity.class);
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
}
