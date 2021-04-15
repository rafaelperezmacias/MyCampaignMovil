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
import android.widget.Toast;


import com.rld.futuro.futuroapp.BottomSheets.VolunteerBottomSheet;
import com.rld.futuro.futuroapp.Models.FileManager;
import com.rld.futuro.futuroapp.Models.Volunteer;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.android.volley.toolbox.Volley;
import com.rld.futuro.futuroapp.Request.RequestManager;
import com.android.volley.RequestQueue;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private ArrayList<Volunteer> volunteers;
    private Volunteer volunteer;
    private FileManager fileManager;
    private TextView text;
    private Button btnGuardar, btnBorrar;

    private static final int CODE_INTENT_MENU = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        volunteers = new ArrayList<>();
        fileManager = new FileManager();

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
                    text.setText(jsonManager.getJson().toString()); */
                });

        btnBorrar = findViewById(R.id.btnBorrar);
        btnBorrar.setOnClickListener(v -> {
            volunteers = fileManager.readFile(getApplicationContext());
            Log.d("TAG1", volunteers.toString());
        });

        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(v -> {
            volunteer = new Volunteer();
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

            fileManager.saveFile(volunteers, getApplicationContext());
        });

        btnUploadLocalBD = findViewById(R.id.btnUploadLocalBD);
        btnGuardar.setOnClickListener(v -> {
            //TODO: Falta validar el estado de la red, para seguir o dar mensaje de error

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
            }
        });
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