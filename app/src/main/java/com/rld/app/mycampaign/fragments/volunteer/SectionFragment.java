package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.Main2Activity;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.request.AppConfig;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

import java.util.ArrayList;

public class SectionFragment extends Fragment {

    private TextInputLayout lytStateName;
    private TextInputLayout lytStateNumber;
    private TextInputLayout lytSection;
    private TextInputLayout lytMunicipioName;
    private TextInputLayout lytMunicipioNumber;
    private TextInputLayout lytSector;
    private TextInputLayout lytDistritoLocalName;
    private TextInputLayout lytDistritoLocalNumber;

    private Volunteer volunteer;

    private ArrayList<Municipality> municipalities;
    private ArrayList<LocalDistrict> localDistricts;

    private boolean isLocal;

    public SectionFragment(Volunteer volunteer, Main2Activity mainActivity)
    {
        this.volunteer = volunteer;
//        municipalities = mainActivity.getMunipalities();
//        localDistricts = mainActivity.getLocalDistricts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section_volunteer_bs, container, false);

        lytStateName = view.findViewById(R.id.fsvbs_stateName_lyt);
        lytStateNumber = view.findViewById(R.id.fsvbs_stateNumber_lyt);
        lytSection = view.findViewById(R.id.fsvbs_section_lyt);
        lytMunicipioName = view.findViewById(R.id.fsvbs_municipioName_lyt);
        lytMunicipioNumber = view.findViewById(R.id.fsvbs_municipioNumber_lyt);
        lytSector = view.findViewById(R.id.fsvbs_sector_lyt);
        lytDistritoLocalName = view.findViewById(R.id.fsvbs_dlocal_name_lyt);
        lytDistritoLocalNumber = view.findViewById(R.id.fsvbs_dlocal_number_lyt);

        lytSector.getEditText().setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        return view;
    }

    public void setInfo() {
        if ( !volunteer.isLocal() ) {
            if ( municipalities.isEmpty() || municipalities.size() != AppConfig.MUNICIPALITIES_SIZE
                    || localDistricts.isEmpty() || localDistricts.size() != AppConfig.LOCAL_DISTRICTS_SIZE ) {
                isLocal = true;
            } else {
                isLocal = false;
            }
        } else {
            isLocal = true;
        }
        if ( !isLocal && volunteer.isJalisco() ) {
            for ( Municipality municipality : municipalities ) {
                if ( municipality.getMunicipalityNumber() == volunteer.getSectionObject().getNumberMunicipality() ) {
                    lytMunicipioName.getEditText().setText("" + municipality.getMunicipality());
                    lytMunicipioName.getEditText().setEnabled(false);
                    lytMunicipioNumber.getEditText().setText("" + municipality.getMunicipalityNumber());
                    lytMunicipioNumber.getEditText().setEnabled(false);
                    break;
                }
            }
            for ( LocalDistrict localDistrict : localDistricts ) {
                if ( localDistrict.getNumberLocalDistrict() == volunteer.getSectionObject().getNumberLocalDistrict() ) {
                    lytDistritoLocalName.getEditText().setText("" + localDistrict.getLocalDistrict());
                    lytDistritoLocalName.getEditText().setEnabled(false);
                    lytDistritoLocalNumber.getEditText().setText("" + localDistrict.getNumberLocalDistrict());
                    lytDistritoLocalNumber.getEditText().setEnabled(false);
                    break;
                }
            }
        }
        if ( !volunteer.isJalisco() ) {
            lytSection.getEditText().setText("");
            lytSection.getEditText().setEnabled(true);
            lytSection.getEditText().requestFocus();
            lytMunicipioName.getEditText().setText("");
            lytMunicipioName.getEditText().setEnabled(true);
            lytMunicipioNumber.getEditText().setText("");
            lytMunicipioNumber.getEditText().setEnabled(true);
            lytDistritoLocalName.getEditText().setText("");
            lytDistritoLocalName.getEditText().setEnabled(true);
            lytDistritoLocalNumber.getEditText().setText("");
            lytDistritoLocalNumber.getEditText().setEnabled(true);
        }
    }

    public void setVolunter() {
        volunteer.setStateNumber(Integer.parseInt(lytStateNumber.getEditText().getText().toString().trim()));
        volunteer.setSector(lytSector.getEditText().getText().toString().trim());
        volunteer.setSection(lytSection.getEditText().getText().toString().trim());
        volunteer.setMunicipality(lytMunicipioName.getEditText().getText().toString().trim());
        volunteer.setNumberMunicipality(lytMunicipioNumber.getEditText().getText().toString().trim());
        volunteer.setLocalDistrict(lytDistritoLocalName.getEditText().getText().toString().trim());
        volunteer.setNumberLocalDistrict(lytDistritoLocalName.getEditText().getText().toString().trim());
    }

    public boolean isComplete() {
        if ( volunteer.isJalisco() && !isLocal ) {
            return true;
        }
        return  !(!TextInputLayoutUtils.isValid(lytSection, getString(R.string.fSvbs_section_error))
                | !TextInputLayoutUtils.isValid(lytMunicipioName, getString(R.string.fSvbs_municipio_name_error))
                | !TextInputLayoutUtils.isValid(lytMunicipioNumber, getString(R.string.fSvbs_municipio_number_error))
                | !TextInputLayoutUtils.isValid(lytDistritoLocalName, getString(R.string.fSvbs_municipio_name_error))
                | !TextInputLayoutUtils.isValid(lytDistritoLocalNumber, getString(R.string.fSvbs_municipio_number_error))
                | !TextInputLayoutUtils.isValisMayus(lytDistritoLocalName, "Ingrese solo letras en mayuscula")
                | !TextInputLayoutUtils.isValisMayus(lytMunicipioName, "Ingrese solo letras en mayuscula"));
    }

    public void setState() {
        lytStateName.getEditText().setText("" + volunteer.getState());
        lytStateNumber.getEditText().setText("" + volunteer.getStateNumber());
        if ( volunteer.isJalisco() ) {
            if ( volunteer.isLocal() ) {
                lytSection.getEditText().requestFocus();
            } else {
                lytSection.getEditText().setEnabled(false);
                lytSection.getEditText().setText("" + volunteer.getSectionObject().getSection());
                lytSector.getEditText().requestFocus();
            }
        } else {
            lytSection.getEditText().setText("");
            lytSection.getEditText().requestFocus();
            lytMunicipioName.getEditText().setText("");
            lytMunicipioName.getEditText().setEnabled(true);
            lytMunicipioNumber.getEditText().setText("");
            lytMunicipioNumber.getEditText().setEnabled(true);
            lytDistritoLocalName.getEditText().setText("");
            lytDistritoLocalName.getEditText().setEnabled(true);
            lytDistritoLocalNumber.getEditText().setText("");
            lytDistritoLocalNumber.getEditText().setEnabled(true);
        }
    }
}
