package com.example.nhom06_socialgamenetwork;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.adapter.AdapterNews;
import com.example.nhom06_socialgamenetwork.models.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityQuanLyTinTuc extends AppCompatActivity implements RecyclerViewInterface{

    ImageButton back;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    List<Pair<String, News>> list;
    AdapterNews adapterNews;
    FloatingActionButton btnThemNews,daXoaGanDay;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qly_tintuc);
        initItem();
        backActivity();
        setData();
        openDaXoaGanDay();
        eventAddTinTuc();
        delTempData();
    }

    public void initItem(){
        daXoaGanDay = findViewById(R.id.daXoaGanDay); //TODO implement da xoa gan day
        back = findViewById(R.id.buttonBackQL);
        recyclerView = findViewById(R.id.recyclerViewQuanLyTinTuc);
        btnThemNews = findViewById(R.id.themTinTuc);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        daXoaGanDay.setImageTintList(null);
        list = new ArrayList<>();
    }
    public void eventAddTinTuc(){
        btnThemNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeview = new Intent(ActivityQuanLyTinTuc.this, WriteNews.class);
                startActivity(changeview);
            }
        });
    }
    public void backActivity(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void openDaXoaGanDay(){
        daXoaGanDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent daXoa = new Intent(ActivityQuanLyTinTuc.this, ActivityDaXoaGanDay.class);
                startActivity(daXoa);
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
                        if (news.getIsDelete() == 0){
                            list.add(new Pair<>(snapshot1.getKey(),news));
                        }
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
    public void delTempData(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder ask = new AlertDialog.Builder(ActivityQuanLyTinTuc.this);
                ask.setMessage("Bạn có đẩy tin tức này vào thùng rác không ? ");
                ask.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String key = list.get(viewHolder.getBindingAdapterPosition()).first;
                        News news = list.get(viewHolder.getBindingAdapterPosition()).second;
                        DatabaseReference dataEdit = databaseReference.child("post").child(key);
                        if (news.getIsDelete() == 0){
                            news.setIsDelete(1);
                        }else news.setIsDelete(0);
                        dataEdit.setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ActivityQuanLyTinTuc.this, "Chuyển vào thùng rác thàng công", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                ask.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        recyclerView.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                    }
                });
                ask.setCancelable(false);
                ask.show();
            }
        }).attachToRecyclerView(recyclerView);
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
