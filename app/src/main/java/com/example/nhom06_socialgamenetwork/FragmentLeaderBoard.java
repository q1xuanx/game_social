package com.example.nhom06_socialgamenetwork;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

public class FragmentLeaderBoard extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leader_board, container, false);
        setAutoSlide(v);

        return v;
    }
    public void setAutoSlide(View v){
        ImageCarousel autoSlide = v.findViewById(R.id.leaderBoardAutoScroll);
        List<CarouselItem> listImg = new ArrayList<>();
        listImg.add(new CarouselItem("https://cdn.tuoitre.vn/zoom/480_300/471584752817336320/2023/8/18/z460403153766270591260b9275f06f3198fd73b71bfc8-1692320194754518028680-0-0-775-1240-crop-16923201998441869011790.jpg"));
        listImg.add(new CarouselItem("https://thietkegame.com/wp-content/uploads/2019/12/co-che-dieu-khien-trong-game.jpg"));
        listImg.add(new CarouselItem("https://png.pngtree.com/thumb_back/fw800/background/20210910/pngtree-professional-gamer-playing-games-online-portrait-image_867022.jpg"));
        autoSlide.setData(listImg);
        autoSlide.setAutoPlay(true);
    }
}