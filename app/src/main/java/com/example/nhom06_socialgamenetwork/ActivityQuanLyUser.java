package com.example.nhom06_socialgamenetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.adapter.AdapterQlyUser;
import com.example.nhom06_socialgamenetwork.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

public class ActivityQuanLyUser extends AppCompatActivity implements RecyclerViewInterface {

    RecyclerView recyclerView;
    TextView nameUser, role;
    DatabaseReference databaseref;
    ImageButton backToPages;
    List<Pair<String, User>> list;
    AdapterQlyUser adapterQlyUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_user);
        initItem();
        backEvent();
    }


    public void initItem(){
        backToPages = findViewById(R.id.back);
        recyclerView = findViewById(R.id.recyclerViewQlyUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityQuanLyUser.this));
        nameUser = findViewById(R.id.username);
        role = findViewById(R.id.role);
        nameUser.setText(nameUser.getText() + MainActivity.user.getFullname());
        role.setText(role.getText() + " Super Admin");
        list = new ArrayList<>();
        databaseref = FirebaseDatabase.getInstance().getReference();
        getData();
    }
    public void backEvent(){
        backToPages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void getData(){
        DatabaseReference get = databaseref.child("user");
        get.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                        list.add(new Pair<>(snapshot1.getKey(), snapshot1.getValue(User.class)));
                    }
                    adapterQlyUser = new AdapterQlyUser(list, ActivityQuanLyUser.this);
                    recyclerView.setAdapter(adapterQlyUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public ArrayAdapter<String> initItemRole(){
        List<String> roleHave = Arrays.asList("user", "admin", "super admin");
        ArrayAdapter<String> temp = new ArrayAdapter<>(ActivityQuanLyUser.this, android.R.layout.simple_spinner_item, roleHave);
        temp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return temp;
    }
    public ArrayAdapter<String> initItemStats(){
        List<String> statsHave = Arrays.asList("Hoạt Động", "Đã bị chặn");
        ArrayAdapter<String> temp = new ArrayAdapter<>(ActivityQuanLyUser.this, android.R.layout.simple_spinner_item, statsHave);
        temp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return temp;
    }

    @Override
    public void onItemClick(int postion) {
        User user = list.get(postion).second;
        String key = list.get(postion).first;
        Dialog dialog = new Dialog(ActivityQuanLyUser.this);
        dialog.setContentView(R.layout.dialog_userholder);
        TextView email = dialog.findViewById(R.id.email);
        EditText pass = dialog.findViewById(R.id.password);
        Spinner role = dialog.findViewById(R.id.role), stats = dialog.findViewById(R.id.banned);
        ImageButton seePass = dialog.findViewById(R.id.seePass);
        Button saveChange = dialog.findViewById(R.id.luuCaiDat);
        email.setText(email.getText()+user.getEmail());
        pass.setText(pass.getText()+user.getPass());
        role.setAdapter(initItemRole());
        int roleTem= user.getIsAdmin();
        stats.setAdapter(initItemStats());
        role.setSelection(user.getIsAdmin());
        stats.setSelection(user.getIsBanned());
        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user.setIsAdmin(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        stats.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user.setIsBanned(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        seePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getTransformationMethod() != null){
                    pass.setTransformationMethod(null);
                }else {
                    pass.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
        saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pass.getText().toString().equals("")){
                    Toast.makeText(ActivityQuanLyUser.this, "Vui lòng không để trống pass", Toast.LENGTH_SHORT).show();
                }else {
                    if (roleTem != user.getIsAdmin()){
                        if(user.getNoti() == null){
                            user.setNoti(new Stack<>());
                            user.getNoti().add(getDate()+": "+"Bạn đã được thay đổi vai trò");
                        }else {
                            user.getNoti().add(getDate()+": "+"Bạn đã được thay đổi vai trò");
                        }
                    }
                    DatabaseReference databaseReference = databaseref.child("user").child(key);
                    user.setPass(pass.getText().toString());
                    databaseReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ActivityQuanLyUser.this,"Đã thay đổi thành công", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
    @Override
    public void itemClickGame(int position, String gameType) {

    }
    public String getDate(){
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return formattedDate;
    }
}