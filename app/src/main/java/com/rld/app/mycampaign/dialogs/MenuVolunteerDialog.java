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

    private View.OnClickListener btnAddVolunteerListener;
    private View.OnClickListener btnUploadListener;

    public MenuVolunteerDialog(@NonNull Activity activity)
    {
        super(activity);
    }

    public MenuVolunteerDialog(@NonNull Context context)
    {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = FragmentMenuVolunteerDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialButton btnAddVolunteer = binding.btnAddVolunteer;
        MaterialButton btnUpload = binding.btnUpload;

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
