package com.rld.app.mycampaign.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.rld.app.mycampaign.databinding.DialogErrorMessageBinding;

public class ErrorMessageDialog extends Dialog {

    private DialogErrorMessageBinding binding;

    private String title;
    private String message;
    private String error;
    private Spanned spannedError;
    private String buttonText;
    private boolean isCancelable;

    private View.OnClickListener buttonClickListener;

    public ErrorMessageDialog(@NonNull Activity activity, ErrorMessageDialogBuilder builder) {
        super(activity);
        title = builder.getTitle();
        message = builder.getMessage();
        error = builder.getError();
        spannedError = builder.getSpannedError();
        isCancelable = builder.isCancelable();
        buttonText = builder.getButtonText();
    }

    public ErrorMessageDialog(@NonNull Context context, ErrorMessageDialogBuilder builder) {
        super(context);
        title = builder.getTitle();
        message = builder.getMessage();
        error = builder.getError();
        spannedError = builder.getSpannedError();
        buttonText = builder.getButtonText();
        isCancelable = builder.isCancelable();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = DialogErrorMessageBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtTitle = binding.txtTitle;
        TextView txtMessage = binding.txtMessage;
        TextView txtError = binding.txtError;
        MaterialButton btnAction = binding.btnAction;

        if ( spannedError != null ) {
            txtError.setText(spannedError);
        } else {
            txtError.setText(error);
        }

        if ( buttonText != null && !buttonText.isEmpty() ) {
            btnAction.setText(buttonText);
            btnAction.setOnClickListener(buttonClickListener);
        }

        txtTitle.setText(title);
        txtMessage.setText(message);

        txtError.setMovementMethod(new ScrollingMovementMethod());

        setCancelable(isCancelable);
    }

    public void setButtonClickListener(View.OnClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

}

