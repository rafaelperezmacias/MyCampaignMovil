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
import com.rld.app.mycampaign.databinding.FragmentSectionVolunteerBsBinding;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.request.AppConfig;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

import java.util.ArrayList;

public class SectionFragment extends Fragment {

    private FragmentSectionVolunteerBsBinding binding;

    private TextInputLayout lytStateName;
    private TextInputLayout lytStateNumber;
    private TextInputLayout lytSection;
    private TextInputLayout lytMunicipioName;
    private TextInputLayout lytMunicipioNumber;
    private TextInputLayout lytSector;
    private TextInputLayout lytDistritoLocalName;
    private TextInputLayout lytDistritoLocalNumber;

    private Volunteer volunteer;

    public SectionFragment(Volunteer volunteer)
    {
        this.volunteer = volunteer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSectionVolunteerBsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        lytStateName = view.findViewById(R.id.fsvbs_stateName_lyt);
//        lytStateNumber = view.findViewById(R.id.fsvbs_stateNumber_lyt);
//        lytSection = view.findViewById(R.id.fsvbs_section_lyt);
//        lytMunicipioName = view.findViewById(R.id.fsvbs_municipioName_lyt);
//        lytMunicipioNumber = view.findViewById(R.id.fsvbs_municipioNumber_lyt);
//        lytSector = view.findViewById(R.id.fsvbs_sector_lyt);
//        lytDistritoLocalName = view.findViewById(R.id.fsvbs_dlocal_name_lyt);
//        lytDistritoLocalNumber = view.findViewById(R.id.fsvbs_dlocal_number_lyt);

        // lytSector.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });

        return root;
    }

    public void setVolunter() {
        /* volunteer.setStateNumber(Integer.parseInt(lytStateNumber.getEditText().getText().toString().trim()));
        volunteer.setSector(lytSector.getEditText().getText().toString().trim());
        volunteer.setSection(lytSection.getEditText().getText().toString().trim());
        volunteer.setMunicipality(lytMunicipioName.getEditText().getText().toString().trim());
        volunteer.setNumberMunicipality(lytMunicipioNumber.getEditText().getText().toString().trim());
        volunteer.setLocalDistrict(lytDistritoLocalName.getEditText().getText().toString().trim());
        volunteer.setNumberLocalDistrict(lytDistritoLocalName.getEditText().getText().toString().trim()); */
    }

    public boolean isComplete() {
        /*if ( volunteer.isJalisco() && !isLocal ) {
            return true;
        }
        return  !(!TextInputLayoutUtils.isValid(lytSection, getString(R.string.fSvbs_section_error))
                | !TextInputLayoutUtils.isValid(lytMunicipioName, getString(R.string.fSvbs_municipio_name_error))
                | !TextInputLayoutUtils.isValid(lytMunicipioNumber, getString(R.string.fSvbs_municipio_number_error))
                | !TextInputLayoutUtils.isValid(lytDistritoLocalName, getString(R.string.fSvbs_municipio_name_error))
                | !TextInputLayoutUtils.isValid(lytDistritoLocalNumber, getString(R.string.fSvbs_municipio_number_error))
                | !TextInputLayoutUtils.isValisMayus(lytDistritoLocalName, "Ingrese solo letras en mayuscula")
                | !TextInputLayoutUtils.isValisMayus(lytMunicipioName, "Ingrese solo letras en mayuscula")); */
        return true;
    }

}
