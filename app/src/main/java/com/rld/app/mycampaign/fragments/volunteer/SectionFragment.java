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
import com.rld.app.mycampaign.bottomsheets.VolunteerBottomSheet;
import com.rld.app.mycampaign.databinding.FragmentSectionVolunteerBsBinding;
import com.rld.app.mycampaign.files.LocalDataFileManager;
import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.Section;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

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
    private int type;

    private String currentSection;

    public SectionFragment(Volunteer volunteer, LocalDataFileManager localDataFileManager, int type) {
        this.volunteer = volunteer;
        this.localDataFileManager = localDataFileManager;
        this.type = type;
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

        lytMunicipalityName.getEditText().setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        lytSector.getEditText().setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        lytLocalDistrictName.getEditText().setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        lytFederalDistrictName.getEditText().setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        lytStateName.getEditText().setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        if ( type == VolunteerBottomSheet.TYPE_SHOW || type == VolunteerBottomSheet.TYPE_UPDATE ) {
            loadData();
            lytSector.getEditText().setText(volunteer.getSector());
        }

        if ( type == VolunteerBottomSheet.TYPE_SHOW ) {
            lytSection.getEditText().setText(volunteer.getSection().getSection());
            TextInputLayoutUtils.setEditableEditText(lytSection.getEditText(), false);
            TextInputLayoutUtils.setEditableEditText(lytMunicipalityName.getEditText(), false);
            TextInputLayoutUtils.setEditableEditText(lytMunicipalityNumber.getEditText(), false);
            TextInputLayoutUtils.setEditableEditText(lytSector.getEditText(), false);
            TextInputLayoutUtils.setEditableEditText(lytLocalDistrictName.getEditText(), false);
            TextInputLayoutUtils.setEditableEditText(lytLocalDistrictNumber.getEditText(), false);
            TextInputLayoutUtils.setEditableEditText(lytFederalDistrictName.getEditText(), false);
            TextInputLayoutUtils.setEditableEditText(lytFederalDistrictNumber.getEditText(), false);
            TextInputLayoutUtils.setEditableEditText(lytStateName.getEditText(), false);
            TextInputLayoutUtils.setEditableEditText(lytStateNumber.getEditText(), false);
        }

        return root;
    }

    public boolean isComplete() {
        return true;
    }

    public void setVolunter() {
        volunteer.setSector(lytSector.getEditText().getText().toString().trim());
        // Estado
        State state = getState();
        if ( state == null ) {
            state = new State();
            state.setName(lytStateName.getEditText().getText().toString().trim());
            state.setId(Integer.parseInt(lytStateNumber.getEditText().getText().toString().trim()));
        }
        // Distrito federal
        FederalDistrict federalDistrict = getFederalDistrict(state);
        if ( federalDistrict == null ) {
            federalDistrict = new FederalDistrict();
            federalDistrict.setName(lytFederalDistrictName.getEditText().getText().toString().trim());
            federalDistrict.setNumber(Integer.parseInt(lytFederalDistrictNumber.getEditText().getText().toString().trim()));
            federalDistrict.setState(state);
        }
        // Distrito local
        LocalDistrict localDistrict = getLocalDistrict(state);
        if ( localDistrict == null ) {
            localDistrict = new LocalDistrict();
            localDistrict.setName(lytLocalDistrictName.getEditText().getText().toString().trim());
            localDistrict.setNumber(Integer.parseInt(lytLocalDistrictNumber.getEditText().getText().toString().trim()));
            localDistrict.setState(state);
        }
        // Municipio
        Municipality municipality = getMunicipality(state);
        if ( municipality == null ) {
            municipality = new Municipality();
            municipality.setName(lytMunicipalityName.getEditText().getText().toString().trim());
            municipality.setNumber(Integer.parseInt(lytMunicipalityNumber.getEditText().getText().toString().trim()));
            municipality.setState(state);
        }
        // Seccion
        Section section = getSection(state, localDistrict, federalDistrict, municipality);
        if ( section == null ) {
            section = new Section();
            section.setSection(lytSection.getEditText().getText().toString().trim());
            section.setState(state);
            section.setFederalDistrict(federalDistrict);
            section.setLocalDistrict(localDistrict);
            section.setMunicipality(municipality);
        }
        volunteer.setSection(section);
    }

    private Section getSection(State state, LocalDistrict localDistrict, FederalDistrict federalDistrict, Municipality municipality) {
        String numberSection = lytSection.getEditText().getText().toString().trim();
        for ( Section section : localDataFileManager.getSections() ) {
            if ( section.getSection().equals(numberSection) && section.getState().getId() == state.getId()
                    && section.getMunicipality().getId() == municipality.getId()
                    && section.getLocalDistrict().getId() == localDistrict.getId()
                    && section.getFederalDistrict().getId() == federalDistrict.getId()) {
                return section;
            }
        }
        return null;
    }

    private Municipality getMunicipality(State state) {
        String name = lytMunicipalityName.getEditText().getText().toString().trim();
        int number = Integer.parseInt(lytMunicipalityNumber.getEditText().getText().toString().trim());
        for ( Municipality municipality : localDataFileManager.getMunicipalities() ) {
            if ( municipality.getName().equals(name) && municipality.getNumber() == number && municipality.getState().getId() == state.getId() ) {
                return municipality;
            }
        }
        return null;
    }

    private LocalDistrict getLocalDistrict(State state) {
        String name = lytLocalDistrictName.getEditText().getText().toString().trim();
        int number = Integer.parseInt(lytLocalDistrictNumber.getEditText().getText().toString().trim());
        for ( LocalDistrict localDistrict : localDataFileManager.getLocalDistricts() ) {
            if ( localDistrict.getName().equals(name) && localDistrict.getNumber() == number && localDistrict.getState().getId() == state.getId() ) {
                return localDistrict;
            }
        }
        return null;
    }

    private FederalDistrict getFederalDistrict(State state) {
        String name = lytFederalDistrictName.getEditText().getText().toString().trim();
        int number = Integer.parseInt(lytFederalDistrictNumber.getEditText().getText().toString().trim());
        for ( FederalDistrict federalDistrict : localDataFileManager.getFederalDistricts() ) {
            if ( federalDistrict.getName().equals(name) && federalDistrict.getNumber() == number && federalDistrict.getState().getId() == state.getId() ) {
                return federalDistrict;
            }
        }
        return null;
    }

    private State getState() {
        String name = lytFederalDistrictName.getEditText().getText().toString().trim();
        int id = Integer.parseInt(lytStateNumber.getEditText().getText().toString().trim());
        for ( State state : localDataFileManager.getStates() ) {
            if ( state.getName().equals(name) && state.getId() == id ) {
                return state;
            }
        }
        return null;
    }

    public void loadData() {
        if ( volunteer.getSection() != null ) {
            Section section = volunteer.getSection();
            if ( volunteer.getSection().getFederalDistrict() != null ) {
                lytFederalDistrictName.getEditText().setText(section.getFederalDistrict().getName());
                lytFederalDistrictNumber.getEditText().setText(String.valueOf(section.getFederalDistrict().getNumber()));
            } else {
                lytFederalDistrictName.getEditText().setText("");
                lytFederalDistrictNumber.getEditText().setText("");
            }
            if ( volunteer.getSection().getLocalDistrict() != null ) {
                lytLocalDistrictName.getEditText().setText(section.getLocalDistrict().getName());
                lytLocalDistrictNumber.getEditText().setText(String.valueOf(section.getLocalDistrict().getNumber()));
            } else {
                lytLocalDistrictName.getEditText().setText("");
                lytLocalDistrictNumber.getEditText().setText("");
            }
            if ( volunteer.getSection().getMunicipality() != null ) {
                lytMunicipalityName.getEditText().setText(section.getMunicipality().getName());
                lytMunicipalityNumber.getEditText().setText(String.valueOf(section.getMunicipality().getNumber()));
            } else {
                lytMunicipalityName.getEditText().setText("");
                lytMunicipalityNumber.getEditText().setText("");
            }
            if ( volunteer.getSection().getState() != null ) {
                lytStateName.getEditText().setText(section.getState().getName());
                lytStateNumber.getEditText().setText(String.valueOf(section.getState().getId()));
            } else {
                lytStateName.getEditText().setText("");
                lytStateNumber.getEditText().setText("");
            }
            if ( currentSection != null && !currentSection.equals(section.getSection()) ) {
                lytSector.getEditText().setText("");
            }
            if ( section.getId() != 0 ) {
                lytSection.getEditText().setText(section.getSection());
                currentSection = section.getSection();
            } else {
                lytSection.getEditText().setText("");
            }
        }
    }

    public void setCurrentSection(String currentSection) {
        this.currentSection = currentSection;
    }
}
