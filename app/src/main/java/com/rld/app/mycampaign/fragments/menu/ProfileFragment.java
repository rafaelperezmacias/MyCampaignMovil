package com.rld.app.mycampaign.fragments.menu;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.rld.app.mycampaign.MainActivity;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.databinding.FragmentProfileBinding;
import com.rld.app.mycampaign.models.Campaign;
import com.rld.app.mycampaign.models.User;
import com.rld.app.mycampaign.preferences.CampaignPreferences;
import com.rld.app.mycampaign.preferences.UserPreferences;

import de.hdodenhof.circleimageview.CircleImageView;

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
        CircleImageView imageProfile = binding.imageProfile;
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
        byte[] decodeImage = Base64.decode(user.getProfileImage(), Base64.DEFAULT);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodeImage, 0, decodeImage.length);
        imageProfile.setImageBitmap(imageBitmap);
        Campaign campaign = CampaignPreferences.getCampaign(requireContext());
        txtCampaign.setText(campaign.getName());
        txtParty.setText(campaign.getParty());
        txtStartDate.setText(campaign.getStart_date());
        if ( campaign.getDescription().isEmpty() ) {
            txtDescription.setText("Sin descripción");
        } else {
            txtDescription.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black));
            txtDescription.setText(campaign.getDescription());
            txtDescription.setMovementMethod(new ScrollingMovementMethod());
        }

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