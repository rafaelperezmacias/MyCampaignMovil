package com.rld.futuro.futuroapp.Fragments.VolunteerBS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rld.futuro.futuroapp.R;
import com.rld.futuro.futuroapp.Utils.TextInputLayoutUtils;

public class PersonalFragment extends Fragment {

    private TextInputLayout lytNames;
    private TextInputLayout lytLastNames;
    private TextInputLayout lytStreet;
    private TextInputLayout lytOutNumber;
    private TextInputLayout lytComplement;
    private TextInputLayout lytCP;

    public PersonalFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_volunteer_bs, container, false);

        lytNames = view.findViewById(R.id.fpvbs_names_lyt);
        lytLastNames = view.findViewById(R.id.fpvbs_lastNames_lyt);
        lytStreet = view.findViewById(R.id.fpvbs_street_lyt);
        lytOutNumber = view.findViewById(R.id.fpvbs_outNumber_lyt);
        lytComplement = view.findViewById(R.id.fpvbs_complement_lyt);
        lytCP = view.findViewById(R.id.fpvbs_cp_lyt);

        return view;
    }

    public boolean isComplete() {
        return  !(!TextInputLayoutUtils.isValid(lytNames, getString(R.string.fpvbs_names_error))
            | !TextInputLayoutUtils.isValid(lytLastNames, getString(R.string.fpvbs_lastNames_error))
            | !TextInputLayoutUtils.isValid(lytStreet, getString(R.string.fpvbs_street_error))
            | !TextInputLayoutUtils.isValid(lytOutNumber, getString(R.string.fpvbs_out_number_error))
            | !TextInputLayoutUtils.isValid(lytCP, getString(R.string.fpvbs_cp_error))) ;
    }


}
