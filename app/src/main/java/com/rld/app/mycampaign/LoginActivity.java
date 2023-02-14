package com.rld.app.mycampaign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.databinding.ActivityLoginBinding;
import com.rld.app.mycampaign.secrets.AppConfig;
import com.rld.app.mycampaign.utils.Internet;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    private static final int EMAIL_MAX_LIMIT = 70;
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

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        lytEmail.clearFocus();
        lytEmail.getEditText().setOnFocusChangeListener((view, focus) -> {
            if (focus) {
                lytEmail.setHint("");
            } else if (lytEmail.getEditText().getText().toString().trim().isEmpty()) {
                lytEmail.setHint("Ingrese su correo electónico");
            }
        });

        lytPassword.getEditText().setOnFocusChangeListener((view, focus) -> {
            if (focus) {
                lytPassword.setHint("");
            } else if (lytPassword.getEditText().getText().toString().trim().isEmpty()) {
                lytPassword.setHint("Ingrese su contraseña");
            }
        });

        btnLogin.setOnClickListener(view -> {
            if (!isEmailComplete() | !isPasswordComplete()) {
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
                    if ( !Internet.isNetDisponible(LoginActivity.this) && !Internet.isOnlineNet() ) {
                        LoginActivity.this.runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this, "Internet no disponible", Toast.LENGTH_SHORT).show();
                            lytLoad.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                        });
                        return;
                    }
                    JSONObject bodyRequest = new JSONObject();
                    JSONObject userObject = new JSONObject();
                    try {
                        userObject.put("email", lytEmail.getEditText().getText().toString().trim());
                        userObject.put("password", lytPassword.getEditText().getText().toString().trim());
                        bodyRequest.put("user", userObject);
                    } catch (JSONException ignored) {
                    }
                    String url = AppConfig.LOGIN;
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, bodyRequest, response -> {
                        lytLoad.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                        lytEmail.getEditText().setEnabled(true);
                        lytPassword.getEditText().setEnabled(true);
                        try {
                            if (response.getBoolean("success")) {
                                cardError.setVisibility(View.GONE);
                                saveSympathizerData(response.getJSONObject("user"));
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                cardError.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException ignored) {
                        }
                    }, error -> {
                        lytEmail.getEditText().setEnabled(true);
                        lytPassword.getEditText().setEnabled(true);
                        lytLoad.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "" + error.getMessage(), Toast.LENGTH_LONG).show();
                    });
                    request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(request);
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

    private void saveSympathizerData(JSONObject user) {
        try {
            JSONObject userable = user.getJSONObject("userable");
            getSharedPreferences("sessions", Context.MODE_PRIVATE).edit().putString("sympathizerId", userable.getString("id")).apply();
            JSONArray campaignsArray = userable.getJSONArray("campaigns");
            if ( campaignsArray.length() > 0 ) {
                JSONObject campaignObject = campaignsArray.getJSONObject(0);
                getSharedPreferences("campaign", Context.MODE_PRIVATE).edit().putString("id", campaignObject.getString("id")).apply();
            }
        } catch ( JSONException ex ) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String id = getSharedPreferences("sessions", Context.MODE_PRIVATE).getString("sympathizerId", null);
        if ( id != null ) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

}