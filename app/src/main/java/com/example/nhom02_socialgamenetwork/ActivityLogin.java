package com.example.nhom02_socialgamenetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom02_socialgamenetwork.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
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
    TextView forgetPass;
    User user;
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
        forgetPassEvent();
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
        forgetPass = findViewById(R.id.forgetPass);
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
    public void forgetPassEvent(){
        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ActivityLogin.this);
                dialog.setContentView(R.layout.dialog_laylaimatkhau);
                dialog.show();
                TextInputEditText email = dialog.findViewById(R.id.inputEmailForget), password = dialog.findViewById(R.id.inputPasswordNew), confirmpass = dialog.findViewById(R.id.inputConfirmPasswordNew);
                Button findPass = dialog.findViewById(R.id.thayDoiPass);
                email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(!Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()){
                            email.setError("Không đúng định dạng email");
                        }else email.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                findPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (email.getText().toString().equals("") || password.getText().toString().equals("") || confirmpass.getText().toString().equals("")){
                            Toast.makeText(ActivityLogin.this, "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
                        }else if (!password.getText().toString().equals(confirmpass.getText().toString())){
                            Toast.makeText(ActivityLogin.this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                        }else {
                            Query checkEmail = databaseReference.child("user").orderByChild("email").equalTo(email.getText().toString());
                            checkEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        user = new User();
                                        String key = "";
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                            user = snapshot1.getValue(User.class);
                                            key = snapshot1.getKey();
                                        }
                                        user.setPass(password.getText().toString());
                                        DatabaseReference editPass = databaseReference.child("user").child(key);
                                        editPass.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ActivityLogin.this, "Thay đổi thành công", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        });
                                    }else {
                                        Toast.makeText(ActivityLogin.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });
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
                            if (user.getPass().equals(pass.getText().toString()) && user.getIsBanned() == 0) {
                                // Save email and password in SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(PREF_EMAIL, user.getEmail());
                                editor.putString(PREF_PASSWORD, pass.getText().toString());
                                editor.apply();

                                Bundle bundle = new Bundle();
                                bundle.putParcelable("user", user);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                                Toast.makeText(ActivityLogin.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ActivityLogin.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ActivityLogin.this, "Email không tồn tại hoặc đã bị khóa", Toast.LENGTH_SHORT).show();
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