package com.example.nhom06_socialgamenetwork.adapter;

import android.content.Context;
import android.content.UriMatcher;
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

public class AdapterReadDetails extends RecyclerView.Adapter<AdapterReadDetails.HolderView> {

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
    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_news_add,parent,false);
        return new HolderView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderView holder, int position) {
        String news = list.get(position);
        if (Patterns.WEB_URL.matcher(news).matches() || news.contains("content://")){
            Picasso.get().load(Uri.parse(news)).into(holder.imgView);
            holder.txtView.setVisibility(View.GONE);
        }else {
            holder.txtView.setText(news);
            holder.imgView.setVisibility(View.GONE);
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
        ImageView imgView;
        TextView txtView;
        public HolderView(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgViewDisplay);
            txtView = itemView.findViewById(R.id.textViewDisplay);
        }
    }
}
