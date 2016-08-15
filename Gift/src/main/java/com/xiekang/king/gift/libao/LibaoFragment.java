package com.xiekang.king.gift.libao;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiekang.king.gift.JavaBean.Libao;
import com.xiekang.king.gift.R;
import com.xiekang.king.gift.utils.HttpUtils;
import com.xiekang.king.gift.utils.ICallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by King on 2016/8/15.
 */
public class LibaoFragment extends Fragment implements ICallBack{


    public static final String urlString = "http://www.1688wan.com/majax.action?method=getGiftList";
    public static final String headString = "http://www.1688wan.com/";
    private Context mContext;
    private ListView mListView;
    private FragmentManager fragmentManager;
    private LibaoAdapter mLibaoAdapter;
    private List<Libao.LibaoGiftBean> giftList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        fragmentManager = getFragmentManager();
        giftList = new Libao().getList();

        Map<String,Object> params = new HashMap<>();
        params.put("pageno",1);
        HttpUtils.load(urlString).post(params).callBack(this,1);

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
        PullToRefreshListView refreshView = (PullToRefreshListView) view.findViewById(R.id.libao_list_view);
        //将RefreshView转换成ListView
        mListView = refreshView.getRefreshableView();
        //
        mListView.addHeaderView(topView);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ad_frame_layout,new AdFragment().newInstance());
        fragmentTransaction.commit();

        mLibaoAdapter = new LibaoAdapter();
        mListView.setAdapter(mLibaoAdapter);
        return view;

    }

    @Override
    public void successJson(String result, int requestCode) {

    }

    @Override
    public void successBitmap(Bitmap bitmap, int requestCode) {

    }


    class LibaoAdapter extends BaseAdapter{

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
            ViewHolder viewHolder ;
            if (convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.libao_item_list, parent, false);
                viewHolder = new ViewHolder(convertView);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.imageView.setImageResource(R.drawable.applogo);
            Libao.LibaoGiftBean listBean = giftList.get(position);

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
