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
        listImg.add(new CarouselItem("https://img.4gamers.com.tw/ckfinder-vn/image2/auto/2023-12/bal-231208-142308.jfif?versionId=PEE99.bw0oCDYNtvoOU2NgacA4LswttQ"));
        listImg.add(new CarouselItem("https://pbs.twimg.com/media/FjgyTj7WAAMIHAx?format=jpg&name=4096x4096"));
        listImg.add(new CarouselItem("https://images.pushsquare.com/9573e1fea4b6d/goty-it-takes-two.large.jpg"));
        autoSlide.setData(listImg);
        autoSlide.setAutoPlay(true);
    }
}