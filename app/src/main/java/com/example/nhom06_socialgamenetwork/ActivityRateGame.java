package com.example.nhom06_socialgamenetwork;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_game);
        init();

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

                        if(comment.getText().toString().equals("")){
                            Toast.makeText(ActivityRateGame.this, "Vui lòng nhập ý kiến của bạn", Toast.LENGTH_SHORT);
                        }else {
                            DatabaseReference dbref = databaseReference.child("game").child(key);
                            dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        Game game = snapshot.getValue(Game.class);
                                        GameComment gm = new GameComment(MainActivity.user.getEmail(), comment.getText().toString(), point.getProgress());
                                        if (game.getList() == null){
                                            List<GameComment> listComment = new ArrayList<>();
                                            game.setList(listComment);
                                            game.getList().add(gm);
                                        }else {
                                            game.getList().add(gm);
                                        }
                                        dbref.setValue(game).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ActivityRateGame.this, "Cảm ơn đánh giá của bạn", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ActivityRateGame.this, "Có lỗi xảy ra trong quá trình rate game", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });
                DatabaseReference dbcheckExist = databaseReference.child("game").child(key);
                dbcheckExist.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            Game game1 = snapshot.getValue(Game.class);
                            if (game1.getList() != null){
                                for(GameComment gm1 : game1.getList()){
                                    if (gm1.getNameComment().equals(MainActivity.user.getEmail())){
                                        Toast.makeText(ActivityRateGame.this, "Bạn đã đánh giá game này rồi", Toast.LENGTH_SHORT).show();
                                        return;
                                    }else {
                                        dialog.show();
                                    }
                                }
                            }else {
                                dialog.show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    public void init(){
        imgView = findViewById(R.id.imageGame);
        gameName = findViewById(R.id.textView2);
        totalPointGet = findViewById(R.id.textViewPt);
        recyclerView = findViewById(R.id.listUserRate);
        writeRate = findViewById(R.id.writeRate);
        key = getIntent().getStringExtra("key");
        idPic = getIntent().getStringExtra("Pic");
        Picasso.get().load(Uri.parse(idPic)).into(imgView);
        gameName.setText(getIntent().getStringExtra("NameGame"));
        totalPointGet.setText(String.valueOf(getIntent().getIntExtra("Point",-1)));
        game = new Game(getIntent().getStringExtra("Pic"), getIntent().getStringExtra("NameGame"),list);
        list = new ArrayList<>();
        game = new Game();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        dataChange();
    }
    public void dataChange(){
        databaseReference.child("game").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                Game game = snapshot.getValue(Game.class);
                int avg = 0;
                if (game.getList() == null){
                    game.setList(list);
                }else {
                    list = game.getList();
                }
                for (GameComment gm1 : list){
                    avg += gm1.getPoint();
                }
                if (list.size() != 0){
                    totalPointGet.setText(String.valueOf(avg / list.size()));
                }
                adapterUserRateGame = new AdapterUserRateGame(list);
                recyclerView.setAdapter(adapterUserRateGame);
                recyclerView.setLayoutManager(new LinearLayoutManager(ActivityRateGame.this));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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