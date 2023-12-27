package com.example.nhom06_socialgamenetwork;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.adapter.AdapterDiscussComment;
import com.example.nhom06_socialgamenetwork.models.CommentDiscuss;
import com.example.nhom06_socialgamenetwork.models.Discuss;
import com.example.nhom06_socialgamenetwork.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscussComment extends AppCompatActivity {
    Discuss discuss;
    TextView username, title, details, totalLike, totalDislike;
    ImageView imgView;
    Button like, dislike;
    RecyclerView listComment;
    AppCompatButton writeComment;
    String key;
    DatabaseReference databaseReference;
    List<String> listLike, listDislike;
    List<CommentDiscuss> discussComments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_discuss_comment);
        discuss = new Discuss();
        discussComments = new ArrayList<>();
        getData(getIntent());
        initItem();
        setContent();
        likeEvent();
        dislikeEvent();
        writeCommentEvent();
        addRep();
    }

    public void getData(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (!bundle.getString("idPic").equals("khong co hinh")) {
            discuss.setIdPic(bundle.getString("idPic"));
        }
        discuss.setTitle(bundle.getString("title"));
        discuss.setDetails(bundle.getString("details"));
        discuss.setNamePost(bundle.getString("username"));
        discuss.setLike(bundle.getStringArrayList("like"));
        discuss.setDislike(bundle.getStringArrayList("dislike"));
        key = bundle.getString("key");
    }

    public void initItem() {
        username = findViewById(R.id.nameUserPost);
        title = findViewById(R.id.titlePost);
        imgView = findViewById(R.id.imageTopic);
        details = findViewById(R.id.detailsTopic);
        like = findViewById(R.id.buttonLike);
        dislike = findViewById(R.id.buttonDislike);
        totalLike = findViewById(R.id.totalLike);
        totalDislike = findViewById(R.id.totalDislike);
        writeComment = findViewById(R.id.writeCommentTopic);
        listComment = findViewById(R.id.listComment);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        initComment();
    }

    public void initComment() {
        databaseReference.child("discuss").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                discussComments.clear();
                if (snapshot.exists()) {
                    Discuss discuss1 = snapshot.getValue(Discuss.class);
                    if(discuss1.getComment() == null){
                        discuss.setComment(discussComments);
                    }else {
                        discuss.setComment(discuss1.getComment());
                    }
                    discussComments = discuss1.getComment();
                    AdapterDiscussComment adc = new AdapterDiscussComment(discussComments);
                    listComment.setAdapter(adc);
                    listComment.setLayoutManager(new LinearLayoutManager(DiscussComment.this));
                    totalLike.setText(String.valueOf(discuss.getLike().size()));
                    totalDislike.setText(String.valueOf(discuss.getDislike().size()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setContent() {
        username.setText(discuss.getNamePost());
        title.setText(discuss.getTitle());
        details.setText(discuss.getDetails());
        if (discuss.getLike() == null) {
            listLike = new ArrayList<>();
            discuss.setLike(listLike);
        }
        if (discuss.getDislike() == null) {
            listDislike = new ArrayList<>();
            discuss.setDislike(listDislike);
        }
        if (discuss.getComment() == null) {
            discussComments = new ArrayList<>();
            discuss.setComment(discussComments);
        }else {
            discussComments = discuss.getComment();
        }
        totalLike.setText(String.valueOf(discuss.getLike().size()));
        totalDislike.setText(String.valueOf(discuss.getDislike().size()));
        if (discuss.getIdPic() != null) {
            Picasso.get().load(Uri.parse(discuss.getIdPic())).into(imgView);
        } else {
            Picasso.get().load(R.drawable.game_logo).into(imgView);
        }
    }
    public void addRep(){
        Query dbcheck = FirebaseDatabase.getInstance().getReference("user").orderByChild("email").equalTo(discuss.getNamePost());
        dbcheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        DatabaseReference dbedit = databaseReference.child("user").child(snapshot1.getKey());
                        User user = snapshot1.getValue(User.class);
                        if (Integer.parseInt(totalLike.getText().toString()) % 5 == 0) {
                            int likeTotal = Integer.parseInt(totalLike.getText().toString());
                            user.setReputation(likeTotal / 5);
                        }
                        if (Integer.parseInt(totalDislike.getText().toString()) % 5 == 0){
                            int dislikeTotal = Integer.parseInt(totalDislike.getText().toString());
                            user.setReputation(user.getReputation() - (dislikeTotal / 5));
                        }
                        dbedit.setValue(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void likeEvent() {
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbedit = databaseReference.child("discuss").child(key);
                if (discuss.getDislike().indexOf(MainActivity.user.getEmail()) != -1) {
                    int indexDislike = discuss.getDislike().indexOf(MainActivity.user.getEmail());
                    discuss.getDislike().remove(indexDislike);
                    int indexLike = discuss.getLike().indexOf(MainActivity.user.getEmail());
                    if (indexLike != -1) {
                        discuss.getLike().remove(indexLike);
                    } else {
                        discuss.getLike().add(MainActivity.user.getEmail());
                    }
                } else {
                    int indexLike = discuss.getLike().indexOf(MainActivity.user.getEmail());
                    if (indexLike != -1) {
                        discuss.getLike().remove(indexLike);
                    } else {
                        discuss.getLike().add(MainActivity.user.getEmail());
                    }
                }
                dbedit.setValue(discuss);
            }
        });
    }

    public void dislikeEvent() {
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference dbedit = databaseReference.child("discuss").child(key);
                if (discuss.getLike().indexOf(MainActivity.user.getEmail()) != -1) {
                    int indexLike = discuss.getLike().indexOf(MainActivity.user.getEmail());
                    discuss.getLike().remove(indexLike);
                    int indexDislike = discuss.getDislike().indexOf(MainActivity.user.getEmail());
                    if (indexDislike != -1) {
                        discuss.getDislike().remove(indexLike);
                    } else {
                        discuss.getDislike().add(MainActivity.user.getEmail());
                    }
                } else {
                    int indexDislike = discuss.getDislike().indexOf(MainActivity.user.getEmail());
                    if (indexDislike != -1) {
                        discuss.getDislike().remove(indexDislike);
                    } else {
                        discuss.getDislike().add(MainActivity.user.getEmail());
                    }
                }
                dbedit.setValue(discuss);
            }
        });
    }

    public void writeCommentEvent() {
        writeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(DiscussComment.this);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_writecomment_topic);
                EditText comment = dialog.findViewById(R.id.comment);
                AppCompatButton post = dialog.findViewById(R.id.postComment);
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (comment.getText().toString().equals("")) {
                            Toast.makeText(DiscussComment.this, "Vui lòng nhập ý kiến của bản", Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference dbRef = databaseReference.child("discuss").child(key);
                            discuss.getComment().add(new CommentDiscuss(MainActivity.user.getEmail(), comment.getText().toString()));
                            dbRef.setValue(discuss).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(DiscussComment.this, "Đã bình luận", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
                dialog.show();
            }
        });
    }
}
