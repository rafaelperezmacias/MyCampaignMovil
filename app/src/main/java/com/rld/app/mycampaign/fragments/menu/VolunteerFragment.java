package com.rld.app.mycampaign.fragments.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rld.app.mycampaign.MainActivity;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.models.Volunteer;

public class VolunteerFragment extends Fragment {

    private MainActivity mainActivity;

    public VolunteerFragment(MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_volunteer, container, false);

        FloatingActionButton btnAddVolunteer = view.findViewById(R.id.btn_add_volunteer);

        btnAddVolunteer.setOnClickListener(view1 -> {
            if ( btnAddVolunteer.getVisibility() == View.VISIBLE ) {
                Volunteer volunteer = new Volunteer();
            }
        });

        return view;
    }

}