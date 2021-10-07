package com.credit.backup.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.credit.backup.R;
import com.credit.backup.adapter.ImageAdapter;
import com.credit.backup.api.JsonPlaceHolderApi;
import com.credit.backup.api.Post;
import com.credit.backup.model.ImageHelperClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ArrayList<ImageHelperClass> NameArray;
    ProgressDialog progressDialog;
    ImageAdapter adapter;
    ListView json_lv;
    TextView textViewResult;
    DatabaseReference myRef;
    static final int REQUEST_CODE=144;
    String name,comment,image,id,published;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        json_lv=findViewById(R.id.json_lv);
        ImageView camera_btn=findViewById(R.id.camera_btn);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CameraActivity.class));
            }
        });

        InitalisizeList();
        requestpermission();
        ImageView refresh_img=findViewById(R.id.refresh_img);
        refresh_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Loading Images...");
                progressDialog.show();
                NameArray.clear();
                FirebaseParse();
                Gsonparse();
            }
        });

        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading Images...");
        progressDialog.show();
        FirebaseParse();
        Gsonparse();

        json_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,ImagePreviewActivity.class);
                intent.putExtra("name",NameArray.get(i).getName());
                intent.putExtra("image",NameArray.get(i).getImage());
                intent.putExtra("id",NameArray.get(i).getId());
                intent.putExtra("publish",NameArray.get(i).getPublishedAt());
                intent.putExtra("comment",NameArray.get(i).getComment());
                startActivity(intent);
            }
        });

    }

    private void FirebaseParse() {
        myRef= FirebaseDatabase.getInstance().getReference().child("Images");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Result will be holded Here
               // adapter.notifyDataSetChanged();
                if (snapshot.exists()){
                    //Toast.makeText(getContext(), "Exits", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(getContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                }
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    name=String.valueOf(dsp.child("name").getValue());
                    comment=String.valueOf(dsp.child("comment").getValue());
                    image=String.valueOf(dsp.child("image").getValue());
                    id=String.valueOf(dsp.child("id").getValue());
                    published=String.valueOf(dsp.child("publishedAt").getValue());

                    if (name.equals("null")){

                    }
                    else {
                        //Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();

                        NameArray.add(new ImageHelperClass(id,name,image,comment,published));
                        adapter.notifyDataSetChanged();

                        //Toast.makeText(HistoryActivity.this,names, Toast.LENGTH_SHORT).show(); //add result into array list
                        // Toast.makeText(HistoryActivity.this,dates, Toast.LENGTH_SHORT).show(); //add result into array list
                        // Toast.makeText(HistoryActivity.this,amounts, Toast.LENGTH_SHORT).show(); //add result into array list
                        // Toast.makeText(HistoryActivity.this,numbers, Toast.LENGTH_SHORT).show(); //add result into array list
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void InitalisizeList() {
        NameArray = new ArrayList<ImageHelperClass>();
        adapter=new ImageAdapter(this,R.layout.single_image_item,NameArray);
        json_lv.setAdapter(adapter);
    }


    private void Gsonparse(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonkeeper.com/b/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.isSuccessful()) {

                    textViewResult.setText("Code: " + response.code());
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    String content = "";
                    content += "_id: " + post.getId() + "\n";
                    content += "picture: " + post.getImage() + "\n";
                    content += "title: " + post.getName() + "\n";
                    content += "Published At: " + post.getPublishedAt() + "\n\n";
                    NameArray.add(new ImageHelperClass(post.getId(),post.getName(),post.getImage(),post.getComment(),post.getPublishedAt()));
                    progressDialog.cancel();
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    public void requestpermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)+
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)+
                ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CAMERA)||
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)|
                    ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE);

            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE);
            }

        }
        else {
            //Toast.makeText(this, "Permission Already Found", Toast.LENGTH_SHORT).show();
        }


    }
}