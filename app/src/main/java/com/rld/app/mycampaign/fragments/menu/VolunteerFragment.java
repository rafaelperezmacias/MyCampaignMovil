package com.rld.app.mycampaign.fragments.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.databinding.FragmentVolunteerBinding;

public class VolunteerFragment extends Fragment {

    private FragmentVolunteerBinding binding;

    private static OnClickAddVolunteer onClickAddVolunteer;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVolunteerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton btnAddVolunteer = binding.btnAddVolunteer;

        btnAddVolunteer.setOnClickListener(v -> {
            if ( btnAddVolunteer.getVisibility() == View.VISIBLE && onClickAddVolunteer != null ) {
                onClickAddVolunteer.addVolunteerFragment();
            }
        });

        return root;
    }

    public interface OnClickAddVolunteer {
        void addVolunteerFragment();
    }

    public static void setOnClickAddVolunteer(OnClickAddVolunteer onClickAddVolunteer) {
        VolunteerFragment.onClickAddVolunteer = onClickAddVolunteer;
    }

}