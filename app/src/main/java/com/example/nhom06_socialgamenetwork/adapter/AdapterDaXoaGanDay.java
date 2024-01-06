package com.example.nhom06_socialgamenetwork.adapter;

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
import com.example.nhom06_socialgamenetwork.models.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterDaXoaGanDay extends RecyclerView.Adapter<AdapterDaXoaGanDay.HolderImage> {

    List<Pair<String,News>> list;
    RecyclerViewInterface recyclerViewInterface;

    public AdapterDaXoaGanDay(List<Pair<String,News>> list, RecyclerViewInterface recyclerViewInterface){
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    @NonNull
    @Override
    public HolderImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.qly_holder, parent, false);
        return new HolderImage(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderImage holder, int position) {
        News news = list.get(position).second;
        holder.txtView.setText(news.getTitle());
        Picasso.get().load(news.getIdPic()).into(holder.imgView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HolderImage extends RecyclerView.ViewHolder{
        ImageView imgView;
        TextView txtView;
        public HolderImage(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imageThumbnail);
            txtView = itemView.findViewById(R.id.titleDaXoa);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int i = getBindingAdapterPosition();
                        if (i != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(i);
                        }
                    }
                }
            });

        }
    }
}
