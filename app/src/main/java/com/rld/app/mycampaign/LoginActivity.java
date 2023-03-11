package com.rld.app.mycampaign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.api.AuthAPI;
import com.rld.app.mycampaign.api.CampaignAPI;
import com.rld.app.mycampaign.api.Client;
import com.rld.app.mycampaign.api.ImageAPI;
import com.rld.app.mycampaign.api.VolunteerAPI;
import com.rld.app.mycampaign.databinding.ActivityLoginBinding;
import com.rld.app.mycampaign.dialogs.ErrorMessageDialog;
import com.rld.app.mycampaign.dialogs.ErrorMessageDialogBuilder;
import com.rld.app.mycampaign.dialogs.MessageDialog;
import com.rld.app.mycampaign.dialogs.MessageDialogBuilder;
import com.rld.app.mycampaign.files.VolunteerFileManager;
import com.rld.app.mycampaign.models.Campaign;
import com.rld.app.mycampaign.models.Token;
import com.rld.app.mycampaign.models.User;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.models.api.LoginResponse;
import com.rld.app.mycampaign.models.api.ServerVolunteer;
import com.rld.app.mycampaign.models.api.UserRequest;
import com.rld.app.mycampaign.preferences.CampaignPreferences;
import com.rld.app.mycampaign.preferences.TokenPreferences;
import com.rld.app.mycampaign.preferences.UserPreferences;
import com.rld.app.mycampaign.secrets.AppConfig;
import com.rld.app.mycampaign.utils.Internet;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final int EMAIL_MAX_LIMIT = 255;
    private static final int PASSWORD_MAX_LIMIT = 16;

    private ActivityLoginBinding binding;

    private TextInputLayout lytEmail;
    private TextInputLayout lytPassword;

    private LinearLayout lytLoad;
    private MaterialCardView cardError;

    private MaterialButton btnLogin;

    private static final int UNKNOWN_ERROR = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lytEmail = binding.lytEmail;
        lytPassword = binding.lytPassword;
        btnLogin = binding.btnLogin;

        lytLoad = binding.lytLoad;
        cardError = binding.cardError;

        if ( UserPreferences.isUserLogin(getApplicationContext()) ) {
            User user = UserPreferences.getUser(getApplicationContext());
            lytEmail.getEditText().setText(user.getEmail());
            lytEmail.setHint("");
            TextInputLayoutUtils.cursorToEnd(lytEmail.getEditText());
        }

        lytEmail.getEditText().setOnFocusChangeListener((view, focus) -> {
            if ( focus ) {
                lytEmail.setHint("");
            } else if (lytEmail.getEditText().getText().toString().trim().isEmpty()) {
                lytEmail.setHint("Ingrese su correo electónico");
            }
        });

        lytPassword.getEditText().setOnFocusChangeListener((view, focus) -> {
            if ( focus ) {
                lytPassword.setHint("");
            } else if (lytPassword.getEditText().getText().toString().trim().isEmpty()) {
                lytPassword.setHint("Ingrese su contraseña");
            }
        });

        btnLogin.setOnClickListener(view -> {
            if ( !isEmailComplete() | !isPasswordComplete() ) {
                return;
            }
            lytLoad.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            cardError.setVisibility(View.GONE);
            lytEmail.getEditText().setEnabled(false);
            lytPassword.getEditText().setEnabled(false);
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if ( !Internet.isNetworkAvailable(LoginActivity.this) || !Internet.isOnlineNetwork() ) {
                        LoginActivity.this.runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "Internet no disponible", Toast.LENGTH_SHORT).show();
                            lytLoad.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            lytEmail.getEditText().setEnabled(true);
                            lytPassword.getEditText().setEnabled(true);
                        });
                        return;
                    }
                    UserRequest userRequest = new UserRequest();
                    userRequest.setEmail(lytEmail.getEditText().getText().toString().trim());
                    userRequest.setPassword(lytPassword.getEditText().getText().toString().trim());
                    Call<LoginResponse> loginCall = Client.getClient().create(AuthAPI.class)
                            .login(userRequest);
                    loginCall.enqueue(new Callback<LoginResponse>() {
                        @Override
                        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                            if ( response.isSuccessful() ) {
                                assert response.body() != null;
                                User user = response.body().getUser().toUser(userRequest.getPassword(), null, true);
                                Token token = response.body().getTokenObject();
                                getCurrentCampaign(user, token);
                                return;
                            }
                            if ( response.code() == 401 ) {
                                cardError.setVisibility(View.VISIBLE);
                                lytLoad.setVisibility(View.GONE);
                                btnLogin.setVisibility(View.VISIBLE);
                                lytEmail.getEditText().setEnabled(true);
                                lytPassword.getEditText().setEnabled(true);
                                return;
                            }
                            try {
                                showErrorRequest(response.code(), response.message(),  "Error con la petición del login", response.errorBody().string());
                            } catch ( IOException ex ) {
                                showErrorRequest(UNKNOWN_ERROR, "IOException", "Error con la petición del login", ex.getMessage());
                            }
                            lytLoad.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            lytEmail.getEditText().setEnabled(true);
                            lytPassword.getEditText().setEnabled(true);
                        }
                        @Override
                        public void onFailure(Call<LoginResponse> call, Throwable t) {
                            lytLoad.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            lytEmail.getEditText().setEnabled(true);
                            lytPassword.getEditText().setEnabled(true);
                            if ( t instanceof SocketTimeoutException ) {
                                showErrorRequest(UNKNOWN_ERROR, "Exception", "Error con la petición del login", "SocketTimeoutException");
                            } else {
                                showErrorRequest(UNKNOWN_ERROR, "Exception", "Error con la petición del login", t.getMessage());
                            }
                        }
                    });
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 500);
        });

        TextInputLayoutUtils.initializeFilters(lytEmail.getEditText(), false, EMAIL_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytPassword.getEditText(), false, PASSWORD_MAX_LIMIT);

    }

    private boolean isEmailComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytEmail, "Ingrese su correo electronico", null, getApplicationContext())
                && TextInputLayoutUtils.isValidEmail(lytEmail, null, getApplicationContext());
    }

    private boolean isPasswordComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytPassword, "Ingrese su contraseña", null, getApplicationContext());
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
        ErrorMessageDialog errorMessageDialog = new ErrorMessageDialog(LoginActivity.this, builder);
        errorMessageDialog.setButtonClickListener(v -> errorMessageDialog.dismiss());
        errorMessageDialog.show();
    }

    private void getCurrentCampaign(User user, Token token) {
        Call<Campaign> campaignCall = Client.getClient().create(CampaignAPI.class)
                .getCurrentCampaign(token.toAPIRequest());
        campaignCall.enqueue(new Callback<Campaign>() {
            @Override
            public void onResponse(Call<Campaign> call, Response<Campaign> response) {
                if ( response.isSuccessful() ) {
                    Campaign campaign = response.body();
                    getImageProfile(user, token, campaign);
                    return;
                }
                if ( response.code() == 204 ) {
                    MessageDialogBuilder builder = new MessageDialogBuilder();
                    builder.setTitle("Campaña invalida")
                            .setMessage("Actualmente el sistema no cuenta con una campaña activa. ¿Es un error? Comentalo a los administradores para que te brinden ayuda")
                            .setPrimaryButtonText("Ok")
                            .setCancelable(true);
                    MessageDialog messageDialog = new MessageDialog(LoginActivity.this, builder);
                    messageDialog.setPrimaryButtonListener(v -> messageDialog.dismiss());
                    messageDialog.setOnDismissListener(dialog -> {
                        lytLoad.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                        lytEmail.getEditText().setEnabled(true);
                        lytPassword.getEditText().setEnabled(true);
                    });
                    messageDialog.show();
                    return;
                }
                try {
                    showErrorRequest(response.code(), response.message(),  "Error con la petición de la campaña", response.errorBody().string());
                } catch ( IOException ex ) {
                    showErrorRequest(UNKNOWN_ERROR, "IOException", "Error con la petición de la campaña", ex.getMessage());
                }
                lytLoad.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                lytEmail.getEditText().setEnabled(true);
                lytPassword.getEditText().setEnabled(true);
            }
            @Override
            public void onFailure(Call<Campaign> call, Throwable t) {
                showErrorRequest(UNKNOWN_ERROR, "Exception", "Error con la petición de la campaña", t.getMessage());
                lytLoad.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                lytEmail.getEditText().setEnabled(true);
                lytPassword.getEditText().setEnabled(true);
            }
        });
    }

    private void getImageProfile(User user, Token token, Campaign campaign) {
        Call<ResponseBody> imageCall = Client.getClientForImage().create(ImageAPI.class)
                .getProfileImage(
                        user.getName(),
                        "7F9CF5",
                        "EBF4FF",
                        "512",
                        "0.33"
                );
        imageCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if ( response.isSuccessful() ) {
                    try {
                        byte[] imageByte = response.body().bytes();
                        String base64 = Base64.encodeToString(imageByte, Base64.DEFAULT);
                        user.setProfileImage(base64);
                        getRemoteVolunteers(user, token, campaign);
                    } catch ( IOException ex ) {
                        showErrorRequest(UNKNOWN_ERROR, "IOException", "Error con la petición de la imagen", ex.getMessage());
                        lytLoad.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                        lytEmail.getEditText().setEnabled(true);
                        lytPassword.getEditText().setEnabled(true);
                    }
                    return;
                }
                try {
                    showErrorRequest(response.code(), response.message(),  "Error con la petición de la imagen", response.errorBody().string());
                    lytLoad.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                    lytEmail.getEditText().setEnabled(true);
                    lytPassword.getEditText().setEnabled(true);
                } catch ( IOException ex ) {
                    showErrorRequest(UNKNOWN_ERROR, "IOException", "Error con la petición de la imagen", ex.getMessage());
                    lytLoad.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                    lytEmail.getEditText().setEnabled(true);
                    lytPassword.getEditText().setEnabled(true);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showErrorRequest(UNKNOWN_ERROR, "Exception", "Error con la petición de la campaña", t.getMessage());
                lytLoad.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                lytEmail.getEditText().setEnabled(true);
                lytPassword.getEditText().setEnabled(true);
            }
        });
    }

    private void getRemoteVolunteers(User user, Token token, Campaign campaign) {
        Call<ArrayList<ServerVolunteer>> getVolunteersCall = Client.getClient().create(VolunteerAPI.class)
                .getVolunteers(
                        token.toAPIRequest()
                );
        getVolunteersCall.enqueue(new Callback<ArrayList<ServerVolunteer>>() {
            @Override
            public void onResponse(Call<ArrayList<ServerVolunteer>> call, Response<ArrayList<ServerVolunteer>> response) {
                if ( response.isSuccessful() ) {
                    UserPreferences.saveUser(getApplicationContext(), user);
                    CampaignPreferences.saveCampaign(LoginActivity.this, campaign);
                    TokenPreferences.saveToken(getApplicationContext(), token);
                    ArrayList<Volunteer> volunteers = new ArrayList<>();
                    for ( ServerVolunteer serverVolunteer : response.body() ) {
                        volunteers.add(serverVolunteer.toVolunteer());
                    }
                    VolunteerFileManager.writeJSON(volunteers, false, getApplicationContext());
                    Toast.makeText(LoginActivity.this, "Bienvenido, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    return;
                }
                try {
                    showErrorRequest(response.code(), response.message(),  "Error con la petición de volutnarios", response.errorBody().string());
                    lytLoad.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                    lytEmail.getEditText().setEnabled(true);
                    lytPassword.getEditText().setEnabled(true);
                } catch ( IOException ex ) {
                    showErrorRequest(UNKNOWN_ERROR, "IOException", "Error con la petición de volutnarios", ex.getMessage());
                    lytLoad.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                    lytEmail.getEditText().setEnabled(true);
                    lytPassword.getEditText().setEnabled(true);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<ServerVolunteer>> call, Throwable t) {
                showErrorRequest(UNKNOWN_ERROR, "Exception", "Error con la petición de volutnarios", t.getMessage());
                lytLoad.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                lytEmail.getEditText().setEnabled(true);
                lytPassword.getEditText().setEnabled(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ( UserPreferences.isUserLogin(getApplicationContext()) && UserPreferences.isUserConnect(getApplicationContext()) ) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

}