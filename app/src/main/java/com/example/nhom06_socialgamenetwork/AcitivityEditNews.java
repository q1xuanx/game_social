package com.example.nhom06_socialgamenetwork;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom06_socialgamenetwork.adapter.AddNewsAdapter;
import com.example.nhom06_socialgamenetwork.models.News;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import android.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AcitivityEditNews extends AppCompatActivity {

    String key, uriTemp, uriTempEdit, tempAnhBia;

    DatabaseReference database;
    StorageReference storageReference;
    TextInputEditText title;
    ImageView thumbnail, thumbnailTemp;
    Button chonAnhBia, themNoiDung, themAnhNoiDung, themTinTuc, thoat;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    News news;
    AddNewsAdapter adapter;
    List<Pair<String, String>> listAdapter;
    List<String> contentList, saveURLDelete;
    int totalPics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_news);
        key = getIntent().getStringExtra("key");
        initItem();
        chonAnhBiaEvent();
        themNoiDungEvent();
        themAnhNoiDungEvent();
        themTinTucEvent();
        editAnhNoiDungHoacNoiDung();
        thoatEvent();
        xoaAnhNoiDungHoacNoiDung();
    }

    public void initItem() {
        title = findViewById(R.id.titleNews);
        thumbnail = findViewById(R.id.picNewsAdd);
        chonAnhBia = findViewById(R.id.anhBiaPic);
        themNoiDung = findViewById(R.id.buttonAddText);
        themAnhNoiDung = findViewById(R.id.ButtonAddImg);
        themTinTuc = findViewById(R.id.addTinTuc);
        thoat = findViewById(R.id.backToNews);
        recyclerView = findViewById(R.id.container);
        progressBar = findViewById(R.id.waitUpload);
        progressBar.setVisibility(View.GONE);
        database = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        saveURLDelete = new ArrayList<>();
        totalPics = 0;
        getNews();
    }

    public List<Pair<String, String>> modList(List<String> list) {
        List<Pair<String, String>> temp = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String news1 = list.get(i);
            if (Patterns.WEB_URL.matcher(news1).matches()) {
                temp.add(new Pair<>("picture", news1));
            } else {
                temp.add(new Pair<>("text", news1));
            }
        }
        return temp;
    }

    public void getNews() {
        DatabaseReference getData = database.child("post").child(key);
        news = new News();
        getData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    news = snapshot.getValue(News.class);
                    listAdapter = modList(news.getPicNews());
                    contentList = news.getPicNews();
                    adapter = new AddNewsAdapter(listAdapter);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AcitivityEditNews.this));
                    title.setText(news.getTitle());
                    tempAnhBia = news.getIdPic();
                    Picasso.get().load(Uri.parse(news.getIdPic())).into(thumbnail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void chonAnhBiaEvent() {
        chonAnhBia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(AcitivityEditNews.this);
                dialog.setContentView(R.layout.add_img_to_news);
                Button chonAnh = dialog.findViewById(R.id.chosePicNews), hoantat = dialog.findViewById(R.id.alertNewsImg);
                thumbnailTemp = dialog.findViewById(R.id.imageViewNewsAdd);
                Picasso.get().load(news.getIdPic()).into(thumbnailTemp);
                tempAnhBia = news.getIdPic();
                chonAnh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        layAnhBia.launch(intent);
                    }
                });
                hoantat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.get().load(splitString(news.getIdPic())).into(thumbnail);
                        dialog.dismiss();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }

    ActivityResultLauncher<Intent> layAnhBia = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (!news.getIdPic().equals(data.getData().toString())) {
                    news.setIdPic("picture:" + data.getData().toString());
                }
                Picasso.get().load(splitString(news.getIdPic())).into(thumbnailTemp);
            }
        }
    });

    public void themNoiDungEvent() {
        themNoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(AcitivityEditNews.this);
                dialog.setContentView(R.layout.add_text_to_news);
                EditText editText = dialog.findViewById(R.id.noiDungTinTuc);
                Button hoantat = dialog.findViewById(R.id.textSuccessAddNews);
                hoantat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editText.getText().toString().equals("")) {
                            Toast.makeText(AcitivityEditNews.this, "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
                        } else {
                            adapter.addData("text", editText.getText().toString());
                            news.getPicNews().add(editText.getText().toString());
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    public void themAnhNoiDungEvent() {

        themAnhNoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(AcitivityEditNews.this);
                dialog.setContentView(R.layout.add_img_to_news);
                Button chonAnh = dialog.findViewById(R.id.chosePicNews), hoantat = dialog.findViewById(R.id.alertNewsImg);
                thumbnailTemp = dialog.findViewById(R.id.imageViewNewsAdd);
                chonAnh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        layThemAnhNoiDung.launch(intent);
                    }
                });
                hoantat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (thumbnailTemp.getDrawable() != null) {
                            news.getPicNews().add("picture:" + uriTemp);
                            adapter.addData("picture", uriTemp);
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }

    ActivityResultLauncher<Intent> layThemAnhNoiDung = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                uriTemp = data.getData().toString();
                Picasso.get().load(uriTemp).into(thumbnailTemp);
            }
        }
    });

    public void themTinTucEvent() {
        totalPics = 0;
        themTinTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (news.getPicNews().size() == 0) {
                    Toast.makeText(AcitivityEditNews.this, "Nội dung tin không được trông, cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    AcitivityEditNews.this.finish();
                    return;
                }else if (title.getText().toString().equals("")){
                    Toast.makeText(AcitivityEditNews.this, "Nội dung tin không được trông, cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    return;
                }
                news.setTitle(title.getText().toString());
                for (int i = 0; i < saveURLDelete.size(); i++) {
                    StorageReference deleteImage = FirebaseStorage.getInstance().getReferenceFromUrl(saveURLDelete.get(i));
                    deleteImage.delete();
                }
                int newPicAdd = 0;
                for (int i = 0; i < news.getPicNews().size(); i++) {
                    if (news.getPicNews().get(i).contains("picture:")) {
                        newPicAdd++;
                    }
                }
                if (newPicAdd != 0) {
                    for (int i = 0; i < news.getPicNews().size(); i++) {
                        if (news.getPicNews().get(i).contains("picture:")) {
                            totalPics++;
                            int tempIdx = i;
                            String temp = splitString(news.getPicNews().get(i));
                            StorageReference pushPic = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(Uri.parse(temp)));
                            pushPic.putFile(Uri.parse(temp)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    totalPics--;
                                    pushPic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            news.getPicNews().set(tempIdx, uri.toString());
                                            if (totalPics == 0) {
                                                uploadThumbnail();
                                            }
                                        }
                                    });

                                }
                            });
                        }
                    }
                } else {
                    uploadThumbnail();
                }
            }
        });
    }

    public String splitString(@NonNull String s) {
        if (s.contains("picture:")) {
            String temp = s.substring(8, s.length());
            return temp;
        }
        return null;
    }

    public void uploadThumbnail() {
        if (!tempAnhBia.equals(news.getIdPic()) && Patterns.WEB_URL.matcher(tempAnhBia).matches()) {
            StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(tempAnhBia);
            ref.delete();
        }
        if (news.getIdPic().contains("picture:")) {
            String newAnhBia = splitString(news.getIdPic());
            StorageReference uploadNewImage = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(Uri.parse(newAnhBia)));
            uploadNewImage.putFile(Uri.parse(newAnhBia)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadNewImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            news.setIdPic(uri.toString());
                            progressBar.setVisibility(View.VISIBLE);
                            DatabaseReference editValue = database.child("post").child(key);
                            editValue.setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(AcitivityEditNews.this, "Đã Chỉnh Sửa Tin Tức Thành Công", Toast.LENGTH_SHORT).show();
                                    AcitivityEditNews.this.finish();
                                }
                            });
                        }
                    });

                }
            });
        } else {
            news.setTitle(title.getText().toString());
            progressBar.setVisibility(View.VISIBLE);
            DatabaseReference editValue = database.child("post").child(key);
            editValue.setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AcitivityEditNews.this, "Đã Chỉnh Sửa Tin Tức Thành Công", Toast.LENGTH_SHORT).show();
                    AcitivityEditNews.this.finish();
                }
            });
        }
    }

    public void thoatEvent() {
        thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcitivityEditNews.this.finish();
            }
        });
    }

    public void editAnhNoiDungHoacNoiDung() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder.getItemViewType() == 1) {
                    Dialog dialog = new Dialog(AcitivityEditNews.this);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.add_img_to_news);
                    Button addImg = dialog.findViewById(R.id.alertNewsImg);
                    thumbnailTemp = dialog.findViewById(R.id.imageViewNewsAdd);
                    uriTempEdit = news.getPicNews().get(viewHolder.getBindingAdapterPosition());
                    if (news.getPicNews().get(viewHolder.getBindingAdapterPosition()).contains("picture:")) {
                        String pics = splitString(news.getPicNews().get(viewHolder.getBindingAdapterPosition()));
                        Picasso.get().load(Uri.parse(pics)).into(thumbnailTemp);
                    } else {
                        Picasso.get().load(Uri.parse(news.getPicNews().get(viewHolder.getBindingAdapterPosition()))).into(thumbnailTemp);
                    }
                    Button changePic = dialog.findViewById(R.id.chosePicNews);
                    changePic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setType("image/");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            editHinhAnhNoiHoacNoiDung.launch(intent);
                        }
                    });
                    addImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (thumbnailTemp.getDrawable() != null) {
                                if(Patterns.WEB_URL.matcher(news.getPicNews().get(viewHolder.getBindingAdapterPosition())).matches()){
                                    saveURLDelete.add(news.getPicNews().get(viewHolder.getBindingAdapterPosition()));
                                }
                                news.getPicNews().set(viewHolder.getBindingAdapterPosition(), uriTempEdit);
                                adapter.editData(viewHolder.getBindingAdapterPosition(), "picture", splitString(news.getPicNews().get(viewHolder.getBindingAdapterPosition())));
                                dialog.dismiss();
                            } else {
                                recyclerView.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                                dialog.dismiss();
                            }
                        }
                    });
                    dialog.show();
                } else {
                    Dialog dialog = new Dialog(AcitivityEditNews.this);
                    dialog.setContentView(R.layout.add_text_to_news);
                    dialog.setCancelable(false);
                    EditText edt = dialog.findViewById(R.id.noiDungTinTuc);
                    Button btn = dialog.findViewById(R.id.textSuccessAddNews);
                    edt.setText(news.getPicNews().get(viewHolder.getBindingAdapterPosition()));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!edt.getText().equals("")) {
                                news.getPicNews().set(viewHolder.getBindingAdapterPosition(), edt.getText().toString());
                                adapter.editData(viewHolder.getBindingAdapterPosition(), "text", edt.getText().toString());
                                dialog.dismiss();
                            } else {
                                Toast.makeText(AcitivityEditNews.this, "Vui lòng điền tin tức vào", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog.show();
                }
            }

        }).attachToRecyclerView(recyclerView);
    }

    ActivityResultLauncher<Intent> editHinhAnhNoiHoacNoiDung = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                uriTempEdit = "picture:" + data.getData().toString();
                Picasso.get().load(Uri.parse(splitString(uriTempEdit))).into(thumbnailTemp);
            }
        }
    });

    public void xoaAnhNoiDungHoacNoiDung() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder ask = new AlertDialog.Builder(AcitivityEditNews.this);
                ask.setMessage("Bạn có muốn xóa nội dung này ?");
                ask.setCancelable(false);
                ask.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (news.getPicNews().size() == 1){
                            Toast.makeText(AcitivityEditNews.this, "Không được xóa hết nội dung", Toast.LENGTH_SHORT).show();
                            recyclerView.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                        } else if (news.getPicNews().get(viewHolder.getBindingAdapterPosition()).contains("picture:") || Patterns.WEB_URL.matcher(news.getPicNews().get(viewHolder.getBindingAdapterPosition())).matches()) {
                            saveURLDelete.add(news.getPicNews().get(viewHolder.getBindingAdapterPosition()));
                            news.getPicNews().remove(viewHolder.getBindingAdapterPosition());
                            adapter.delData(viewHolder.getBindingAdapterPosition());
                            Toast.makeText(AcitivityEditNews.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            news.getPicNews().remove(viewHolder.getBindingAdapterPosition());
                            adapter.delData(viewHolder.getBindingAdapterPosition());
                            Toast.makeText(AcitivityEditNews.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ask.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        recyclerView.getAdapter().notifyItemChanged(viewHolder.getBindingAdapterPosition());
                    }
                });
                ask.show();
            }

        }).attachToRecyclerView(recyclerView);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver ct = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(ct.getType(uri));
    }
}
