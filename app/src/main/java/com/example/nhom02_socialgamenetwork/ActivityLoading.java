package com.example.nhom02_socialgamenetwork;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ActivityLoading extends AppCompatActivity {

    private static final int LOADING_DELAY = 3100; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        ImageView imageView = findViewById(R.id.loadingbar_gif);
        Glide.with(this).load(R.drawable.loadingbar).into(imageView);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.nekopara);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.start();
                Intent intent = new Intent(ActivityLoading.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        }, LOADING_DELAY);
    }
}