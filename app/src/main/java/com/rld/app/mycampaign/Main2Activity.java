package com.rld.app.mycampaign;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.google.android.material.snackbar.Snackbar;
import com.rld.app.mycampaign.files.FileManager;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.Section;
import com.rld.app.mycampaign.models.Volunteer;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    private Toolbar toolbar;

    private ArrayList<Volunteer> volunteers;

    private AlertDialog alertDialog;

    private int peticiones;
    private int cont_peticiones;
    private int cont_peticiones_pos;

    private Button btnCarga;
    private Button btnCrear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnCarga = findViewById(R.id.btnCarga);
        btnCrear = findViewById(R.id.btnTest);

//
//        btnCarga.setOnClickListener(v -> {
//            btnCarga.setEnabled(false);
//            AlertDialog.Builder alertDialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this)
//                    .setTitle("Alerta")
//                    .setMessage("Subiendo datos al servidor. Favor de no cerrar la aplicación.")
//                    .setCancelable(false);
//            alertDialog = alertDialogBuilder.create();
//            alertDialog.setOnShowListener(dialog -> {
//                if ( !Internet.isNetDisponible(MainActivity.this) ) {
//                    createSnackBar("Sin acceso a internet, verifique su conexión.");
//                    btnCarga.setEnabled(true);
//                    alertDialog.dismiss();
//                    return;
//                }
//                JSONObject jsonBD = new JSONObject();
//                fileManager.readFile(MainActivity.this);
//                jsonBD = fileManager.getJson();
//                if (jsonBD == null) {
//                    return;
//                }
//                try {
//                    JSONArray ja_data = jsonBD.getJSONArray("users");
//                    int arraySize = ja_data.length();
//                    peticiones = arraySize;
//                    for (int i = 0; i < arraySize; i++) {
//                        try {
//                            Thread.sleep(100);
//                        } catch (InterruptedException ignored) {
//
//                        }
//                        JsonObjectRequest request = RequestManager.request(ja_data.getJSONObject(i), MainActivity.this);
//                        if (request == null) {
//                            continue;
//                        }
//                        requestQueue.add(request);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            });
//            alertDialog.show();
//        });
//
    }

    public void deleteFromServer( String electorKey, boolean isInsert ) {
        int index = -1;
        for ( int i = 0; i < volunteers.size(); i++ ) {
            if ( volunteers.get(i).getElectorKey().equals(electorKey) ) {
                index = i;
                break;
            }
        }
        if ( index != -1 ) {
            volunteers.remove(index);
        }
        if ( volunteers.size() > 0 ) {
            btnCarga.setVisibility(View.VISIBLE);
            btnCarga.setText("Carga al servidor (" + volunteers.size()+")");
        } else {
            btnCarga.setVisibility(View.GONE);
        }
        cont_peticiones++;
        if ( isInsert ) {
            cont_peticiones_pos++;
        }
        if ( cont_peticiones == peticiones ) {
            alertDialog.dismiss();
            if ( cont_peticiones_pos == peticiones ) {
                btnCarga.setEnabled(true);
            } else {
                btnCarga.setEnabled(true);
                // ArrayList<Volunteer> tmp = new ArrayList<>(volunteers);
                // fileManager.saveFile(tmp, getApplicationContext());
                // volunteers = fileManager.readFile(Main2Activity.this);
            }
            cont_peticiones = 0;
            cont_peticiones_pos = 0;
        }
    }

}