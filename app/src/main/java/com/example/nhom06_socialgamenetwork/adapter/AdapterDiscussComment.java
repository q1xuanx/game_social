package com.example.nhom06_socialgamenetwork.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.R;

public class AdapterDiscussComment extends RecyclerView.Adapter<AdapterDiscussComment.HolderItem>{

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_holder, parent, false);
        return new HolderItem(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
