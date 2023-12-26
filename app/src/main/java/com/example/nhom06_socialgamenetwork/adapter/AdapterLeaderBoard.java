package com.example.nhom06_socialgamenetwork.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.R;
import com.example.nhom06_socialgamenetwork.models.User;

import java.util.List;

public class AdapterLeaderBoard extends RecyclerView.Adapter<AdapterLeaderBoard.HoldItem>{

    List<User> list;

    public AdapterLeaderBoard(List<User> list){
        this.list = list;
    }

    @NonNull
    @Override
    public HoldItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_leaderboard, parent, false);
        return new HoldItem(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HoldItem holder, int position) {
        User user = list.get(position);
        holder.stt.setText(String.valueOf(position + 1));
        String s[] = user.getEmail().split("@");
        holder.name.setText(s[0]);
        holder.rep.setText(String.valueOf(user.getReputation()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HoldItem extends RecyclerView.ViewHolder{
        TextView stt, name, rep;
        public HoldItem(@NonNull View itemView) {
            super(itemView);
            stt = itemView.findViewById(R.id.rankNumber);
            name = itemView.findViewById(R.id.nameUserRank);
            rep = itemView.findViewById(R.id.reputation);
        }
    }
}
