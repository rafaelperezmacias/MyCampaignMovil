package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.databinding.FragmentPersonalVolunteerBsBinding;
import com.rld.app.mycampaign.models.Address;
import com.rld.app.mycampaign.models.Volunteer;

import java.util.Calendar;
import java.util.Date;

public class PersonalFragment extends Fragment {

    private FragmentPersonalVolunteerBsBinding binding;

    private TextInputLayout lytFathersLastname;
    private TextInputLayout lytMothersLastname;
    private TextInputLayout lytName;
    private TextInputLayout lytBirthdate;
    private TextInputLayout lytStreet;
    private TextInputLayout lytExternalNumber;
    private TextInputLayout lytInternalNumber;
    private TextInputLayout lytSuburb;
    private TextInputLayout lytZipcode;

    private Volunteer volunteer;

    private Calendar birthDate;

    public PersonalFragment(Volunteer volunteer)
    {
        this.volunteer = volunteer;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPersonalVolunteerBsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lytFathersLastname = binding.lytFathersLastname;
        lytMothersLastname = binding.lytMothersLastname;
        lytName = binding.lytName;
        lytBirthdate = binding.lytBirthdate;
        lytStreet = binding.lytStreet;
        lytExternalNumber = binding.lytExternalNumber;
        lytInternalNumber = binding.lytInternalNumber;
        lytSuburb = binding.lytSuburb;
        lytZipcode = binding.lytZipcode;

        birthDate = Calendar.getInstance();

        lytBirthdate.getEditText().setOnFocusChangeListener((view, focus) -> {
            if ( focus ) {
                lytBirthdate.getEditText().callOnClick();
            }
        });

        lytBirthdate.getEditText().setOnClickListener(view -> {
            CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now());
            MaterialDatePicker<Long> dialog = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Seleccione la fecha")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(calendarConstraints.build())
                    .build();
            dialog.show(getChildFragmentManager(), dialog.getTag());
            dialog.addOnPositiveButtonClickListener(selection -> {
                birthDate.setTimeInMillis(selection);
                String date = birthDate.get(Calendar.DAY_OF_MONTH) + "/" + (birthDate.get(Calendar.MONTH) + 1) + "/" + birthDate.get(Calendar.YEAR);
                lytBirthdate.getEditText().setText(date);
            });
        });

        lytFathersLastname.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytMothersLastname.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytName.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytStreet.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytInternalNumber.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytSuburb.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });

        return root;
    }

    public void setVolunteer() {
        volunteer.setFathersLastname(lytFathersLastname.getEditText().getText().toString().trim());
        volunteer.setMothersLastname(lytMothersLastname.getEditText().getText().toString().trim());
        volunteer.setName(lytName.getEditText().getText().toString().trim());
        volunteer.setBirthdate(birthDate);
        Address address = volunteer.getAddress();
        if ( address == null ) {
            address = new Address();
            volunteer.setAddress(address);
        }
        address.setStreet(lytStreet.getEditText().getText().toString().trim());
        address.setExternalNumber(lytExternalNumber.getEditText().getText().toString().trim());
        if ( !lytInternalNumber.getEditText().getText().toString().isEmpty() ) {
            address.setInternalNumber(lytInternalNumber.getEditText().getText().toString().trim());
        }
        address.setSuburb(lytSuburb.getEditText().getText().toString().trim());
        address.setZipcode(lytZipcode.getEditText().getText().toString().trim());
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
