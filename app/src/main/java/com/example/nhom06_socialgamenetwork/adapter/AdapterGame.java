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
import com.example.nhom06_socialgamenetwork.RecyclerViewInterface;
import com.example.nhom06_socialgamenetwork.models.Game;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterGame extends RecyclerView.Adapter<AdapterGame.HolderImage>{

    RecyclerViewInterface choseItem;
    List<Pair<String,Game>> list;
    public AdapterGame (List<Pair<String,Game>> list, RecyclerViewInterface choseItem){
        this.list = list;
        this.choseItem = choseItem;
    }
    @NonNull
    @Override
    public HolderImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_holder, parent, false);
        return new HolderImage(v,choseItem,list);
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

        public HolderImage(@NonNull View itemView, RecyclerViewInterface choseItem, List<Pair<String,Game>> list) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imageViewName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (choseItem != null){
                        int i = getBindingAdapterPosition();
                        if (i != RecyclerView.NO_POSITION){
                            choseItem.itemClickGame(i,list.get(i).second.getGameType());
                        }
                    }
                }
            });
        }
    }
}
