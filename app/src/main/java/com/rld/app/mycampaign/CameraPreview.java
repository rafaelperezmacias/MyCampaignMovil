package com.rld.app.mycampaign;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CameraPreview extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;

    public static final int RESULT_OK = 1000;
    public static final int RESULT_CAMERA_NOT_PERMISSION = 1001;
    public static final int RESULT_IMAGE_FILE_NOT_CREATE = 1002;
    public static final int RESULT_IMAGE_NOT_TAKEN = 1003;

    private ActivityResultLauncher<Intent> startCameraIntent;

    private String imagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        startCameraIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if ( result.getResultCode() == Activity.RESULT_OK ) {
                        Intent outputPath = new Intent();
                        outputPath.putExtra("imagePath", imagePath);
                        setResult(RESULT_OK, outputPath);
                        finish();
                    } else {
                        setResult(RESULT_IMAGE_NOT_TAKEN);
                        finish();
                    }
                }
        );

        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
    }

    public void checkPermission(String permission, int requestCode) {
        if ( ContextCompat.checkSelfPermission(CameraPreview.this, permission) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(CameraPreview.this, new String[]{ permission }, requestCode);
        } else {
            takePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
            takePhoto();
        } else {
            setResult(RESULT_CAMERA_NOT_PERMISSION);
            finish();
        }
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile;
        try {
            photoFile = createImageFile();
        } catch ( IOException ex ) {
            setResult(RESULT_IMAGE_FILE_NOT_CREATE);
            finish();
            return;
        }
        Uri photoURI = FileProvider.getUriForFile(this, "com.rld.app.mycampaign.provider", photoFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startCameraIntent.launch(takePictureIntent);
    }

    private File createImageFile() throws IOException {
        String imageFileName = UUID.randomUUID().toString();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        imagePath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_IMAGE_NOT_TAKEN);
        super.onBackPressed();
    }

}