package com.rld.app.mycampaign;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.rld.app.mycampaign.dialogs.MenuVolunteerDialog;
import com.rld.app.mycampaign.dialogs.MessageDialog;
import com.rld.app.mycampaign.dialogs.MessageDialogBuilder;
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
import com.rld.app.mycampaign.models.Address;
import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.Image;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.Section;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.ocr.ReadINE;
import com.rld.app.mycampaign.secrets.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;
import java.util.Calendar;
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

    static {
        OpenCVLoader.initDebug();
    }

    public static final int WRITE_STORAGE_PERMISSION_CODE = 100;
    private boolean isWritrable = false;

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

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_volunteer, R.id.nav_profile, R.id.nav_local_section)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                getSharedPreferences("sessions", Context.MODE_PRIVATE).edit().putString("sympathizerId", null).apply();
                getSharedPreferences("campaign", Context.MODE_PRIVATE).edit().putString("id", null).apply();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return false;
            }
        });
        navigationView.setCheckedItem(R.id.nav_volunteer);

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
                                showFormVolunteerWithOCR(currentVolunteer);
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

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_STORAGE_PERMISSION_CODE);
    }

    public void checkPermission(String permission, int requestCode) {
        if ( ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ permission }, requestCode);
        } else {
            isWritrable = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ( requestCode == WRITE_STORAGE_PERMISSION_CODE ) {
            if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                isWritrable = true;
            }
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
            if ( localVolunteers.size() > 0 ) {
                uploadVolunteersToServer();
            } else {
                Toast.makeText(MainActivity.this, "Ningún voluntario disponible para la carga", Toast.LENGTH_SHORT).show();
            }
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

    public void showFormVolunteerWithLocalData(Volunteer volunteer, int type) {
        ProgressDialogBuilder builder = new ProgressDialogBuilder()
                .setTitle("Cargando datos")
                .setCancelable(false);
        progressDialog = new ProgressDialog(MainActivity.this, builder);
        progressDialog.show();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                LocalDataFileManager localDataFileManager = LocalDataFileManager.getInstance(MainActivity.this);
                volunteerBottomSheet = new VolunteerBottomSheet(volunteer, MainActivity.this, localDataFileManager, type);
                volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 100);
    }

    public void showFormVolunteerWithOCR(Volunteer volunteer) {
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
                if ( isWritrable ) {
                    initializeVolunteerWithOCRData(localDataFileManager, volunteer);
                }
                volunteerBottomSheet = new VolunteerBottomSheet(volunteer, MainActivity.this, localDataFileManager, VolunteerBottomSheet.TYPE_INSERT);
                volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 100);
    }

    private void initializeVolunteerWithOCRData(LocalDataFileManager localDataFileManager, Volunteer volunteer) {
        ReadINE readINE = new ReadINE(MainActivity.this, volunteer.getImageCredential().getBlob());
        if (!readINE.isFourSides()) {
            return;
        }
        Log.e("nacimiento", readINE.getString("nacimiento"));
        Log.e("sexo", readINE.getString("sexo"));
        Log.e("nombre", readINE.getString("nombre"));
        Log.e("domicilio", readINE.getString("domicilio"));
        Log.e("clave", readINE.getString("clave"));
        Log.e("curp", readINE.getString("curp"));
        Log.e("estado", readINE.getString("estado"));
        Log.e("municipio", readINE.getString("municipio"));
        Log.e("seccion", readINE.getString("seccion"));
        Log.e("localidad", readINE.getString("localidad"));

        // Nombre
        String[] ocrFullName = readINE.getString("nombre").split("\n");
        if (ocrFullName.length == 3) {
            volunteer.setFathersLastname(removeInvalidCharacters(ocrFullName[0].trim(), "[A-ZÑ]"));
            volunteer.setMothersLastname(removeInvalidCharacters(ocrFullName[1].trim(), "[A-ZÑ]"));
            volunteer.setName(removeInvalidCharacters(ocrFullName[2].trim(), "[A-ZÑ]"));
        }
        // Fecha de nacimiento
        String ocrBirthdate = removeInvalidCharacters(readINE.getString("nacimiento"), "[0-9/]");
        if ( ocrBirthdate.matches("[0-3]*[0-9]{1}/[0-1]{1}[0-9]{1}/[0-9]{4}") ) {
            Calendar calendar = Calendar.getInstance();
            String[] birthdate = ocrBirthdate.split("/");
            calendar.set(Calendar.YEAR, Integer.parseInt(birthdate[2]));
            calendar.set(Calendar.MONTH, Integer.parseInt(birthdate[1]) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(birthdate[0]));
            volunteer.setBirthdate(calendar);
        }
        // Domicilio
        String[] ocrAddress = readINE.getString("domicilio").split("\n");
        if ( ocrAddress.length == 2 ) {
            Address address = new Address();
            // Calle
            String ocrStreet = removeInvalidCharacters(ocrAddress[0].trim(), "[A-ZÑ0-9 ]");
            if ( ocrStreet.startsWith("C ") ) {
                ocrStreet = ocrStreet.substring(ocrStreet.indexOf("C ") + "C ".length());
            }
            String[] streetFields = ocrStreet.split(" ");
            int idxExternalNumber = 0;
            for ( int i = 0; i < streetFields.length; i++ ) {
                if ( streetFields[i].matches("[0-9]+") ) {
                    idxExternalNumber = i;
                    break;
                }
            }
            StringBuilder street = new StringBuilder();
            for ( int i = 0; i < idxExternalNumber; i++ ) {
                street.append(streetFields[i]).append(" ");
            }
            address.setStreet(street.toString());
            address.setExternalNumber(streetFields[idxExternalNumber]);
            if ( idxExternalNumber == streetFields.length - 2 ) {
                address.setInternalNumber(streetFields[idxExternalNumber + 1]);
            }
            // Colonia y codigo postal
            String ocrSuburb = removeInvalidCharacters(ocrAddress[1].trim(), "[A-ZÑ0-9 ]");
            if ( ocrSuburb.startsWith("COL ") ) {
                ocrSuburb = ocrSuburb.substring(ocrSuburb.indexOf("COL ") + "COL ".length());
            }
            String[] suburbFields = ocrSuburb.split(" ");
            if ( suburbFields.length > 1 ) {
                StringBuilder suburb = new StringBuilder();
                for ( int i = 0; i < suburbFields.length - 1; i++ ) {
                    suburb.append(suburbFields[i]).append(" ");
                }
                address.setSuburb(suburb.toString());
                address.setZipcode(suburbFields[suburbFields.length - 1]);
            } else {
                address.setSuburb(suburbFields[0]);
            }
            volunteer.setAddress(address);
        }
        // Seccion
        String ocrState = removeInvalidCharacters(readINE.getString("estado").trim(), "[0-9]");
        String stateId = findBiggestString(ocrState, "[0-9]+");
        String ocrMunicipality = removeInvalidCharacters(readINE.getString("municipio").trim(), "[0-9]");
        String municipalityNumber = findBiggestString(ocrMunicipality,"[0-9]+");
        String ocrSection = removeInvalidCharacters(readINE.getString("seccion").trim(), "[0-9]");
        String sectionNumber = findBiggestString(ocrSection, "[0-9]+");

        State state = LocalDataFileManager.findState(localDataFileManager.getStates(), Integer.parseInt(stateId));
        if ( state != null ) {
            Section section = null;
            String currentStateName = getSharedPreferences("localData", Context.MODE_PRIVATE).getString("state_name", null);
            if ( currentStateName.equals(state.getName()) ) {
                for ( int i = 0; i < localDataFileManager.getSections().size(); i++ ) {
                    if ( localDataFileManager.getSections().get(i).getSection().equals(String.valueOf(sectionNumber))
                            && localDataFileManager.getSections().get(i).getMunicipality().getNumber() == Integer.parseInt(municipalityNumber)) {
                        section = localDataFileManager.getSections().get(i);
                        break;
                    }
                }
            }
            if ( section == null ) {
                section = new Section();
                section.setState(state);
                Municipality municipality = new Municipality();
                municipality.setNumber(Integer.parseInt(municipalityNumber));
                section.setMunicipality(municipality);
                section.setSection(sectionNumber);
            }
            volunteer.setSection(section);
        }
        // Clave de elector
        String ocrElectoralKey = removeInvalidCharacters(readINE.getString("clave"), "[0-9A-ZÑ ]");
        String electoralKey = findBiggestString(ocrElectoralKey, "[0-9A-ZN ]+");
        volunteer.setElectorKey(electoralKey);
        readINE.kill();
    }

    private String removeInvalidCharacters(String field, String expression) {
        StringBuilder result = new StringBuilder();
        String[] characters = field.split("");
        for ( String character : characters ) {
            if ( character.matches(expression) ) {
                result.append(character);
            }
        }
        return result.toString();
    }

    private String findBiggestString(String field, String expression) {
        String[] fields = field.split(" ");
        if ( fields.length == 1 && fields[0].matches(expression) ) {
            return fields[0];
        }
        int idxBiggest = -1;
        int maxLength = Integer.MIN_VALUE;
        for ( int i = fields.length - 1; i >= 0; i-- ) {
            if ( fields[i].length() > maxLength ) {
                maxLength = fields[i].length();
                idxBiggest = i;
            }
        }
        if ( fields[idxBiggest].matches(expression) ) {
            return fields[idxBiggest];
        }
        return null;
    }

    public void showFormVolunteerWithoutLocalData(Volunteer volunteer, int type) {
        VolunteerBottomSheet volunteerBottomSheet = new VolunteerBottomSheet(volunteer, MainActivity.this, null, type);
        volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
    }

    public void updateVolunteer(Volunteer volunteer, VolunteerBottomSheet volunteerBottomSheet) {
        VolunteerFileManager.writeJSON(localVolunteers, true, MainActivity.this);
        volunteerBottomSheet.dismiss();
        if ( volunteerFragment != null ) {
            volunteerFragment.updateVolunteers(volunteersToRecyclerView());
        }
        showSnackBarWithVolunteer("Voluntario actualizado con éxito", volunteer);
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
        for ( Volunteer volunteer : localVolunteers ) {
            volunteer.setLoad(true);
            volunteer.setError(null);
        }
        if ( volunteerFragment != null ) {
            volunteerFragment.notifyChangeOnRecyclerView();
        }
        nextVolunteerRequest(volunteersArray, 0, volunteersToRemove);
    }

    private void requestInsertVolunteer(JSONArray volunteeers, int index, ArrayList<Volunteer> volunteersToRemove) {
        JSONObject bodyRequest = new JSONObject();
        JSONObject campaign = new JSONObject();
        JSONObject sympathizer = new JSONObject();
        try {
            sympathizer.put("id", getSharedPreferences("sessions", Context.MODE_PRIVATE).getString("sympathizerId", null));
            campaign.put("id", getSharedPreferences("campaign", Context.MODE_PRIVATE).getString("id", null));
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
                    volunteer.setError(null);
                    volunteersToRemove.add(volunteer);
                } else {
                    addErrorsFromRequest(localVolunteers.get(index), response.getJSONObject("errors"));
                }
            } catch ( JSONException ex ) {
                ex.printStackTrace();
            }
            localVolunteers.get(index).setLoad(false);
            if ( volunteerFragment != null ) {
                volunteerFragment.notifyChangeOnRecyclerView(index + remoteVolunteers.size());
            }
            nextVolunteerRequest(volunteeers, index + 1, volunteersToRemove);
        }, error -> {
            localVolunteers.get(index).setLoad(false);
            if ( volunteerFragment != null ) {
                volunteerFragment.notifyChangeOnRecyclerView(index);
            }
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
        timer.schedule(task, 2000);
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

    private void addErrorsFromRequest(Volunteer volunteer, JSONObject errors) {
        Log.e("a", errors.toString());
        Volunteer.Error error = volunteer.getError();
        if ( error == null ) {
            error = new Volunteer.Error();
            volunteer.setError(error);
        }
        try {
            // Estado
            if ( !errors.isNull("state") ) {
                JSONObject stateObject = errors.getJSONObject("state");
                int id = stateObject.getInt("id");
                State state = new State();
                state.setId(id);
                if ( id != 0 ) {
                    state.setName(stateObject.getString("name"));
                } else {
                    state.setName("");
                }
                error.setState(state);
            } else {
                error.setState(null);
            }
            // Municipio
            if ( !errors.isNull("municipality") ) {
                JSONObject municipalityObject = errors.getJSONObject("municipality");
                int id = municipalityObject.getInt("id");
                Municipality municipality = new Municipality();
                municipality.setId(id);
                if ( id != 0 ) {
                    municipality.setNumber(municipalityObject.getInt("number"));
                    municipality.setName(municipalityObject.getString("name"));
                } else {
                    municipality.setNumber(0);
                    municipality.setName("");
                }
                error.setMunicipality(municipality);
            } else {
                error.setMunicipality(null);
            }
            // Distrito local
            if ( !errors.isNull("localDistrict") ) {
                JSONObject localDistrictObject = errors.getJSONObject("localDistrict");
                int id = localDistrictObject.getInt("id");
                LocalDistrict localDistrict = new LocalDistrict();
                localDistrict.setId(id);
                if ( id != 0 ) {
                    localDistrict.setNumber(localDistrictObject.getInt("number"));
                    localDistrict.setName(localDistrictObject.getString("name"));
                } else {
                    localDistrict.setNumber(0);
                    localDistrict.setName("");
                }
                error.setLocalDistrict(localDistrict);
            } else {
                error.setLocalDistrict(null);
            }
            // Distrito federal
            if ( !errors.isNull("federalDistrict") ) {
                JSONObject federalDistrictObject = errors.getJSONObject("federalDistrict");
                int id = federalDistrictObject.getInt("id");
                FederalDistrict federalDistrict = new FederalDistrict();
                federalDistrict.setId(id);
                if ( id != 0 ) {
                    federalDistrict.setNumber(federalDistrictObject.getInt("number"));
                    federalDistrict.setName(federalDistrictObject.getString("name"));
                } else {
                    federalDistrict.setNumber(0);
                    federalDistrict.setName("");
                }
                error.setFederalDistrict(federalDistrict);
            } else {
                error.setFederalDistrict(null);
            }
            if ( !errors.isNull("section") ) {
                JSONObject sectionObject = errors.getJSONObject("section");
                int id = sectionObject.getInt("id");
                Section section = new Section();
                section.setId(id);
                error.setSection(section);
            } else {
                error.setSection(null);
            }
        } catch ( JSONException ex ) {
            ex.printStackTrace();
        }
    }

    public void deleteVolunteer(Volunteer volunteer) {
        MessageDialogBuilder builder = new MessageDialogBuilder()
                .setTitle("Alerta")
                .setMessage("¿Esta seguro de querer eliminar el registro del voluntario?")
                .setPrimaryButtonText("Eliminar")
                .setSecondaryButtonText("Cancelar")
                .setCancelable(false);
        MessageDialog messageDialog = new MessageDialog(MainActivity.this, builder);
        messageDialog.setPrimaryButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localVolunteers.remove(volunteer);
                VolunteerFileManager.writeJSON(localVolunteers, true, MainActivity.this);
                if ( volunteerFragment != null ) {
                    volunteerFragment.updateVolunteers(volunteersToRecyclerView());
                }
                SpannableStringBuilder snackbarText = new SpannableStringBuilder();
                snackbarText.append("Voluntario eliminado con éxito!");
                snackbarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, snackbarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Snackbar.make(binding.getRoot(), snackbarText, Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.blue))
                        .setTextColor(getResources().getColor(R.color.light_white))
                        .setAction("DESHACER", v -> {
                            localVolunteers.add(volunteer);
                            VolunteerFileManager.writeJSON(localVolunteers, true, MainActivity.this);
                            if ( volunteerFragment != null ) {
                                volunteerFragment.updateVolunteers(volunteersToRecyclerView());
                            }
                            Toast.makeText(MainActivity.this, "Voluntario no eliminado", Toast.LENGTH_SHORT).show();
                        })
                        .setActionTextColor(getResources().getColor(R.color.white))
                        .show();
                messageDialog.dismiss();
            }
        });
        messageDialog.setSecondaryButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageDialog.dismiss();
            }
        });
        messageDialog.show();
    }

}