package com.rld.app.mycampaign;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.rld.app.mycampaign.bottomsheets.VolunteerBottomSheet;
import com.rld.app.mycampaign.databinding.ActivityMainBinding;
import com.rld.app.mycampaign.dialogs.ProgressDialog;
import com.rld.app.mycampaign.dialogs.ProgressDialogBuilder;
import com.rld.app.mycampaign.files.FederalDistrictFileManager;
import com.rld.app.mycampaign.files.LocalDataFileManager;
import com.rld.app.mycampaign.files.LocalDistrictFileManager;
import com.rld.app.mycampaign.files.MunicipalityFileManager;
import com.rld.app.mycampaign.files.SectionFileManager;
import com.rld.app.mycampaign.files.StateFileManager;
import com.rld.app.mycampaign.files.VolunteerFileManager;
import com.rld.app.mycampaign.firm.FirmActivity;
import com.rld.app.mycampaign.fragments.menu.VolunteerFragment;
import com.rld.app.mycampaign.models.Image;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.secrets.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements VolunteerFragment.OnClickAddVolunteer {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private ActivityResultLauncher<Intent> startCameraPreviewIntent;
    private ActivityResultLauncher<Intent> startFirmActivityIntent;
    
    private VolunteerBottomSheet volunteerBottomSheet;
    private ProgressDialog progressDialog;

    private Volunteer currentVolunteer;

    private RequestQueue requestQueue;

    private ArrayList<Volunteer> localVolunteers;
    private ArrayList<Volunteer> remoteVolunteers;

    private VolunteerFragment volunteerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localVolunteers = VolunteerFileManager.readJSON(true, MainActivity.this);
        remoteVolunteers = VolunteerFileManager.readJSON(false, MainActivity.this);
        VolunteerFragment.setOnClickAddVolunteer(MainActivity.this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;


        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_volunteer, R.id.nav_profile)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        startCameraPreviewIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    switch ( result.getResultCode() ) {
                        case CameraPreview.RESULT_OK: {
                            if ( result.getData() != null ) {
                                String path = result.getData().getStringExtra("imagePath");
                                currentVolunteer = new Volunteer();
                                initializeImageOfVolunteer(currentVolunteer, path, true);
                                createFormToCreateVolunteer();
                            }
                        } break;
                        case CameraPreview.RESULT_CAMERA_NOT_PERMISSION: {
                            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                        } break;
                        case CameraPreview.RESULT_IMAGE_NOT_TAKEN: {
                            Toast.makeText(this, "Fotografia no tomada", Toast.LENGTH_SHORT).show();
                        } break;
                        case CameraPreview.RESULT_IMAGE_FILE_NOT_CREATE: {
                            Toast.makeText(this, "La fotografia no se pudo crear", Toast.LENGTH_SHORT).show();
                        } break;
                    }
                }
        );

        startFirmActivityIntent = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    switch ( result.getResultCode() ) {
                        case FirmActivity.RESULT_ACCEPTED_FIRM: {
                            if ( result.getData() != null ) {
                                String path = result.getData().getStringExtra("imageFirmPath");
                                initializeImageOfVolunteer(currentVolunteer, path, false);
                                addLocalVolunteer();
                            }
                        } break;
                        case FirmActivity.RESULT_REJECT_FIRM: {
                            Toast.makeText(this, "No se firmo", Toast.LENGTH_SHORT).show();
                        } break;
                        case FirmActivity.RESULT_IMAGE_FILE_NOT_CREATE: {
                            Toast.makeText(this, "Ocurrion un error, intentlo de nuevo", Toast.LENGTH_SHORT).show();
                        } break;
                    }
                }
        );

        boolean localDataSaved = getSharedPreferences("localData", Context.MODE_PRIVATE).getBoolean("saved", false);
        if ( !localDataSaved ) {
            downloadDataOfSections();
        } else {
            // TODO uptade en un service (background)
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void initializeVolunteersList(VolunteerFragment volunteerFragment) {
        this.volunteerFragment = volunteerFragment;
        volunteerFragment.updateVolunteers(volunteersToRecyclerView());
    }

    @Override
    public void addVolunteerFragment() {
        startCameraPreviewIntent.launch(new Intent(MainActivity.this, CameraPreview.class));
    }

    private void addLocalVolunteer() {
        localVolunteers.add(currentVolunteer);
        VolunteerFileManager.writeJSON(localVolunteers, true, MainActivity.this);
        volunteerBottomSheet.dismiss();
        if ( volunteerFragment != null ) {
            volunteerFragment.updateVolunteers(volunteersToRecyclerView());
        }
        SpannableStringBuilder snackbarText = new SpannableStringBuilder();
        snackbarText.append("Voluntario registrado con éxito");
        snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Snackbar.make(binding.getRoot(), snackbarText, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.blue))
                .setTextColor(getResources().getColor(R.color.light_white))
                .setAction("DETALLES", view -> {

                })
                .setActionTextColor(getResources().getColor(R.color.white))
                .show();
    }

    private void initializeImageOfVolunteer(Volunteer volunteer, String path, boolean credential) {
        Image image = new Image();
        image.setPath(path);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        image.setBlob(bitmap);
        if ( credential ) {
            volunteer.setImageCredential(image);
        } else {
            volunteer.setImageFirm(image);
        }
    }

    public void firmActivityForVolunteer() {
        startFirmActivityIntent.launch(new Intent(MainActivity.this, FirmActivity.class));
    }

    private void downloadDataOfSections() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, AppConfig.GET_SECTIONS, null, response -> {
            try {
                if ( response.getInt("code") == 200 ) {
                    StateFileManager.writeJSON(response.getJSONArray("states"), MainActivity.this);
                    JSONObject state = response.getJSONObject("state");
                    getSharedPreferences("localData", MODE_PRIVATE).edit().putInt("state_id", state.getInt("id")).apply();
                    getSharedPreferences("localData", MODE_PRIVATE).edit().putString("state_name", state.getString("name")).apply();
                    FederalDistrictFileManager.writeJSON(state.getJSONArray("federal_districts"), state.getInt("id"), MainActivity.this);
                    LocalDistrictFileManager.writeJSON(state.getJSONArray("local_districts"), state.getInt("id"), MainActivity.this);
                    MunicipalityFileManager.writeJSON(state.getJSONArray("municipalities"), state.getInt("id"), MainActivity.this);
                    SectionFileManager.writeJSON(state.getJSONArray("sections"), MainActivity.this);
                }
            } catch ( JSONException ex ) {
                
            }
        }, error -> {
            Log.e("Error", "" + error.getMessage());
        });
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    private void createFormToCreateVolunteer() {
        ProgressDialogBuilder builder = new ProgressDialogBuilder()
                .setTitle("Cargando datos locales")
                .setCancelable(false);
        progressDialog = new ProgressDialog(MainActivity.this, builder);
        progressDialog.show();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                LocalDataFileManager localDataFileManager = LocalDataFileManager.getInstance(MainActivity.this);
                volunteerBottomSheet = new VolunteerBottomSheet(currentVolunteer, MainActivity.this, localDataFileManager);
                volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 100);
    }

    public void hideProgressDialog() {
        if ( progressDialog != null ) {
            progressDialog.dismiss();
        }
    }

    public ArrayList<Volunteer> volunteersToRecyclerView() {
        ArrayList<Volunteer> volunteers = new ArrayList<>();
        volunteers.addAll(remoteVolunteers);
        volunteers.addAll(localVolunteers);
        return volunteers;
    }

}