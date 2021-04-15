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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.GET_SECTIONS, null, response -> {
            Log.e("Response", response.toString());
            try {
                ArrayList<Municipality> municipalities = DataTrasform.getMunicipalities(response.getJSONArray("municipalities"));
                if ( municipalities.size() == AppConfig.MUNICIPALITIES_SIZE ) {
                    Log.e("Error", "AS: " + municipalities.size());
                } else {
                    Log.e("Error", "" + municipalities.size());
                }
                for ( Municipality municipality : municipalities ) {
                    Log.e("m", "" + municipality.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                ArrayList<LocalDistrict> localDistricts = DataTrasform.getLocalDistricts(response.getJSONArray("localDistricts"));
                if ( localDistricts.size() == AppConfig.LOCAL_DISTRICTS_SIZE ) {
                    Log.e("Error", "AD: " + localDistricts.size());
                } else {
                    Log.e("Error", "AD: " + localDistricts.size());
                }
                for ( LocalDistrict localDistrict : localDistricts ) {
                    Log.e("l", "" + localDistrict.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                ArrayList<Section> sections = DataTrasform.getSections(response.getJSONArray("sections"));
                if ( sections.size() == AppConfig.SECTIONS_SIZE ) {
                    Log.e("Error", "AF: " + sections.size());
                } else {
                    Log.e("Error", "AF: " + sections.size());
                }
                for ( Section section : sections ) {
                    Log.e("2", "" + section.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            if ( error == null ) {
                return;
            }
            Log.e("Error", "" + error.getMessage());
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);
        /* volunteers = new ArrayList<>();

        text = findViewById(R.id.txt); */



        ((Button) findViewById(R.id.btnTest))
                .setOnClickListener(v -> {
                    // startActivityForResult(new Intent(MainActivity.this, MenuVolunteerActivity.class), CODE_INTENT_MENU);
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
                    */
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