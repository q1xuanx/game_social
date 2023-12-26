package com.example.nhom06_socialgamenetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;

public class ActivityLogin extends AppCompatActivity {

    AppCompatButton buttonLogin, buttonSignUp;
    TextInputEditText email, pass;
    CheckBox showPass;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        showPassEvent();
        signUpEvent();
        loginEvent();
    }
    public void init(){
        buttonLogin = findViewById(R.id.appCompatButtonLogin);
        buttonSignUp = findViewById(R.id.signUpButton);
        email = findViewById(R.id.inputEmail);
        pass = findViewById(R.id.inputPassword);
        showPass = findViewById(R.id.checkBoxShowPass);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    public void showPassEvent(){
        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    pass.setTransformationMethod(null);
                }else {
                    pass.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

    }
    public void signUpEvent(){
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void loginEvent(){
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().equals("") || pass.getText().toString().equals("")){
                    Toast.makeText(ActivityLogin.this,"Vui lòng nhập đầu đủ thông tin" ,Toast.LENGTH_SHORT).show();
                    return;
                }
                Query query = FirebaseDatabase.getInstance().getReference("user").orderByChild("email").equalTo(email.getText().toString());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                            User user = new User();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                user = snapshot1.getValue(User.class);
                            }
                            if (user.getPass().equals(pass.getText().toString())) {
                                intent.putExtra("username", user.getEmail());
                                intent.putExtra("fullname", user.getFullname());
                                intent.putExtra("isAdmin", user.getIsAdmin());
                                intent.putExtra("reputation", user.getReputation());
                                startActivity(intent);
                                Toast.makeText(ActivityLogin.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(ActivityLogin.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(ActivityLogin.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}