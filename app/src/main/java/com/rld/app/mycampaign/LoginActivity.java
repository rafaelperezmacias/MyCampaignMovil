package com.rld.app.mycampaign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.api.AuthAPI;
import com.rld.app.mycampaign.api.Client;
import com.rld.app.mycampaign.databinding.ActivityLoginBinding;
import com.rld.app.mycampaign.models.Token;
import com.rld.app.mycampaign.models.User;
import com.rld.app.mycampaign.models.api.LoginResponse;
import com.rld.app.mycampaign.models.api.UserRequest;
import com.rld.app.mycampaign.preferences.TokenPreferences;
import com.rld.app.mycampaign.preferences.UserPreferences;
import com.rld.app.mycampaign.secrets.AppConfig;
import com.rld.app.mycampaign.utils.Internet;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

import java.util.Timer;
import java.util.TimerTask;

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
                                saveUserInfo(response.body(), userRequest.getPassword());
                                Toast.makeText(LoginActivity.this, "Bienvenido, " + response.body().getUser().getName() + "!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
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

    private void saveUserInfo(LoginResponse loginResponse, String password) {
        User user = loginResponse.getUser().toUser(password);
        UserPreferences.saveUser(getApplicationContext(), user);
        Token token = loginResponse.getTokenObject();
        TokenPreferences.saveToken(getApplicationContext(), token);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ( UserPreferences.isUserLogin(getApplicationContext()) ) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

}