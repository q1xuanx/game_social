package com.example.nhom06_socialgamenetwork;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.adapter.AdapterNews;
import com.example.nhom06_socialgamenetwork.models.Discuss;
import com.example.nhom06_socialgamenetwork.models.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragementNews extends Fragment implements RecyclerViewInterface {

    RecyclerView recyclerView;
    List<Pair<String, News>> list, listTem;
    AdapterNews adapterNews;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        ImageCarousel autoScoll = v.findViewById(R.id.autoScroll);
        recyclerView = v.findViewById(R.id.recyclerView2);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();
        listTem = new ArrayList<>();
        databaseReference.child("post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    News news = snapshot1.getValue(News.class);
                    String key = snapshot1.getKey();
                    if(news.getIsDelete() == 0) {
                        list.add(new Pair<>(key, news));
                    }
                }
                adapterNews = new AdapterNews(list, FragementNews.this.getContext(), FragementNews.this);
                recyclerView.setAdapter(adapterNews);
                recyclerView.setLayoutManager(new LinearLayoutManager(FragementNews.this.getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        autoScoll.registerLifecycle(getLifecycle());
        List<CarouselItem> listImg = new ArrayList<>();
        listImg.add(new CarouselItem("https://assets-prd.ignimgs.com/2023/12/04/tga-1701711888611.png"));
        listImg.add(new CarouselItem("https://pbs.twimg.com/media/F_G4oG3XoAAnIpx.jpg"));
        listImg.add(new CarouselItem("https://i.ytimg.com/vi/Ae89jZYuDg4/maxresdefault.jpg"));
        autoScoll.setData(listImg);
        autoScoll.setAutoPlay(true);
        return v;
    }

    @Override
    public void onItemClick(int postion) {
        Intent intent = new Intent(FragementNews.this.getContext(), ReadDetailsNews.class);
        DatabaseReference databaseReference1 = databaseReference.child("post").child(list.get(postion).first);
        News news = list.get(postion).second;
        news.setViews(news.getViews() + 1);
        databaseReference1.setValue(news, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null){
                    databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                GenericTypeIndicator<ArrayList<String>> typeIndicator = new GenericTypeIndicator<ArrayList<String>>() {
                                };
                                intent.putExtra("title", snapshot.child("title").getValue(String.class));
                                intent.putExtra("thumnail", snapshot.child("idPic").getValue(String.class));
                                ArrayList<String> picNewsList = snapshot.child("picNews").getValue(typeIndicator);
                                intent.putStringArrayListExtra("details", picNewsList);
                                intent.putExtra("time", snapshot.child("timePost").getValue(String.class));
                                intent.putExtra("key", snapshot.getKey());
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void itemClickGame(int position, String gameType) {

    }

    @Override
    public void onResume() {
        super.onResume();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    News news = snapshot1.getValue(News.class);
                    String key = snapshot1.getKey();
                    if(news.getIsDelete() == 0) {
                        list.add(new Pair<>(key, news));
                    }
                }
                adapterNews = new AdapterNews(list, FragementNews.this.getContext(), FragementNews.this);
                recyclerView.setAdapter(adapterNews);
                recyclerView.setLayoutManager(new LinearLayoutManager(FragementNews.this.getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
