package com.example.nhom06_socialgamenetwork;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.util.Pair;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.adapter.AdapterDiscuss;
import com.example.nhom06_socialgamenetwork.models.Discuss;
import com.example.nhom06_socialgamenetwork.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentDiscuss extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton addTopic;
    Discuss discuss, discuss1;
    ImageView imgViewTopic;
    StorageReference firebaseStorage;
    DatabaseReference databaseReference;
    List<Pair<String, Discuss>> list;
    AdapterDiscuss adapterDiscuss;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discuss, container, false);
        init(v);
        addTopicEvent();
        deleteData();
        editData();
        return v;
    }

    public void init(View v) {
        recyclerView = v.findViewById(R.id.recyclerViewDiscuss);
        addTopic = v.findViewById(R.id.postTopic);
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();
        getDataFromDatabase();
    }

    public void deleteData() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Discuss discuss1 = list.get(viewHolder.getBindingAdapterPosition()).second;
                String key = list.get(viewHolder.getBindingAdapterPosition()).first;
                if (discuss1.getNamePost().equals(MainActivity.user.getEmail()) || MainActivity.user.getIsAdmin() > 0) {
                    DatabaseReference dbedit = databaseReference.child("discuss").child(key);
                    if (discuss1.getIsDelete() == 0) {
                        discuss1.setIsDelete(1);
                    } else discuss1.setIsDelete(0);

                    AlertDialog.Builder ask = new AlertDialog.Builder(FragmentDiscuss.this.getContext());
                    ask.setMessage("Bạn có muốn thêm " + discuss1.getTitle() + " vào thùng rác không?");
                    ask.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbedit.setValue(discuss1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if(MainActivity.user.getIsAdmin() > 0){
                                        Query query = databaseReference.child("user").orderByChild("email").equalTo(discuss1.getNamePost());
                                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                User user = new User();
                                                String key = "";
                                                if (snapshot.exists()){
                                                    for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                                        user = snapshot1.getValue(User.class);
                                                        key = snapshot1.getKey();
                                                    }
                                                    if (user.getNoti() == null){
                                                        user.setNoti(new ArrayList<>());
                                                        user.getNoti().add(getDate()+": " + " Bài viết của bạn đã bị admin xóa do vi phạm tiêu chuẩn vui lòng kiểm tra thùng rác");
                                                    }else {
                                                        user.getNoti().add(getDate()+": " + " Bài viết của bạn đã bị admin xóa do vi phạm tiêu chuẩn vui lòng kiểm tra thùng rác");
                                                    }
                                                    DatabaseReference dataedit = databaseReference.child("user").child(key);
                                                    dataedit.setValue(user);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                    Toast.makeText(FragmentDiscuss.this.getContext(), "Đã thêm vào thùng rác", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    ask.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            recyclerView.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                        }
                    });
                    ask.show();
                } else {
                    recyclerView.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                    Toast.makeText(FragmentDiscuss.this.getContext(), "Không được xóa bài của user khác", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void editData() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                discuss1 = list.get(viewHolder.getBindingAdapterPosition()).second;
                String key = list.get(viewHolder.getBindingAdapterPosition()).first, temp = discuss1.getIdPic();

                if (discuss1.getNamePost().equals(MainActivity.user.getEmail())) {
                    Dialog dialog = new Dialog(FragmentDiscuss.this.getContext());
                    dialog.setContentView(R.layout.dialog_create_topic);
                    dialog.getWindow().setAttributes(changeSizeOfDialog(dialog)); // thay doi size cua topic 
                    TextView userCreate = dialog.findViewById(R.id.nameUserCreateTopic);
                    EditText title = dialog.findViewById(R.id.titleTopic);
                    EditText details = dialog.findViewById(R.id.addDetailsTopic);
                    ImageButton image = dialog.findViewById(R.id.delPic);
                    imgViewTopic = dialog.findViewById(R.id.imageViewTopic);
                    ProgressBar progressBar1 = dialog.findViewById(R.id.progressBar2);
                    Button addTopic = dialog.findViewById(R.id.addTopic);
                    userCreate.setText(MainActivity.user.getEmail());
                    title.setText(discuss1.getTitle());
                    details.setText(discuss1.getDetails());
                    image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Picasso.get().load(R.drawable.img_sample).into(imgViewTopic);
                            discuss1.setIdPic("");
                        }
                    });
                    dialog.setCancelable(false);
                    int ok = 0;
                    if (discuss1.getIdPic() != null && !discuss1.getIdPic().equals("")) {
                        Picasso.get().load(Uri.parse(discuss1.getIdPic())).into(imgViewTopic);
                        ok = 1;
                    }
                    if (ok == 0){
                        imgViewTopic.setImageResource(R.drawable.img_sample);
                    }
                    imgViewTopic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/");
                            suaHinhAnh.launch(intent);
                        }
                    });
                    addTopic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (temp != discuss1.getIdPic() && temp != null && !temp.equals("")){
                                StorageReference fst = FirebaseStorage.getInstance().getReferenceFromUrl(temp);
                                fst.delete();
                            }
                            if (title.getText().toString().equals("") || details.getText().toString().equals("")){
                                Toast.makeText(FragmentDiscuss.this.getContext(), "Vui lòng không để trống thông tin", Toast.LENGTH_SHORT).show();
                            }else {
                                discuss1.setTitle(title.getText().toString());
                                discuss1.setDetails(details.getText().toString());
                                if (discuss1.getIdPic() == null || temp == discuss1.getIdPic() || discuss1.getIdPic().equals("")) {
                                    progressBar1.setVisibility(View.VISIBLE);
                                    DatabaseReference dbEdit = databaseReference.child("discuss").child(key);
                                    dbEdit.setValue(discuss1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(FragmentDiscuss.this.getContext(), "Đã sửa bài thành công", Toast.LENGTH_SHORT).show();
                                            progressBar1.setVisibility(View.GONE);
                                            dialog.dismiss();
                                            recyclerView.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                                        }
                                    });
                                } else {
                                    progressBar1.setVisibility(View.VISIBLE);
                                    StorageReference storageReference = firebaseStorage.child(System.currentTimeMillis() + "." + getFileExtension(Uri.parse(discuss1.getIdPic())));
                                    storageReference.putFile(Uri.parse(discuss1.getIdPic())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    discuss1.setIdPic(uri.toString());

                                                    DatabaseReference dbEdit = databaseReference.child("discuss").child(key);
                                                    dbEdit.setValue(discuss1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            progressBar1.setVisibility(View.GONE);
                                                            Toast.makeText(FragmentDiscuss.this.getContext(), "Đã sửa bài thành công", Toast.LENGTH_SHORT).show();
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
                } else {
                    recyclerView.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                    Toast.makeText(FragmentDiscuss.this.getContext(), "Không được chỉnh sửa bài của user khác", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void addTopicEvent() {
        addTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(FragmentDiscuss.this.getContext());
                dialog.setContentView(R.layout.dialog_create_topic);
                dialog.setCancelable(false);
                discuss = new Discuss();
                TextView userCreate = dialog.findViewById(R.id.nameUserCreateTopic);
                userCreate.setText(MainActivity.user.getEmail());
                dialog.getWindow().setAttributes(changeSizeOfDialog(dialog));
                EditText title = dialog.findViewById(R.id.titleTopic);
                EditText details = dialog.findViewById(R.id.addDetailsTopic);
                imgViewTopic = dialog.findViewById(R.id.imageViewTopic);
                ImageButton image = dialog.findViewById(R.id.delPic);
                ProgressBar progressBar1 = dialog.findViewById(R.id.progressBar2);
                Button addTopic = dialog.findViewById(R.id.addTopic);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.get().load(R.drawable.img_sample).into(imgViewTopic);
                        discuss.setIdPic("");
                    }
                });
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
                        if (title.getText().toString().equals("") || details.getText().toString().equals("")) {
                            Toast.makeText(FragmentDiscuss.this.getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        } else {
                            discuss.setDetails(details.getText().toString());
                            discuss.setTitle(title.getText().toString());
                            discuss.setNamePost(MainActivity.user.getEmail());
                            discuss.setDatePost(String.valueOf(Calendar.getInstance().getTime()));
                            // Neu ko co hinh anh duoc them
                            if (discuss.getIdPic() == null || discuss.getIdPic().equals("")) {
                                progressBar1.setVisibility(View.VISIBLE);
                                DatabaseReference dbAdd = databaseReference.child("discuss").push();
                                dbAdd.setValue(discuss).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        progressBar1.setVisibility(View.GONE);
                                        Toast.makeText(FragmentDiscuss.this.getContext(), "Đăng bài thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                            } else {
                                //Neu co hinh anh duoc them vao
                                StorageReference storageReference = firebaseStorage.child(System.currentTimeMillis() + "." + getFileExtension(Uri.parse(discuss.getIdPic())));
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
                                                        progressBar1.setVisibility(View.GONE);
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

    public WindowManager.LayoutParams changeSizeOfDialog(Dialog dialog) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        return layoutParams;
    }

    ActivityResultLauncher<Intent> themHinhAnh = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                discuss.setIdPic(String.valueOf(data.getData()));
                Picasso.get().load(Uri.parse(discuss.getIdPic())).into(imgViewTopic);
            }
        }
    });
    ActivityResultLauncher<Intent> suaHinhAnh = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                discuss1.setIdPic(String.valueOf(data.getData()));
                Picasso.get().load(Uri.parse(discuss1.getIdPic())).into(imgViewTopic);
            }
        }
    });

    public void getDataFromDatabase() {
        databaseReference.child("discuss").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Discuss discuss1 = snapshot1.getValue(Discuss.class);
                    if (discuss1.getIsDelete() == 0) {
                        list.add(new Pair<>(snapshot1.getKey(), discuss1));
                    }
                }
                adapterDiscuss = new AdapterDiscuss(list, FragmentDiscuss.this.getContext());
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
    public String getDate(){
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        return formattedDate;
    }
}