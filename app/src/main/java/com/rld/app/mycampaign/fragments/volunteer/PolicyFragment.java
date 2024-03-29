package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.databinding.FragmentPolicyVolunteerBsBinding;

public class PolicyFragment extends Fragment {

    private FragmentPolicyVolunteerBsBinding binding;
    private CheckBox btnChecked;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPolicyVolunteerBsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnChecked = binding.btnChecked;
        TextView txtPrivacyPolicy = binding.txtPrivacyPolicy;

        txtPrivacyPolicy.setText(R.string.aviso_privacidad);

        return root;
    }

    public boolean isComplete() {
        if ( !btnChecked.isChecked() ) {
            Toast.makeText(getContext(), "Lea y confirme las politicas de privacidad", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
