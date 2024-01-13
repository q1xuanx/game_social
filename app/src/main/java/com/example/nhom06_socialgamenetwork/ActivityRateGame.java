package com.example.nhom06_socialgamenetwork;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
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
import com.example.nhom06_socialgamenetwork.models.User;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    Retrofit retrofit;
    CallApiRetrofit callApiRetrofit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_game);
        init();
        setGradientColor();

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
                Button backCommet =dialog.findViewById(R.id.btnBackRateGame);
                callAPI();
                backCommet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
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
                                        Call<String> check = callApiRetrofit.predictToxicity(comment.getText().toString());
                                        check.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                String predict = response.body();
                                                if (predict.equals("clean")){
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
                                                }else {
                                                    Toast.makeText(ActivityRateGame.this, "Bạn đang mất bình tĩnh, vui lòng kiềm chế",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {

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
                            boolean isCmt = false;
                            if (game1.getList() != null){
                                for(GameComment gm1 : game1.getList()){
                                    if (gm1.getNameComment().equals(MainActivity.user.getEmail())){
                                        isCmt = true;
                                    }
                                }
                                if (isCmt){
                                    Toast.makeText(ActivityRateGame.this, "Bạn đã đánh giá game này rồi", Toast.LENGTH_SHORT).show();
                                }else{
                                    dialog.show();
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
        delComment();
    }
    public void callAPI(){
        retrofit = new Retrofit.Builder().baseUrl("https://3007-203-205-32-22.ngrok-free.app/").addConverterFactory(GsonConverterFactory.create()).build();
        callApiRetrofit = retrofit.create(CallApiRetrofit.class);
    }
    public void delComment(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                GameComment gameComment = game.getList().get(viewHolder.getBindingAdapterPosition());
                if (gameComment.getNameComment().equals(MainActivity.user.getEmail()) || MainActivity.user.getIsAdmin() > 0){
                    game.getList().remove(viewHolder.getBindingAdapterPosition());
                    DatabaseReference databaseReference1 = databaseReference.child("game").child(key);
                    databaseReference1.setValue(game).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if (MainActivity.user.getIsAdmin() > 0 && !gameComment.getNameComment().equals(MainActivity.user.getEmail())) {
                                Query addNoti = databaseReference.child("user").orderByChild("email").equalTo(gameComment.getNameComment());
                                addNoti.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            User user = new User();
                                            String key = "";
                                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                                user = snapshot1.getValue(User.class);
                                                key = snapshot1.getKey();
                                            }
                                            if (!gameComment.getNameComment().equals(MainActivity.user.getEmail())) {
                                                if (user.getNoti() == null) {
                                                    user.setNoti(new ArrayList<>());
                                                    user.getNoti().add(getDate() + ": " + "Comment đánh giá của bạn đã bị admin xóa do vi phạm tiêu chuẩn");
                                                } else {
                                                    user.getNoti().add(getDate()  + ": " + "Comment đánh giá của bạn đã bị admin xóa do vi phạm tiêu chuẩn");
                                                }
                                            }
                                            DatabaseReference dataEdit = databaseReference.child("user").child(key);
                                            dataEdit.setValue(user);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            Toast.makeText(ActivityRateGame.this, "Đã xóa đánh giá thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(ActivityRateGame.this, "Không thể xóa được comment", Toast.LENGTH_SHORT).show();
                    recyclerView.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                }
            }
        }).attachToRecyclerView(recyclerView);
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
        databaseReference = FirebaseDatabase.getInstance().getReference();
        dataChange();
    }
    public void dataChange(){
        databaseReference.child("game").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                game = snapshot.getValue(Game.class);
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
    private void setGradientColor() {
        int startColor1 = Color.parseColor("#CC00FF");
        int endColor1 = Color.parseColor("#3366CC");

        int startColor2 = Color.parseColor("#3366CC");
        int endColor2 = Color.parseColor("#CC00FF");


        TextView[] textViews = {gameName};

        for (TextView textView : textViews) {
            int[] colors1 = {startColor1, endColor1};
            int[] colors2 = {startColor2, endColor2};
            float[] positions = {0f,1f};
            float[] positions2 = {0.9f,1f};
            if (textView.getText().toString().split(" ").length <= 10) {
                Shader textShader = new LinearGradient(0, 0, textView.getPaint().measureText(textView.getText().toString()), textView.getTextSize(),
                        colors2, positions2, Shader.TileMode.CLAMP);
                textView.getPaint().setShader(textShader);
            }
            else{
                Shader textShader = new LinearGradient(0, 0, textView.getPaint().measureText(textView.getText().toString()), textView.getTextSize(),
                        colors1, positions, Shader.TileMode.CLAMP);
                textView.getPaint().setShader(textShader);
            }
        }
    }
    public String getDate(){
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return formattedDate;
    }
}