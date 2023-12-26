package com.example.nhom06_socialgamenetwork.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.R;
import com.example.nhom06_socialgamenetwork.models.CommentDiscuss;

import java.util.List;

public class AdapterDiscussComment extends RecyclerView.Adapter<AdapterDiscussComment.HolderItem>{

    List<CommentDiscuss> list;
    public AdapterDiscussComment(List<CommentDiscuss> list){
        this.list = list;
    }
    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_holder, parent, false);
        return new HolderItem(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        CommentDiscuss cd = list.get(position);
        holder.comment.setText(cd.getComment());
        holder.nameUser.setText(cd.getNameUser());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HolderItem extends RecyclerView.ViewHolder{

        TextView nameUser, comment;

        public HolderItem(@NonNull View itemView) {
            super(itemView);
            nameUser = itemView.findViewById(R.id.nameUser);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}
