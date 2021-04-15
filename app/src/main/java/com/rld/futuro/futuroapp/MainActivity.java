package com.rld.futuro.futuroapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.rld.futuro.futuroapp.BottomSheets.VolunteerBottomSheet;
import com.rld.futuro.futuroapp.Models.FileManager;
import com.rld.futuro.futuroapp.Models.Volunteer;
import com.rld.futuro.futuroapp.Request.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ArrayList<Volunteer> volunteers;

    private FileManager fileManager;
    private RequestQueue requestQueue;

    public static final int CODE_TAKE_IMAGE = 1234;

    @Override
    protected void onStart() {
        super.onStart();
        volunteers = new ArrayList<>();
        fileManager = new FileManager(MainActivity.this);
        volunteers = fileManager.readFile(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        ( (Button) findViewById(R.id.btnCarga))
                .setOnClickListener(v -> {
                    JSONObject jsonBD = new JSONObject();

                    fileManager.readFile(MainActivity.this);
                    jsonBD = fileManager.getJson();
                    if ( jsonBD == null ) {
                        return;
                    }
                    try {
                        JSONArray ja_data = jsonBD.getJSONArray("users");
                        int arraySize = ja_data.length();

                        for (int i = 0; i < arraySize; i++) {
                            JsonObjectRequest request = RequestManager.request(ja_data.getJSONObject(i));
                            if ( request == null ) {
                                continue;
                            }
                            requestQueue.add(request);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Error: ", e.getMessage());
                    }
                });

        ((Button) findViewById(R.id.btnTest))
                .setOnClickListener(v -> {
                    VolunteerBottomSheet volunteerBottomSheet = new VolunteerBottomSheet(volunteers, MainActivity.this);
                    volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
                });

    }

    public void addVoluteerWithoutImage(Volunteer volunteer) {
        volunteers.add(volunteer);
        saveVolunteers();
    }

    public void addVolunteerWithImage(Volunteer volunteer) {
        volunteers.add(volunteer);
        Intent intent = new Intent(MainActivity.this, CameraPreview.class);
        intent.putExtra("voluntario", volunteer);
        startActivityForResult(intent, CODE_TAKE_IMAGE);
    }

    public void saveVolunteers() {
        fileManager.saveFile(volunteers, getApplicationContext());
        SpannableStringBuilder snackbarText = new SpannableStringBuilder();
        snackbarText.append(getString(R.string.fbs_snackbar));
        snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Snackbar.make(this.findViewById(R.id.am_main_lyt), snackbarText, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.dark_orange_liane))
                .setTextColor(getResources().getColor(R.color.white))
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == CODE_TAKE_IMAGE ) {
            if ( resultCode == CameraPreview.RESULT_CAMERA_NOT_PERMISSON ) {

            } else {

            }
            saveVolunteers();
        }
    }
}