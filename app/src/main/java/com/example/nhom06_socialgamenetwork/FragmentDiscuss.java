package com.example.nhom06_socialgamenetwork;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.adapter.AdapterDiscuss;
import com.example.nhom06_socialgamenetwork.models.Discuss;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FragmentDiscuss extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton addTopic;
    Discuss discuss;
    ImageView imgViewTopic;
    StorageReference firebaseStorage;
    DatabaseReference databaseReference;
    List<Discuss> list;
    AdapterDiscuss adapterDiscuss;
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
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();
        getDataFromDatabase();
    }
    public void addTopicEvent(){
        addTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(FragmentDiscuss.this.getContext());
                dialog.setContentView(R.layout.dialog_create_topic);
                discuss = new Discuss();
                TextView userCreate = dialog.findViewById(R.id.nameUserCreateTopic);
                userCreate.setText(MainActivity.user.getEmail());
                dialog.getWindow().setAttributes(changeSizeOfDialog(dialog));
                EditText title = dialog.findViewById(R.id.titleTopic);
                EditText details = dialog.findViewById(R.id.addDetailsTopic);
                imgViewTopic = dialog.findViewById(R.id.imageViewTopic);
                TextView nameUserPost = dialog.findViewById(R.id.nameUserCreateTopic);
                Button addTopic = dialog.findViewById(R.id.addTopic);
                imgViewTopic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/");
                        themHinhAnh.launch(intent);
                    }
                });
                addTopic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (title.getText().toString().equals("") || details.getText().toString().equals("")){
                            Toast.makeText(FragmentDiscuss.this.getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        }else {
                            discuss.setDetails(details.getText().toString());
                            discuss.setTitle(details.getText().toString());
                            discuss.setNamePost(MainActivity.user.getEmail());
                            // Neu ko co hinh anh duoc them
                            if (discuss.getIdPic() == null){
                                DatabaseReference dbAdd = databaseReference.child("discuss").push();
                                dbAdd.setValue(discuss).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(FragmentDiscuss.this.getContext(), "Đăng bài thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                            }else {
                                //Neu co hinh anh duoc them vao
                                StorageReference storageReference = firebaseStorage.child(System.currentTimeMillis()+"."+getFileExtension(Uri.parse(discuss.getIdPic())));
                                storageReference.putFile(Uri.parse(discuss.getIdPic())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                discuss.setIdPic(uri.toString());
                                                DatabaseReference dbAdd = databaseReference.child("discuss").push();
                                                dbAdd.setValue(discuss).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(FragmentDiscuss.this.getContext(), "Đăng bài thành công", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
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
                Picasso.get().load(Uri.parse(discuss.getIdPic())).into(imgViewTopic);
            }
        }
    });
    public void getDataFromDatabase(){
        databaseReference.child("discuss").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Discuss discuss1 = snapshot1.getValue(Discuss.class);
                    list.add(discuss1);
                }
                adapterDiscuss = new AdapterDiscuss(list);
                recyclerView.setAdapter(adapterDiscuss);
                recyclerView.setLayoutManager(new LinearLayoutManager(FragmentDiscuss.this.getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver ct = FragmentDiscuss.this.getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(ct.getType(uri));
    }
}