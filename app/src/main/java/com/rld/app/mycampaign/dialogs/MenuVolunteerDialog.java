package com.rld.app.mycampaign.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.rld.app.mycampaign.databinding.FragmentMenuVolunteerDialogBinding;

public class MenuVolunteerDialog extends Dialog {

    private FragmentMenuVolunteerDialogBinding binding;

    private int volunteers;

    private View.OnClickListener btnAddVolunteerListener;
    private View.OnClickListener btnUploadListener;

    public MenuVolunteerDialog(@NonNull Activity activity, int volunteers)
    {
        super(activity);
        this.volunteers = volunteers;
    }

    public MenuVolunteerDialog(@NonNull Context context, int volunteers)
    {
        super(context);
        this.volunteers = volunteers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = FragmentMenuVolunteerDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialButton btnAddVolunteer = binding.btnAddVolunteer;
        MaterialButton btnUpload = binding.btnUpload;

        if ( volunteers == 0 ) {
            btnUpload.setEnabled(false);
        } else {
            btnUpload.setEnabled(true);
            btnUpload.setText("Cargar al servidor (" + volunteers + ")");
        }

        btnAddVolunteer.setOnClickListener(btnAddVolunteerListener);
        btnUpload.setOnClickListener(btnUploadListener);

        setCancelable(true);
    }

    public void setBtnAddVolunteerListener(View.OnClickListener btnAddVolunteerListener) {
        this.btnAddVolunteerListener = btnAddVolunteerListener;
    }

    public void setBtnUploadListener(View.OnClickListener btnUploadListener) {
        this.btnUploadListener = btnUploadListener;
    }

}
