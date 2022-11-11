package com.rld.app.mycampaign;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.rld.app.mycampaign.models.Volunteer;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CameraPreview extends AppCompatActivity {

    private Volunteer volunteer;

    private static final int CAMERA_PERMISSION_CODE = 100;

    public static final int RESULT_CAMERA_NOT_PERMISSON = 1000;
    public static final int RESULT_IMAGE_FILE_NOT_CREATE = 1001;
    public static final int RESULT_IMAGE_NOT_TAKEN = 1002;

    private ActivityResultLauncher<Intent> startCameraIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        Bundle bundle = getIntent().getBundleExtra("data");
        volunteer = (Volunteer) bundle.getSerializable("volunteer");

        startCameraIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if ( result.getResultCode() == RESULT_OK ) {
                        setPicture();
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
            Toast.makeText(CameraPreview.this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CAMERA_NOT_PERMISSON);
            finish();
        }
    }

    public void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if ( takePictureIntent.resolveActivity(getPackageManager()) != null ) {
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                setResult(RESULT_IMAGE_FILE_NOT_CREATE);
                finish();
                return;
            }
            Uri photoURI = FileProvider.getUriForFile(this, "com.rld.app.mycampaign.provider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startCameraIntent.launch(takePictureIntent);
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = UUID.randomUUID().toString();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void setPicture() {
        try {
            // Bitmap bitmap = BitmapFactory.decodeFile(volunteer.getPathPhoto());
            // volunteer.setImg(bitmap);
            // LISTENER.saveVolunteer(volunteer);
        } catch (Exception ex) {

        }
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_IMAGE_NOT_TAKEN);
        super.onBackPressed();
    }

}