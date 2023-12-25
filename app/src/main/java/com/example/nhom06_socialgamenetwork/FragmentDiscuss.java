package com.example.nhom06_socialgamenetwork;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhom06_socialgamenetwork.models.Discuss;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class FragmentDiscuss extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton addTopic;
    Discuss discuss;
    int pic = 0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discuss, container, false);
        init(v);
        addTopicEvent();
        return v;
    }
    public void init(View v){
        recyclerView = v.findViewById(R.id.recyclerViewDiscuss);
        addTopic = v.findViewById(R.id.postTopic);
    }
    public void addTopicEvent(){
        addTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(FragmentDiscuss.this.getContext());
                dialog.setContentView(R.layout.dialog_create_topic);
                discuss = new Discuss();
                dialog.getWindow().setAttributes(changeSizeOfDialog(dialog));
                EditText title = dialog.findViewById(R.id.titleTopic);
                EditText details = dialog.findViewById(R.id.addDetailsTopic);
                ImageView imgViewTopic = dialog.findViewById(R.id.imageViewTopic);
                TextView nameUserPost = dialog.findViewById(R.id.nameUserCreateTopic);
                Button addTopic = dialog.findViewById(R.id.addTopic);
                imgViewTopic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/");
                        themHinhAnh.launch(intent);
                        if (pic == 1){
                            Picasso.get().load(Uri.parse(discuss.getIdPic())).into(imgViewTopic);
                            pic = 0;
                        }
                        
                    }
                });
                dialog.show();
            }
        });
    }
    public WindowManager.LayoutParams changeSizeOfDialog(Dialog dialog){
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return layoutParams;
    }
    ActivityResultLauncher<Intent> themHinhAnh = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                discuss.setIdPic(String.valueOf(data.getData()));
                pic = 1;
            }
        }
    });
}