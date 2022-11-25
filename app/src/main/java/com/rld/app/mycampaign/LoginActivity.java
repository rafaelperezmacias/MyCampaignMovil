package com.rld.app.mycampaign;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.databinding.ActivityLoginBinding;
import com.rld.app.mycampaign.secrets.AppConfig;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.ml.EM;
import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private static final int EMAIL_MAX_LIMIT = 70;
    private static final int PASSWORD_MAX_LIMIT = 16;

    private ActivityLoginBinding binding;

    private TextInputLayout lytEmail;
    private TextInputLayout lytPassowrd;

    private MaterialButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lytEmail = binding.lytEmail;
        lytPassowrd = binding.lytPassword;
        btnLogin = binding.btnLogin;

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        lytEmail.getEditText().setOnFocusChangeListener((view, focus) -> {
            if (focus) {
                lytEmail.setHint("");
            } else if (lytEmail.getEditText().getText().toString().trim().isEmpty()) {
                lytEmail.setHint("Ingrese su correo electónico");
            }
        });

        lytPassowrd.getEditText().setOnFocusChangeListener((view, focus) -> {
            if (focus) {
                lytPassowrd.setHint("");
            } else if (lytPassowrd.getEditText().getText().toString().trim().isEmpty()) {
                lytPassowrd.setHint("Ingrese su contraseña");
            }
        });

        btnLogin.setOnClickListener(view -> {
            if ( !isEmailComplete() | !isPasswordComplete() ) {
                return;
            }
            JSONObject bodyRequest = new JSONObject();
            JSONObject userObject = new JSONObject();
            try {
                userObject.put("email", lytEmail.getEditText().getText().toString().trim());
                userObject.put("password", lytPassowrd.getEditText().getText().toString().trim());
                bodyRequest.put("user", userObject);
            } catch ( JSONException ex) {

            }
            String url = AppConfig.LOGIN;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, bodyRequest, response -> {

            }, error -> {

            });
            request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);
        });

        TextInputLayoutUtils.initializeFilters(lytEmail.getEditText(), false, EMAIL_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytPassowrd.getEditText(), false, PASSWORD_MAX_LIMIT);
    }

    private boolean isEmailComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytEmail, "Ingrese su correo electronico", null, getApplicationContext())
                && TextInputLayoutUtils.isValidEmail(lytEmail, null, getApplicationContext());
    }

    private boolean isPasswordComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytPassowrd, "Ingrese su contraseña", null, getApplicationContext());
    }

}