package com.example.nhom06_socialgamenetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottom;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottom = findViewById(R.id.bottomNavigation);
        frameLayout = findViewById(R.id.frameContainer);
        loadFragment(new FragementNews());
        bottom.setItemIconTintList(null);
        user = getUser(getIntent());
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
        setActionBar();
    }
    public void loadFragment(Fragment fragment){
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.frameContainer, fragment);
        trans.addToBackStack(null);
        trans.commit();
    }
    public User getUser(Intent intent){
        User user1 = getIntent().getParcelableExtra("user");
        return user1;
    }
    public void setActionBar(){
        navigationView = findViewById(R.id.navigationBar);
        drawerLayout = findViewById(R.id.layoutDraw);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.quanlytintuc){
                    Intent intent = new Intent(MainActivity.this, ActivityQuanLyTinTuc.class);
                    startActivity(intent);
                    return true;
                }else if (item.getItemId() == R.id.quanlyuser){
                    Intent intent = new Intent(MainActivity.this,ActivityQuanLyUser.class);
                    startActivity(intent);
                    return true;
                }else if (item.getItemId() == R.id.baixoaganday){
                    Intent intent = new Intent(MainActivity.this, ActivityManageDiscuss.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        Menu menu = navigationView.getMenu();
        if (MainActivity.user.getIsAdmin() == 1){
            MenuItem menuItem = menu.findItem(R.id.quanlyuser);
            menuItem.setVisible(false);
        }else if (MainActivity.user.getIsAdmin() == 0){
            MenuItem menuItem1 = menu.findItem(R.id.quanlyuser);
            MenuItem menuItem2 = menu.findItem(R.id.quanlytintuc);
            menuItem1.setVisible(false);
            menuItem2.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else super.onBackPressed();
    }
}