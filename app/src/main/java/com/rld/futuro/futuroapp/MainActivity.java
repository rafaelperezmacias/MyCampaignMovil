package com.rld.futuro.futuroapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.rld.futuro.futuroapp.BottomSheets.VolunteerBottomSheet;
import com.rld.futuro.futuroapp.Models.FileManager;
import com.rld.futuro.futuroapp.Models.LocalDistrict;
import com.rld.futuro.futuroapp.Models.Municipality;
import com.rld.futuro.futuroapp.Models.Section;
import com.rld.futuro.futuroapp.Models.Volunteer;
import com.rld.futuro.futuroapp.Request.AppConfig;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CameraPreview.onTakePhotoFinish {

    private Toolbar toolbar;

    private ArrayList<Volunteer> volunteers;

    private FileManager fileManager;
    private RequestQueue requestQueue;

    @Override
    protected void onStart() {
        super.onStart();
        volunteers = new ArrayList<>();
        fileManager = new FileManager(MainActivity.this);
        volunteers = fileManager.readFile(getApplicationContext());
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        CameraPreview.setLISTENER(MainActivity.this);

        ArrayList<Municipality> municipalities = fileManager.readJSONMunicipalities(MainActivity.this);
        ArrayList<LocalDistrict> localDistricts = fileManager.readJSONLocalDistricts(MainActivity.this);
        ArrayList<Section> sections = fileManager.readJSONSections(MainActivity.this);
        if ( municipalities.isEmpty() || municipalities.size() != AppConfig.MUNICIPALITIES_SIZE
            || localDistricts.isEmpty() || localDistricts.size() != AppConfig.LOCAL_DISTRICTS_SIZE
            || sections.isEmpty() || sections.size() != AppConfig.SECTIONS_SIZE ) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, AppConfig.GET_SECTIONS, null, response -> {
                try {
                    if ( response.getInt("code") == 205) {
                        fileManager.createJSONFromDB(response.getJSONArray("municipalities"), "data-municipalities.json", "municipalities", MainActivity.this);
                        fileManager.createJSONFromDB(response.getJSONArray("localDistricts"), "data-localDistricts.json", "localDistricts", MainActivity.this);
                        fileManager.createJSONFromDB(response.getJSONArray("sections"), "data-sections.json", "sections", MainActivity.this);
                    }
                } catch (JSONException ignored) {

                }
            }, error -> {

            });
            requestQueue.add(request);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ( (Button) findViewById(R.id.btnCarga))
                .setOnClickListener(v -> {

                });

        ((Button) findViewById(R.id.btnTest))
                .setOnClickListener(v -> {
                    VolunteerBottomSheet volunteerBottomSheet = new VolunteerBottomSheet(MainActivity.this);
                    volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
                });

    }

    public void addVoluteerWithoutImage(Volunteer volunteer) {
        volunteers.add(volunteer);
        saveVolunteers();
    }

    public void addVolunteerWithImage(Volunteer volunteer) {
        Intent intent = new Intent(MainActivity.this, CameraPreview.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("volutario", volunteer);
        intent.putExtra("data", bundle);
        startActivity(intent);
    }

    public void createSnackBar(String text) {
        SpannableStringBuilder snackbarText = new SpannableStringBuilder();
        snackbarText.append(text);
        snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Snackbar.make(this.findViewById(R.id.am_main_lyt), snackbarText, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.dark_orange_liane))
                .setTextColor(getResources().getColor(R.color.white))
                .show();
    }

    public void saveVolunteers() {
        fileManager.saveFile(volunteers, getApplicationContext());
        createSnackBar(getString(R.string.fbs_snackbar));
        for ( Volunteer v : volunteers ) {
            Log.e("Rafa", "" + v.toString());
        }
    }

    public ArrayList<Section> getSections() {
        return fileManager.readJSONSections(MainActivity.this);
    }

    public ArrayList<Municipality> getMunipalities() {
        return fileManager.readJSONMunicipalities(MainActivity.this);
    }

    public ArrayList<LocalDistrict> getLocalDistricts() {
        return fileManager.readJSONLocalDistricts(MainActivity.this);
    }

    @Override
    public void saveVolunteer(Volunteer volunteer) {
        volunteers.add(volunteer);
        saveVolunteers();
    }

}