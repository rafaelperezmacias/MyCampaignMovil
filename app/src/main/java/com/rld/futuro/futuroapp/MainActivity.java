package com.rld.futuro.futuroapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rld.futuro.futuroapp.BottomSheets.VolunteerBottomSheet;
import com.rld.futuro.futuroapp.Models.JSONManager;
import com.rld.futuro.futuroapp.Request.RequestExample;
import com.rld.futuro.futuroapp.Models.Volunteer;
import com.rld.futuro.futuroapp.Request.AppConfig;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ArrayList<Volunteer> volunteers;
    private Volunteer volunteer;
    private JSONManager jsonManager;
    private TextView text;

    private static final int CODE_INTENT_MENU = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String url =  AppConfig.URL_SERVER + "api/v1/volunteer";
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Log.e("Respuesta", response);
        }, error -> {
            Log.e("Error", error.getMessage());
        });

        RequestExample requestExample = new RequestExample();
        requestExample.setNombres("Rafael");
        requestExample.setApaterno("Android");
        requestExample.setAmaterno("Es lo maximo");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestExample.getJSON(), response -> {
            Log.e("Response", response.toString());
        }, error -> {
            Log.e("Error", error.getMessage());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);

        /* volunteers = new ArrayList<>();

        text = findViewById(R.id.txt);

        ((Button) findViewById(R.id.btnTest))
                .setOnClickListener( v -> {
                    startActivityForResult(new Intent(MainActivity.this, MenuVolunteerActivity.class), CODE_INTENT_MENU);
                    /*volunteer = new Volunteer();
                    volunteer.setNames("Luis");
                    volunteer.setLastNames("Rayas");
                    volunteer.setAddressName("Real de Liliput");
                    volunteers.add(volunteer);

                    volunteer = new Volunteer();
                    volunteer.setNames("Rafael");
                    volunteer.setLastNames("Macias");
                    volunteer.setAddressName("Tona York");
                    volunteers.add(volunteer);

                    volunteer = new Volunteer();
                    volunteer.setNames("Daniel");
                    volunteer.setLastNames("Michel");
                    volunteer.setAddressName("Tortugas ninja Av");
                    volunteers.add(volunteer);

                    jsonManager = new JSONManager();
                    jsonManager.createJSON(volunteers);
                    text.setText(jsonManager.getJson().toString());
                }); */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == CODE_INTENT_MENU ) {
            if ( resultCode == MenuVolunteerActivity.TAKE_PHOTO ) {
                Toast.makeText(this, "TOMAR FOTO", Toast.LENGTH_SHORT).show();
            } else if ( resultCode == MenuVolunteerActivity.CAPTURE_MANUAL ) {
                VolunteerBottomSheet volunteerBottomSheet = new VolunteerBottomSheet(volunteers, MainActivity.this);
                volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
            }
        }
    }
}