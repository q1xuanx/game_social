package com.example.nhom06_socialgamenetwork.adapter;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.R;
import com.example.nhom06_socialgamenetwork.RecyclerViewInterface;
import com.example.nhom06_socialgamenetwork.models.User;

import java.util.List;

public class AdapterQlyUser extends RecyclerView.Adapter<AdapterQlyUser.HolderItem> {

    List<Pair<String,User>> list;

    RecyclerViewInterface recyclerViewInterface;
    public AdapterQlyUser(List<Pair<String, User>> list, RecyclerViewInterface recyclerViewInterface) {
        this.list = list;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_user_qly, parent, false);
        return new HolderItem(v, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        User user1 = list.get(position).second;
        holder.email.setText(user1.getEmail());
        String role = "user", stats = "Hoạt động";
        if (user1.getIsBanned() == 1){
            stats = "Đã bị chặn";
        }
        if (user1.getIsAdmin() == 1){
            role = "admin";
        }else if (user1.getIsAdmin() == 2){
            role = "super admin";
        }
        holder.role.setText(role);
        holder.stats.setText(stats);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HolderItem extends RecyclerView.ViewHolder{
        TextView email, role, stats;
        public HolderItem(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            email = itemView.findViewById(R.id.emailuser1);
            role = itemView.findViewById(R.id.roleuser1);
            stats = itemView.findViewById(R.id.tinhTranguser1);
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
