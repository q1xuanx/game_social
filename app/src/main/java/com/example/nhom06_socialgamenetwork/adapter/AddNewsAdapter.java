package com.example.nhom06_socialgamenetwork.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class AddNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Pair<String, String>> list;
    public AddNewsAdapter(List<Pair<String, String>> list){
        this.list = list;
    }
    public void addData(String key,String s){
        list.add(new Pair<String,String>(key,s));
        notifyDataSetChanged();
    }
    public void delData(int pos){
        list.remove(pos);
        notifyDataSetChanged();
    }
    public void editData(int pos, String key, String s){
        Pair<String, String> pair = list.get(pos);
        pair = new Pair<String,String>(key,s);
        list.set(pos,pair);
        notifyDataSetChanged();
    }
    public String getData(int pos){
        return list.get(pos).second;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_add_img, parent, false);
            return new HolderImage(v);
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_add, parent, false);
            return new HolderNews(v);
        }
    }
    @Override
    public int getItemViewType(int position) {
        String key = list.get(position).first;
        if (key.equals("picture")){
            return 1;
        }else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String key = list.get(position).first;
        String value = list.get(position).second;
        if (key.equals("picture")){
            HolderImage imgHold = (HolderImage) holder;
            Picasso.get().load(Uri.parse(value)).into(imgHold.imageView);
        }else {
            HolderNews holderNews = (HolderNews) holder;
            holderNews.txtView.setText(value);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HolderNews extends RecyclerView.ViewHolder{
        TextView txtView;
        public HolderNews(@NonNull View itemView) {
            super(itemView);
            txtView = itemView.findViewById(R.id.textViewDisplay);
        }
    }
    static class HolderImage extends  RecyclerView.ViewHolder{
        ImageView imageView;
        public HolderImage(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgNews);
        }
    }
}
