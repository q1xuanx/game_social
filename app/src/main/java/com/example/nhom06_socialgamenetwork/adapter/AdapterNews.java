package com.example.nhom06_socialgamenetwork.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.R;
import com.example.nhom06_socialgamenetwork.models.News;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.ViewHolder>{

    List<News> list;
    Context context;
    public AdapterNews(List<News> list, Context context){
        this.list = list;
        this.context = context;
    }
    public List<News> tempData(){
        List<News> lst = new ArrayList<>();
        lst.add(new News("https://gamek.mediacdn.vn/133514250583805952/2023/12/15/fakerchovy-6-17026338260671926608548.png","Faker và Chovy có thể lại trở thành đồng đội, nhưng một cái tên khác lại chiếm spotlight"));
        lst.add(new News("https://gamek.mediacdn.vn/133514250583805952/2023/12/15/imagem-2023-12-11-170031585cgwy-1702631537765-1702631537953145132510.jpg","Quá cuồng Attack on Titan, cặp vợ chồng đặt tên con trai là Eren Yeager"));
        lst.add(new News("https://gamek.mediacdn.vn/133514250583805952/2023/12/13/photo-1702440721352-17024407219781223357473.png","Ra mắt siêu tệ, bị đánh giá quá kém, game bom tấn có doanh thu thấp kỷ lục nhưng vẫn bá đạo nhờ điều này"));
        lst.add(new News("https://gamek.mediacdn.vn/133514250583805952/2023/12/15/634d731de9aa2215a9dc4e40-1-1702611197339-17026111975722069379933.jpg","Nhận 5 game miễn phí cực hay từ nền tảng GOG của CD Projekt"));
        lst.add(new News("https://gamek.mediacdn.vn/133514250583805952/2023/12/14/2708dfe2255a1ed95c6f7252538db858-1616-1702549416682-17025494168201204696004.jpg","Tiếp nối Dead Cells, bom tấn roguelike đình đám nhất thập kỷ sắp có mặt trên di động"));
        return lst;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_holder,parent,false);
        return new ViewHolder(view);
    }

    public void addNews(News s){
        list.add(s);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News news = list.get(position);
        Uri uri = Uri.parse(news.getIdPic());
        Picasso.get().load(uri).into(holder.img);
        holder.title.setText(news.getTitle().toString());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_title);
            img = itemView.findViewById(R.id.news_img);
        }
    }
}
