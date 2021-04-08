package com.rld.futuro.futuroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.rld.futuro.futuroapp.BottomSheets.VolunteerBottomSheet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.btnTest))
                .setOnClickListener( v -> {
                    VolunteerBottomSheet volunteerBottomSheet = new VolunteerBottomSheet();
                    volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
                });
    }
}