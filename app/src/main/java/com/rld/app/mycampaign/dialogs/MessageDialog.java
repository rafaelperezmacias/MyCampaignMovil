package com.rld.app.mycampaign.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.button.MaterialButton;
import com.rld.app.mycampaign.databinding.FragmentActionDialogBinding;

public class MessageDialog extends Dialog {

    private FragmentActionDialogBinding binding;

    private String title;
    private String message;
    private String primaryButtonText;
    private String secondaryButtonText;

    private View.OnClickListener primaryButtonListener;
    private View.OnClickListener secondaryButtonListener;

    private boolean isCancelable;

    public MessageDialog(@NonNull Activity activity, @NonNull MessageDialogBuilder builder)
    {
        super(activity);
        title = builder.getTitle();
        message = builder.getMessage();
        primaryButtonText = builder.getPrimaryButtonText();
        secondaryButtonText = builder.getSecondaryButtonText();
        isCancelable = builder.isCancelable();
    }

    public MessageDialog(@NonNull Context context, @NonNull MessageDialogBuilder builder)
    {
        super(context);
        title = builder.getTitle();
        message = builder.getMessage();
        primaryButtonText = builder.getPrimaryButtonText();
        secondaryButtonText = builder.getSecondaryButtonText();
        isCancelable = builder.isCancelable();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        binding = FragmentActionDialogBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtTitle = binding.txtTitle;
        TextView txtMessage = binding.txtMessage;
        MaterialButton btnPrimaryAction = binding.btnDialogPrimary;
        MaterialButton btnSecondatyAction = binding.btnDialogSecondary;

        txtTitle.setText(title);
        txtMessage.setText(message);

        if ( primaryButtonText != null && !primaryButtonText.isEmpty() &&  secondaryButtonText != null && !secondaryButtonText.isEmpty() ) {
            btnPrimaryAction.setText(primaryButtonText);
            btnPrimaryAction.setOnClickListener(primaryButtonListener);
            btnSecondatyAction.setText(secondaryButtonText);
            btnSecondatyAction.setOnClickListener(secondaryButtonListener);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            btnPrimaryAction.setLayoutParams(params);
            btnSecondatyAction.setLayoutParams(params);
        } else if ( primaryButtonText != null && !primaryButtonText.isEmpty() ) {
            btnSecondatyAction.setText(primaryButtonText);
            btnSecondatyAction.setOnClickListener(primaryButtonListener);
            btnPrimaryAction.setVisibility(View.GONE);
        } else if ( secondaryButtonText != null && !secondaryButtonText.isEmpty() ) {
            btnSecondatyAction.setText(secondaryButtonText);
            btnSecondatyAction.setOnClickListener(secondaryButtonListener);
            btnPrimaryAction.setVisibility(View.GONE);
        }else {
            btnPrimaryAction.setVisibility(View.GONE);
            btnSecondatyAction.setVisibility(View.GONE);
        }


        setCancelable(isCancelable);
    }

    public void setPrimaryButtonListener(View.OnClickListener primaryButtonListener) {
        this.primaryButtonListener = primaryButtonListener;
    }

    public void setSecondaryButtonListener(View.OnClickListener secondaryButtonListener) {
        this.secondaryButtonListener = secondaryButtonListener;
    }

}
