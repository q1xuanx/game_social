package com.example.nhom06_socialgamenetwork;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom06_socialgamenetwork.adapter.AdapterNews;
import com.example.nhom06_socialgamenetwork.adapter.AdapterReadDetails;
import com.example.nhom06_socialgamenetwork.models.News;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.cache.DiskLruCache;

public class ReadDetailsNews extends AppCompatActivity {

    Button backToNews, editNews, buttonChangeAnhBia, buttonThemNoiDung, buttonThemAnhNoiDung;
    TextView titleNews;
    ImageView imgView, imgChose;
    RecyclerView detailsNews;
    FloatingActionButton complete;
    LinearLayout layoutButton, layoutContainButtonAndView;
    News news, temp;
    List<String> list, tempList;
    String idPic = null, idPicThumb = null, idPicTemp = null, titleTemp = null;
    Boolean isClicked = false;
    AdapterReadDetails adapterReadDetails, adapterTemp;
    int index = -1;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_details_news);
        news = new News();
        int isAdmin = 1;
        news.setTitle(getIntent().getStringExtra("title"));
        news.setIdPic(getIntent().getStringExtra("thumnail"));
        news.setPicNews((List<String>) getIntent().getStringArrayListExtra("details"));
        news.setTimePost(getIntent().getStringExtra("time"));
        key = getIntent().getStringExtra("key");
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        backToNews = findViewById(R.id.backBtnDetails);
        editNews = findViewById(R.id.editDetailsNews);
        titleNews = findViewById(R.id.tileDetails);
        detailsNews = findViewById(R.id.recyclerViewDetails);
        imgView = findViewById(R.id.imageViewDetails);
        layoutButton = findViewById(R.id.layoutButton);
        layoutContainButtonAndView = findViewById(R.id.layOutView);
        buttonChangeAnhBia = findViewById(R.id.editAnhBia);
        buttonThemAnhNoiDung = findViewById(R.id.themAnhNoiDung);
        buttonThemNoiDung = findViewById(R.id.themNoiDung);
        editNews = findViewById(R.id.editDetailsNews);
        complete = findViewById(R.id.completeBtn);

        buttonThemAnhNoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ReadDetailsNews.this);
                dialog.setContentView(R.layout.add_img_to_news);
                Button btnfindpic = dialog.findViewById(R.id.chosePicNews);
                Button finishAction = dialog.findViewById(R.id.alertNewsImg);
                imgChose = dialog.findViewById(R.id.imageViewNewsAdd);
                btnfindpic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/");
                        chosePicToAdd.launch(intent);
                    }
                });
                finishAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (idPic != null) {
                            tempList.add(idPic);
                            adapterReadDetails.changeList(tempList);
                            dialog.dismiss();
                        } else {
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
        buttonThemNoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ReadDetailsNews.this);
                dialog.setContentView(R.layout.add_text_to_news);
                EditText edt = dialog.findViewById(R.id.noiDungTinTuc);
                Button btn = dialog.findViewById(R.id.textSuccessAddNews);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edt.getText().toString().equals("")) {
                            Toast.makeText(ReadDetailsNews.this, "Vui lòng nhập đầy đủ tin tức", Toast.LENGTH_SHORT);
                        } else {
                            tempList.add(edt.getText().toString());
                            adapterReadDetails.changeList(tempList);
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });
        buttonChangeAnhBia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/");
                idPicThumb = news.getIdPic();
                changThumnail.launch(intent);
            }
        });
        editNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isClicked) {
                    layoutButton.setVisibility(View.VISIBLE);
                    imgView.setLayoutParams(changeWhenEdit());
                    tempList = list;
                    isClicked = true;
                    editNews.setVisibility(View.GONE);
                    complete.setVisibility(View.VISIBLE);
                    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                            if (viewHolder.itemView.findViewById(R.id.imgViewDisplay).isShown()) {
                                Dialog dialog = new Dialog(ReadDetailsNews.this);
                                dialog.setContentView(R.layout.add_img_to_news);
                                Button addImg = dialog.findViewById(R.id.alertNewsImg);
                                imgChose = dialog.findViewById(R.id.imageViewNewsAdd);
                                Picasso.get().load(Uri.parse(adapterReadDetails.getData(viewHolder.getBindingAdapterPosition()))).into(imgChose);
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
                                        if (idPic != null) {
                                            tempList.set(viewHolder.getBindingAdapterPosition(), idPic);
                                            adapterReadDetails.editData(viewHolder.getBindingAdapterPosition(), idPic);
                                            addImg.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    if (idPic != null) {
                                                        tempList.add(idPic);
                                                        adapterReadDetails.changeList(tempList);
                                                        dialog.dismiss();
                                                        idPic = null;
                                                    } else {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            });
                                            idPic = null;
                                            dialog.dismiss();
                                        } else {
                                            detailsNews.setAdapter(adapterReadDetails);
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                dialog.show();
                            } else {
                                Dialog dialog = new Dialog(ReadDetailsNews.this);
                                dialog.setContentView(R.layout.add_text_to_news);
                                EditText edt = dialog.findViewById(R.id.noiDungTinTuc);
                                Button btn = dialog.findViewById(R.id.textSuccessAddNews);
                                edt.setText(adapterReadDetails.getData(viewHolder.getBindingAdapterPosition()));
                                btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (!edt.getText().equals("")) {
                                            tempList.set(viewHolder.getBindingAdapterPosition(), edt.getText().toString());
                                            adapterReadDetails.editData(viewHolder.getBindingAdapterPosition(), edt.getText().toString());
                                            dialog.dismiss();
                                        } else {
                                            detailsNews.setAdapter(adapterReadDetails);
                                            Toast.makeText(ReadDetailsNews.this, "Vui lòng điền tin tức vào", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                dialog.show();
                            }
                        }
                    }).attachToRecyclerView(detailsNews);
                    titleNews.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Dialog dialog = new Dialog(ReadDetailsNews.this);
                            dialog.setContentView(R.layout.add_text_to_news);
                            EditText edt = dialog.findViewById(R.id.noiDungTinTuc);
                            edt.setText(news.getTitle());
                            Button btn = dialog.findViewById(R.id.textSuccessAddNews);
                            titleTemp = news.getTitle();
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (edt.getText().toString().equals("")) {
                                        Toast.makeText(ReadDetailsNews.this, "Vui lòng nhập đầy đủ tin tức", Toast.LENGTH_SHORT);
                                    } else {
                                        titleTemp = edt.getText().toString();
                                        dialog.dismiss();
                                    }
                                }
                            });
                            dialog.show();
                        }
                    });
                }
            }
        });
        complete.setOnClickListener(new View.OnClickListener() {
            int toltalPic = 0, totalPicNews = 0, totalPicSame = 0;

            @Override
            public void onClick(View view) {
                List<String> urlToDel = new ArrayList<>();
                boolean isDifference = false;
                for (int i = 0; i < news.getPicNews().size(); i++){
                    if (!tempList.get(i).equals(news.getPicNews().get(i))){
                        isDifference = true;
                    }
                }
                if (idPic == news.getIdPic() && titleTemp == news.getTitle() && !isDifference){
                    isDifference = true;
                }
                if (isDifference){
                    imgView.setLayoutParams(changeWhenNotEdit());
                    editNews.setVisibility(View.VISIBLE);
                    complete.setVisibility(View.GONE);
                    idPic = null;
                    tempList = null;
                    return;
                }
                for (int i = 0; i < news.getPicNews().size(); i++) {
                    if (Patterns.WEB_URL.matcher(news.getPicNews().get(i)).matches()) {
                        totalPicNews++;
                        urlToDel.add(news.getPicNews().get(i));
                    }
                }
                totalPicNews = 0;
                for (int i = 0; i < urlToDel.size(); i++){
                    if (tempList.indexOf(urlToDel.get(i)) == -1){
                        StorageReference imgRmv = FirebaseStorage.getInstance().getReferenceFromUrl(urlToDel.get(i));
                        imgRmv.delete();
                    }
                }
                news.setPicNews(tempList);
                if (idPic != null){
                    StorageReference imgRmv = FirebaseStorage.getInstance().getReferenceFromUrl(news.getIdPic());
                    Toast.makeText(ReadDetailsNews.this, news.getIdPic(), Toast.LENGTH_SHORT).show();
                    imgRmv.delete();
                    news.setIdPic(idPic);
                }
                if (titleTemp != null){
                    news.setTitle(titleTemp);
                }
                for (int i = 0; i < news.getPicNews().size(); i++){
                    if (news.getPicNews().get(i).contains("content://")){
                        totalPicNews++;
                        int index = i;
                        Uri uri = Uri.parse(news.getPicNews().get(i));
                        StorageReference imgPush = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(uri));
                        imgPush.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imgPush.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        totalPicNews--;
                                        news.getPicNews().set(index, uri.toString());
                                        if (totalPicNews == 0){
                                            uploadThumnail(news);
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            }
        });
        layoutButton.setVisibility(View.GONE);
        if (!layoutButton.isShown()) {
            imgView.setLayoutParams(changeWhenNotEdit());
            complete.setVisibility(View.GONE);
        }
        backToNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleNews.setText(news.getTitle());
        Picasso.get().load(news.getIdPic()).into(imgView);
        list = news.getPicNews();
        adapterReadDetails = new AdapterReadDetails(list, this);
        detailsNews.setAdapter(adapterReadDetails);
        detailsNews.setLayoutManager(new LinearLayoutManager(this));
    }

    public LinearLayout.LayoutParams changeWhenEdit() {
        LinearLayout.LayoutParams changeGravity = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(320, 170));
        changeGravity.gravity = Gravity.LEFT;
        return changeGravity;
    }

    public ViewGroup.LayoutParams changeWhenNotEdit() {
        LinearLayout.LayoutParams changeGravity = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        changeGravity.gravity = Gravity.CENTER_VERTICAL;
        changeGravity.rightMargin = 60;
        return changeGravity;
    }

    public void uploadThumnail(News news){
        StorageReference imgPush = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(Uri.parse(news.getIdPic())));
        if (Patterns.WEB_URL.matcher(news.getIdPic()).matches()){
            editData(news);
        }else {
            imgPush.putFile(Uri.parse(news.getIdPic())).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgPush.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            news.setIdPic(uri.toString());
                            editData(news);
                        }
                    });
                }
            });
        }
    }
    public void editData(News news){
        DatabaseReference dbedit = databaseReference.child("post").child(key);
        dbedit.setValue(news).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                detailsNews.setLayoutParams(changeWhenNotEdit());
                complete.setVisibility(View.GONE);
                Toast.makeText(ReadDetailsNews.this, "Đã sửa thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ActivityResultLauncher<Intent> acitivityEditHinhAnhNoiDung = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri uri = data.getData();
                if (index != -1) {
                    Picasso.get().load(uri).into(imgChose);
                    idPic = uri.toString();
                    index = -1;
                }
            }
        }
    });
    ActivityResultLauncher<Intent> chosePicToAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                idPic = data.getData().toString();
                if (idPic != null) {
                    Picasso.get().load(Uri.parse(idPic)).into(imgChose);
                }
            }
        }
    });

    ActivityResultLauncher<Intent> changThumnail = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                idPicThumb = data.getData().toString();
                if (idPicThumb != null) {
                    Picasso.get().load(Uri.parse(idPicThumb)).into(imgView);
                }
            }
        }
    });

    private String getFileExtension(Uri uri) {
        ContentResolver ct = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(ct.getType(uri));
    }
}