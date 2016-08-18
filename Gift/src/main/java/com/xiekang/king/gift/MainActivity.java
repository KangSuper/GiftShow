package com.xiekang.king.gift;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xiekang.king.gift.hot.HotFragment;
import com.xiekang.king.gift.libao.LibaoFragment;
import com.xiekang.king.gift.tese.TeseFragment;
import com.xiekang.king.gift.utils.InfoCallBack;
import com.xiekang.king.gift.youxi.KaifuFragment;
import com.xiekang.king.gift.youxi.YouxiFragment;

public class MainActivity extends BaseActivity implements InfoCallBack{

    private RadioGroup mRadioGroup;
    private FragmentManager mFragmentManager;
    private Fragment mCurrentShowFragment;
    private LibaoFragment libaoFragment;
    private YouxiFragment youxiFragment;
    private HotFragment hotFragment;
    private TeseFragment teseFragment;
    private ImageView mMenuImg;
    private TextView mTextTxt;
    private TextView mSearchTxt;
    private DrawerLayout mDrawerLayout;
    private TextView mLoadTxt;
    private TextView mHomeTxt;
    private TextView mGift;
    private TextView mCountTxt;
    private TextView mOpitionTxt;
    private TextView mAboutTxt;
    private boolean isExit;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isExit = false;
            if (msg.what == 1){
                mTextTxt.setVisibility(View.VISIBLE);
                mCountTxt.setVisibility(View.INVISIBLE);
                mCountTxt.setText("");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActionBar();

        mFragmentManager = getSupportFragmentManager();
        initFragment();
        initView();
    }

    private void initActionBar() {
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowCustomEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.action_bar_view, null);
        mMenuImg = (ImageView) view.findViewById(R.id.bar_menu_view);
        mTextTxt = (TextView) view.findViewById(R.id.bar_text_view);
        mSearchTxt = (TextView) view.findViewById(R.id.bar_search_text);
        mCountTxt = (TextView) view.findViewById(R.id.bar_count_text);
        mSearchTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                Toast.makeText(MainActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        supportActionBar.setCustomView(view);


    }


    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.gift_radio_group);
        mRadioGroup.setOnCheckedChangeListener(checkedChangeListener);
        //初始化抽屉控件
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_drawer_layout);

        mLoadTxt = (TextView) findViewById(R.id.drawer_load_txt);
        mHomeTxt = (TextView) findViewById(R.id.drawer_home_text_view);
        mGift = (TextView) findViewById(R.id.drawer_gift_text_view);
        mOpitionTxt = (TextView) findViewById(R.id.drawer_option_text_view);
        mAboutTxt = (TextView) findViewById(R.id.drawer_about_text_view);

        mMenuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        mHomeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
            }
        });

        mLoadTxt.setOnClickListener(loadListener);
        mGift.setOnClickListener(loadListener);
        mOpitionTxt.setOnClickListener(loadListener);
        mAboutTxt.setOnClickListener(loadListener);
    }

    private View.OnClickListener loadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, LoadActivity.class);
            startActivity(intent);
        }
    };


    private RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.gift_libao_rb:
                    ctrlFragment(libaoFragment);
                    mTextTxt.setText("礼包精灵");
                    mSearchTxt.setVisibility(View.VISIBLE);
                    break;
                case R.id.gift_youxi_rb:
                    mTextTxt.setText("开服");
                    mSearchTxt.setVisibility(View.INVISIBLE);
                    ctrlFragment(youxiFragment);
                    break;
                case R.id.gift_hot_rb:
                    mTextTxt.setText("热门游戏");
                    mSearchTxt.setVisibility(View.INVISIBLE);
                    ctrlFragment(hotFragment);
                    break;
                case R.id.gift_tese_rb:
                    mTextTxt.setText("独家企划");
                    mSearchTxt.setVisibility(View.INVISIBLE);
                    ctrlFragment(teseFragment);
                    break;
            }
        }
    };

    private void initFragment() {
        libaoFragment = LibaoFragment.newInstance();
        youxiFragment = YouxiFragment.newInstance();
        hotFragment = HotFragment.newInstance();
        teseFragment = TeseFragment.newInstance();
        ctrlFragment(libaoFragment);
    }


    /**
     * @param fragment 当前要显示的Fragment
     */
    private void ctrlFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (mCurrentShowFragment != null && mCurrentShowFragment.isAdded()) {
            fragmentTransaction.hide(mCurrentShowFragment);
        }
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.main_frame_layout, fragment);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.commit();

        mCurrentShowFragment = fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    public void dataCount(int count) {
        mTextTxt.setVisibility(View.INVISIBLE);
        mCountTxt.setVisibility(View.VISIBLE);
        mCountTxt.setText("共发现"+count+"条数据");
        mHandler.sendEmptyMessageDelayed(1,2000);
    }
}
