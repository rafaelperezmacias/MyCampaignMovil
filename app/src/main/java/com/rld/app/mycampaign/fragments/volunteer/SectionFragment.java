package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.databinding.FragmentSectionVolunteerBsBinding;
import com.rld.app.mycampaign.files.LocalDataFileManager;
import com.rld.app.mycampaign.models.Section;
import com.rld.app.mycampaign.models.Volunteer;

public class SectionFragment extends Fragment {

    private FragmentSectionVolunteerBsBinding binding;

    private TextInputLayout lytSection;
    private TextInputLayout lytMunicipalityName;
    private TextInputLayout lytMunicipalityNumber;
    private TextInputLayout lytSector;
    private TextInputLayout lytLocalDistrictName;
    private TextInputLayout lytLocalDistrictNumber;
    private TextInputLayout lytFederalDistrictName;
    private TextInputLayout lytFederalDistrictNumber;
    private TextInputLayout lytStateName;
    private TextInputLayout lytStateNumber;

    private Volunteer volunteer;
    private LocalDataFileManager localDataFileManager;

    public SectionFragment(Volunteer volunteer, LocalDataFileManager localDataFileManager)
    {
        this.volunteer = volunteer;
        this.localDataFileManager = localDataFileManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSectionVolunteerBsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lytSection = binding.lytSection;
        lytMunicipalityName = binding.lytMunicipalityName;
        lytMunicipalityNumber = binding.lytMunicipalityNumber;
        lytSector = binding.lytSector;
        lytLocalDistrictName = binding.lytLocalDistrictName;
        lytLocalDistrictNumber = binding.lytLocalDistrictNumber;
        lytFederalDistrictName = binding.lytFederalDistrictName;
        lytFederalDistrictNumber = binding.lytFederalDistrictNumber;
        lytStateName = binding.lytStateName;
        lytStateNumber = binding.lytStateNumber;

        lytMunicipalityName.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytSector.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytLocalDistrictName.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytFederalDistrictName.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytStateName.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });

        return root;
    }

    public void setVolunter() {
        volunteer.setSector(lytSector.getEditText().getText().toString().trim());
        /* volunteer.setStateNumber(Integer.parseInt(lytStateNumber.getEditText().getText().toString().trim()));
        volunteer.setSector(lytSector.getEditText().getText().toString().trim());
        volunteer.setMunicipality(lytMunicipioName.getEditText().getText().toString().trim());
        volunteer.setNumberMunicipality(lytMunicipioNumber.getEditText().getText().toString().trim());
        volunteer.setLocalDistrict(lytDistritoLocalName.getEditText().getText().toString().trim());
        volunteer.setNumberLocalDistrict(lytDistritoLocalName.getEditText().getText().toString().trim()); */
    }

    public boolean isComplete() {
        return true;
    }

    public void loadData() {
        if ( volunteer.getSection() != null ) {
            Section section = volunteer.getSection();
            if ( volunteer.getSection().getFederalDistrict() != null ) {
                lytFederalDistrictName.getEditText().setText(section.getFederalDistrict().getName());
                lytFederalDistrictNumber.getEditText().setText(String.valueOf(section.getFederalDistrict().getNumber()));
            }
            if ( volunteer.getSection().getLocalDistrict() != null ) {
                lytLocalDistrictName.getEditText().setText(section.getLocalDistrict().getName());
                lytLocalDistrictNumber.getEditText().setText(String.valueOf(section.getLocalDistrict().getNumber()));
            }
            if ( volunteer.getSection().getMunicipality() != null ) {
                lytMunicipalityName.getEditText().setText(section.getMunicipality().getName());
                lytMunicipalityNumber.getEditText().setText(String.valueOf(section.getMunicipality().getNumber()));
            }
            if ( volunteer.getSection().getState() != null ) {
                lytStateName.getEditText().setText(section.getState().getName());
                lytStateNumber.getEditText().setText(String.valueOf(section.getState().getId()));
            }
            if ( section.getId() != 0 ) {
                lytSection.getEditText().setText(section.getSection());
            }
        }
    }

}
