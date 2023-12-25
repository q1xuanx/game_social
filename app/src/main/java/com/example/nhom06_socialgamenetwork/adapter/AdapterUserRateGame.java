package com.example.nhom06_socialgamenetwork.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.R;
import com.example.nhom06_socialgamenetwork.models.GameComment;

import java.util.List;

public class AdapterUserRateGame extends RecyclerView.Adapter<AdapterUserRateGame.HolderItem>{
    List<GameComment> list;
    public AdapterUserRateGame(List<GameComment> list){
        this.list = list;
    }
    public void addComment(GameComment game){
        list.add(game);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_rate_game, parent, false);
        return new HolderItem(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        GameComment gm = list.get(position);
        holder.pointUserGive.setText(String.valueOf(gm.getPoint()));
        holder.nameUser.setText(gm.getNameComment());
        holder.comment.setText(gm.getContent());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HolderItem extends RecyclerView.ViewHolder{
        TextView pointUserGive, nameUser, comment;

        public HolderItem(@NonNull View itemView) {
            super(itemView);
            pointUserGive = itemView.findViewById(R.id.pointGet);
            nameUser = itemView.findViewById(R.id.nameUserRate);
            comment = itemView.findViewById(R.id.commentRateGame);
        }
    }
}
