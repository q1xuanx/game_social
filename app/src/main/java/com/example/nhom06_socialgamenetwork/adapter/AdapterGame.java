package com.example.nhom06_socialgamenetwork.adapter;

import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.R;
import com.example.nhom06_socialgamenetwork.models.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterGame extends RecyclerView.Adapter<AdapterGame.HolderImage>{

    List<Pair<String,Game>> list;
    public AdapterGame (List<Pair<String,Game>> list){
        this.list = list;
    }
    @NonNull
    @Override
    public HolderImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_holder, parent, false);
        return new HolderImage(v);
    }
    @Override
    public void onBindViewHolder(@NonNull HolderImage holder, int position) {
        Game game = list.get(position).second;
        Picasso.get().load(Uri.parse(game.getIdPic())).into(holder.imgView);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HolderImage extends RecyclerView.ViewHolder{

        ImageView imgView;
        TextView txtName, txtPoint;
        public HolderImage(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imageViewName);
        }
    }
}
