package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.bottomsheets.VolunteerBottomSheet;
import com.rld.app.mycampaign.databinding.FragmentPersonalVolunteerBsBinding;
import com.rld.app.mycampaign.models.Address;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.utils.DateFormats;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PersonalFragment extends Fragment {

    private static final int FATHERS_LASTNAME_MAX_LIMIT = 40;
    private static final int MOTHERS_LASTNAME_MAX_LIMIT = 40;
    private static final int NAME_MAX_LIMIT = 70;
    private static final int STREET_MAX_LIMIT = 70;
    private static final int EXTERNAL_NUMBER_MAX_LIMIT = 10;
    private static final int INTERNAL_NUMBER_MAX_LIMIT = 10;
    private static final int SUBURB_MAX_LIMIT = 50;
    private static final int ZIPCODE_MAX_LIMIT = 10;

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

    private AppCompatImageView iconFathersLastname;
    private AppCompatImageView iconBirthdate;
    private AppCompatImageView iconStreet;
    private AppCompatImageView iconExternalNumber;
    private AppCompatImageView iconZipcode;

    private Calendar birthDate;

    private Volunteer volunteer;
    private int type;

    public PersonalFragment(Volunteer volunteer, int type) {
        this.volunteer = volunteer;
        this.type = type;
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

        iconFathersLastname = binding.iconFathersLastname;
        iconBirthdate = binding.iconBirthdate;
        iconStreet = binding.iconStreet;
        iconExternalNumber = binding.iconExternalNumber;
        iconZipcode = binding.iconZipcode;

        birthDate = Calendar.getInstance();

        lytBirthdate.getEditText().setClickable(true);
        lytBirthdate.getEditText().setFocusable(false);
        lytBirthdate.getEditText().setOnClickListener(view -> {
            boolean isCalendarActive = false;
            Calendar calendar = Calendar.getInstance();
            if ( !lytBirthdate.getEditText().getText().toString().isEmpty() ) {
                calendar = DateFormats.getDateInFormat(lytBirthdate.getEditText().getText().toString(), "dd/MM/yyyy");
                isCalendarActive = true;
            }
            CalendarConstraints.Builder calendarConstraints = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now());
            MaterialDatePicker<Long> dialog = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Seleccione la fecha")
                    .setSelection(isCalendarActive ? calendar.getTimeInMillis() : MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(calendarConstraints.build())
                    .build();
            dialog.show(getChildFragmentManager(), dialog.getTag());
            dialog.addOnPositiveButtonClickListener(selection -> {
                TimeZone timeZoneUTC = TimeZone.getDefault();
                int offSetFromUTC = timeZoneUTC.getOffset(new Date().getTime()) * - 1;
                birthDate.setTimeInMillis(selection + offSetFromUTC);
                lytBirthdate.getEditText().setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(birthDate.getTime()) );
            });
        });

        lytFathersLastname.getEditText().setText(volunteer.getFathersLastname());
        lytMothersLastname.getEditText().setText(volunteer.getMothersLastname());
        lytName.getEditText().setText(volunteer.getName());
        if ( volunteer.getBirthdate() != null ) {
            String date = volunteer.getBirthdate().get(Calendar.DAY_OF_MONTH) + "/" + (volunteer.getBirthdate().get(Calendar.MONTH) + 1) + "/" + volunteer.getBirthdate().get(Calendar.YEAR);
            lytBirthdate.getEditText().setText(date);
        }
        if ( volunteer.getAddress() != null ) {
            lytStreet.getEditText().setText(volunteer.getAddress().getStreet());
            lytInternalNumber.getEditText().setText(volunteer.getAddress().getInternalNumber());
            lytExternalNumber.getEditText().setText(volunteer.getAddress().getExternalNumber());
            lytSuburb.getEditText().setText(volunteer.getAddress().getSuburb());
            lytZipcode.getEditText().setText(volunteer.getAddress().getZipcode());
        }

        if ( type == VolunteerBottomSheet.TYPE_UPDATE ) {
            TextInputLayoutUtils.cursorToEnd(lytFathersLastname.getEditText());
        }

        if ( type == VolunteerBottomSheet.TYPE_SHOW ) {
            TextInputLayoutUtils.disableEditText(lytFathersLastname.getEditText());
            TextInputLayoutUtils.disableEditText(lytMothersLastname.getEditText());
            TextInputLayoutUtils.disableEditText(lytName.getEditText());
            TextInputLayoutUtils.disableEditText(lytStreet.getEditText());
            TextInputLayoutUtils.disableEditText(lytInternalNumber.getEditText());
            TextInputLayoutUtils.disableEditText(lytBirthdate.getEditText());
            TextInputLayoutUtils.disableEditText(lytExternalNumber.getEditText());
            TextInputLayoutUtils.disableEditText(lytSuburb.getEditText());
            TextInputLayoutUtils.disableEditText(lytZipcode.getEditText());
        }

        TextInputLayoutUtils.initializeFilters(lytFathersLastname.getEditText(), true, FATHERS_LASTNAME_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytMothersLastname.getEditText(), true, MOTHERS_LASTNAME_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytName.getEditText(), true, NAME_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytStreet.getEditText(), true, STREET_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytExternalNumber.getEditText(), false, EXTERNAL_NUMBER_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytInternalNumber.getEditText(), true, INTERNAL_NUMBER_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytSuburb.getEditText(), true, SUBURB_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytZipcode.getEditText(), false, ZIPCODE_MAX_LIMIT);

        return root;
    }

    public boolean isComplete() {
        // return true;
        return isFathersLastnameComplete() & isMothersLastnameComplete() & isNameComplete() & isCompleteBirthdate()
                & isCompleteStreet() & isCompleteExternalNumber() & isCompleteInternalNumber()
                & isCompleteSuburb() & isCompleteZipcode();
    }

    private boolean isFathersLastnameComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytFathersLastname, "Ingrese el apellido paterno", iconFathersLastname, getContext())
                && TextInputLayoutUtils.isValidMayusWithoutNumbers(lytFathersLastname, iconFathersLastname, getContext());
    }

    private boolean isMothersLastnameComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytMothersLastname, "Ingrese el apellido materno", null, getContext())
                && TextInputLayoutUtils.isValidMayusWithoutNumbers(lytMothersLastname, null, getContext());
    }

    private boolean isNameComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytName, "Ingrese el nombre", null, getContext())
                && TextInputLayoutUtils.isValidMayusWithoutNumbers(lytName, null, getContext());
    }

    private boolean isCompleteBirthdate() {
        return TextInputLayoutUtils.isNotEmpty(lytBirthdate, "Ingrese la fecha de nacimiento", iconBirthdate, getContext());
    }

    private boolean isCompleteStreet() {
        return TextInputLayoutUtils.isNotEmpty(lytStreet, "Ingrese la calle", iconStreet, getContext())
                && TextInputLayoutUtils.isValidMayusWithoutNumbers(lytStreet, iconStreet, getContext());
    }

    private boolean isCompleteExternalNumber() {
        return TextInputLayoutUtils.isNotEmpty(lytExternalNumber, "Ingrese el número exterior", iconExternalNumber, getContext())
                && TextInputLayoutUtils.isValidNumbers(lytExternalNumber, iconExternalNumber, getContext());
    }

    private boolean isCompleteInternalNumber() {
        return TextInputLayoutUtils.isValidMayusWithNumbers(lytInternalNumber, null, getContext());
    }

    private boolean isCompleteSuburb() {
        return TextInputLayoutUtils.isNotEmpty(lytSuburb, "Ingrese la colonia", null, getContext())
                && TextInputLayoutUtils.isValidMayusWithoutNumbers(lytSuburb, null, getContext());
    }

    private boolean isCompleteZipcode() {
        return TextInputLayoutUtils.isNotEmpty(lytZipcode, "Ingrese el código postal", iconZipcode, getContext())
                && TextInputLayoutUtils.isValidNumbers(lytZipcode, iconZipcode, getContext());
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
        if ( !lytInternalNumber.getEditText().getText().toString().trim().isEmpty() ) {
            address.setInternalNumber(lytInternalNumber.getEditText().getText().toString().trim());
        } else if ( type == VolunteerBottomSheet.TYPE_UPDATE ) {
            address.setInternalNumber("");
        }
        address.setSuburb(lytSuburb.getEditText().getText().toString().trim());
        address.setZipcode(lytZipcode.getEditText().getText().toString().trim());
    }

}
