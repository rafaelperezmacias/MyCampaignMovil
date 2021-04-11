package com.rld.futuro.futuroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import com.rld.futuro.futuroapp.BottomSheets.VolunteerBottomSheet;
import com.rld.futuro.futuroapp.Models.JSONManager;
import com.rld.futuro.futuroapp.Models.Volunteer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Volunteer> volunteers;
    private Volunteer volunteer;
    private JSONManager jsonManager;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        volunteers = new ArrayList<>();

        text = findViewById(R.id.txt);

        ((Button) findViewById(R.id.btnTest))
                .setOnClickListener( v -> {
                    VolunteerBottomSheet volunteerBottomSheet = new VolunteerBottomSheet(volunteers);
                    volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());

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
    }

}