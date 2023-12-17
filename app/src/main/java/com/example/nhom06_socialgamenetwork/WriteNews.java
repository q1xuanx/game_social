package com.example.nhom06_socialgamenetwork;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.databinding.ActivityMainBinding;
import com.example.nhom06_socialgamenetwork.models.News;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kotlin.OverloadResolutionByLambdaReturnType;

public class WriteNews extends AppCompatActivity {

    Button btnAddText, btnAddImg, anhBia, addTinTucBtn;
    LinearLayout container;
    News news;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ImageView imgTempToAdd, anhBiaAdd;
    String isAdd, idAnhBia;
    TextInputEditText title;
    List<Uri> saveUriPic;
    List<String> getAllIdPic;
    Boolean uploadThumnail = false, uploadPic = false;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_news);
        btnAddImg = findViewById(R.id.ButtonAddImg);
        btnAddText = findViewById(R.id.buttonAddText);
        container = findViewById(R.id.layOutDisplay);
        anhBia = findViewById(R.id.anhBiaPic);
        anhBiaAdd = findViewById(R.id.picNewsAdd);
        addTinTucBtn = findViewById(R.id.addTinTuc);
        title = findViewById(R.id.titleNews);
        progressBar = findViewById(R.id.waitUpload);
        news = new News();
        getAllIdPic = new ArrayList<>();
        saveUriPic = new ArrayList<>();
        progressBar.setVisibility(View.GONE);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        addTinTucBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.toString().equals("") || idAnhBia.equals(null)){
                    Toast.makeText(WriteNews.this, "Có lỗi xảy ra, vui lòng kiểm tra lại", Toast.LENGTH_LONG).show();
                }else {
                    uploadPicToCloud();
                }
            }
        });
        anhBia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                addAnhBiaActivity.launch(intent);
            }
        });
        btnAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(WriteNews.this);
                dialog.setContentView(R.layout.add_text_to_news);
                EditText edt = dialog.findViewById(R.id.noiDungTinTuc);
                Button btn = dialog.findViewById(R.id.textSuccessAddNews);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edt.getText().toString().equals("")){
                            Toast.makeText(WriteNews.this, "Vui lòng nhập đầy đủ tin tức", Toast.LENGTH_SHORT);
                        }else {
                            EditText edttemp = new EditText(WriteNews.this);
                            getAllIdPic.add(edt.getText().toString());
                            edttemp.setText(edt.getText().toString());
                            edttemp.setBackground(getDrawable(R.drawable.del_border));
                            dialog.dismiss();
                            container.addView(edttemp);
                        }
                    }
                });
                dialog.show();
            }
        });
        btnAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(WriteNews.this);
                dialog.setContentView(R.layout.add_img_to_news);
                Button chosePic, successAdd;
                imgTempToAdd = dialog.findViewById(R.id.imageViewNewsAdd);
                chosePic = dialog.findViewById(R.id.chosePicNews);
                successAdd = dialog.findViewById(R.id.alertNewsImg);
                chosePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        addHinhAnhNoiDung.launch(intent);
                    }
                });
                successAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isAdd != null){
                            dialog.dismiss();
                            getAllIdPic.add("PICTURE:" + isAdd.toString());
                            ImageView img = new ImageView(WriteNews.this);
                            img.setImageURI(Uri.parse(isAdd));
                            container.addView(img);
                            isAdd = null;
                        }else {
                            dialog.dismiss();
                            isAdd = null;
                        }
                    }
                });
                dialog.show();
            }
        });
    }
    ActivityResultLauncher<Intent> addHinhAnhNoiDung = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                Uri uri = data.getData();
                Picasso.get().load(uri).into(imgTempToAdd);
                isAdd = uri.toString();
            }
        }
    });
    ActivityResultLauncher<Intent> addAnhBiaActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK){
                Intent data = result.getData();
                Uri uri = data.getData();
                Picasso.get().load(uri).into(anhBiaAdd);
                idAnhBia = uri.toString();
                Toast.makeText(WriteNews.this, "ID anh bia: " + idAnhBia, Toast.LENGTH_LONG).show();
            }
        }
    });
    public String splitString (String s){
        if (s.contains("PICTURE:")){
            String temp = s.substring(8,s.length());
            return temp;
        }
        return null;
    }
    int totalPic = 0;
    public void uploadThumbnail(Uri uri){
        StorageReference imgRef = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
        imgRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        idAnhBia = uri.toString();
                        news.setTitle(title.getText().toString());
                        news.setIdPic(idAnhBia);
                        news.setPicNews(getAllIdPic);
                        Date timePost = new Date();
                        news.setTimePost(String.valueOf(timePost.getTime()));
                        databaseReference.child("post").child(news.getTitle()).setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(WriteNews.this, "Đã thêm thành công", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(WriteNews.this, FragementNews.class);
                                intent.putExtra("Title", news.getTitle());
                                intent.putExtra("Picture", news.getIdPic());
                                intent.putStringArrayListExtra("ListDetail", (ArrayList<String>) news.getPicNews());
                                intent.putExtra("Time", news.getTimePost());
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(WriteNews.this, "Có lỗi trong quá trinh tải ảnh", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadPicToCloud(){
        for (int i = 0; i < getAllIdPic.size(); i++) {
            if (splitString(getAllIdPic.get(i)) != null) {
                Toast.makeText(WriteNews.this, getAllIdPic.get(i), Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(splitString(getAllIdPic.get(i)));
                int index = i;
                totalPic++;
                StorageReference imgRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
                imgRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                getAllIdPic.set(index,uri.toString());
                                totalPic--;
                                if(totalPic==0){
                                    uploadThumbnail(Uri.parse(idAnhBia));
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WriteNews.this, "Có lỗi trong quá trinh tải ảnh", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver ct = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(ct.getType(uri));
    }
}