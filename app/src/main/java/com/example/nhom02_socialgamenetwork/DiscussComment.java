package com.example.nhom02_socialgamenetwork;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom02_socialgamenetwork.adapter.AdapterDiscussComment;
import com.example.nhom02_socialgamenetwork.models.CommentDiscuss;
import com.example.nhom02_socialgamenetwork.models.Discuss;
import com.example.nhom02_socialgamenetwork.models.User;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DiscussComment extends AppCompatActivity {
    Discuss discuss;
    TextView username, title, details, totalLike, totalDislike;
    ImageView imgView;
    Button like, dislike;
    RecyclerView listComment;
    AppCompatButton writeComment;
    String key, predict;
    DatabaseReference databaseReference;
    List<String> listLike, listDislike;
    List<CommentDiscuss> discussComments;
    FloatingActionButton btnClose;
    Retrofit retrofit;
    CallApiRetrofit callApiRetrofit;
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
        closeTopicEvent();
        delComment();
        retrofit = ApiCall.getClient();
        callApiRetrofit = retrofit.create(CallApiRetrofit.class);
    }

    public void getData(Intent intent) {
        Bundle bundle = intent.getExtras();
        discuss = bundle.getParcelable("discuss");
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
        btnClose = findViewById(R.id.closeTopic);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if (discuss.getNamePost().equals(MainActivity.user.getEmail())) {
            btnClose.setVisibility(View.VISIBLE);
        }
        initComment();
    }

    public void initComment() {
        databaseReference.child("discuss").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                discussComments.clear();
                if (snapshot.exists()) {
                    Discuss discuss1 = snapshot.getValue(Discuss.class);
                    if (discuss1.getComment() == null) {
                        discuss.setComment(discussComments);
                    } else {
                        discuss.setComment(discuss1.getComment());
                    }
                    discussComments = discuss.getComment();
                    AdapterDiscussComment adc = new AdapterDiscussComment(discussComments);
                    discuss.setIsClosed(discuss1.getIsClosed());
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
        } else {
            discussComments = discuss.getComment();
        }
        totalLike.setText(String.valueOf(discuss.getLike().size()));
        totalDislike.setText(String.valueOf(discuss.getDislike().size()));
        if (discuss.getIdPic() != null && !discuss.getIdPic().equals("")) {
            Picasso.get().load(Uri.parse(discuss.getIdPic())).into(imgView);
        } else {
            Picasso.get().load(R.drawable.game_logo).into(imgView);
        }
    }

    public void delComment() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                CommentDiscuss comment = discuss.getComment().get(viewHolder.getBindingAdapterPosition());
                if (comment.getNameUser().equals(MainActivity.user.getEmail()) || MainActivity.user.getIsAdmin() > 0) {
                    AlertDialog.Builder ask = new AlertDialog.Builder(DiscussComment.this);
                    ask.setMessage("Bạn có muốn xóa comment");
                    ask.setNegativeButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DatabaseReference dataDel = databaseReference.child("discuss").child(key);
                            discuss.getComment().remove(viewHolder.getBindingAdapterPosition());
                            dataDel.setValue(discuss).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if (MainActivity.user.getIsAdmin() > 0 && !comment.getNameUser().equals(MainActivity.user.getEmail())) {
                                        Query query = databaseReference.child("user").orderByChild("email").equalTo(comment.getNameUser());
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    User user = new User();
                                                    String key = "";
                                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                        user = snapshot1.getValue(User.class);
                                                        key = snapshot1.getKey();
                                                    }
                                                    if (user.getNoti() == null) {
                                                        user.setNoti(new ArrayList<>());
                                                        user.getNoti().add(getDate()  + ": " + " Comment của bạn đã bị admin xóa do vi phạm tiêu chuẩn vui lòng kiểm tra thùng rác");
                                                    } else {
                                                        user.getNoti().add(getDate()  + ": " + " Comment của bạn đã bị admin xóa do vi phạm tiêu chuẩn vui lòng kiểm tra thùng rác");
                                                    }
                                                    DatabaseReference dataedit = databaseReference.child("user").child(key);
                                                    dataedit.setValue(user);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    Toast.makeText(DiscussComment.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    ask.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            listComment.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                        }
                    });
                    ask.show();
                } else {
                    listComment.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                    Toast.makeText(DiscussComment.this, "Không được xóa bài của user khác", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(listComment);
    }

    public void addRep() {
        Query dbcheck = FirebaseDatabase.getInstance().getReference("user").orderByChild("email").equalTo(discuss.getNamePost());
        dbcheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        DatabaseReference dbedit = databaseReference.child("user").child(snapshot1.getKey());
                        User user = snapshot1.getValue(User.class);
                        if (Integer.parseInt(totalLike.getText().toString()) % 5 == 0) {
                            int likeTotal = Integer.parseInt(totalLike.getText().toString());
                            user.setReputation(user.getReputation() + (likeTotal / 5));
                        }
                        if (Integer.parseInt(totalDislike.getText().toString()) % 5 == 0) {
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
                dbedit.setValue(discuss).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (!MainActivity.user.getEmail().equals(discuss.getNamePost())) {
                            Query query = databaseReference.child("user").orderByChild("email").equalTo(discuss.getNamePost());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        User user = new User();
                                        String key = "";
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            user = snapshot1.getValue(User.class);
                                            key = snapshot1.getKey();
                                            if (user.getNoti() == null) {
                                                user.setNoti(new ArrayList<>());
                                                user.getNoti().add(getDate()  + ": " + discuss.getTitle() + " có thêm tương tác");
                                            } else {
                                                user.getNoti().add(getDate()  + ": " + discuss.getTitle() + " có thêm tương tác");
                                            }
                                        }
                                        DatabaseReference dataedit = databaseReference.child("user").child(key);
                                        dataedit.setValue(user);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });
                addRep();
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
                dbedit.setValue(discuss).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (!MainActivity.user.getEmail().equals(discuss.getNamePost())) {
                            Query query = databaseReference.child("user").orderByChild("email").equalTo(discuss.getNamePost());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        User user = new User();
                                        String key = "";
                                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                            user = snapshot1.getValue(User.class);
                                            key = snapshot1.getKey();
                                            if (user.getNoti() == null) {
                                                user.setNoti(new ArrayList<>());
                                                user.getNoti().add(getDate()  + ": " + discuss.getTitle() + " có thêm tương tác");
                                            } else {
                                                user.getNoti().add(getDate() + ": " + discuss.getTitle() + " có thêm tương tác");
                                            }
                                        }
                                        DatabaseReference dataedit = databaseReference.child("user").child(key);
                                        dataedit.setValue(user);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });
                addRep();
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
                AppCompatButton backPost = dialog.findViewById(R.id.btnBackComment);
                backPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (comment.getText().toString().equals("")) {
                            Toast.makeText(DiscussComment.this, "Vui lòng nhập ý kiến của bản", Toast.LENGTH_SHORT).show();
                        } else {
                            DatabaseReference dbRef = databaseReference.child("discuss").child(key);
                            Call<String> makePredict = callApiRetrofit.predictToxicity(comment.getText().toString());
                            predict = "";
                            makePredict.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    predict = response.body();
                                    if (predict.equals("clean")) {
                                        discuss.getComment().add(new CommentDiscuss(MainActivity.user.getEmail(), comment.getText().toString()));
                                        dbRef.setValue(discuss).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                if (!discuss.getNamePost().equals(MainActivity.user.getEmail())) {
                                                    Query query = databaseReference.child("user").orderByChild("email").equalTo(discuss.getNamePost());
                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                User user = new User();
                                                                String key = "";
                                                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                                    user = snapshot1.getValue(User.class);
                                                                    key = snapshot1.getKey();
                                                                }
                                                                if (user.getNoti() == null) {
                                                                    user.setNoti(new ArrayList<>());
                                                                    user.getNoti().add(getDate() + ": " + "Bạn có thêm bình luận mới ở bài viết " + discuss.getTitle());
                                                                } else {
                                                                    user.getNoti().add(getDate() + ": " + "Bạn có thêm bình luận mới ở bài viết " + discuss.getTitle());
                                                                }
                                                                DatabaseReference dataedit = databaseReference.child("user").child(key);
                                                                dataedit.setValue(user);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                                Toast.makeText(DiscussComment.this, "Đã bình luận", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        });
                                    }else {
                                        Toast.makeText(DiscussComment.this, "Bạn đang mất bình tĩnh, vui lòng kiềm chế", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t) {

                                }
                            });
                        }
                    }
                });
                if (discuss.getIsClosed() == 1) {
                    Toast.makeText(DiscussComment.this, "Topic này đã bị tác giả đóng", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                }
            }
        });
    }

    public void closeTopicEvent() {
        if (discuss.getNamePost().equals(MainActivity.user.getEmail()) || MainActivity.user.getIsAdmin() == 1) {
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference dataEdit = databaseReference.child("discuss").child(key);
                    if (discuss.getIsClosed() == 0) {
                        discuss.setIsClosed(1);
                    } else {
                        discuss.setIsClosed(0);
                    }
                    dataEdit.setValue(discuss).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(DiscussComment.this, "Đã chỉnh sửa topic ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
    public String getDate(){
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return formattedDate;
    }
}
