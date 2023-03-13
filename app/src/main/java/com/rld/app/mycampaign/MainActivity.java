package com.rld.app.mycampaign;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
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

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.rld.app.mycampaign.api.Client;
import com.rld.app.mycampaign.api.DownloadManager;
import com.rld.app.mycampaign.api.VolunteerAPI;
import com.rld.app.mycampaign.bottomsheets.VolunteerBottomSheet;
import com.rld.app.mycampaign.databinding.ActivityMainBinding;
import com.rld.app.mycampaign.dialogs.ErrorMessageDialog;
import com.rld.app.mycampaign.dialogs.ErrorMessageDialogBuilder;
import com.rld.app.mycampaign.dialogs.MenuVolunteerDialog;
import com.rld.app.mycampaign.dialogs.MessageDialog;
import com.rld.app.mycampaign.dialogs.MessageDialogBuilder;
import com.rld.app.mycampaign.dialogs.ProgressDialog;
import com.rld.app.mycampaign.dialogs.ProgressDialogBuilder;
import com.rld.app.mycampaign.files.LocalDataFileManager;
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
import com.rld.app.mycampaign.models.Token;
import com.rld.app.mycampaign.models.User;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.models.api.VolunteerRequest;
import com.rld.app.mycampaign.models.api.VolunteerResponse;
import com.rld.app.mycampaign.ocr.ReadINE;
import com.rld.app.mycampaign.preferences.CampaignPreferences;
import com.rld.app.mycampaign.preferences.DownloadManagerPreferences;
import com.rld.app.mycampaign.preferences.LocalDataPreferences;
import com.rld.app.mycampaign.preferences.TokenPreferences;
import com.rld.app.mycampaign.preferences.UserPreferences;
import com.rld.app.mycampaign.utils.Internet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements VolunteerFragment.OnClickMenuVolunteerListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private ActivityResultLauncher<Intent> startCameraPreviewIntent;
    private ActivityResultLauncher<Intent> startFirmActivityIntent;

    private VolunteerBottomSheet volunteerBottomSheet;

    private Volunteer currentVolunteer;

    private ArrayList<Volunteer> localVolunteers;
    private ArrayList<Volunteer> remoteVolunteers;

    private VolunteerFragment volunteerFragment;

    private static final int REQUEST_PERMISSIONS_CODE = 100;

    private static final String[] REQUEST_PERMISSIONS = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };

    private static final int UNKNOWN_ERROR = -1;

    static {
        OpenCVLoader.initDebug();
    }

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

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            if ( localVolunteers.size() > 0 ) {
                MessageDialogBuilder messageDialogBuilder = new MessageDialogBuilder()
                        .setTitle("Voluntarios sin cargar")
                        .setMessage("Quedan aun voluntarios almacenados localmente, al cerrar sesión esta información se perdera. ¿Esta seguro de querer cerrar sesión?")
                        .setPrimaryButtonText("Cancelar")
                        .setSecondaryButtonText("Cerrar sesión")
                        .setCancelable(false);
                MessageDialog messageDialog = new MessageDialog(MainActivity.this, messageDialogBuilder);
                messageDialog.setPrimaryButtonListener(v -> messageDialog.dismiss());
                messageDialog.setSecondaryButtonListener(v -> {
                    messageDialog.dismiss();
                    logout();
                });
                messageDialog.show();
                return false;
            }
            logout();
            return false;
        });
        navigationView.setCheckedItem(R.id.nav_volunteer);

        View headerLayout = navigationView.getHeaderView(0);
        CircleImageView imageProfile = headerLayout.findViewById(R.id.image_profile);
        TextView txtName = headerLayout.findViewById(R.id.txt_name);
        TextView txtEmail = headerLayout.findViewById(R.id.txt_email);

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
                            MessageDialogBuilder builder = new MessageDialogBuilder()
                                    .setTitle("Imagen no tómada")
                                    .setMessage("Para registrar a un voluntario es necesario tomar una fotografía a su identificación oficial")
                                    .setPrimaryButtonText("Aceptar")
                                    .setCancelable(true);
                            MessageDialog messageDialog = new MessageDialog(MainActivity.this, builder);
                            messageDialog.setPrimaryButtonListener(v -> messageDialog.dismiss());
                            messageDialog.show();
                        } break;
                        case CameraPreview.RESULT_IMAGE_FILE_NOT_CREATE: {
                            ErrorMessageDialogBuilder builder = new ErrorMessageDialogBuilder()
                                    .setTitle("Imagen no tómada")
                                    .setMessage("La fotografía no se pudo crear de manera satisfactoria")
                                    .setError("IOException")
                                    .setButtonText("Aceptar")
                                    .setCancelable(true);
                            ErrorMessageDialog errorMessageDialog = new ErrorMessageDialog(MainActivity.this, builder);
                            errorMessageDialog.setButtonClickListener(v -> errorMessageDialog.dismiss());
                            errorMessageDialog.show();
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
                            MessageDialogBuilder builder = new MessageDialogBuilder()
                                    .setTitle("Firma no registrada")
                                    .setMessage("Para terminar el registro es necesario que el voluntario ingrese su firma")
                                    .setPrimaryButtonText("Aceptar")
                                    .setCancelable(true);
                            MessageDialog messageDialog = new MessageDialog(MainActivity.this, builder);
                            messageDialog.setPrimaryButtonListener(v -> messageDialog.dismiss());
                            messageDialog.show();
                        } break;
                        case FirmActivity.RESULT_IMAGE_FILE_NOT_CREATE: {
                            ErrorMessageDialogBuilder builder = new ErrorMessageDialogBuilder()
                                    .setTitle("Firma no registrada")
                                    .setMessage("La firma no se pudo crear de manera satisfactoria")
                                    .setError("IOException")
                                    .setButtonText("Aceptar")
                                    .setCancelable(true);
                            ErrorMessageDialog errorMessageDialog = new ErrorMessageDialog(MainActivity.this, builder);
                            errorMessageDialog.setButtonClickListener(v -> errorMessageDialog.dismiss());
                            errorMessageDialog.show();
                        } break;
                    }
                }
        );

        User user = UserPreferences.getUser(MainActivity.this);
        byte[] decodeImage = Base64.decode(user.getProfileImage(), Base64.DEFAULT);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.length);
        imageProfile.setImageBitmap(imageBitmap);
        txtName.setText(user.getName());
        txtEmail.setText(user.getEmail());

        if ( DownloadManagerPreferences.isFirstTimeDownloadData(getApplicationContext()) ) {
            downloadDataOfSections();
        }
    }

    /**
     * Funcion para cerrar sesion
     */
    private void logout() {
        ProgressDialogBuilder builder = new ProgressDialogBuilder()
                .setTitle("Cerrando sesión")
                .setCancelable(false);
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, builder);
        progressDialog.show();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                VolunteerFileManager.deleteLocalVolunteerFile(getApplicationContext());
                VolunteerFileManager.deleteRemoteVolunteerFile(getApplicationContext());
                UserPreferences.deleteUser(getApplicationContext());
                CampaignPreferences.deleteCampaign(getApplicationContext());
                TokenPreferences.deleteToken(getApplicationContext());
                LocalDataPreferences.deleteLocalPreferences(getApplicationContext());
                progressDialog.dismiss();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 500);
    }

    /**
     * Diseño
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    /**
     * Descarga de datos
     */
    private void downloadDataOfSections() {
        ProgressDialogBuilder builder = new ProgressDialogBuilder()
                .setTitle("Descargando datos...")
                .setCancelable(false);
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, builder);
        progressDialog.show();
        Token token = TokenPreferences.getToken(getApplicationContext());
        if ( !Internet.isNetworkAvailable(getApplicationContext()) || !Internet.isOnlineNetwork() ) {
            progressDialog.dismiss();
            MessageDialogBuilder messageDialogBuilder = new MessageDialogBuilder()
                    .setTitle("Sin conexión a internet")
                    .setMessage("Para descargar los datos locales necesita de una conexión a internet")
                    .setPrimaryButtonText("Aceptar")
                    .setCancelable(true);
            MessageDialog messageDialog = new MessageDialog(MainActivity.this, messageDialogBuilder);
            messageDialog.setPrimaryButtonListener(v -> messageDialog.dismiss());
            messageDialog.show();
            return;
        }
        DownloadManager.downloadDataOfServer(getApplicationContext(), token, new DownloadManager.OnResolveRequestListener() {
            @Override
            public void onSuccessListener() {
                DownloadManagerPreferences.setIsLocalDataSaved(getApplicationContext(), true);
                DownloadManagerPreferences.setFirstTimeDownloadData(getApplicationContext(), false);
                progressDialog.dismiss();
                MessageDialogBuilder messageDialogBuilder = new MessageDialogBuilder()
                        .setTitle("Actualización exitosa")
                        .setMessage("Los datos se han actualizado con exito!")
                        .setCancelable(false)
                        .setPrimaryButtonText("Aceptar");
                MessageDialog messageDialog = new MessageDialog(MainActivity.this, messageDialogBuilder);
                messageDialog.setPrimaryButtonListener(v -> messageDialog.dismiss());
                messageDialog.show();
            }
            @Override
            public void onFailureListener(int type, int code, String error)  {
                DownloadManagerPreferences.setIsLocalDataSaved(getApplicationContext(), false);
                progressDialog.dismiss();
                ErrorMessageDialogBuilder builder = new ErrorMessageDialogBuilder();
                builder.setTitle("Error descargando datos")
                        .setButtonText("Aceptar")
                        .setCancelable(false);
                switch ( type ) {
                    case DownloadManager.TYPE_STATE_REQUEST : {
                        builder.setMessage("Ha ocurrido un error descargando los datos de los estados");
                    } break;
                    case DownloadManager.TYPE_FEDERAL_DISTRICT_REQUEST : {
                        builder.setMessage("Ha ocurrido un error descargando los datos de los distritos federales");
                    } break;
                    case DownloadManager.TYPE_LOCAL_DISTRICT_REQUEST : {
                        builder.setMessage("Ha ocurrido un error descargando los datos de los distritos locales");
                    } break;
                    case DownloadManager.TYPE_MUNICIPALITY_REQUEST : {
                        builder.setMessage("Ha ocurrido un error descargando los datos de los municipios");
                    } break;
                    case DownloadManager.TYPE_SECTION_REQUEST : {
                        builder.setMessage("Ha ocurrido un error descargando los datos de las secciones");
                    } break;
                }
                switch ( code ) {
                    case DownloadManager.BAD_REQUEST : {
                        builder.setError("400 - " + error);
                    } break;
                    case DownloadManager.UNAUTHORIZED : {
                        builder.setError("401 - " + error);
                    } break;
                    case DownloadManager.TOO_MANY_REQUEST : {
                        builder.setError("429 - " + error);
                    } break;
                    case DownloadManager.INTERNAL_ERROR : {
                        builder.setError("500 - " + error);
                    } break;
                    case DownloadManager.UNKNOWN_ERROR : {
                        builder.setError("Error desconocido - " + error);
                    } break;
                    case DownloadManager.ERROR_REQUEST : {
                        builder.setError("Error con la petición - " + error);
                    } break;
                }
                ErrorMessageDialog messageDialog = new ErrorMessageDialog(MainActivity.this, builder);
                messageDialog.setButtonClickListener(v -> messageDialog.dismiss());
                messageDialog.show();
                messageDialog.setOnDismissListener(dialog -> {
                    if ( code == DownloadManager.UNAUTHORIZED ) {
                        MessageDialogBuilder messageDialogBuilder = new MessageDialogBuilder();
                        messageDialogBuilder.setTitle("La sesión ha caducado")
                                .setMessage("Para seguir utilizando la aplicación, por favor vuelve a iniciar sesión.")
                                .setPrimaryButtonText("Aceptar")
                                .setCancelable(false);
                        MessageDialog closeDialog = new MessageDialog(MainActivity.this, messageDialogBuilder);
                        closeDialog.setPrimaryButtonListener(v -> {
                            closeDialog.dismiss();
                        });
                        closeDialog.setOnDismissListener(dialog1 -> {
                            UserPreferences.clearConnect(getApplicationContext());
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        });
                        closeDialog.show();
                    }
                });
            }
        });
    }

    /**
     * Evento de menu lanzado desde VolunteerFragment
     */
    @Override
    public void showMenuVolunteer() {
        MenuVolunteerDialog menuVolunteerDialog = new MenuVolunteerDialog(MainActivity.this, localVolunteers.size());
        menuVolunteerDialog.setBtnAddVolunteerListener(view -> {
            menuVolunteerDialog.dismiss();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if ( volunteerFragment != null ) {
                        volunteerFragment.showMenuVolunteer();
                    }
                    checkPermissions();
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

    @Override
    public void showRegisterForm() {
        checkPermissions();
    }

    /**
     * Carga de voluntarios en el listado de VolunteerFragment
     */
    @Override
    public void initializeVolunteersList(VolunteerFragment volunteerFragment) {
        this.volunteerFragment = volunteerFragment;
        volunteerFragment.updateVolunteers(volunteersToRecyclerView());
    }

    public ArrayList<Volunteer> volunteersToRecyclerView() {
        ArrayList<Volunteer> volunteers = new ArrayList<>();
        volunteers.addAll(remoteVolunteers);
        volunteers.addAll(localVolunteers);
        return volunteers;
    }

    /**
     * Verificamos los permisos en antes de insertar un nuevo voluntario
     */
    private void checkPermissions() {
        ArrayList<String> requestPermissions = getListOfPermissionsNotAllowed();
        if ( requestPermissions.isEmpty() ) {
            startCameraPreviewIntent.launch(new Intent(MainActivity.this, CameraPreview.class));
        } else {
            String[] permissions = new String[requestPermissions.size()];
            for ( int i = 0; i < requestPermissions.size(); i++ ) {
                permissions[i] = requestPermissions.get(i);
            }
            ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_PERMISSIONS_CODE);
        }
    }

    /**
     * Obtenemos la lista de permisos faltantes
     */
    private ArrayList<String> getListOfPermissionsNotAllowed() {
        ArrayList<String> permissionsNotAllowed = new ArrayList<>();
        for ( String permission : REQUEST_PERMISSIONS ) {
            if ( ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED ) {
                permissionsNotAllowed.add(permission);
            }
        }
        return permissionsNotAllowed;
    }

    /**
     * Resultado de pedir los permisos
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ( requestCode == REQUEST_PERMISSIONS_CODE ) {
            boolean isAllGranted = true;
            for ( int permissionResult : grantResults ) {
                if ( permissionResult != PackageManager.PERMISSION_GRANTED ) {
                    isAllGranted = false;
                    break;
                }
            }
            if ( isAllGranted ) {
                startCameraPreviewIntent.launch(new Intent(MainActivity.this, CameraPreview.class));
            } else {
                // TODO
                SpannableStringBuilder snackBarText = new SpannableStringBuilder();
                snackBarText.append("Por favor, acepte los permisos para poder realizar un registro.");
                snackBarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, snackBarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Snackbar.make(binding.getRoot(), snackBarText, Snackbar.LENGTH_LONG)
                        .setBackgroundTint(getResources().getColor(R.color.blue))
                        .setTextColor(getResources().getColor(R.color.light_white))
                        .setAction("CONCEDER", view -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                            startActivity(intent);
                        })
                        .setActionTextColor(getResources().getColor(R.color.white))
                        .show();
            }
        }
    }

    /**
     * 1° Iniciamos la imagen del voluntario
     */
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

    /**
     * 2° Carga de datos y OCR
     */
    public void showFormVolunteerWithOCR(Volunteer volunteer) {
        ProgressDialogBuilder builder = new ProgressDialogBuilder()
                .setTitle("Cargando datos...")
                .setCancelable(false);
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, builder);
        progressDialog.show();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // OCR
                LocalDataFileManager localDataFileManager = LocalDataFileManager.getInstanceWithPreferences(MainActivity.this);
                initializeVolunteerWithOCRData(localDataFileManager, volunteer);
                progressDialog.dismiss();
                volunteerBottomSheet = new VolunteerBottomSheet(volunteer, MainActivity.this, localDataFileManager, VolunteerBottomSheet.TYPE_INSERT);
                volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 100);
    }

    /**
     * 3° Cargar informacion del OCR al voluntario
     */
    private void initializeVolunteerWithOCRData(LocalDataFileManager localDataFileManager, Volunteer volunteer) {
        ReadINE readINE = new ReadINE(MainActivity.this, volunteer.getImageCredential().getBlob());
        if ( !readINE.isFourSides() ) {
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
        if ( ocrAddress.length == 2 || ocrAddress.length == 3) {
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

        State state = LocalDataFileManager.findState(localDataFileManager.getStates(), Integer.parseInt(stateId.equals("") ? "0" : stateId));
        if ( state != null ) {
            Section section = null;
            String currentStateName = LocalDataPreferences.getNameStateSelected(getApplicationContext());
            if ( currentStateName.equalsIgnoreCase(state.getName()) ) {
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
                if ( !municipalityNumber.isEmpty() ) {
                    Municipality municipality = new Municipality();
                    municipality.setNumber(Integer.parseInt(municipalityNumber));
                    section.setMunicipality(municipality);
                }
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

    /**
     * Limpieza de datos del OCR
     */
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
        if ( idxBiggest == -1 ) {
            return "";
        }
        if ( fields[idxBiggest].matches(expression) ) {
            return fields[idxBiggest];
        }
        return "";
    }

    /**
     * 4° Obtencion de la firma del voluntario
     */
    public void firmActivityForVolunteer() {
        startFirmActivityIntent.launch(new Intent(MainActivity.this, FirmActivity.class));
    }

    /**
     * 5° Almacenamos la informacion del voluntario localmente
     */
    private void addLocalVolunteer() {
        localVolunteers.add(currentVolunteer);
        VolunteerFileManager.writeJSON(localVolunteers, true, MainActivity.this);
        volunteerBottomSheet.dismiss();
        if ( volunteerFragment != null ) {
            volunteerFragment.updateVolunteers(volunteersToRecyclerView());
        }
        showSnackBarWithVolunteer("Voluntario registrado con éxito", currentVolunteer);
    }

    /**
     * Edicion del voluntario
     */
    public void showFormVolunteerWithLocalData(Volunteer volunteer, int type) {
        ProgressDialogBuilder builder = new ProgressDialogBuilder()
                .setTitle("Cargando datos")
                .setCancelable(false);
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, builder);
        progressDialog.show();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                LocalDataFileManager localDataFileManager = LocalDataFileManager.getInstanceWithPreferences(MainActivity.this);
                progressDialog.dismiss();
                volunteerBottomSheet = new VolunteerBottomSheet(volunteer, MainActivity.this, localDataFileManager, type);
                volunteerBottomSheet.show(getSupportFragmentManager(), volunteerBottomSheet.getTag());
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 100);
    }

    /**
     * Visualizacion del voluntario
     */
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
        ProgressDialogBuilder builder = new ProgressDialogBuilder()
                .setTitle("Verificando conexión a internet...")
                .setCancelable(false);
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, builder);
        progressDialog.show();
        if ( !Internet.isNetworkAvailable(getApplicationContext()) || !Internet.isOnlineNetwork() ) {
            progressDialog.dismiss();
            MessageDialogBuilder messageDialogBuilder = new MessageDialogBuilder()
                    .setTitle("Sin conexión a internet")
                    .setMessage("Para almacenar los voluntarios locales se necesita de una conexión a internet")
                    .setPrimaryButtonText("Aceptar")
                    .setCancelable(true);
            MessageDialog messageDialog = new MessageDialog(MainActivity.this, messageDialogBuilder);
            messageDialog.setPrimaryButtonListener(v -> messageDialog.dismiss());
            messageDialog.show();
            return;
        }
        progressDialog.dismiss();
        ArrayList<Volunteer> volunteersToRemove = new ArrayList<>();
        for ( Volunteer volunteer : localVolunteers ) {
            volunteer.setLoad(true);
            volunteer.setError(null);
        }
        if ( volunteerFragment != null ) {
            volunteerFragment.notifyChangeOnRecyclerView();
        }
        nextVolunteerRequest(localVolunteers, 0, volunteersToRemove);
    }


    private void requestInsertVolunteer(ArrayList<Volunteer> volunteers, int index, ArrayList<Volunteer> volunteersToRemove) {
        Volunteer volunteer = volunteers.get(index);
        VolunteerRequest volunteerRequest = volunteer.toVolunteerRequest();
        Token token = TokenPreferences.getToken(getApplicationContext());
        Call<VolunteerResponse> insertVolunteerCall = Client.getClient().create(VolunteerAPI.class)
                .saveVolunteer(
                        token.toAPIRequest(),
                        volunteerRequest
                );
        insertVolunteerCall.enqueue(new Callback<VolunteerResponse>() {
            @Override
            public void onResponse(Call<VolunteerResponse> call, Response<VolunteerResponse> response) {
                if ( response.isSuccessful() ) {
                    volunteer.setLoad(false);
                    volunteer.setError(null);
                    volunteer.setId(response.body().getMessage());
                    volunteer.getImageFirm().setPath("");
                    volunteer.getImageCredential().setPath("");
                    volunteersToRemove.add(volunteer);
                    if ( volunteerFragment != null ) {
                        volunteerFragment.notifyChangeOnRecyclerView(index + remoteVolunteers.size());
                    }
                    nextVolunteerRequest(volunteers, index + 1, volunteersToRemove);
                    return;
                }
                if ( response.code() == 400 ) {
                    try {
                        JSONObject errorResponse = new JSONObject(response.errorBody().string());
                        JSONArray entities = errorResponse.getJSONArray("entities");
                        volunteer.setError(null);
                        for ( int i = 0; i < entities.length(); i++ ) {
                            addErrorsFromRequest(volunteer, entities.getJSONObject(i));
                        }
                    } catch ( JSONException | IOException ex ) {
                        Log.e("", "" + ex.getMessage());
                        ex.printStackTrace();
                    }
                    volunteer.setLoad(false);
                    nextVolunteerRequest(volunteers, index + 1, volunteersToRemove);
                    return;
                }
                for ( Volunteer volunteer : localVolunteers ) {
                    volunteer.setLoad(false);
                }
                if ( volunteerFragment != null ) {
                    volunteerFragment.notifyChangeOnRecyclerView();
                }
                localVolunteers.removeAll(volunteersToRemove);
                remoteVolunteers.addAll(volunteersToRemove);
                VolunteerFileManager.writeJSON(localVolunteers, true, MainActivity.this);
                VolunteerFileManager.writeJSON(remoteVolunteers, false, MainActivity.this);
                if ( volunteerFragment != null ) {
                    volunteerFragment.updateVolunteers(volunteersToRecyclerView());
                }
                if ( response.code() == 401 ) {
                    MessageDialogBuilder messageDialogBuilder = new MessageDialogBuilder();
                    messageDialogBuilder.setTitle("La sesión ha caducado")
                            .setMessage("Para seguir utilizando la aplicación, por favor vuelve a iniciar sesión.")
                            .setPrimaryButtonText("Aceptar")
                            .setCancelable(false);
                    MessageDialog closeDialog = new MessageDialog(MainActivity.this, messageDialogBuilder);
                    closeDialog.setPrimaryButtonListener(v -> {
                        closeDialog.dismiss();
                    });
                    closeDialog.setOnDismissListener(dialog1 -> {
                        UserPreferences.clearConnect(getApplicationContext());
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    });
                    closeDialog.show();
                    return;
                }
                try {
                    showErrorRequest(response.code(), response.message(),  "Error con la carga de voluntarios", response.errorBody().string());
                } catch ( IOException ex ) {
                    showErrorRequest(UNKNOWN_ERROR, "IOException", "Error con la carga de voluntarios", ex.getMessage());
                }
            }
            @Override
            public void onFailure(Call<VolunteerResponse> call, Throwable t) {
                for ( Volunteer volunteer : localVolunteers ) {
                    volunteer.setLoad(false);
                }
                if ( volunteerFragment != null ) {
                    volunteerFragment.notifyChangeOnRecyclerView();
                }
                localVolunteers.removeAll(volunteersToRemove);
                remoteVolunteers.addAll(volunteersToRemove);
                VolunteerFileManager.writeJSON(localVolunteers, true, MainActivity.this);
                VolunteerFileManager.writeJSON(remoteVolunteers, false, MainActivity.this);
                if ( volunteerFragment != null ) {
                    volunteerFragment.updateVolunteers(volunteersToRecyclerView());
                }
                if ( t instanceof SocketTimeoutException) {
                    showErrorRequest(UNKNOWN_ERROR, "Exception", "Error con la carga de voluntarios", "SocketTimeoutException");
                } else {
                    showErrorRequest(UNKNOWN_ERROR, "Exception", "Error con la carga de voluntarios", t.getMessage());
                }
            }
        });
    }

    private void nextVolunteerRequest(ArrayList<Volunteer> volunteers, int index, ArrayList<Volunteer> volunteersToRemove) {
        if ( index >= volunteers.size() ) {
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
                requestInsertVolunteer(volunteers, index, volunteersToRemove);
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000);
    }


    private void addErrorsFromRequest(Volunteer volunteer, JSONObject entityError) {
        Log.e("", "" + entityError);
        Volunteer.Error error = volunteer.getError();
        if ( error == null ) {
            error = new Volunteer.Error();
            volunteer.setError(error);
        }
        try {
            // Estado
            if ( entityError.getString("name").equals("state") ) {
                JSONObject stateObject = entityError.getJSONObject("message");
                int id = stateObject.getInt("id");
                State state = new State();
                state.setId(id);
                if ( id != 0 ) {
                    state.setName(stateObject.getString("name"));
                } else {
                    state.setName("");
                }
                error.setState(state);
            }
            // Municipio
            if ( entityError.getString("name").equals("municipality") ) {
                JSONObject municipalityObject = entityError.getJSONObject("message");
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
            }
            // Distrito local
            if ( entityError.getString("name").equals("localDistric") ) {
                JSONObject localDistrictObject = entityError.getJSONObject("message");
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
            }
            // Distrito federal
            if ( entityError.getString("name").equals("federalDistric") ) {
                JSONObject federalDistrictObject = entityError.getJSONObject("message");
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
            }
            if ( entityError.getString("name").equals("section") ) {
                JSONObject sectionObject = entityError.getJSONObject("message");
                int id = sectionObject.getInt("id");
                Section section = new Section();
                section.setId(id);
                error.setSection(section);
            }
        } catch ( JSONException ex ) {
            ex.printStackTrace();
            Log.e("addErrorsFromRequest", "" + ex);
        }
    }

    private void showErrorRequest(int code, String error, String message, String errorMessage) {
        StringBuilder titleBuilder = new StringBuilder();
        if ( code != UNKNOWN_ERROR ) {
            titleBuilder.append(code).append(" - ");
        }
        titleBuilder.append(error);
        ErrorMessageDialogBuilder builder = new ErrorMessageDialogBuilder()
                .setTitle(titleBuilder.toString())
                .setMessage(message)
                .setError(errorMessage)
                .setButtonText("Aceptar")
                .setCancelable(true);
        ErrorMessageDialog errorMessageDialog = new ErrorMessageDialog(MainActivity.this, builder);
        errorMessageDialog.setButtonClickListener(v -> errorMessageDialog.dismiss());
        errorMessageDialog.show();
    }

    public void deleteVolunteer(Volunteer volunteer) {
        MessageDialogBuilder builder = new MessageDialogBuilder()
                .setTitle("Alerta")
                .setMessage("¿Esta seguro de querer eliminar el registro del voluntario?")
                .setPrimaryButtonText("Eliminar")
                .setSecondaryButtonText("Cancelar")
                .setCancelable(false);
        MessageDialog messageDialog = new MessageDialog(MainActivity.this, builder);
        messageDialog.setPrimaryButtonListener(view -> {
            localVolunteers.remove(volunteer);
            VolunteerFileManager.writeJSON(localVolunteers, true, MainActivity.this);
            if ( volunteerFragment != null ) {
                volunteerFragment.updateVolunteers(volunteersToRecyclerView());
            }
            SpannableStringBuilder snackBarText = new SpannableStringBuilder();
            snackBarText.append("Voluntario eliminado con éxito!");
            snackBarText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, snackBarText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            Snackbar.make(binding.getRoot(), snackBarText, Snackbar.LENGTH_LONG)
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
        });
        messageDialog.setSecondaryButtonListener(view -> messageDialog.dismiss());
        messageDialog.show();
    }

    /**
     * Manejo manual de eventos para cerrar
     */
    @Override
    public void onBackPressed() {
        NavigationView navigationView = binding.navView;
        if ( navigationView.getCheckedItem().getItemId() != R.id.nav_volunteer ) {
            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_volunteer);
            navigationView.setCheckedItem(R.id.nav_volunteer);
            return;
        }
        finish();
    }

    /**
     * Verificamos si el token esta vigente todavia, sino lo mondamos al login
     */
    @Override
    protected void onResume() {
        super.onResume();
        Token token = TokenPreferences.getToken(getApplicationContext());
        if ( !token.isValid() ) {
            MessageDialogBuilder builder = new MessageDialogBuilder();
            builder.setTitle("La sesión ha caducado")
                    .setMessage("Para seguir utilizando la aplicación, por favor vuelve a iniciar sesión.")
                    .setPrimaryButtonText("Aceptar")
                    .setCancelable(false);
            MessageDialog messageDialog = new MessageDialog(MainActivity.this, builder);
            messageDialog.setPrimaryButtonListener(v -> {
                messageDialog.dismiss();
            });
            messageDialog.setOnDismissListener(dialog -> {
                UserPreferences.clearConnect(MainActivity.this);
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            });
            messageDialog.show();
        }
    }

    public ArrayList<Volunteer> getLocalVolunteers() {
        return localVolunteers;
    }

    public ArrayList<Volunteer> getRemoteVolunteers() {
        return remoteVolunteers;
    }

}