package com.example.nhom06_socialgamenetwork;

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

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

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
        setGradientColor();
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