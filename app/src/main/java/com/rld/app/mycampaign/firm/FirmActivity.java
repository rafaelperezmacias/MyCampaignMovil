package com.rld.app.mycampaign.firm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.databinding.ActivityFirmBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FirmActivity extends AppCompatActivity {

    private ActivityFirmBinding binding;

    private CanvasFirm canvasFirm;
    private MaterialButton btnSave;
    private MaterialButton btnDelete;
    private ImageButton btnClose;

    public static final int RESULT_ACCEPTED_FIRM = 1000;
    public static final int RESULT_REJECT_FIRM = 1001;
    public static final int RESULT_IMAGE_FILE_NOT_CREATE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FuturoApp);
        binding = ActivityFirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            toolbar.setElevation(12.0f);
        }

        canvasFirm = binding.canvas;
        btnSave = binding.btnSave;
        btnDelete = binding.btnDelete;
        btnClose = binding.btnClose;

        canvasFirm.setFirmaActivity(FirmActivity.this);

        btnDelete.setOnClickListener(v -> {
            btnSave.setVisibility(View.INVISIBLE);
            canvasFirm.clear();
        });

        btnSave.setOnClickListener(v -> {
            File image;
            try {
                image = createImageFile();
            } catch (IOException ex) {
                setResult(RESULT_IMAGE_FILE_NOT_CREATE);
                finish();
                return;
            }

            Bitmap bitmap = Bitmap.createBitmap(canvasFirm.getWidth(), canvasFirm.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Drawable background = canvasFirm.getBackground();
            background.draw(canvas);
            canvasFirm.draw(canvas);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bitmapdata = byteArrayOutputStream.toByteArray();

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(image);
                fileOutputStream.write(bitmapdata);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch ( IOException ex ) {
                setResult(RESULT_IMAGE_FILE_NOT_CREATE);
                finish();
                return;
            }

            Intent outputPath = new Intent();
            outputPath.putExtra("imageFirmPath", image.getAbsolutePath());
            setResult(RESULT_ACCEPTED_FIRM, outputPath);
            finish();
        });

        btnClose.setOnClickListener(v -> {
            setResult(RESULT_REJECT_FIRM);
            finish();
        });
    }

    public void showBtnSave() {
        btnSave.setVisibility(View.VISIBLE);
    }

    private File createImageFile() throws IOException {
        String imageFileName = UUID.randomUUID().toString();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_REJECT_FIRM);
        super.onBackPressed();
    }
}