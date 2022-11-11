package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.databinding.FragmentPersonalVolunteerBsBinding;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

public class PersonalFragment extends Fragment {

    private FragmentPersonalVolunteerBsBinding binding;

    private TextInputLayout lytFathersLastname;
    private TextInputLayout lytMothersLastname;
    private TextInputLayout lytNames;
    private TextInputLayout lytBirthdate;
    private TextInputLayout lytStreet;
    private TextInputLayout lytExternalNumber;
    private TextInputLayout lytInternalNumber;
    private TextInputLayout lytSuburb;
    private TextInputLayout lytZipcode;

    private Volunteer volunteer;


    public PersonalFragment(Volunteer volunteer)
    {
        this.volunteer = volunteer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalVolunteerBsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lytFathersLastname = binding.lytFathersLastname;
        lytMothersLastname = binding.lytMothersLastname;
        lytNames = binding.lytNames;
        lytBirthdate = binding.lytBirthdate;
        lytStreet = binding.lytStreet;
        lytExternalNumber = binding.lytExternalNumber;
        lytInternalNumber = binding.lytInternalNumber;
        lytSuburb = binding.lytSuburb;
        lytZipcode = binding.lytZipcode;

        lytFathersLastname.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytMothersLastname.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytNames.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytStreet.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytInternalNumber.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytSuburb.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });

        return root;
    }


    public void setVolunteer() {
        /* volunteer.setLastName1(lytLastName1.getEditText().getText().toString().trim());
        volunteer.setLastName2(lytLastName2.getEditText().getText().toString().trim());
        volunteer.setNames(lytNames.getEditText().getText().toString().trim());
        volunteer.setAge(lytAge.getEditText().getText().toString().trim());
        volunteer.setAddressName(lytStreet.getEditText().getText().toString().trim());
        volunteer.setAddressNumExt(lytOutNumber.getEditText().getText().toString().trim());
        if ( !lytComplement.getEditText().getText().toString().isEmpty() ) {
            volunteer.setAddressNumInt(lytComplement.getEditText().getText().toString().trim());
        }
        volunteer.setSuburb(lytSuburb.getEditText().getText().toString().trim());
        volunteer.setZipCode(lytCP.getEditText().getText().toString().trim()); */
    }

    public boolean isComplete() {
//        return  !(!TextInputLayoutUtils.isValid(lytNames, getString(R.string.fpvbs_names_error))
//            | !TextInputLayoutUtils.isVali40(lytNames, "Error, revise la información de este campo")
//            | !TextInputLayoutUtils.isValid(lytLastName1, getString(R.string.fpvbs_lastName1_error))
//            | !TextInputLayoutUtils.isVali50(lytLastName1, "Error, revise la información de este campo")
//            | !TextInputLayoutUtils.isValid(lytLastName2, getString(R.string.fpvbs_lastName2_error))
//            | !TextInputLayoutUtils.isVali50(lytLastName2, "Error, revise la información de este campo")
//            | !TextInputLayoutUtils.isValid(lytStreet, getString(R.string.fpvbs_street_error))
//            | !TextInputLayoutUtils.isVali70(lytStreet,"Error, revise la información de este campo")
//            | !TextInputLayoutUtils.isValid(lytOutNumber, getString(R.string.fpvbs_out_number_error))
//            | !TextInputLayoutUtils.isVali10_1(lytComplement, "Error, revise la información de este campo")
//            | !TextInputLayoutUtils.isValid(lytSuburb, getString(R.string.fpvbs_suburb_error))
//            | !TextInputLayoutUtils.isVali40(lytSuburb, "Error, revise la información de este campo"));
        return true;
    }


}
