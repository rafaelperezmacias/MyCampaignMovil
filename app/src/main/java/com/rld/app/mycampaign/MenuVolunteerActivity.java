package com.rld.app.mycampaign;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;

public class MenuVolunteerActivity extends AppCompatActivity {

    private AppCompatImageView btnCamera;
    private AppCompatImageView btnCapture;

    public static final int CAPTURE_MANUAL = 2001;
    public static final int TAKE_PHOTO = 2002;
    public static final int CANCEL = 2003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_volunteer);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbar);
        btnCamera = findViewById(R.id.amv_camera_btn);
        TextView txtCamera = findViewById(R.id.amv_takePhoto_txt);
        btnCapture = findViewById(R.id.amv_capture_btn);
        TextView txtCapture = findViewById(R.id.amv_capture_txt);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Drawable unwrappedDrawable = AppCompatResources.getDrawable(MenuVolunteerActivity.this, R.drawable.ic_baseline_arrow_back_24);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(wrappedDrawable);

        toolbar.setNavigationOnClickListener(v -> {
            setResult(CANCEL);
            finish();
        });

        btnCapture.setOnClickListener(v -> {
            setResult(CAPTURE_MANUAL);
            finish();
        });

        btnCamera.setOnClickListener(v -> {
            setResult(TAKE_PHOTO);
            finish();
        });

        txtCamera.setOnClickListener(v -> btnCamera.callOnClick());

        txtCapture.setOnClickListener(v -> btnCapture.callOnClick());
    }

    @Override
    public void onBackPressed() {
        setResult(CANCEL);
        super.onBackPressed();
    }
}