package com.rld.futuro.futuroapp.Fragments.VolunteerBS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.rld.futuro.futuroapp.Models.Volunteer;
import com.rld.futuro.futuroapp.R;

public class OtherFragment extends Fragment {

    private Volunteer volunteer;

    public OtherFragment(Volunteer volunteer)
    {
        this.volunteer = volunteer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_volunteer_bs, container, false);
        return view;
    }
}
