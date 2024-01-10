package com.example.nhom06_socialgamenetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.adapter.AdapterDaXoaDiscuss;
import com.example.nhom06_socialgamenetwork.models.Discuss;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ActivityManageDiscuss extends AppCompatActivity implements RecyclerViewInterface {
    ImageButton back;
    RecyclerView recyclerView;
    AdapterDaXoaDiscuss adapter;
    List<Pair<String, Discuss>> list;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_discuss);
        initItem();
        backEvent();
    }
    public void initItem(){
        list = new ArrayList<>();
        back = findViewById(R.id.backToPage);
        recyclerView = findViewById(R.id.recyclerView4);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        getData();
    }
    public void getData(){
        if (MainActivity.user.getIsAdmin() > 0){
            DatabaseReference dataref = databaseReference.child("discuss");
            dataref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    if (snapshot.exists()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Discuss discuss = snapshot1.getValue(Discuss.class);
                            if (discuss.getIsDelete() == 1){
                                list.add(new Pair<>(snapshot1.getKey(), discuss));
                            }
                        }
                    }
                    adapter = new AdapterDaXoaDiscuss(list, ActivityManageDiscuss.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ActivityManageDiscuss.this));
                    recyclerView.setAdapter(adapter);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            DatabaseReference dataref = databaseReference.child("discuss");
            dataref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    if (snapshot.exists()){
                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                            Discuss discuss = snapshot1.getValue(Discuss.class);
                            if (MainActivity.user.getEmail().equals(discuss.getNamePost()) && discuss.getIsDelete() == 1){
                                list.add(new Pair<>(snapshot1.getKey(), discuss));
                            }
                        }
                    }
                    adapter = new AdapterDaXoaDiscuss(list,ActivityManageDiscuss.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ActivityManageDiscuss.this));
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void backEvent(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(int postion) {
        AlertDialog.Builder ask = new AlertDialog.Builder(ActivityManageDiscuss.this);
        ask.setMessage("Bạn muốn khôi phục hay xóa bài thảo luận?");
        ask.setNegativeButton("Khôi phục", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Discuss discuss = list.get(postion).second;
                discuss.setIsDelete(0);
                String key = list.get(postion).first;
                DatabaseReference editData = databaseReference.child("discuss").child(key);
                editData.setValue(discuss).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ActivityManageDiscuss.this, "Khôi phục thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        ask.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Discuss discuss = list.get(postion).second;
                String key = list.get(postion).first;
                if (discuss.getIdPic() != null && !discuss.getIdPic().equals("")) {
                    StorageReference delImg = FirebaseStorage.getInstance().getReferenceFromUrl(discuss.getIdPic());
                    delImg.delete();
                }
                DatabaseReference datadel = databaseReference.child("discuss").child(key);
                datadel.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ActivityManageDiscuss.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
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