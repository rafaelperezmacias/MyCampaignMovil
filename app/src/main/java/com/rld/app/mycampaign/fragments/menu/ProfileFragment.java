package com.rld.app.mycampaign.fragments.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.rld.app.mycampaign.databinding.FragmentProfileBinding;
import com.rld.app.mycampaign.models.User;
import com.rld.app.mycampaign.preferences.UserPreferences;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView txtName = binding.txtName;
        TextView txtEmail = binding.txtEmail;
        TextView txtCampaign = binding.txtCampaign;
        TextView txtParty = binding.txtParty;
        TextView txtStartDate = binding.txtStartDate;
        TextView txtEndDate = binding.txtEndDate;
        TextView txtDescription = binding.txtDescription;
        TextView txtTotalVolunteers = binding.txtTotalVolunteers;
        TextView txtTotalServerVolunteers = binding.txtTotalServerVolunteers;
        TextView txtTotalLocalVolunteers = binding.txtTotalLocalVolunteers;
        MaterialCardView cardAuthorized = binding.cardAuthorized;
        MaterialCardView cardNoAuthorized = binding.cardNoAuthorized;

        User user = UserPreferences.getUser(getContext());
        txtName.setText(user.getName());
        txtEmail.setText(user.getEmail());

        return root;
    }

}