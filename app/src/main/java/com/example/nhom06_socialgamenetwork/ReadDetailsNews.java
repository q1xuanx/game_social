package com.example.nhom06_socialgamenetwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.adapter.AdapterNews;
import com.example.nhom06_socialgamenetwork.adapter.AdapterReadDetails;
import com.example.nhom06_socialgamenetwork.models.News;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ReadDetailsNews extends AppCompatActivity {

    Button backToNews, editNews, buttonChangeAnhBia, buttonChangeNoiDung, buttonChangeAnhNoiDung;
    TextView titleNews;
    ImageView imgView;
    RecyclerView detailsNews;
    LinearLayout layoutButton, layoutContainButtonAndView;
    News news;
    List<String> list;
    Boolean isClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_details_news);
        news = new News();
        int isAdmin = 1;
        news.setTitle(getIntent().getStringExtra("title"));
        news.setIdPic(getIntent().getStringExtra("thumnail"));
        news.setPicNews((List<String>)getIntent().getStringArrayListExtra("details"));
        news.setTimePost(getIntent().getStringExtra("time"));
        backToNews = findViewById(R.id.backBtnDetails);
        editNews = findViewById(R.id.editDetailsNews);
        titleNews = findViewById(R.id.tileDetails);
        detailsNews = findViewById(R.id.recyclerViewDetails);
        imgView = findViewById(R.id.imageViewDetails);
        layoutButton = findViewById(R.id.layoutButton);
        layoutContainButtonAndView = findViewById(R.id.layOutView);
        buttonChangeAnhBia = findViewById(R.id.editAnhBia);
        buttonChangeAnhNoiDung = findViewById(R.id.themAnhNoiDung);
        buttonChangeNoiDung = findViewById(R.id.themAnhNoiDung);
        editNews = findViewById(R.id.editDetailsNews);
        //Todo fix UI
        editNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isClicked) {

                    isClicked = true;
                }else {
                    layoutButton.setVisibility(View.GONE);
                    LinearLayout.LayoutParams changeGravity = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    changeGravity.gravity= Gravity.CENTER_VERTICAL;
                    changeGravity.rightMargin =60;
                    imgView.setLayoutParams(changeGravity);
                    isClicked = false;
                }
            }
        });
        layoutButton.setVisibility(View.GONE);
        if(!layoutButton.isShown()){
            LinearLayout.LayoutParams changeGravity = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            changeGravity.gravity= Gravity.CENTER_VERTICAL;
            changeGravity.rightMargin =60;
            imgView.setLayoutParams(changeGravity);
        }
        editNews.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (isAdmin == 1){
                    Dialog dialog = new Dialog(ReadDetailsNews.this);
                    dialog.setContentView(R.layout.add_text_to_news);
                    return true;
                }
                return false;
            }
        });
        backToNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleNews.setText(news.getTitle());
        Picasso.get().load(news.getIdPic()).into(imgView);
        list = news.getPicNews();
        AdapterReadDetails adapterReadDetails = new AdapterReadDetails(list,this);
        detailsNews.setAdapter(adapterReadDetails);
        detailsNews.setLayoutManager(new LinearLayoutManager(this));
    }
}