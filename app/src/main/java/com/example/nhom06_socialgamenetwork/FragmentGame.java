package com.example.nhom06_socialgamenetwork;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.adapter.AdapterGame;
import com.example.nhom06_socialgamenetwork.models.Game;
import com.example.nhom06_socialgamenetwork.models.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentGame extends Fragment implements RecyclerViewInterface {
    RecyclerView recyclerMoba, recyclerFPS, recyclerGacha;
    String typeGame = "GACHA";
    FloatingActionButton buttonAddGame;
    DatabaseReference databaseReference;
    List<Pair<String,Game>> listMoba, listFPS, listGacha;

    public void initRecycler(View v){
        recyclerMoba = v.findViewById(R.id.recyclerMOBA);
        recyclerFPS = v.findViewById(R.id.recyclerFPS);
        recyclerGacha = v.findViewById(R.id.recyclerGACHA);
    }
    public void setItemRecycler(List<Pair<String,Game>> listMoba, List<Pair<String,Game>> listFPS, List<Pair<String,Game>> listGacha){
        AdapterGame adapterFps = new AdapterGame(listFPS,this);
        AdapterGame adapterMoba = new AdapterGame(listMoba,this);
        AdapterGame adapterGacha = new AdapterGame(listGacha,this);
        recyclerMoba.setAdapter(adapterMoba);
        recyclerFPS.setAdapter(adapterFps);
        recyclerGacha.setAdapter(adapterGacha);
        setAllLayout(recyclerGacha,recyclerMoba,recyclerFPS);
    }
    private void setAllLayout(RecyclerView recyclerGacha, RecyclerView recyclerMoba, RecyclerView recyclerFPS) {
        recyclerGacha.setLayoutManager(new LinearLayoutManager(FragmentGame.this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerMoba.setLayoutManager(new LinearLayoutManager(FragmentGame.this.getContext(),LinearLayoutManager.HORIZONTAL, false));
        recyclerFPS.setLayoutManager(new LinearLayoutManager(FragmentGame.this.getContext(),LinearLayoutManager.HORIZONTAL, false));
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_game_layout,container,false);
        listMoba = new ArrayList<>();
        listFPS = new ArrayList<>();
        listGacha = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        initRecycler(v);
        databaseReference.child("game").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listMoba.clear();
                listFPS.clear();
                listGacha.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Game game = snapshot1.getValue(Game.class);
                    if (game.getGameType().equals("GACHA")) {
                        listGacha.add(new Pair<>(snapshot1.getKey(), game));
                    }else if (game.getGameType().equals("MOBA")){
                        listMoba.add(new Pair<>(snapshot1.getKey(), game));
                    }else {
                        listFPS.add(new Pair<>(snapshot1.getKey(), game));
                    }
                }
                setItemRecycler(listMoba, listFPS, listGacha);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        buttonAddGame = v.findViewById(R.id.addGameToRate);
        if (MainActivity.user.getIsAdmin() == 0){
            buttonAddGame.setVisibility(View.GONE);
        }
        buttonAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(FragmentGame.this.getContext());
                dialog.setContentView(R.layout.dialog_add_game);
                EditText nameGame = dialog.findViewById(R.id.tenGame);
                EditText urlImg = dialog.findViewById(R.id.anhGame);
                Button addGame = dialog.findViewById(R.id.themGameDeDanhGia);
                Button BackGame =dialog.findViewById(R.id.btnBackAddGame);
                Spinner spinner = dialog.findViewById(R.id.spinnerAddGame);
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FragmentGame.this.getContext(),R.array.game_type, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                dialog.getWindow().setAttributes(changeSizeOfDialog(dialog));

                BackGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        typeGame = (String) adapterView.getItemAtPosition(i);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        typeGame = "GACHA";
                    }
                });
                addGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (nameGame.getText().toString().equals("") || urlImg.getText().toString().equals("")){
                            Toast.makeText(FragmentGame.this.getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                        }else {
                            Game game = new Game(urlImg.getText().toString(),nameGame.getText().toString(), typeGame);
                            DatabaseReference dataadd = databaseReference.child("game").push();
                            dataadd.setValue(game).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(FragmentGame.this.getContext(),"Đã thêm thành công", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
                dialog.show();
            }
        });
        return v;
    }
    public WindowManager.LayoutParams changeSizeOfDialog(Dialog dialog){
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return layoutParams;
    }
    @Override
    public void onItemClick(int postion) {

    }

    @Override
    public void itemClickGame(int position, String gameType) {
        Intent intent = new Intent(FragmentGame.this.getContext(), ActivityRateGame.class);
        if(gameType.equals("GACHA")){
            intent.putExtra("key",listGacha.get(position).first);
            intent.putExtra("Pic",listGacha.get(position).second.getIdPic());
            intent.putExtra("NameGame",listGacha.get(position).second.getNameGame());
            intent.putExtra("Point",listGacha.get(position).second.getTotalPoint());
        }else if (gameType.equals("FPS")){
            intent.putExtra("key",listFPS.get(position).first);
            intent.putExtra("Pic",listFPS.get(position).second.getIdPic());
            intent.putExtra("NameGame",listFPS.get(position).second.getNameGame());
            intent.putExtra("Point",listFPS.get(position).second.getTotalPoint());
        }else {
            intent.putExtra("key",listMoba.get(position).first);
            intent.putExtra("Pic",listMoba.get(position).second.getIdPic());
            intent.putExtra("NameGame",listMoba.get(position).second.getNameGame());
            intent.putExtra("Point",listMoba.get(position).second.getTotalPoint());
        }
        startActivity(intent);
    }
}
