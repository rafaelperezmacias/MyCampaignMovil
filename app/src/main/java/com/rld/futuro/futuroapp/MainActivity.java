package com.rld.futuro.futuroapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rld.futuro.futuroapp.BottomSheets.VolunteerBottomSheet;
import com.rld.futuro.futuroapp.Models.FileManager;
import com.rld.futuro.futuroapp.Models.Volunteer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ArrayList<Volunteer> volunteers;

    private FileManager fileManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        volunteers = new ArrayList<>();

        /*
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            RequestManager rm = new RequestManager();
            JSONObject jsonBD = new JSONObject();

            fileManager.readFile(MainActivity.this);
            jsonBD=fileManager.getJson();
            try {
                JSONArray ja_data = jsonBD.getJSONArray("users");
                int arraySize = ja_data.length();

                for (int i = 0; i < arraySize; i++) {
                    requestQueue.add(rm.request(ja_data.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Error: ", e.getMessage());
            } */

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