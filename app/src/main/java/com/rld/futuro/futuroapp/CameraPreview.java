package com.rld.futuro.futuroapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rld.futuro.futuroapp.Models.Volunteer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;

public class CameraPreview extends AppCompatActivity {

    private ImageView imageView;
    private Button btn;
    private Volunteer volunteer;


    private final int REQUEST_IMAGE_CAPTURE = 0;
    private final int REQUEST_TAKE_PHOTO = 1;
    private static final int CAMERA_PERMISSION_CODE = 100;

    public static final int RESULT_CAMERA_NOT_PERMISSON = 6666;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        imageView = findViewById(R.id.imageView);
        btn = findViewById(R.id.btnTomarFoto);
        btn.setOnClickListener(v -> {
            volunteer.deleteImage();
        });

        volunteer = (Volunteer) getIntent().getSerializableExtra("voluntario");
        if ( volunteer != null ) {
            Log.e("voluntario", "" + volunteer.toString());
        }

        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                //Bitmap image = (Bitmap) data.getExtras().get("data");
                //imageView.setImageBitmap(image);
                setPic();
                break;
            case RESULT_CANCELED:
                break;
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        /*int targetW = 1;
        int targetH = 1;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;*/

        Bitmap bitmap = BitmapFactory.decodeFile(volunteer.getPathPhoto());
        volunteer.setImg(bitmap);
        imageView.setImageBitmap(volunteer.getImg());
    }



    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(CameraPreview.this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(CameraPreview.this,
                    new String[]{permission},
                    requestCode);
        } else {
            //Toast.makeText(CameraPreview.this, "Permiso de camara concedido", Toast.LENGTH_SHORT).show();
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePhoto();
        } else {
            Toast.makeText(CameraPreview.this, "Permiso de Camara Denegado", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CAMERA_NOT_PERMISSON);
            finish();
        }
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(this.volunteer);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                                                      "com.rld.futuro.futuroapp.provider",
                                                      photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile(Volunteer volunteer) throws IOException {
        // Create an image file name
        String imageFileName = "IMG_" +  volunteer.getElectorKey() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        volunteer.setPathPhoto(image.getAbsolutePath());
        return image;
    }
}