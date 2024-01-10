package com.example.nhom06_socialgamenetwork.adapter;

import android.app.appsearch.PackageIdentifier;
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
import com.example.nhom06_socialgamenetwork.models.Discuss;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterDaXoaDiscuss extends RecyclerView.Adapter<AdapterDaXoaDiscuss.HolderView> {

    List<Pair<String,Discuss>> list;
    RecyclerViewInterface recyclerViewInterface;
    public AdapterDaXoaDiscuss(List<Pair<String, Discuss>> list, RecyclerViewInterface recyclerViewInterface) {
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public HolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_daxoagandaydiscuss, parent, false);
        return new HolderView(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderView holder, int position) {
        Discuss discuss = list.get(position).second;
        int ok = 0;
        if (discuss.getIdPic() != null && !discuss.getIdPic().equals("")){
            Picasso.get().load(discuss.getIdPic()).into(holder.imgPic);
            ok = 1;
        }
        if (ok == 0){
            holder.imgPic.setImageResource(R.drawable.game_logo);
        }
        holder.txtNamePost.setText(discuss.getNamePost());
        holder.txtDetails.setText(discuss.getDetails());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HolderView extends RecyclerView.ViewHolder{
        TextView txtNamePost, txtDetails;
        ImageView imgPic;
        public HolderView(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            txtNamePost = itemView.findViewById(R.id.namePost);
            txtDetails = itemView.findViewById(R.id.idDetails);
            imgPic = itemView.findViewById(R.id.imgPicTemp);
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
