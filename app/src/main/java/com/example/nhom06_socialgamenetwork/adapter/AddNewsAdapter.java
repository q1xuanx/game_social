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

import java.util.List;
import java.util.Map;

public class AddNewsAdapter extends RecyclerView.Adapter<AddNewsAdapter.HolderNews> {

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
    public HolderNews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_add,parent,false);
        return new HolderNews(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderNews holder, int position) {
        String key = list.get(position).first;
        String value = list.get(position).second;
        if (key.equals("picture")){
            holder.imgView.setImageURI(Uri.parse(value));
            holder.txtView.setVisibility(View.GONE);
        }else {
            holder.txtView.setText(value);
            holder.imgView.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HolderNews extends RecyclerView.ViewHolder{
        ImageView imgView;
        TextView txtView;
        public HolderNews(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgViewDisplay);
            txtView = itemView.findViewById(R.id.textViewDisplay);
        }
    }
}
