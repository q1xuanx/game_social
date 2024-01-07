package com.example.nhom06_socialgamenetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class ActivityLogin extends AppCompatActivity {

    AppCompatButton buttonLogin, buttonSignUp;
    TextInputEditText email, pass;
    CheckBox showPass;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        showPassEvent();
        signUpEvent();
        loginEvent();

        // Check for saved email and password
        String savedEmail = sharedPreferences.getString(PREF_EMAIL, null);
        String savedPassword = sharedPreferences.getString(PREF_PASSWORD, null);

        if (savedEmail != null && savedPassword != null) {
            // Auto-fill the email and password fields
            email.setText(savedEmail);
            pass.setText(savedPassword);
        }
    }

    public void init() {
        buttonLogin = findViewById(R.id.appCompatButtonLogin);
        buttonSignUp = findViewById(R.id.signUpButton);
        email = findViewById(R.id.inputEmail);
        pass = findViewById(R.id.inputPassword);
        showPass = findViewById(R.id.checkBoxShowPass);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    public void showPassEvent() {
        showPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    pass.setTransformationMethod(null);
                } else {
                    pass.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });
    }

    public void signUpEvent() {
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void loginEvent() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().equals("") || pass.getText().toString().equals("")) {
                    Toast.makeText(ActivityLogin.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                Query query = FirebaseDatabase.getInstance().getReference("user").orderByChild("email").equalTo(email.getText().toString());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                            User user = new User();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                user = snapshot1.getValue(User.class);
                            }
                            if (user.getPass().equals(pass.getText().toString())) {
                                // Save email and password in SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(PREF_EMAIL, user.getEmail());
                                editor.putString(PREF_PASSWORD, pass.getText().toString());
                                editor.apply();

                                intent.putExtra("username", user.getEmail());
                                intent.putExtra("fullname", user.getFullname());
                                intent.putExtra("isAdmin", user.getIsAdmin());
                                intent.putExtra("reputation", user.getReputation());
                                startActivity(intent);
                                finish();
                                Toast.makeText(ActivityLogin.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ActivityLogin.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        } else {
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