package com.example.nhom06_socialgamenetwork;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.adapter.AdapterNews;
import com.example.nhom06_socialgamenetwork.models.News;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragementNews extends Fragment {

    RecyclerView recyclerView;
    List<News> list;
    FloatingActionButton btnAddNews;
    AdapterNews adapterNews;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        ImageCarousel autoScoll = v.findViewById(R.id.autoScroll);
        recyclerView = v.findViewById(R.id.recyclerView2);
        btnAddNews = v.findViewById(R.id.addNewsAdmin);
        list = new ArrayList<>();
        list.add(new News("https://gamek.mediacdn.vn/133514250583805952/2023/12/15/fakerchovy-6-17026338260671926608548.png","Faker và Chovy có thể lại trở thành đồng đội, nhưng một cái tên khác lại chiếm spotlight"));
        list.add(new News("https://gamek.mediacdn.vn/133514250583805952/2023/12/15/imagem-2023-12-11-170031585cgwy-1702631537765-1702631537953145132510.jpg","Quá cuồng Attack on Titan, cặp vợ chồng đặt tên con trai là Eren Yeager"));
        list.add(new News("https://gamek.mediacdn.vn/133514250583805952/2023/12/13/photo-1702440721352-17024407219781223357473.png","Ra mắt siêu tệ, bị đánh giá quá kém, game bom tấn có doanh thu thấp kỷ lục nhưng vẫn bá đạo nhờ điều này"));
        list.add(new News("https://gamek.mediacdn.vn/133514250583805952/2023/12/15/634d731de9aa2215a9dc4e40-1-1702611197339-17026111975722069379933.jpg","Nhận 5 game miễn phí cực hay từ nền tảng GOG của CD Projekt"));
        list.add(new News("https://gamek.mediacdn.vn/133514250583805952/2023/12/14/2708dfe2255a1ed95c6f7252538db858-1616-1702549416682-17025494168201204696004.jpg","Tiếp nối Dead Cells, bom tấn roguelike đình đám nhất thập kỷ sắp có mặt trên di động"));
        autoScoll.registerLifecycle(getLifecycle());
        List<CarouselItem> listImg = new ArrayList<>();
        listImg.add(new CarouselItem("https://cdn.tuoitre.vn/zoom/480_300/471584752817336320/2023/8/18/z460403153766270591260b9275f06f3198fd73b71bfc8-1692320194754518028680-0-0-775-1240-crop-16923201998441869011790.jpg"));
        listImg.add(new CarouselItem("https://thietkegame.com/wp-content/uploads/2019/12/co-che-dieu-khien-trong-game.jpg"));
        listImg.add(new CarouselItem("https://png.pngtree.com/thumb_back/fw800/background/20210910/pngtree-professional-gamer-playing-games-online-portrait-image_867022.jpg"));
        autoScoll.setData(listImg);
        autoScoll.setAutoPlay(true);
        adapterNews = new AdapterNews(list,this.getContext());
        recyclerView.setAdapter(adapterNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        btnAddNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeview = new Intent(FragementNews.this.getActivity(), WriteNews.class);
                updateRecyclerView.launch(changeview);
            }
        });
        return v;
    }
    ActivityResultLauncher<Intent> updateRecyclerView = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent temp = result.getData();
                News news = new News();
                news.setTitle(temp.getStringExtra("Title"));
                news.setIdPic(temp.getStringExtra("Picture"));
                news.setPicNews((List<String>) temp.getStringArrayListExtra("ListDetails"));
                news.setTimePost(temp.getStringExtra("Time"));
                adapterNews.addNews(news);
            }
        }
    });
}
