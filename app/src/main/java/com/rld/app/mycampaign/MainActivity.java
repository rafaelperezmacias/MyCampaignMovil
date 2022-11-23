package com.rld.app.mycampaign;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.rld.app.mycampaign.bottomsheets.VolunteerBottomSheet;
import com.rld.app.mycampaign.databinding.ActivityMainBinding;
import com.rld.app.mycampaign.dialogs.MenuVolunteerDialog;
import com.rld.app.mycampaign.dialogs.MessageDialog;
import com.rld.app.mycampaign.dialogs.MessageDialogBuilder;
import com.rld.app.mycampaign.dialogs.ProgressDialog;
import com.rld.app.mycampaign.dialogs.ProgressDialogBuilder;
import com.rld.app.mycampaign.files.FederalDistrictFileManager;
import com.rld.app.mycampaign.files.FileManager;
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
import com.rld.app.mycampaign.ocr.ReadINE;
import com.rld.app.mycampaign.secrets.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements VolunteerFragment.OnClickMenuVolunteerListener {

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
                                showFormVolunteerWithOCR(currentVolunteer, null);
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
    public void showMenuVolunteer() {
        MenuVolunteerDialog menuVolunteerDialog = new MenuVolunteerDialog(MainActivity.this);
        menuVolunteerDialog.setBtnAddVolunteerListener(view -> {
            menuVolunteerDialog.dismiss();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if ( volunteerFragment != null ) {
                        volunteerFragment.showMenuVolunteer();
                    }
                    startCameraPreviewIntent.launch(new Intent(MainActivity.this, CameraPreview.class));
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 100);
        });
        menuVolunteerDialog.setBtnUploadListener(view -> {
            menuVolunteerDialog.dismiss();
            if ( volunteerFragment != null ) {
                volunteerFragment.showMenuVolunteer();
            }
            uploadVolunteersToServer();
        });
        menuVolunteerDialog.setOnDismissListener(dialogInterface -> {
            if ( volunteerFragment != null ) {
                volunteerFragment.showMenuVolunteer();
            }
        });
        Window window = menuVolunteerDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;
        layoutParams.flags &= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(layoutParams);
        menuVolunteerDialog.show();
    }

    public void firmActivityForVolunteer() {
        startFirmActivityIntent.launch(new Intent(MainActivity.this, FirmActivity.class));
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

    private void addLocalVolunteer() {
        localVolunteers.add(currentVolunteer);
        VolunteerFileManager.writeJSON(localVolunteers, true, MainActivity.this);
        volunteerBottomSheet.dismiss();
        if ( volunteerFragment != null ) {
            volunteerFragment.updateVolunteers(volunteersToRecyclerView());
        }
        showSnackBarWithVolunteer("Voluntario registrado con éxito", currentVolunteer);
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

    public void showFormVolunteerWithLocalData(Volunteer editableVolunteer, Volunteer noEditableVolunteer, int type) {
        ProgressDialogBuilder builder = new ProgressDialogBuilder()
                .setTitle("Cargando datos")
                .setCancelable(false);
        progressDialog = new ProgressDialog(MainActivity.this, builder);
        progressDialog.show();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                LocalDataFileManager localDataFileManager = LocalDataFileManager.getInstance(MainActivity.this);
                volunteerBottomSheet = new VolunteerBottomSheet(editableVolunteer, noEditableVolunteer, MainActivity.this, localDataFileManager, type);
                volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 100);
    }

    public void showFormVolunteerWithOCR(Volunteer editableVolunteer, Volunteer noEditableVolunteer) {
        ProgressDialogBuilder builder = new ProgressDialogBuilder()
                .setTitle("Cargando datos")
                .setCancelable(false);
        progressDialog = new ProgressDialog(MainActivity.this, builder);
        progressDialog.show();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // OCR
                LocalDataFileManager localDataFileManager = LocalDataFileManager.getInstance(MainActivity.this);
                volunteerBottomSheet = new VolunteerBottomSheet(editableVolunteer, noEditableVolunteer, MainActivity.this, localDataFileManager, VolunteerBottomSheet.TYPE_INSERT);
                volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 100);
    }


    public void showFormVolunteerWithoutLocalData(Volunteer volunteer, int type) {
        VolunteerBottomSheet volunteerBottomSheet = new VolunteerBottomSheet(volunteer, null, MainActivity.this, null, type);
        volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
    }

    public void updateVolunteer(Volunteer editableVolunteer, Volunteer noEditableVolunteer, VolunteerBottomSheet volunteerBottomSheet) {
        int idx = 0;
        for ( int i = 0; i < localVolunteers.size(); i++ ) {
            if ( localVolunteers.get(i).equals(noEditableVolunteer) ) {
                idx = i;
                break;
            }
        }
        localVolunteers.remove(idx);
        localVolunteers.add(idx, editableVolunteer);
        VolunteerFileManager.writeJSON(localVolunteers, true, MainActivity.this);
        volunteerBottomSheet.dismiss();
        if ( volunteerFragment != null ) {
            volunteerFragment.updateVolunteers(volunteersToRecyclerView());
        }
        showSnackBarWithVolunteer("Voluntario actualizado con éxito", editableVolunteer);
    }

    private void showSnackBarWithVolunteer(String message, Volunteer volunteer) {
        SpannableStringBuilder snackbarText = new SpannableStringBuilder();
        snackbarText.append(message);
        snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Snackbar.make(binding.getRoot(), snackbarText, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(R.color.blue))
                .setTextColor(getResources().getColor(R.color.light_white))
                .setAction("DETALLES", view -> {
                    showFormVolunteerWithoutLocalData(volunteer, VolunteerBottomSheet.TYPE_SHOW);
                })
                .setActionTextColor(getResources().getColor(R.color.white))
                .show();
    }

    private void uploadVolunteersToServer() {
        JSONArray volunteersArray = VolunteerFileManager.arrayListToJsonArray(localVolunteers, true);
        ArrayList<Volunteer> volunteersToRemove = new ArrayList<>();
        nextVolunteerRequest(volunteersArray, 0, volunteersToRemove);
    }

    private void requestInsertVolunteer(JSONArray volunteeers, int index, ArrayList<Volunteer> volunteersToRemove) {
        JSONObject bodyRequest = new JSONObject();
        JSONObject campaign = new JSONObject();
        JSONObject sympathizer = new JSONObject();
        try {
            sympathizer.put("id", 1);
            campaign.put("id", 1);
            bodyRequest.put("volunteer", volunteeers.get(index));
            bodyRequest.put("campaign", campaign);
            bodyRequest.put("sympathizer", sympathizer);
        } catch ( JSONException ex ) {

        }
        String url = AppConfig.INSERT_VOLUNTEER;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, bodyRequest, response -> {
            try {
                if ( response.getBoolean("success") ) {
                    Volunteer volunteer = localVolunteers.get(index);
                    volunteer.setId( response.getJSONObject("volunteer").getInt("id") );
                    volunteersToRemove.add(volunteer);
                }
            } catch ( JSONException ex ) {
                ex.printStackTrace();
            }
            nextVolunteerRequest(volunteeers, index + 1, volunteersToRemove);
        }, error -> {
            nextVolunteerRequest(volunteeers, index + 1, volunteersToRemove);
        });
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    private void nextVolunteerRequest(JSONArray volunteeers, int index, ArrayList<Volunteer> volunteersToRemove) {
        if ( index >= volunteeers.length() ) {
            localVolunteers.removeAll(volunteersToRemove);
            remoteVolunteers.addAll(volunteersToRemove);
            VolunteerFileManager.writeJSON(localVolunteers, true, MainActivity.this);
            VolunteerFileManager.writeJSON(remoteVolunteers, false, MainActivity.this);
            if ( volunteerFragment != null ) {
                volunteerFragment.updateVolunteers(volunteersToRecyclerView());
            }
            return;
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                requestInsertVolunteer(volunteeers, index, volunteersToRemove);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 5000);
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
                    getSharedPreferences("localData", Context.MODE_PRIVATE).edit().putBoolean("saved", true).apply();
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