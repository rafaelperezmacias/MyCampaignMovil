package com.rld.app.mycampaign.fragments.menu;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.rld.app.mycampaign.MainActivity;
import com.rld.app.mycampaign.databinding.FragmentProfileBinding;
import com.rld.app.mycampaign.models.Campaign;
import com.rld.app.mycampaign.models.User;
import com.rld.app.mycampaign.preferences.CampaignPreferences;
import com.rld.app.mycampaign.preferences.UserPreferences;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    private MainActivity mainActivity;

    private TextView txtTotalVolunteers;
    private TextView txtTotalServerVolunteers;
    private TextView txtTotalLocalVolunteers;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView txtName = binding.txtName;
        TextView txtEmail = binding.txtEmail;
        TextView txtCampaign = binding.txtCampaign;
        TextView txtParty = binding.txtParty;
        TextView txtStartDate = binding.txtStartDate;
        TextView txtDescription = binding.txtDescription;
        txtTotalVolunteers = binding.txtTotalVolunteers;
        txtTotalServerVolunteers = binding.txtTotalServerVolunteers;
        txtTotalLocalVolunteers = binding.txtTotalLocalVolunteers;

        Activity parentActivity = getActivity();
        if ( parentActivity instanceof MainActivity) {
            mainActivity = (MainActivity) parentActivity;
        }

        User user = UserPreferences.getUser(requireContext());
        txtName.setText(user.getName());
        txtEmail.setText(user.getEmail());
        Campaign campaign = CampaignPreferences.getCampaign(requireContext());
        txtCampaign.setText(campaign.getName());
        txtParty.setText(campaign.getParty());
        txtStartDate.setText(campaign.getStart_date());
        txtDescription.setText(campaign.getDescription());
        txtDescription.setMovementMethod(new ScrollingMovementMethod());

        if ( mainActivity != null ) {
            txtTotalLocalVolunteers.setText(String.valueOf(mainActivity.getLocalVolunteers().size()));
            txtTotalServerVolunteers.setText(String.valueOf(mainActivity.getRemoteVolunteers().size()));
            txtTotalVolunteers.setText(String.valueOf(mainActivity.getLocalVolunteers().size() + mainActivity.getRemoteVolunteers().size()));
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if ( mainActivity != null ) {
            txtTotalLocalVolunteers.setText(String.valueOf(mainActivity.getLocalVolunteers().size()));
            txtTotalServerVolunteers.setText(String.valueOf(mainActivity.getRemoteVolunteers().size()));
            txtTotalVolunteers.setText(String.valueOf(mainActivity.getLocalVolunteers().size() + mainActivity.getRemoteVolunteers().size()));
        }
    }



}