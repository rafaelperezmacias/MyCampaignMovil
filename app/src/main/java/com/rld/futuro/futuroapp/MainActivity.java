package com.rld.futuro.futuroapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.rld.futuro.futuroapp.BottomSheets.VolunteerBottomSheet;
import com.rld.futuro.futuroapp.Models.FileManager;
import com.rld.futuro.futuroapp.Models.Volunteer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ArrayList<Volunteer> volunteers;
    private Volunteer volunteer;
    private TextView text;
    private FileManager fileManager;

    private static final int CODE_INTENT_MENU = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        volunteers = new ArrayList<>();

        text = findViewById(R.id.txt);

        ((Button) findViewById(R.id.btnTest))
                .setOnClickListener(v -> {
                    startActivityForResult(new Intent(MainActivity.this, MenuVolunteerActivity.class), CODE_INTENT_MENU);

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_INTENT_MENU) {
            if (resultCode == MenuVolunteerActivity.TAKE_PHOTO) {
                Intent intent = new Intent(this, CameraPreview.class);
                startActivity(intent);
            } else if (resultCode == MenuVolunteerActivity.CAPTURE_MANUAL) {
                VolunteerBottomSheet volunteerBottomSheet = new VolunteerBottomSheet(volunteers, MainActivity.this);
                volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
            }
        }
    }


}