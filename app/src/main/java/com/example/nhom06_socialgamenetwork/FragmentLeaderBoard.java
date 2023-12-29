package com.example.nhom06_socialgamenetwork;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nhom06_socialgamenetwork.adapter.AdapterLeaderBoard;
import com.example.nhom06_socialgamenetwork.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FragmentLeaderBoard extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leader_board, container, false);
        setAutoSlide(v);
        initItem(v);
        getUserToLeaderBoard();
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
    public void initItem(View v){
        recyclerView = v.findViewById(R.id.recyclerViewLeaderBoard);
        db = FirebaseDatabase.getInstance().getReference();
    }
    public void getUserToLeaderBoard(){
        Query q = db.child("user").orderByChild("reputation").limitToLast(10);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    List<User> list = new ArrayList<>();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        User user = snapshot1.getValue(User.class);
                        list.add(user);
                    }
                    for (int i = 0; i < list.size()-1; i++){
                        for (int j = i + 1; j < list.size(); j++){
                            if (list.get(i).getReputation() < list.get(j).getReputation()){
                                Collections.swap(list,i,j);
                            }
                        }
                    }
                    AdapterLeaderBoard adapterLeaderBoard = new AdapterLeaderBoard(list);
                    recyclerView.setAdapter(adapterLeaderBoard);
                    recyclerView.setLayoutManager(new LinearLayoutManager(FragmentLeaderBoard.this.getContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}