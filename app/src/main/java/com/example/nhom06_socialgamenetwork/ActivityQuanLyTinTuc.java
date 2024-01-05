package com.example.nhom06_socialgamenetwork;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.adapter.AdapterNews;
import com.example.nhom06_socialgamenetwork.models.News;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityQuanLyTinTuc extends AppCompatActivity implements RecyclerViewInterface{

    ImageButton daXoaGanDay, back;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<Pair<String, News>> list;
    AdapterNews adapterNews;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qly_tintuc);
        initItem();
        backActivity();
        setData();
    }

    public void initItem(){
        daXoaGanDay = findViewById(R.id.daXoaGanDay); //TODO implement da xoa gan day
        back = findViewById(R.id.buttonBackQL);
        recyclerView = findViewById(R.id.recyclerViewQuanLyTinTuc);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        daXoaGanDay.setImageTintList(null);
        back.setImageTintList(null);
        list = new ArrayList<>();
    }

    public void backActivity(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setData(){
        DatabaseReference query = databaseReference.child("post");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        News news = snapshot1.getValue(News.class);
                        list.add(new Pair<>(snapshot1.getKey(),news));
                    }
                    adapterNews = new AdapterNews(list,ActivityQuanLyTinTuc.this, ActivityQuanLyTinTuc.this);
                    recyclerView.setAdapter(adapterNews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ActivityQuanLyTinTuc.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(int postion) {
        Intent edit = new Intent(this, AcitivityEditNews.class);
        String key = list.get(postion).first;
        edit.putExtra("key", key);
        startActivity(edit);
    }
    @Override
    public void itemClickGame(int position, String gameType) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }
}
