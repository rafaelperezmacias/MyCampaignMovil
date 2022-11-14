package com.rld.app.mycampaign.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.rld.app.mycampaign.databinding.FragmentProgressDialogBinding;

public class ProgressDialog extends Dialog {

    private FragmentProgressDialogBinding binding;

    private String title;

    private boolean isCancelable;

    public ProgressDialog(@NonNull Activity activity, ProgressDialogBuilder builder)
    {
        super(activity);
        title = builder.getTitle();
        isCancelable = builder.isCancelable();
    }

    public ProgressDialog(@NonNull Context context, ProgressDialogBuilder builder)
    {
        super(context);
        title = builder.getTitle();
        isCancelable = builder.isCancelable();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        binding = FragmentProgressDialogBinding.inflate(getLayoutInflater());
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtTitle = binding.txtTitle;

        txtTitle.setText(title);

        setCancelable(isCancelable);
    }

}
