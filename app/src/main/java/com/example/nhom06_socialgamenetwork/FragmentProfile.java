package com.example.nhom06_socialgamenetwork;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentProfile extends Fragment {
    TextView fullName, userName, rep ;
    AppCompatButton out;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initItem(v);
        setData();
        logoutEvent();
        return v;
    }

    public void initItem(View v){
        fullName = v.findViewById(R.id.userNameProfile);
        userName = v.findViewById(R.id.userEmail);
        rep = v.findViewById(R.id.userRep);
        out = v.findViewById(R.id.logOut);
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
}