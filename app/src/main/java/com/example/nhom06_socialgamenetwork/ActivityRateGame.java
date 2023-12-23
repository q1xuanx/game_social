package com.example.nhom06_socialgamenetwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhom06_socialgamenetwork.models.Game;
import com.example.nhom06_socialgamenetwork.models.GameComment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActivityRateGame extends AppCompatActivity {
    ImageView imgView;
    TextView gameName, totalPointGet;
    FloatingActionButton writeRate;
    RecyclerView recyclerView;
    Game game;
    String key, idPic;
    List<GameComment> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_game);
        init();
        list = new ArrayList<>();
        key = getIntent().getStringExtra("key");
        idPic = getIntent().getStringExtra("Pic");
        Picasso.get().load(Uri.parse(idPic)).into(imgView);
        gameName.setText(getIntent().getStringExtra("NameGame"));
        totalPointGet.setText(String.valueOf(getIntent().getIntExtra("Point",-1)));
        game = new Game(getIntent().getStringExtra("Pic"), getIntent().getStringExtra("NameGame"),list);
    }
    public void init(){
        imgView = findViewById(R.id.imageGame);
        gameName = findViewById(R.id.textView2);
        totalPointGet = findViewById(R.id.textViewPt);
        recyclerView = findViewById(R.id.listUserRate);
        writeRate = findViewById(R.id.writeRate);
        game = new Game();
    }
}