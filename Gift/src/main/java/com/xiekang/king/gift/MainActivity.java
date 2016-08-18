package com.xiekang.king.gift;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xiekang.king.gift.hot.HotFragment;
import com.xiekang.king.gift.libao.LibaoFragment;
import com.xiekang.king.gift.tese.TeseFragment;
import com.xiekang.king.gift.youxi.YouxiFragment;

public class MainActivity extends BaseActivity {

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

        mSearchTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                Toast.makeText(MainActivity.this,"搜索",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        supportActionBar.setCustomView(view);
    }


    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.gift_radio_group);
        mRadioGroup.setOnCheckedChangeListener(checkedChangeListener);
    }


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

    private void initFragment(){
        libaoFragment = LibaoFragment.newInstance();
        youxiFragment = YouxiFragment.newInstance();
        hotFragment = HotFragment.newInstance();
        teseFragment = TeseFragment.newInstance();
        ctrlFragment(libaoFragment);
    }



    /**
     *
     * @param fragment  当前要显示的Fragment
     */
    private void ctrlFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (mCurrentShowFragment != null && mCurrentShowFragment.isAdded()) {
            fragmentTransaction.hide(mCurrentShowFragment);
        }
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.main_frame_layout,fragment);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.commit();

        mCurrentShowFragment = fragment;
    }
}
