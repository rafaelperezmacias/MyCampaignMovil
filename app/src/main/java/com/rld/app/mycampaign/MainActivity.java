package com.rld.app.mycampaign;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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
import com.rld.app.mycampaign.bottomsheets.VolunteerBottomSheet;
import com.rld.app.mycampaign.databinding.ActivityMainBinding;
import com.rld.app.mycampaign.firm.FirmActivity;
import com.rld.app.mycampaign.fragments.menu.VolunteerFragment;
import com.rld.app.mycampaign.models.Image;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.secrets.AppConfig;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements VolunteerFragment.OnClickAddVolunteer {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private ActivityResultLauncher<Intent> startCameraPreviewIntent;
    private ActivityResultLauncher<Intent> startFirmActivityIntent;
    
    private VolunteerBottomSheet volunteerBottomSheet;

    private Volunteer currentVolunteer;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        VolunteerFragment.setOnClickAddVolunteer(MainActivity.this);

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
                                volunteerBottomSheet = new VolunteerBottomSheet(currentVolunteer, MainActivity.this);
                                volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
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
                                volunteerBottomSheet.dismiss();
                            }
                        } break;
                        case FirmActivity.RESULT_REJECT_FIRM: {

                        } break;
                        case FirmActivity.RESULT_IMAGE_FILE_NOT_CREATE: {
                            Toast.makeText(this, "Ocurrion un error, intentlo de nuevo", Toast.LENGTH_SHORT).show();
                        } break;
                    }
                }
        );

        boolean localData = getSharedPreferences("sections", Context.MODE_PRIVATE).getBoolean("localData", false);
        if ( !localData ) {
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
    public void addVolunteerFragment() {
        startCameraPreviewIntent.launch(new Intent(MainActivity.this, CameraPreview.class));
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
                    Toast.makeText(this, "Si se pudo burro", Toast.LENGTH_SHORT).show();
                }
            } catch ( JSONException ex ) {
                
            }
        }, error -> {
            Log.e("Error", "" + error.getMessage());
        });
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

}