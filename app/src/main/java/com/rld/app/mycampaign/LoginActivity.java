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
import com.rld.app.mycampaign.databinding.ActivityLoginBinding;
import com.rld.app.mycampaign.dialogs.MessageDialog;
import com.rld.app.mycampaign.dialogs.MessageDialogBuilder;
import com.rld.app.mycampaign.models.Token;
import com.rld.app.mycampaign.models.User;
import com.rld.app.mycampaign.models.Campaign;
import com.rld.app.mycampaign.models.api.LoginResponse;
import com.rld.app.mycampaign.models.api.UserRequest;
import com.rld.app.mycampaign.preferences.CampaignPreferences;
import com.rld.app.mycampaign.preferences.TokenPreferences;
import com.rld.app.mycampaign.preferences.UserPreferences;
import com.rld.app.mycampaign.secrets.AppConfig;
import com.rld.app.mycampaign.utils.Internet;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class LoginActivity extends AppCompatActivity {

    private static final int EMAIL_MAX_LIMIT = 255;
    private static final int PASSWORD_MAX_LIMIT = 16;

    private ActivityLoginBinding binding;

    private TextInputLayout lytEmail;
    private TextInputLayout lytPassword;

    private LinearLayout lytLoad;
    private MaterialCardView cardError;

    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_FuturoApp);

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
                        });
                        return;
                    }
                    UserRequest userRequest = new UserRequest();
                    userRequest.setEmail(lytEmail.getEditText().getText().toString().trim());
                    userRequest.setPassword(lytPassword.getEditText().getText().toString().trim());
                    Call<LoginResponse> loginCall = Client.getClient(AppConfig.URL_SERVER).create(AuthAPI.class)
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

    private void getCurrentCampaign(User user, Token token) {
        Call<Campaign> campaignCall = Client.getClient(AppConfig.URL_SERVER).create(CampaignAPI.class)
                .getCurrentCampaign(token.toAPIRequest());
        campaignCall.enqueue(new Callback<Campaign>() {
            @Override
            public void onResponse(Call<Campaign> call, Response<Campaign> response) {
                if ( response.code() == 204 ) {
                    MessageDialogBuilder builder = new MessageDialogBuilder();
                    builder.setTitle("Campaña invalida")
                            .setMessage("Actualmente el sistema no cuenta con una campaña activa. ¿Es un error? Comentalo a los administradores para que te brinden ayuda")
                            .setPrimaryButtonText("Ok")
                            .setCancelable(false);
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
                if ( response.isSuccessful() ) {
                    Campaign campaign = response.body();
                    getImageProfile(user, token, campaign);
                    /* CampaignPreferences.saveCampaign(LoginActivity.this, campaign);
                    UserPreferences.saveUser(getApplicationContext(), user);
                    TokenPreferences.saveToken(getApplicationContext(), token);
                    Toast.makeText(LoginActivity.this, "Bienvenido, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class)); */
                    return;
                }
            }
            @Override
            public void onFailure(Call<Campaign> call, Throwable t) {

            }
        });
    }

    private void getImageProfile(User user, Token token, Campaign campaign) {
        try {
            Call<ResponseBody> imageCall = Client.getClientForImage().create(ImageAPI.class)
                    .getProfileImage(
                            URLEncoder.encode(user.getName(), "UTF-8"),
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
                            CampaignPreferences.saveCampaign(LoginActivity.this, campaign);
                            UserPreferences.saveUser(getApplicationContext(), user);
                            TokenPreferences.saveToken(getApplicationContext(), token);
                            Toast.makeText(LoginActivity.this, "Bienvenido, " + user.getName() + "!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } catch (IOException ex) {
                            Log.e("", "" + ex.getMessage());
                        }
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch ( Exception ex ) {

        }
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