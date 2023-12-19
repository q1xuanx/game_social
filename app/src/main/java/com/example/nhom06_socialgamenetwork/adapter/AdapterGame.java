package com.example.nhom06_socialgamenetwork.adapter;

import android.net.Uri;
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

    List<Game> list;
    public AdapterGame (List<Game> list){
        this.list = tempData();
    }
    public List<Game> tempData(){
        List<Game> temp = new ArrayList<>();
        temp.add(new Game("https://wstatic-prod.pubg.com/web/live/static/og/img-og-pubg.jpg","PlayerUnknown's Battlegrounds",""));
        temp.add(new Game("https://cdn.cloudflare.steamstatic.com/steam/apps/2073850/capsule_616x353.jpg?t=1701120203","The finals", ""));
        temp.add(new Game("https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota2_social.jpg", "Dota2",""));
        return temp;
    }
    @NonNull
    @Override
    public HolderImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_holder, parent, false);
        return new HolderImage(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderImage holder, int position) {
        Game game = list.get(position);
        holder.txtPoint.setText("0");
        holder.txtName.setText(game.getNameGame());
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
            txtName = itemView.findViewById(R.id.nameGame);
            txtPoint = itemView.findViewById(R.id.Points);
        }
    }
}
