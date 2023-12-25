package com.example.nhom06_socialgamenetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottom;
    public static String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom = findViewById(R.id.bottomNavigation);
        frameLayout = findViewById(R.id.frameContainer);
        userName = "tester01";
        loadFragment(new FragementNews());
        bottom.setItemIconTintList(null);
        bottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                if (item.getItemId() == R.id.bottom_news){
                    fragment = new FragementNews();
                    loadFragment(fragment);
                }else if (item.getItemId() == R.id.bottom_gamerate){
                    fragment = new FragmentGame();
                    loadFragment(fragment);
                }else if (item.getItemId() == R.id.bottom_disscuss){
                    fragment = new FragmentDiscuss();
                    loadFragment(fragment);
                }else if (item.getItemId() == R.id.bottom_leaderboard){
                    fragment = new FragmentLeaderBoard();
                    loadFragment(fragment);
                }else if (item.getItemId() == R.id.bottom_profile){
                    fragment = new FragmentProfile();
                    loadFragment(fragment);
                }
                return true;
            }
        });
    }
    public void loadFragment(Fragment fragment){
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.frameContainer, fragment);
        //trans.addToBackStack(null);
        trans.commit();
    }
}