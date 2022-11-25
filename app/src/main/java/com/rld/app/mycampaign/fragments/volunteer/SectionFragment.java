package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
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

    private static final int SECTION_MAX_LIMIT = 10;
    private static final int MUNICIPALITY_NAME_MAX_LIMIT = 60;
    private static final int MUNICIPALITY_NUMBER_MAX_LIMIT = 4;
    private static final int SECTOR_MAX_LIMIT = 50;
    private static final int LOCAL_DISTRICT_NAME_MAX_LIMIT = 60;
    private static final int LOCAL_DISTRICT_NUMBER_MAX_LIMIT = 4;
    private static final int FEDERAL_DISTRICT_NAME_MAX_LIMIT = 60;
    private static final int FEDERAL_DISTRICT_NUMBER_MAX_LIMIT = 4;
    private static final int STATE_NAME_MAX_LIMIT = 30;
    private static final int STATE_NUMBER_MAX_LIMIT = 3;

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

    private AppCompatImageView iconSection;
    private AppCompatImageView iconSector;
    private AppCompatImageView iconLocalDistrict;
    private AppCompatImageView iconFederalDistrict;
    private AppCompatImageView iconState;

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

        iconSection = binding.iconSection;
        iconSector = binding.iconSector;
        iconLocalDistrict = binding.iconLocalDisctict;
        iconFederalDistrict = binding.iconFederalDistrict;
        iconState = binding.iconState;

        if ( type == VolunteerBottomSheet.TYPE_UPDATE ) {
            loadData();
            lytSector.getEditText().setText(volunteer.getSector());
            lytSection.getEditText().setText(volunteer.getSection().getSection());
            currentSection = volunteer.getSection().getSection();
        }

        if ( type == VolunteerBottomSheet.TYPE_SHOW ) {
            loadData();
            lytSector.getEditText().setText(volunteer.getSector());
            lytSection.getEditText().setText(volunteer.getSection().getSection());
            TextInputLayoutUtils.disableEditText(lytSection.getEditText());
            TextInputLayoutUtils.disableEditText(lytMunicipalityName.getEditText());
            TextInputLayoutUtils.disableEditText(lytMunicipalityNumber.getEditText());
            TextInputLayoutUtils.disableEditText(lytSector.getEditText());
            TextInputLayoutUtils.disableEditText(lytLocalDistrictName.getEditText());
            TextInputLayoutUtils.disableEditText(lytLocalDistrictNumber.getEditText());
            TextInputLayoutUtils.disableEditText(lytFederalDistrictName.getEditText());
            TextInputLayoutUtils.disableEditText(lytFederalDistrictNumber.getEditText());
            TextInputLayoutUtils.disableEditText(lytStateName.getEditText());
            TextInputLayoutUtils.disableEditText(lytStateNumber.getEditText());
        }

        TextInputLayoutUtils.initializeFilters(lytSection.getEditText(), false, SECTION_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytMunicipalityName.getEditText(), true, MUNICIPALITY_NAME_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytMunicipalityNumber.getEditText(), false, MUNICIPALITY_NUMBER_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytSector.getEditText(), true, SECTOR_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytLocalDistrictName.getEditText(), true, LOCAL_DISTRICT_NAME_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytLocalDistrictNumber.getEditText(), false, LOCAL_DISTRICT_NUMBER_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytFederalDistrictName.getEditText(), true, FEDERAL_DISTRICT_NAME_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytFederalDistrictNumber.getEditText(), false, FEDERAL_DISTRICT_NUMBER_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytStateName.getEditText(), true, STATE_NAME_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytStateNumber.getEditText(), false, STATE_NUMBER_MAX_LIMIT);

        return root;
    }

    public boolean isComplete() {
        // return true;
        return isSectionComplete() & isMunicipalityNameComplete() & isMunicipalityNumberComplete() & isSectorComplete()
                & isLocalDistrictNameComplete() & isLocalDistricNumberComplete() & isFederalDistrictNameComplete()
                & isFederalDistrictNumberComplete() & isStateNameComplete() & isStateNumberComplete();
    }

    private boolean isSectionComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytSection, "Ingrese la sección", iconSection, getContext())
                && TextInputLayoutUtils.isValidNumbers(lytSection, iconSection, getContext());
    }

    private boolean isMunicipalityNameComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytMunicipalityName, "Ingrese el nombre", null, getContext())
                && TextInputLayoutUtils.isValidMayusWithoutNumbers(lytMunicipalityName, null, getContext());
    }

    private boolean isMunicipalityNumberComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytMunicipalityNumber, "Ingrese el número", null, getContext())
                && TextInputLayoutUtils.isValidNumbers(lytMunicipalityNumber, null, getContext());
    }

    private boolean isSectorComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytSector, "Ingrese el sector", iconSector, getContext())
                && TextInputLayoutUtils.isValidMayusWithNumbers(lytSector, iconSector, getContext());
    }

    private boolean isLocalDistrictNameComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytLocalDistrictName, "Ingrese el nombre", iconLocalDistrict, getContext())
                && TextInputLayoutUtils.isValidMayusWithoutNumbers(lytLocalDistrictName, iconLocalDistrict, getContext());
    }

    private boolean isLocalDistricNumberComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytLocalDistrictNumber, "Ingrese el número", iconLocalDistrict, getContext())
                && TextInputLayoutUtils.isValidNumbers(lytLocalDistrictNumber, iconLocalDistrict, getContext());
    }

    private boolean isFederalDistrictNameComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytFederalDistrictName, "Ingrese el nombre", iconFederalDistrict, getContext())
                && TextInputLayoutUtils.isValidMayusWithoutNumbers(lytFederalDistrictName, iconFederalDistrict, getContext());
    }

    private boolean isFederalDistrictNumberComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytFederalDistrictNumber, "Ingrese el número", iconFederalDistrict, getContext())
                && TextInputLayoutUtils.isValidNumbers(lytFederalDistrictNumber, iconFederalDistrict, getContext());
    }

    private boolean isStateNameComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytStateName, "Ingrese el nombre", iconState, getContext())
                && TextInputLayoutUtils.isValidMayusWithoutNumbers(lytStateName, iconState, getContext());
    }

    private boolean isStateNumberComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytStateNumber, "Ingrese el número", iconState, getContext())
                && TextInputLayoutUtils.isValidNumbers(lytStateNumber, iconState, getContext());
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
        currentSection = section.getSection();
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

    public void setFocus() {
        lytSection.getEditText().requestFocus();
        TextInputLayoutUtils.cursorToEnd(lytSection.getEditText());
    }

}
