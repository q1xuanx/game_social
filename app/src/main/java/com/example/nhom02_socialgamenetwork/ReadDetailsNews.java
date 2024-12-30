package com.example.nhom02_socialgamenetwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhom02_socialgamenetwork.adapter.AdapterNews;
import com.example.nhom02_socialgamenetwork.adapter.AdapterReadDetails;
import com.example.nhom02_socialgamenetwork.models.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ReadDetailsNews extends AppCompatActivity {

    ImageButton backToNews;
    TextView titleNews;
    ImageView imgView;
    RecyclerView detailsNews;
    News news;
    List<String> list;
    AdapterReadDetails adapterReadDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_details_news);
        news = new News();
        news.setTitle(getIntent().getStringExtra("title"));
        news.setIdPic(getIntent().getStringExtra("thumnail"));
        news.setPicNews((List<String>) getIntent().getStringArrayListExtra("details"));
        news.setTimePost(getIntent().getStringExtra("time"));
        String key = getIntent().getStringExtra("key");

        backToNews = findViewById(R.id.backBtnDetails);
        titleNews = findViewById(R.id.tileDetails);
        detailsNews = findViewById(R.id.recyclerViewDetails);
        imgView = findViewById(R.id.imageViewDetails);
        backToNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleNews.setText(news.getTitle());
        Picasso.get().load(news.getIdPic()).into(imgView);
        list = news.getPicNews();
        adapterReadDetails = new AdapterReadDetails(list, this);
        detailsNews.setAdapter(adapterReadDetails);
        detailsNews.setLayoutManager(new LinearLayoutManager(this));
    }

}