package com.example.nhom06_socialgamenetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.adapter.AdapterDaXoaGanDay;
import com.example.nhom06_socialgamenetwork.models.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ActivityDaXoaGanDay extends AppCompatActivity implements RecyclerViewInterface {
    DatabaseReference databaseReference;
    StorageReference storageReference;
    RecyclerView recyclerView;
    ImageButton backButton;
    AdapterDaXoaGanDay adapter;
    List<Pair<String, News>> list;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_da_xoa_gan_day);
        initItem();
        setData();
        getBack();
    }

    public void initItem() {
        recyclerView = findViewById(R.id.recyDaXoaGanDay);
        backButton = findViewById(R.id.btnBackDaXoa);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressBar = findViewById(R.id.progressBar1);
        list = new ArrayList<>();
    }
    public void getBack(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void setData() {
        DatabaseReference dbGetData = databaseReference.child("post");
        dbGetData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        News temp = snapshot1.getValue(News.class);
                        if (temp.getIsDelete() == 1) {
                            list.add(new Pair<>(snapshot1.getKey(), temp));
                        }
                    }
                    adapter = new AdapterDaXoaGanDay(list, ActivityDaXoaGanDay.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ActivityDaXoaGanDay.this));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(int postion) {
        AlertDialog.Builder ask = new AlertDialog.Builder(ActivityDaXoaGanDay.this);
        ask.setMessage("Bạn có muốn khôi phục hay xóa tin tức?");
        ask.setNegativeButton("Khôi phục", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String key = list.get(postion).first;
                News news = list.get(postion).second;
                DatabaseReference khoiPhuc = databaseReference.child("post").child(key);
                news.setIsDelete(0);
                khoiPhuc.setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ActivityDaXoaGanDay.this, "Đã khôi phục thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ask.setPositiveButton("Xóa luôn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                News news = list.get(postion).second;
                String key = list.get(postion).first;
                List<String> urlTodel = new ArrayList<>();
                for (String s : news.getPicNews()) {
                    if (Patterns.WEB_URL.matcher(s).matches()) {
                        urlTodel.add(s);
                    }
                }
                for (String s : urlTodel) {
                    StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(s);
                    if (storageReference1 != null) {
                        storageReference1.delete();
                    }
                }
                progressBar.setVisibility(View.VISIBLE);
                DatabaseReference databaseReference1 = databaseReference.child("post").child(key);
                databaseReference1.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ActivityDaXoaGanDay.this, "Đã xóa khỏi thùng rác", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ask.show();
    }

    @Override
    public void itemClickGame(int position, String gameType) {

    }
}