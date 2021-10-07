package com.credit.backup.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.credit.backup.R;
import com.credit.backup.model.ImageHelperClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    private static final int pic_id = 123;
    static final int REQUEST_CODE=164;
    StorageReference mStorageRef;
    DatabaseReference mDatabaseRef;
    String currentPhotoPath;
    // Define the button and imageview type variable

    ImageView click_image_id;
    ProgressDialog progressDialog;
    BitmapDrawable drawable;
    Bitmap bitmap;
    EditText img_name_et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Images");
        dispatchTakePictureIntent();
        Button camera_open_id = (Button)findViewById(R.id.camera_button);
        Button share_btn=findViewById(R.id.img_share_btn);
        img_name_et=findViewById(R.id.img_name_txt);
        click_image_id = (ImageView)findViewById(R.id.click_image);
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                drawable=(BitmapDrawable) click_image_id.getDrawable();
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
        camera_open_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!valid_name())
                {

                    return;
                }
                String img_name=img_name_et.getText().toString().trim();

                File f=new File(currentPhotoPath);

                //Toast.makeText(this, "The Uri is"+Uri.fromFile(f), Toast.LENGTH_SHORT).show();
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                CameraActivity.this.sendBroadcast(mediaScanIntent);
                uploadImagesToFirebase(img_name,contentUri);

                /*StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                drawable=(BitmapDrawable) click_image_id.getDrawable();
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

                 */
            }
        });
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==REQUEST_CODE){
            if (resultCode== Activity.RESULT_OK){

            }
        }
        File f=new File(currentPhotoPath);
        click_image_id.setImageURI(Uri.fromFile(f));
        //Toast.makeText(this, "The Uri is"+Uri.fromFile(f), Toast.LENGTH_SHORT).show();
         //   Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//
         //   Uri contentUri = Uri.fromFile(f);
         //   mediaScanIntent.setData(contentUri);
         //   this.sendBroadcast(mediaScanIntent);
         //  uploadImagesToFirebase(f.getName(),contentUri);


    }


    private void uploadImagesToFirebase(String name, Uri contentUri) {
        progressDialog=new ProgressDialog(CameraActivity.this);
        progressDialog.setMessage("Uploading..");
        progressDialog.show();
        final StorageReference image = mStorageRef.child("pictures/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                        //Picasso.get().load(uri).into(click_image_id);
                    }
                });

                Toast.makeText(CameraActivity.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
                String uploadId = mDatabaseRef.push().getKey();
               // mStorageRef.child("pictures/" + name);
                mStorageRef.child("pictures/").child(name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String downloadUrl = String.valueOf(uri);
                        //Toast.makeText(CameraActivity.this, downloadUrl, Toast.LENGTH_SHORT).show();
                        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        Calendar cal = Calendar.getInstance();
                        String time= dateFormat.format(cal.getTime());
                        ImageHelperClass upload = new ImageHelperClass(uploadId,name,downloadUrl,"",time);
                        mDatabaseRef.child(uploadId).setValue(upload);
                    }
                });

                progressDialog.cancel();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CameraActivity.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });

    }
    private Boolean valid_name(){
        String val=img_name_et.getText().toString().trim();
        if(val.isEmpty()){
            Toast.makeText(this, "Please Enter Image Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }

    }

}