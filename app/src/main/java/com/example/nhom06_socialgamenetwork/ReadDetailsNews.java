package com.example.nhom06_socialgamenetwork;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.adapter.AdapterNews;
import com.example.nhom06_socialgamenetwork.adapter.AdapterReadDetails;
import com.example.nhom06_socialgamenetwork.models.News;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.cache.DiskLruCache;

public class ReadDetailsNews extends AppCompatActivity {

    ImageButton backToNews;
    TextView titleNews;
    ImageView imgView;
    RecyclerView detailsNews;
    News news;
    List<String> list;
    AdapterReadDetails adapterReadDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_details_news);
        news = new News();
        news.setTitle(getIntent().getStringExtra("title"));
        news.setIdPic(getIntent().getStringExtra("thumnail"));
        news.setPicNews((List<String>) getIntent().getStringArrayListExtra("details"));
        news.setTimePost(getIntent().getStringExtra("time"));
        String key = getIntent().getStringExtra("key");

        backToNews = findViewById(R.id.backBtnDetails);
        titleNews = findViewById(R.id.tileDetails);
        detailsNews = findViewById(R.id.recyclerViewDetails);
        imgView = findViewById(R.id.imageViewDetails);
        backToNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleNews.setText(news.getTitle());
        Picasso.get().load(news.getIdPic()).into(imgView);
        list = news.getPicNews();
        adapterReadDetails = new AdapterReadDetails(list, this);
        detailsNews.setAdapter(adapterReadDetails);
        detailsNews.setLayoutManager(new LinearLayoutManager(this));
    }

}