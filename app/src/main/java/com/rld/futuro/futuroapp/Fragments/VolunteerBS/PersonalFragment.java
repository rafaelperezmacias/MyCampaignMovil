package com.rld.futuro.futuroapp.Fragments.VolunteerBS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rld.futuro.futuroapp.Models.Volunteer;
import com.rld.futuro.futuroapp.R;
import com.rld.futuro.futuroapp.Utils.TextInputLayoutUtils;

public class PersonalFragment extends Fragment {

    private TextInputLayout lytNames;
    private TextInputLayout lytLastName1;
    private TextInputLayout lytLastName2;
    private TextInputLayout lytStreet;
    private TextInputLayout lytOutNumber;
    private TextInputLayout lytComplement;
    private TextInputLayout lytSuburb;
    private TextInputLayout lytCP;

    private Volunteer volunteer;

    public PersonalFragment(Volunteer volunteer)
    {
        this.volunteer = volunteer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_volunteer_bs, container, false);

        lytNames = view.findViewById(R.id.fpvbs_names_lyt);
        lytLastName1 = view.findViewById(R.id.fpvbs_lastName1_lyt);
        lytLastName2 = view.findViewById(R.id.fpvbs_lastName2_lyt);
        lytStreet = view.findViewById(R.id.fpvbs_street_lyt);
        lytOutNumber = view.findViewById(R.id.fpvbs_outNumber_lyt);
        lytComplement = view.findViewById(R.id.fpvbs_complement_lyt);
        lytCP = view.findViewById(R.id.fpvbs_cp_lyt);
        lytSuburb = view.findViewById(R.id.fpvbs_suburb_lyt);

        return view;
    }

    public void setVolunteer() {
        volunteer.setLastName1(lytLastName1.getEditText().getText().toString().trim());
        volunteer.setLastName2(lytLastName2.getEditText().getText().toString().trim());
        volunteer.setNames(lytNames.getEditText().getText().toString().trim());
        volunteer.setAddressName(lytStreet.getEditText().getText().toString().trim());
        volunteer.setAddressNumExt(lytOutNumber.getEditText().getText().toString().trim());
        if ( !lytComplement.getEditText().getText().toString().isEmpty() ) {
            volunteer.setAddressNumInt(lytComplement.getEditText().getText().toString().trim());
        }
        volunteer.setSuburb(lytSuburb.getEditText().getText().toString().trim());
        volunteer.setZipCode(lytCP.getEditText().getText().toString().trim());
    }

    public boolean isComplete() {
        return  !(!TextInputLayoutUtils.isValid(lytNames, getString(R.string.fpvbs_names_error))
            | !TextInputLayoutUtils.isValid(lytLastName1, getString(R.string.fpvbs_lastName1_error))
            | !TextInputLayoutUtils.isValid(lytLastName2, getString(R.string.fpvbs_lastName2_error))
            | !TextInputLayoutUtils.isValid(lytStreet, getString(R.string.fpvbs_street_error))
            | !TextInputLayoutUtils.isValid(lytOutNumber, getString(R.string.fpvbs_out_number_error))
            | !TextInputLayoutUtils.isValid(lytCP, getString(R.string.fpvbs_cp_error))
            | !TextInputLayoutUtils.isValid(lytSuburb, getString(R.string.fpvbs_suburb_error)) );
    }


}
