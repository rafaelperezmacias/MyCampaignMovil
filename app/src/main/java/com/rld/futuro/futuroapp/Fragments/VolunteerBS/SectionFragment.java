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

public class SectionFragment extends Fragment {

    private TextInputLayout lytStateName;
    private TextInputLayout lytStateNumber;
    private TextInputLayout lytSection;
    private TextInputLayout lytMunicipioName;
    private TextInputLayout lytMunicipioNumber;
    private TextInputLayout lytSector;
    private TextInputLayout lytDistritoLocalName;
    private TextInputLayout lytDistritoLocalNumber;
    private TextInputLayout lytDistritoFederalName;
    private TextInputLayout lytDistritoFederalNumber;

    private Volunteer volunteer;

    public SectionFragment(Volunteer volunteer)
    {
        this.volunteer = volunteer;
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
        lytDistritoFederalName = view.findViewById(R.id.fsvbs_dfederal_name_lyt);
        lytDistritoFederalNumber = view.findViewById(R.id.fsvbs_dfederal_number_lyt);

        return view;
    }

    public void setVolunter() {
        volunteer.setSector(lytSector.getEditText().getText().toString().trim());
        if ( !volunteer.isJalisco() ) {
            volunteer.setSection(lytSection.getEditText().getText().toString().trim());
            volunteer.setMunicipality(lytMunicipioName.getEditText().getText().toString().trim());
            volunteer.setNumberMunicipality(lytMunicipioNumber.getEditText().getText().toString().trim());
            volunteer.setLocalDistrict(lytDistritoLocalName.getEditText().getText().toString().trim());
            volunteer.setNumberLocalDistrict(lytDistritoLocalName.getEditText().getText().toString().trim());
        }
    }

    public boolean isComplete() {
        if ( volunteer.isJalisco() ) {
            return TextInputLayoutUtils.isValid(lytSector, getString(R.string.fSvbs_sector_error));
        }
        return  !(!TextInputLayoutUtils.isValid(lytSection, getString(R.string.fSvbs_section_error))
                | !TextInputLayoutUtils.isValid(lytMunicipioName, getString(R.string.fSvbs_municipio_name_error))
                | !TextInputLayoutUtils.isValid(lytMunicipioNumber, getString(R.string.fSvbs_municipio_number_error))
                | !TextInputLayoutUtils.isValid(lytSector, getString(R.string.fSvbs_sector_error))
                | !TextInputLayoutUtils.isValid(lytDistritoLocalName, getString(R.string.fSvbs_municipio_name_error))
                | !TextInputLayoutUtils.isValid(lytDistritoLocalNumber, getString(R.string.fSvbs_municipio_number_error))
                | !TextInputLayoutUtils.isValid(lytDistritoFederalName, getString(R.string.fSvbs_municipio_name_error))
                | !TextInputLayoutUtils.isValid(lytDistritoFederalNumber, getString(R.string.fSvbs_municipio_number_error)) );
    }

    public void setState() {
        lytStateName.getEditText().setText("" + volunteer.getState());
        lytStateNumber.getEditText().setText("" + volunteer.getStateNumber());
        if ( volunteer.isJalisco() ) {
            lytSection.getEditText().setEnabled(false);
            lytSection.getEditText().setText("1234");
            lytSector.getEditText().requestFocus();
        } else {
            lytSection.getEditText().requestFocus();
            lytSection.getEditText().setEnabled(true);
            lytSection.getEditText().setText("");
        }
    }
}
