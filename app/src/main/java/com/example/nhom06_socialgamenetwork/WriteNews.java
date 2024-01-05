package com.example.nhom06_socialgamenetwork;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
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

import com.example.nhom06_socialgamenetwork.adapter.AdapterNews;
import com.example.nhom06_socialgamenetwork.adapter.AddNewsAdapter;
import com.example.nhom06_socialgamenetwork.databinding.ActivityMainBinding;
import com.example.nhom06_socialgamenetwork.models.News;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kotlin.OverloadResolutionByLambdaReturnType;

public class WriteNews extends AppCompatActivity {

    Button btnAddText, btnAddImg, anhBia, addTinTucBtn, btnBack;
    RecyclerView container;
    News news;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ImageView imgTempToAdd, anhBiaAdd;
    String isAdd, idAnhBia;
    TextInputEditText title;
    List<Uri> saveUriPic;
    List<String> getAllIdPic;
    ProgressBar progressBar;
    List<Pair<String, String>> listAdapter;
    AddNewsAdapter adapterAddNews;
    int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_news);
        btnAddImg = findViewById(R.id.ButtonAddImg);
        btnAddText = findViewById(R.id.buttonAddText);
        container = findViewById(R.id.container);
        anhBia = findViewById(R.id.anhBiaPic);
        anhBiaAdd = findViewById(R.id.picNewsAdd);
        addTinTucBtn = findViewById(R.id.addTinTuc);
        title = findViewById(R.id.titleNews);
        progressBar = findViewById(R.id.waitUpload);
        btnBack = findViewById(R.id.backToNews);
        news = new News();
        getAllIdPic = new ArrayList<>();
        saveUriPic = new ArrayList<>();
        progressBar.setVisibility(View.GONE);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        listAdapter = new ArrayList<>();
        adapterAddNews = new AddNewsAdapter(listAdapter);
        container.setAdapter(adapterAddNews);
        container.setLayoutManager(new LinearLayoutManager(this));
        idAnhBia = "";
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder ask = new AlertDialog.Builder(WriteNews.this);
                ask.setMessage("Bạn có muốn xóa nội dung này ?");
                ask.setCancelable(false);
                ask.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int position = viewHolder.getBindingAdapterPosition();
                        adapterAddNews.delData(position);
                        getAllIdPic.remove(position);
                    }
                });
                ask.setPositiveButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        container.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                    }
                });
                ask.show();
            }
        }).attachToRecyclerView(container);
        //Chinh sua hinh anh
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder.getItemViewType() == 1) {
                    Dialog dialog = new Dialog(WriteNews.this);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.add_img_to_news);
                    Button addImg = dialog.findViewById(R.id.alertNewsImg);
                    imgTempToAdd = dialog.findViewById(R.id.imageViewNewsAdd);
                    imgTempToAdd.setImageURI(Uri.parse(adapterAddNews.getData(viewHolder.getBindingAdapterPosition())));
                    Button changePic = dialog.findViewById(R.id.chosePicNews);
                    changePic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setType("image/");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            index = viewHolder.getBindingAdapterPosition();
                            acitivityEditHinhAnhNoiDung.launch(intent);
                        }
                    });
                    addImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (isAdd != null) {
                                getAllIdPic.set(viewHolder.getBindingAdapterPosition(), "PICTURE:" + isAdd);
                                adapterAddNews.editData(viewHolder.getBindingAdapterPosition(), "picture", isAdd);
                                addImg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (isAdd != null) {
                                            dialog.dismiss();
                                            getAllIdPic.add("PICTURE:" + isAdd);
                                            adapterAddNews.addData("picture", isAdd.toString());
                                            isAdd = null;
                                        } else {
                                            dialog.dismiss();
                                            isAdd = null;
                                        }
                                    }
                                });
                                isAdd = null;
                                dialog.dismiss();
                            } else {
                                container.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                } else {
                    Dialog dialog = new Dialog(WriteNews.this);
                    dialog.setContentView(R.layout.add_text_to_news);
                    dialog.setCancelable(false);
                    EditText edt = dialog.findViewById(R.id.noiDungTinTuc);
                    Button btn = dialog.findViewById(R.id.textSuccessAddNews);
                    edt.setText(adapterAddNews.getData(viewHolder.getBindingAdapterPosition()));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!edt.getText().equals("")) {
                                getAllIdPic.set(viewHolder.getBindingAdapterPosition(), edt.getText().toString());
                                adapterAddNews.editData(viewHolder.getBindingAdapterPosition(), "text", edt.getText().toString());
                                dialog.dismiss();
                            } else {
                                Toast.makeText(WriteNews.this, "Vui lòng điền tin tức vào", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.show();
                }
            }

        }).attachToRecyclerView(container);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addTinTucBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.toString().equals("") || idAnhBia.equals("") || getAllIdPic.size() == 0) {
                    Toast.makeText(WriteNews.this, "Có lỗi xảy ra, vui lòng kiểm tra lại", Toast.LENGTH_LONG).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
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
                        if (edt.getText().toString().equals("")) {
                            Toast.makeText(WriteNews.this, "Vui lòng nhập đầy đủ tin tức", Toast.LENGTH_SHORT);
                        } else {
                            getAllIdPic.add(edt.getText().toString());
                            adapterAddNews.addData("text", edt.getText().toString());
                            dialog.dismiss();
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
                        if (isAdd != null) {
                            dialog.dismiss();
                            getAllIdPic.add("PICTURE:" + isAdd);
                            adapterAddNews.addData("picture", isAdd.toString());
                            isAdd = null;
                        } else {
                            dialog.dismiss();
                            isAdd = null;
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    ActivityResultLauncher<Intent> acitivityEditHinhAnhNoiDung = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri uri = data.getData();
                isAdd = uri.toString();
                Picasso.get().load(Uri.parse(isAdd)).into(imgTempToAdd);
            }
        }
    });
    ActivityResultLauncher<Intent> addHinhAnhNoiDung = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
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
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri uri = data.getData();
                Picasso.get().load(uri).into(anhBiaAdd);
                idAnhBia = uri.toString();
            }
        }
    });

    public String splitString(@NonNull String s) {
        if (s.contains("PICTURE:")) {
            String temp = s.substring(8, s.length());
            return temp;
        }
        return null;
    }

    int totalPic = 0;

    public void uploadThumbnail(Uri uri) {
        StorageReference imgRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
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
                        news.setTimePost(String.valueOf(Calendar.getInstance().getTime()));
                        DatabaseReference datapush = databaseReference.child("post").push();
                        datapush.setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(WriteNews.this, "Đã thêm thành công", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
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

    private void uploadPicToCloud() {
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
                                getAllIdPic.set(index, uri.toString());
                                totalPic--;
                                if (totalPic == 0) {
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