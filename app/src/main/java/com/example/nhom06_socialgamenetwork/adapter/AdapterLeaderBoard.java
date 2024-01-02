package com.example.nhom06_socialgamenetwork.adapter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
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
        holder.setGradientColor(holder.stt, holder.itemView.getResources());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    static class HoldItem extends RecyclerView.ViewHolder {
        TextView stt, name, rep;

        public HoldItem(@NonNull View itemView) {
            super(itemView);
            stt = itemView.findViewById(R.id.rankNumber);
            name = itemView.findViewById(R.id.nameUserRank);
            rep = itemView.findViewById(R.id.reputation);
        }

        private void setGradientColor(TextView textView, Resources resources) {
            int startColor = Color.parseColor("#CC00FF");
            int endColor = Color.parseColor("#3366CC");
            int[] rainbowColors = {resources.getColor(R.color.red),
                    resources.getColor(R.color.orange),
                    resources.getColor(R.color.yellow),
                    resources.getColor(R.color.green),
                    resources.getColor(R.color.blue),
                    resources.getColor(R.color.indigo),
                    resources.getColor(R.color.violet)};

            TextView[] textViews = {stt, name, rep};

            for (TextView tv : textViews) {
                int[] colors = {startColor, endColor};

                float[] positions = {0f,1f};

                if (textView.getText().toString().equals("1")) {
                    Shader textShader = new LinearGradient(0, 0, tv.getPaint().measureText(tv.getText().toString()), tv.getTextSize(),
                            rainbowColors, null, Shader.TileMode.CLAMP);
                    tv.getPaint().setShader(textShader);
                } else {
                    Shader textShader = new LinearGradient(0, 0, tv.getPaint().measureText(tv.getText().toString()), tv.getTextSize(),
                            colors, positions, Shader.TileMode.CLAMP);
                    tv.getPaint().setShader(textShader);
                }
            }
        }
    }
}
