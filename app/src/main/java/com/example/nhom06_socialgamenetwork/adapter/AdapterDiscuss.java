package com.example.nhom06_socialgamenetwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.DiscussComment;
import com.example.nhom06_socialgamenetwork.MainActivity;
import com.example.nhom06_socialgamenetwork.R;
import com.example.nhom06_socialgamenetwork.models.Discuss;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterDiscuss extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Pair<String,Discuss>> list;
    Context context;
    public AdapterDiscuss(List<Pair<String,Discuss>> list, Context context){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_topic_discuss_no_img, parent, false);
            return new HolderNotImage(v);
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_topic_discuss, parent, false);
            return new HolderImage(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Discuss discuss = list.get(position).second;
        if (discuss.getIdPic() == null){
            HolderNotImage holderNotImage = (HolderNotImage) holder;
            holderNotImage.title.setText(discuss.getTitle());
            holderNotImage.username.setText(discuss.getNamePost());
            holderNotImage.clickToDiscuss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,DiscussComment.class);
                    Bundle bundle = new Bundle();
                    bundle.putIntegerArrayList("like", (ArrayList<Integer>) list.get(holder.getBindingAdapterPosition()).second.getLike());
                    bundle.putIntegerArrayList("dislike", (ArrayList<Integer>) list.get(holder.getBindingAdapterPosition()).second.getDislike());
                    bundle.putString("key", list.get(holder.getBindingAdapterPosition()).first);
                    bundle.putString("title",list.get(holder.getBindingAdapterPosition()).second.getTitle());
                    bundle.putString("username", MainActivity.user.getEmail());
                    bundle.putString("details", list.get(holder.getBindingAdapterPosition()).second.getDetails());
                    bundle.putString("idPic", "khong co hinh");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }else {
            HolderImage holderImage = (HolderImage) holder;
            holderImage.title.setText(discuss.getTitle());
            holderImage.username.setText(discuss.getNamePost());
            Picasso.get().load(Uri.parse(discuss.getIdPic())).into(holderImage.imgPic);
            holderImage.clickToDiscuss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,DiscussComment.class);
                    Bundle bundle = new Bundle();
                    bundle.putIntegerArrayList("like", (ArrayList<Integer>) list.get(holder.getBindingAdapterPosition()).second.getLike());
                    bundle.putIntegerArrayList("dislike", (ArrayList<Integer>) list.get(holder.getBindingAdapterPosition()).second.getDislike());
                    bundle.putString("key", list.get(holder.getBindingAdapterPosition()).first);
                    bundle.putString("title",list.get(holder.getBindingAdapterPosition()).second.getTitle());
                    bundle.putString("username", MainActivity.user.getEmail());
                    bundle.putString("details", list.get(holder.getBindingAdapterPosition()).second.getDetails());
                    bundle.putString("idPic", discuss.getIdPic());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        Discuss discuss = list.get(position).second;
        if (discuss.getIdPic() == null) return 1;
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HolderImage extends RecyclerView.ViewHolder{
        TextView username, title, clickToDiscuss;
        ImageView imgPic;
        public HolderImage(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.userNamePost);
            title = itemView.findViewById(R.id.titlePost);
            imgPic = itemView.findViewById(R.id.imageTopic);
            clickToDiscuss = itemView.findViewById(R.id.clickToDiscuss);
        }
    }
    public static class HolderNotImage extends RecyclerView.ViewHolder{
        TextView title, username, clickToDiscuss;
        public HolderNotImage(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titlePostNoImg);
            username = itemView.findViewById(R.id.userNamePostNoImg);
            clickToDiscuss = itemView.findViewById(R.id.clickToDiscussNoImg);
        }
    }
}
