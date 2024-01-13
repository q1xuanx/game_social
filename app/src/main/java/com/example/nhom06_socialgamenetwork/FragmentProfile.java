package com.example.nhom06_socialgamenetwork;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentProfile extends Fragment {
    TextView fullName, userName, rep ;
    AppCompatButton out, changPassword;
    ImageView noti;

    DatabaseReference databaseReference;

    User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initItem(v);
        setData();
        logoutEvent();
        changePass();
        seenNoti();
        setGradientColor();
        return v;
    }

    public void initItem(View v){
        fullName = v.findViewById(R.id.userNameProfile);
        userName = v.findViewById(R.id.userEmail);
        changPassword = v.findViewById(R.id.changePassword);
        noti = v.findViewById(R.id.noti);
        rep = v.findViewById(R.id.userRep);
        out = v.findViewById(R.id.logOut);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void seenNoti(){
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(FragmentProfile.this.getContext());
                dialog.setContentView(R.layout.dialog_noti);

                ListView notifi = dialog.findViewById(R.id.ViewNoti);
                Button xoaNhanh = dialog.findViewById(R.id.clearNoti);
                Query getListNoti = databaseReference.child("user").orderByChild("email").equalTo(MainActivity.user.getEmail());
                getListNoti.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            user = new User();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                user = snapshot1.getValue(User.class);
                            }
                            if (user.getNoti() == null){
                                user.setNoti(new ArrayList<>());
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FragmentProfile.this.getContext(), android.R.layout.simple_list_item_1, user.getNoti());
                            notifi.setAdapter(arrayAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                xoaNhanh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Query getdata = databaseReference.child("user").orderByChild("email").equalTo(MainActivity.user.getEmail());
                        getdata.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    user = new User();
                                    String key = "";
                                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                        user = snapshot1.getValue(User.class);
                                        key = snapshot1.getKey();
                                    }
                                    user.getNoti().clear();
                                    DatabaseReference edit = databaseReference.child("user").child(key);
                                    edit.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(FragmentProfile.this.getContext(), "Đã xóa thông báo", Toast.LENGTH_SHORT).show();
                                            MainActivity.user.setNoti(new ArrayList<>());
                                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FragmentProfile.this.getContext(), android.R.layout.simple_list_item_1,MainActivity.user.getNoti());
                                            notifi.setAdapter(arrayAdapter);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                dialog.show();
            }
        });
    }
    public void setData(){
        fullName.setText(fullName.getText() + MainActivity.user.getFullname());
        userName.setText(userName.getText() + MainActivity.user.getEmail());
        rep.setText(rep.getText() + String.valueOf(MainActivity.user.getReputation()));
    }
    public void logoutEvent(){
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FragmentProfile.this.getContext(), ActivityLogin.class);
                intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public void changePass(){
        changPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(FragmentProfile.this.getContext());
                dialog.setContentView(R.layout.dialog_changepass);
                EditText pass = dialog.findViewById(R.id.password), passnew = dialog.findViewById(R.id.passwordnew), passnewconf = dialog.findViewById(R.id.passwordnewconfirm);
                Button changPass = dialog.findViewById(R.id.thayPass);
                Button BackchangePass = dialog.findViewById(R.id.btnBackthayPass);
                BackchangePass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                changPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!pass.getText().toString().equals(MainActivity.user.getPass())){
                            Toast.makeText(FragmentProfile.this.getContext(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                        }else if (pass.getText().toString().equals("") || passnew.getText().toString().equals("") || passnewconf.getText().toString().equals("")){
                            Toast.makeText(FragmentProfile.this.getContext(), "Không được để trống", Toast.LENGTH_SHORT).show();
                        }else if (!passnew.getText().toString().equals(passnewconf.getText().toString())){
                            Toast.makeText(FragmentProfile.this.getContext(), "Mật khẩu mới nhập không khớp", Toast.LENGTH_SHORT).show();
                        }else {
                            Query getData = databaseReference.child("user").orderByChild("email").equalTo(MainActivity.user.getEmail());
                            getData.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        user = new User();
                                        String key = "";
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                            user = snapshot1.getValue(User.class);
                                            key = snapshot1.getKey();
                                        }
                                        user.setPass(passnew.getText().toString());
                                        DatabaseReference changPass = databaseReference.child("user").child(key);
                                        changPass.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(FragmentProfile.this.getContext(), "Thay đổi thành công", Toast.LENGTH_SHORT).show();
                                                MainActivity.user = user;
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });
                dialog.show();
            }
        });
    }
    private void setGradientColor() {
        int startColor = Color.parseColor("#33ccff");
        int middleColor = Color.parseColor("#ff9999");
        int endColor = Color.parseColor("#ff3366");

        TextView[] textViews = {fullName, userName, rep};

        for (TextView textView : textViews) {
            int[] colors = {startColor, middleColor, endColor};
            float[] positions = {0f, 0.5f, 1f};

            Shader textShader = new LinearGradient(0, 0, textView.getPaint().measureText(textView.getText().toString()), textView.getTextSize(),
                    colors, positions, Shader.TileMode.CLAMP);
            textView.getPaint().setShader(textShader);
        }
    }
}