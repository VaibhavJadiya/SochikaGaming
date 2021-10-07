package com.credit.backup.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.credit.backup.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

public class ImagePreviewActivity extends AppCompatActivity {
    String image_url,name_txt,id_txt,publish_txt,comment_txt;
    BitmapDrawable drawable;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ImageView image_prev=findViewById(R.id.image_preview);
        TextView image_name=findViewById(R.id.image_name);
        TextView image_id=findViewById(R.id.image_id);
        TextView image_publish=findViewById(R.id.image_published);
        TextView image_comment=findViewById(R.id.image_comment);
        Button share_btn=findViewById(R.id.share_btn);
        image_url=getIntent().getStringExtra("image");
        name_txt=getIntent().getStringExtra("name");
        id_txt=getIntent().getStringExtra("id");
        publish_txt=getIntent().getStringExtra("publish");
        comment_txt=getIntent().getStringExtra("comment");
        Picasso.get().load(image_url).into(image_prev);
        String temp;
        temp="Name:"+name_txt;
        image_name.setText(temp);
        temp="Id:"+id_txt;
        image_id.setText(temp);
        temp="Published on:"+publish_txt;
        image_publish.setText(temp);
        temp="Comment:"+comment_txt;
        image_comment.setText(temp);

        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                drawable=(BitmapDrawable) image_prev.getDrawable();
                bitmap=drawable.getBitmap();
                File file=new File((getExternalCacheDir()+"/"+"test"+".jpg"));
                Intent intent;
                try {
                    FileOutputStream outputStream=new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
                    outputStream.flush();
                    outputStream.close();
                    intent=new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                startActivity(Intent.createChooser(intent,"Share Image"));

            }
        });
    }
}