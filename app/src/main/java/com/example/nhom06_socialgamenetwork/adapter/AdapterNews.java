package com.example.nhom06_socialgamenetwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.FragementNews;
import com.example.nhom06_socialgamenetwork.R;
import com.example.nhom06_socialgamenetwork.ReadDetailsNews;
import com.example.nhom06_socialgamenetwork.RecyclerViewInterface;
import com.example.nhom06_socialgamenetwork.models.News;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder> {

    RecyclerViewInterface recyclerViewInterface;
    List<Pair<String, News>> list;
    public static Context context;

    public AdapterNews(List<Pair<String, News>> list, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.list = list;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_holder, parent, false);
        return new ViewHolder(view, recyclerViewInterface);
    }

    public void addNews(String key, News s) {
        list.add(new Pair<>(key, s));
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = list.get(position).second;
        if(news.getIsDelete() == 0) {
            Uri uri = Uri.parse(news.getIdPic());
            Picasso.get().load(uri).into(holder.img);
            holder.title.setText(news.getTitle().toString());
            holder.timePost.setText(news.getTimePost());
            holder.totalViews.setText(holder.totalViews.getText().toString()+news.getViews());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, timePost, totalViews;
        ImageView img;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            title = itemView.findViewById(R.id.news_title);
            img = itemView.findViewById(R.id.news_img);
            timePost = itemView.findViewById(R.id.timePostNews);
            totalViews = itemView.findViewById(R.id.views);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getBindingAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
