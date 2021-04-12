package com.rld.futuro.futuroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import com.rld.futuro.futuroapp.BottomSheets.VolunteerBottomSheet;
import com.rld.futuro.futuroapp.Models.FileManager;
import com.rld.futuro.futuroapp.Models.JSONManager;
import com.rld.futuro.futuroapp.Models.Volunteer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView text;

    ArrayList<Volunteer> volunteers = new ArrayList<>();
    Volunteer volunteer;
    JSONManager jsonManager;
    FileManager fileManager = new FileManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        text = findViewById(R.id.txt);

        ((Button) findViewById(R.id.btnTest))
                .setOnClickListener( v -> {
                    /*VolunteerBottomSheet volunteerBottomSheet = new VolunteerBottomSheet();
                    volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());*/
                    fileManager.deleteFile(getApplicationContext());
                });

        ((Button) findViewById(R.id.btnRead))
                .setOnClickListener( v -> {
                    //Pruebas
                    /*
                    ArrayList<Volunteer> volunteers = new ArrayList<>();
                    Volunteer volunteer;
                    JSONManager jsonManager;
                    FileManager fileManager = new FileManager();*/

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

                    /*jsonManager = new JSONManager();
                    jsonManager.createJSON(volunteers);*/
                    fileManager.saveFile(volunteers, this.getApplicationContext());
                    text.setText(fileManager.getJson().toString());
                    Log.d("TAG1", "Fichero Salvado en: " + getFilesDir());
                });
    }

}