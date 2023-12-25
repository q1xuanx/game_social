package com.example.nhom06_socialgamenetwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.adapter.AdapterUserRateGame;
import com.example.nhom06_socialgamenetwork.models.Game;
import com.example.nhom06_socialgamenetwork.models.GameComment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActivityRateGame extends AppCompatActivity {
    ImageView imgView; // hinh anh cua game
    TextView gameName, totalPointGet; // ten game, va diem cua game
    FloatingActionButton writeRate; // viet comment
    RecyclerView recyclerView;
    Game game; // tao ra 1 game
    String key, idPic; // key va hinh anh cua game;
    List<GameComment> list;
    AdapterUserRateGame adapterUserRateGame; // adapter add comment
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

        // Button đánh giá game
        writeRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ActivityRateGame.this);
                dialog.setContentView(R.layout.dialog_rate_game);
                dialog.getWindow().setAttributes(changeSizeOfDialog(dialog));
                SeekBar point = dialog.findViewById(R.id.seekBar2);
                EditText comment = dialog.findViewById(R.id.rateUser);
                Button addComment = dialog.findViewById(R.id.addRateGame);
                addComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Todo them vao firebase
                        if(comment.getText().toString().equals("")){
                            Toast.makeText(ActivityRateGame.this, "Vui lòng nhập ý kiến của bạn", Toast.LENGTH_SHORT);
                        }else {
                            GameComment gm = new GameComment(MainActivity.user.getEmail(), comment.getText().toString(), Integer.parseInt(String.valueOf(point.getProgress())));
                            addItemRecyclerView(gm);
                            Toast.makeText(ActivityRateGame.this, "Cảm ơn bạn đã đánh giá", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
    }
    public void init(){
        imgView = findViewById(R.id.imageGame);
        gameName = findViewById(R.id.textView2);
        totalPointGet = findViewById(R.id.textViewPt);
        recyclerView = findViewById(R.id.listUserRate);
        writeRate = findViewById(R.id.writeRate);
        list = new ArrayList<>();
        adapterUserRateGame = new AdapterUserRateGame(list);
        recyclerView.setAdapter(adapterUserRateGame);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivityRateGame.this));
        game = new Game();
    }
    public WindowManager.LayoutParams changeSizeOfDialog(Dialog dialog){
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return layoutParams;
    }
    // Them comment vao recycler view;
    public void addItemRecyclerView(GameComment gameComment){
        adapterUserRateGame.addComment(gameComment);
    }
}