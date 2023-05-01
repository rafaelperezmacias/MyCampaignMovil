package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.R;
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

    private MaterialCardView cardErrorState;
    private TextView txtErrorState;
    private MaterialCardView cardErrorFederalDistrict;
    private TextView txtErrorFederalDistrict;
    private MaterialCardView cardErrorLocalDistrict;
    private TextView txtErrorLocalDistrict;
    private MaterialCardView cardErrorMunicipality;
    private TextView txtErrorMunicipality;
    private MaterialCardView cardErrorSection;
    private TextView txtErrorSection;

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

        cardErrorState = binding.cardErrorState;
        txtErrorState = binding.txtErrorState;
        cardErrorFederalDistrict = binding.cardErrorFederalDistrict;
        txtErrorFederalDistrict = binding.txtErrorFederalDistrict;
        cardErrorLocalDistrict = binding.cardErrorLocalDistrict;
        txtErrorLocalDistrict = binding.txtErrorLocalDistrict;
        cardErrorMunicipality = binding.cardErrorMunicipality;
        txtErrorMunicipality = binding.txtErrorMunicipality;
        cardErrorSection = binding.cardErrorSection;
        txtErrorSection = binding.txtErrorSection;

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

        if ( (type == VolunteerBottomSheet.TYPE_SHOW || type == VolunteerBottomSheet.TYPE_UPDATE) && volunteer.getError() != null ) {
            Volunteer.Error error = volunteer.getError();
            // Estado
            if ( error.getState() != null ) {
                if ( error.getState().getId() == 0 ) {
                    lytStateNumber.setError("El número no existe");
                    lytStateName.setError("El nombre no existe");
                    iconState.setColorFilter(ContextCompat.getColor(getContext(), R.color.error));
                    cardErrorState.setVisibility(View.GONE);
                } else {
                    if ( error.getState().getId() != volunteer.getSection().getState().getId() ) {
                        lytStateNumber.setError("El número no coincide con el nombre");
                        lytStateName.setError(null);
                    } else {
                        lytStateNumber.setError(null);
                        lytStateName.setError("El nombre no concide con el número");
                    }
                    iconState.setColorFilter(ContextCompat.getColor(getContext(), R.color.error));
                    cardErrorState.setVisibility(View.VISIBLE);
                    StringBuilder errorBuilder = new StringBuilder();
                    errorBuilder.append("<b>SUGERENCIA: </b>");
                    errorBuilder.append("Nombre: ").append(error.getState().getName()).append(", ");
                    errorBuilder.append("número: ").append(error.getState().getId());
                    txtErrorState.setText(Html.fromHtml(errorBuilder.toString()));
                }
            } else {
                cardErrorState.setVisibility(View.GONE);
            }
            // Municipio
            if ( error.getMunicipality() != null ) {
                if ( error.getMunicipality().getId() == 0 ) {
                    lytMunicipalityName.setError("El nombre no existe");
                    lytMunicipalityNumber.setError("El número no existe");
                } else {
                    if ( error.getMunicipality().getNumber() != volunteer.getSection().getMunicipality().getNumber() ) {
                        lytMunicipalityNumber.setError("El número no coincide con el nombre");
                        lytMunicipalityName.setError(null);
                    } else {
                        lytMunicipalityNumber.setError(null);
                        lytMunicipalityName.setError("El nombre no coincide con el número");
                    }
                    cardErrorMunicipality.setVisibility(View.VISIBLE);
                    StringBuilder errorBuilder = new StringBuilder();
                    errorBuilder.append("<b>SUGERENCIA: </b>");
                    errorBuilder.append("Nombre: ").append(error.getMunicipality().getName()).append(", ");
                    errorBuilder.append("número: ").append(error.getMunicipality().getNumber());
                    txtErrorMunicipality.setText(Html.fromHtml(errorBuilder.toString()));
                }
            } else {
                cardErrorMunicipality.setVisibility(View.GONE);
            }
            // Distrito local
            if ( error.getLocalDistrict() != null ) {
                if ( error.getLocalDistrict().getId() == 0 ) {
                    iconLocalDistrict.setColorFilter(ContextCompat.getColor(getContext(), R.color.error));
                    lytLocalDistrictName.setError("El nombre no existe");
                    lytLocalDistrictNumber.setError("El número no existe");
                } else {
                    if ( error.getLocalDistrict().getNumber() != volunteer.getSection().getLocalDistrict().getNumber() ) {
                        lytLocalDistrictNumber.setError("El número no coincide con el nombre");
                        lytLocalDistrictName.setError(null);
                    } else {
                        lytLocalDistrictNumber.setError(null);
                        lytLocalDistrictName.setError("El nombre no coincide con el número");
                    }
                    iconLocalDistrict.setColorFilter(ContextCompat.getColor(getContext(), R.color.error));
                    cardErrorLocalDistrict.setVisibility(View.VISIBLE);
                    StringBuilder errorBuilder = new StringBuilder();
                    errorBuilder.append("<b>SUGERENCIA: </b>");
                    errorBuilder.append("Nombre: ").append(error.getLocalDistrict().getName()).append(", ");
                    errorBuilder.append("número: ").append(error.getLocalDistrict().getNumber());
                    txtErrorLocalDistrict.setText(Html.fromHtml(errorBuilder.toString()));
                }
            } else {
                cardErrorLocalDistrict.setVisibility(View.GONE);
            }
            // Distrito federal
            if ( error.getFederalDistrict() != null ) {
                if ( error.getFederalDistrict().getId() == 0 ) {
                    lytFederalDistrictName.setError("El nombre no existe");
                    lytFederalDistrictNumber.setError("El número no existe");
                    iconFederalDistrict.setColorFilter(ContextCompat.getColor(getContext(), R.color.error));
                } else {
                    if ( error.getFederalDistrict().getNumber() != volunteer.getSection().getFederalDistrict().getNumber() ) {
                        lytFederalDistrictNumber.setError("El número no coincide con el nombre");
                        lytFederalDistrictName.setError(null);
                    } else {
                        lytFederalDistrictNumber.setError(null);
                        lytFederalDistrictName.setError("El nombre no coincide con el número");
                    }
                    iconFederalDistrict.setColorFilter(ContextCompat.getColor(getContext(), R.color.error));
                    cardErrorFederalDistrict.setVisibility(View.VISIBLE);
                    StringBuilder errorBuilder = new StringBuilder();
                    errorBuilder.append("<b>SUGERENCIA: </b>");
                    errorBuilder.append("Nombre: ").append(error.getFederalDistrict().getName()).append(", ");
                    errorBuilder.append("número: ").append(error.getFederalDistrict().getNumber());
                    txtErrorFederalDistrict.setText(Html.fromHtml(errorBuilder.toString()));
                }
            } else {
                cardErrorFederalDistrict.setVisibility(View.GONE);
            }
            // Sección
            if ( error.getSection() != null ) {
                if ( error.getSection().getId() == 0 ) {
                    lytSection.setError("El distrito local, fedaral y/o municipio son incompatibles");
                    iconSection.setColorFilter(ContextCompat.getColor(getContext(), R.color.error));
                    cardErrorSection.setVisibility(View.GONE);
                } else {
                    lytSection.setError("El distrito local, fedaral o municipio no pertenecen a la sección");
                    iconSection.setColorFilter(ContextCompat.getColor(getContext(), R.color.error));
                    cardErrorSection.setVisibility(View.VISIBLE);
                    StringBuilder errorBuilder = new StringBuilder();
                    errorBuilder.append("<b>SUGERENCIA</b><br>");
                    errorBuilder.append("<br>Sección: ").append(error.getSection().getSection());
                    errorBuilder.append("<br><br>Municipio<br>");
                    errorBuilder.append("Nombre: ").append(error.getSection().getMunicipality().getName()).append(", ");
                    errorBuilder.append("número: ").append(error.getSection().getMunicipality().getNumber());
                    errorBuilder.append("<br><br>Distrito local<br>");
                    errorBuilder.append("Nombre: ").append(error.getSection().getLocalDistrict().getName()).append(", ");
                    errorBuilder.append("número: ").append(error.getSection().getLocalDistrict().getNumber());
                    errorBuilder.append("<br><br>Distrito federal<br>");
                    errorBuilder.append("Nombre: ").append(error.getSection().getFederalDistrict().getName()).append(", ");
                    errorBuilder.append("número: ").append(error.getSection().getFederalDistrict().getNumber());
                    txtErrorSection.setText(Html.fromHtml(errorBuilder.toString()));
                }
            } else {
                cardErrorSection.setVisibility(View.GONE);
            }
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
                    && section.getState().getName().equals(state.getName())
                    && section.getMunicipality().getId() == municipality.getId()
                    && section.getLocalDistrict().getId() == localDistrict.getId()
                    && section.getFederalDistrict().getId() == federalDistrict.getId() ) {
                return section;
            }
        }
        return null;
    }

    private Municipality getMunicipality(State state) {
        String name = lytMunicipalityName.getEditText().getText().toString().trim();
        int number = Integer.parseInt(lytMunicipalityNumber.getEditText().getText().toString().trim());
        for ( Municipality municipality : localDataFileManager.getMunicipalities() ) {
            if ( municipality.getName().equals(name) && municipality.getNumber() == number
                    && municipality.getState().getId() == state.getId() && municipality.getState().getName().equals(state.getName()) ) {
                return municipality;
            }
        }
        return null;
    }

    private LocalDistrict getLocalDistrict(State state) {
        String name = lytLocalDistrictName.getEditText().getText().toString().trim();
        int number = Integer.parseInt(lytLocalDistrictNumber.getEditText().getText().toString().trim());
        for ( LocalDistrict localDistrict : localDataFileManager.getLocalDistricts() ) {
            if ( localDistrict.getName().equals(name) && localDistrict.getNumber() == number
                    && localDistrict.getState().getId() == state.getId() && localDistrict.getState().getName().equals(state.getName()) ) {
                return localDistrict;
            }
        }
        return null;
    }

    private FederalDistrict getFederalDistrict(State state) {
        String name = lytFederalDistrictName.getEditText().getText().toString().trim();
        int number = Integer.parseInt(lytFederalDistrictNumber.getEditText().getText().toString().trim());
        for ( FederalDistrict federalDistrict : localDataFileManager.getFederalDistricts() ) {
            if ( federalDistrict.getName().equals(name) && federalDistrict.getNumber() == number
                    && federalDistrict.getState().getId() == state.getId() && federalDistrict.getState().getName().equals(state.getName()) ) {
                return federalDistrict;
            }
        }
        return null;
    }

    private State getState() {
        String name = lytStateName.getEditText().getText().toString().trim();
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
