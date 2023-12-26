package com.example.nhom06_socialgamenetwork;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.models.Discuss;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscussComment extends AppCompatActivity {
    Discuss discuss;
    TextView username, title, details, totalLike, totalDislike;
    ImageView imgView;
    ImageButton like, dislike;
    RecyclerView listComment;
    AppCompatButton writeComment;
    String key;
    DatabaseReference databaseReference;
    List<Integer> listLike, listDislike;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_discuss_comment);
        discuss = new Discuss();
        getData(getIntent());
        initItem();
        setContent();
    }

    public void getData(Intent intent){
        Bundle bundle = intent.getExtras();
        if (!bundle.getString("idPic").equals("khong co hinh")){
            discuss.setIdPic(bundle.getString("idPic"));
        }
        discuss.setTitle(bundle.getString("title"));
        discuss.setDetails(bundle.getString("details"));
        discuss.setNamePost(bundle.getString("username"));
        discuss.setLike(bundle.getIntegerArrayList("like"));
        discuss.setDislike(bundle.getIntegerArrayList("dislike"));
        key = bundle.getString("key");
    }
    public void initItem(){
        username = findViewById(R.id.nameUserPost);
        title = findViewById(R.id.titlePost);
        imgView = findViewById(R.id.imageTopic);
        details = findViewById(R.id.detailsTopic);
        like = findViewById(R.id.buttonLike);
        dislike = findViewById(R.id.buttonDislike);
        totalLike = findViewById(R.id.totalLike);
        totalDislike = findViewById(R.id.totalDislike);
        writeComment = findViewById(R.id.writeCommentTopic);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    public void setContent(){
        username.setText(discuss.getNamePost());
        title.setText(discuss.getTitle());
        details.setText(discuss.getDetails());
        if (discuss.getLike() == null){
            listLike = new ArrayList<>();
            discuss.setLike(listLike);
        }
        if (discuss.getDislike() == null){
            listDislike = new ArrayList<>();
            discuss.setDislike(listDislike);
        }
        totalLike.setText(String.valueOf(discuss.getLike().size()));
        totalDislike.setText(String.valueOf(discuss.getDislike().size()));
        if(discuss.getIdPic() != null){
            Picasso.get().load(Uri.parse(discuss.getIdPic())).into(imgView);
        }else {
            Picasso.get().load(R.drawable.game_logo).into(imgView);
        }
    }
    public void likeEvent(){
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
