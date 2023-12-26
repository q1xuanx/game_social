package com.example.nhom06_socialgamenetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.models.User;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ActivityRegister extends AppCompatActivity {

    FirebaseAuth mauth;
    DatabaseReference databaseReference;
    TextInputEditText email, pass, confirmpass, fullname;
    TextView alreadyHaveAccount;
    AppCompatButton createNewAccout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initFirebase();
        initItem();
        checkEmail();
        checkPassword();
        createAccount();
        alreadyHaveAccountEvent();
    }

    public void initFirebase() {
        mauth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void initItem() {
        email = findViewById(R.id.inputEmailRegis);
        pass = findViewById(R.id.inputPassword);
        confirmpass = findViewById(R.id.inputPasswordConfirm);
        fullname = findViewById(R.id.inputFullName);
        createNewAccout = findViewById(R.id.signUp);
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAcc);
    }
    public void alreadyHaveAccountEvent(){
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void checkEmail() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()) {
                    email.setError("Vui lòng nhập đúng đính dạng email");
                } else email.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Query query = FirebaseDatabase.getInstance().getReference("user").orderByChild("email").equalTo(String.valueOf(editable));
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            email.setError("Email đã tồn tại");
                        } else email.setError(null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    public void checkLengthPass(){
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() < 6){
                    pass.setError("Mật khẩu phải có nhiều hơn 6 ký tự");
                }else pass.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void checkPassword() {
        confirmpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.equals("")) {
                    confirmpass.setError("Vui lòng nhập password");
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (pass.getText().toString().equals(editable.toString())) {
                    confirmpass.setError(null);
                } else confirmpass.setError("Mật khẩu nhập lại không khớp");
            }
        });
    }

    public void createAccount() {

        createNewAccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmpass.getText().toString().equals("") || pass.getText().toString().equals("") || email.getText().toString().equals("") || fullname.getText().toString().equals("")){
                    Toast.makeText(ActivityRegister.this, "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseReference pushData = databaseReference.child("user").push();
                    mauth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnSuccessListener(ActivityRegister.this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    User user = new User(email.getText().toString(), pass.getText().toString(), fullname.getText().toString());
                                    pushData.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ActivityRegister.this, "Đăng ký thành công, vui lòng kiểm tra email", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                                            startActivity(intent);
                                            finish();
                                            setAllDefault();
                                        }
                                    });
                                }
                            });
                        }
                    });

                }
            }
        });
    }

    public void setAllDefault() {
        confirmpass.setText("");
        pass.setText("");
        email.setText("");
        confirmpass.setError(null);
        fullname.setText("");
    }
}