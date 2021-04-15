package com.rld.futuro.futuroapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rld.futuro.futuroapp.BottomSheets.VolunteerBottomSheet;

import com.rld.futuro.futuroapp.Models.LocalDistrict;
import com.rld.futuro.futuroapp.Models.Municipality;
import com.rld.futuro.futuroapp.Models.Section;
import com.rld.futuro.futuroapp.Models.FileManager;

import com.rld.futuro.futuroapp.Models.Volunteer;
import com.rld.futuro.futuroapp.Request.AppConfig;
import com.rld.futuro.futuroapp.Utils.DataTrasform;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ArrayList<Volunteer> volunteers;
    private TextView text;
    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        volunteers = new ArrayList<>();

        ((Button) findViewById(R.id.btnTest))
                .setOnClickListener(v -> {
                    VolunteerBottomSheet volunteerBottomSheet = new VolunteerBottomSheet(volunteers, MainActivity.this);
                    volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
                });
    }

    public void addVolunteer(Volunteer volunteer) {
        volunteers.add(volunteer);
        for ( Volunteer v : volunteers ) {
            Log.e("Voluntarios: ", "" + v.toString());
        }
    }

}