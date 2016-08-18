package com.xiekang.king.gift;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayShowCustomEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.yxdetails_bar_view, null, false);
        supportActionBar.setCustomView(view);

        ImageView backImg = (ImageView) view.findViewById(R.id.load_back_image_view);
        TextView registerTxt = (TextView) view.findViewById(R.id.load_register_text_view);
        TextView titleTxt = (TextView) view.findViewById(R.id.load_title_text_view);
        titleTxt.setText("会员注册");
        registerTxt.setVisibility(View.INVISIBLE);


        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
