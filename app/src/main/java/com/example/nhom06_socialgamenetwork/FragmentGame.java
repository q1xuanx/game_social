package com.example.nhom06_socialgamenetwork;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.adapter.AdapterGame;
import com.example.nhom06_socialgamenetwork.models.Game;

import java.util.ArrayList;
import java.util.List;

public class FragmentGame extends Fragment {
    RecyclerView recyclerView;
    AdapterGame adapterGame;
    List<Game> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_game_layout,container,false);
        recyclerView = v.findViewById(R.id.gameOverview);
        list = new ArrayList<>();
        adapterGame = new AdapterGame(list);
        recyclerView.setAdapter(adapterGame);
        recyclerView.setLayoutManager(new LinearLayoutManager(FragmentGame.this.getContext()));
        return v;
    }
}
