package com.example.nhom06_socialgamenetwork.adapter;

import android.content.Context;
import android.content.UriMatcher;
import android.media.Image;
import android.net.Uri;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.R;
import com.example.nhom06_socialgamenetwork.models.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterReadDetails extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> list;
    Context context;
    public AdapterReadDetails(List<String> list, Context context){
        this.list = list;
        this.context = context;
    }
    public void changeList(List<String> s){
        this.list = s;
        notifyDataSetChanged();
    }
    public void editData(int pos, String s){
        list.set(pos,s);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        String news = list.get(position);
        if (Patterns.WEB_URL.matcher(news).matches() || news.contains("content://")){
            return 1;
        }else {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_add_img, parent, false);
            return new HolderImagee(v);
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_add, parent, false);
            return new HolderView(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String news = list.get(position);
        if (Patterns.WEB_URL.matcher(news).matches() || news.contains("content://")){
            HolderImagee holderImg = (HolderImagee) holder;
            Picasso.get().load(Uri.parse(news)).into(holderImg.imgView);
        }else {
            HolderView holderView = (HolderView) holder;
            holderView.txtView.setText(news);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getData(int bindingAdapterPosition) {
        return list.get(bindingAdapterPosition);
    }

    public static class HolderView extends RecyclerView.ViewHolder{
        TextView txtView;
        public HolderView(@NonNull View itemView) {
            super(itemView);
            txtView = itemView.findViewById(R.id.textViewDisplay);
        }
    }

    public static class HolderImagee extends RecyclerView.ViewHolder{
        ImageView imgView;
        public HolderImagee(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgNews);
        }
    }
}
