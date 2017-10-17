package com.android.martyrapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.android.martyrapp.model.Martyr;
import com.android.martyrapp.util.Constants;
import com.squareup.picasso.Picasso;

public class MartyrDetailActivity extends AppCompatActivity {
    public ImageView martyrImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_martyr_detail);
        martyrImage = (ImageView) findViewById(R.id.imageView4);
        Martyr martyr = (Martyr) getIntent().getExtras().get("martyr");

        Picasso.with(this)
                .load( Constants.S3_IMG_PATH + "/" + martyr.getImageName())
                .placeholder(android.R.drawable.ic_btn_speak_now)
                .into(martyrImage);
    }
}
