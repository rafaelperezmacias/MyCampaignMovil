package com.rld.app.mycampaign.fragments.volunteer;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.MainActivity;
import com.rld.app.mycampaign.bottomsheets.VolunteerBottomSheet;
import com.rld.app.mycampaign.databinding.FragmentContactVolunteerBsBinding;
import com.rld.app.mycampaign.files.LocalDataFileManager;
import com.rld.app.mycampaign.models.Section;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.preferences.LocalDataPreferences;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactFragment extends Fragment {

    private static final int ELECTORAL_KEY_SIZE = 18;
    private static final int EMAIL_MAX_LIMIT = 255;
    private static final int PHONE_MAX_LIMIT = 20;

    private FragmentContactVolunteerBsBinding binding;

    private TextInputLayout lytElectorKey;
    private TextInputLayout lytEmail;
    private TextInputLayout lytPhone;

    private TextInputLayout lytStates;
    private TextInputLayout lytSections;

    private AppCompatImageView iconEmail;
    private AppCompatImageView iconPhone;
    private AppCompatImageView iconElectoralKey;

    private Volunteer volunteer;

    private LocalDataFileManager localDataFileManager;

    private boolean isValidState;
    private boolean isValidSection;

    private String currentStateName;
    private int type;

    public ContactFragment(Volunteer volunteer, LocalDataFileManager localDataFileManager, MainActivity mainActivity, int type)
    {
        this.volunteer = volunteer;
        this.localDataFileManager = localDataFileManager;
        this.type = type;
        currentStateName = LocalDataPreferences.getNameStateSelected(mainActivity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentContactVolunteerBsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lytElectorKey = binding.lytElectoralKey;
        lytEmail = binding.lytEmail;
        lytPhone = binding.lytPhone;
        lytStates = binding.lytStates;
        lytSections = binding.lytSections;
        LinearLayout lytSearchSection = binding.lytSearchSection;

        iconEmail = binding.iconEmail;
        iconPhone = binding.iconPhone;
        iconElectoralKey = binding.iconElectoralKey;

        lytElectorKey.getEditText().setText(volunteer.getElectorKey());
        lytPhone.getEditText().setText(volunteer.getPhone());
        lytEmail.getEditText().setText(volunteer.getEmail());

        lytElectorKey.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytStates.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });

        isValidState = false;
        isValidSection = false;
        if ( type == VolunteerBottomSheet.TYPE_INSERT ) {
            if ( localDataFileManager.isEmpty() ) {
                lytSearchSection.setVisibility(View.GONE);
            } else {
                addStates();
                addSections();
                lytSections.setVisibility(View.GONE);
                if ( volunteer.getSection() != null && volunteer.getSection().getState() != null ) {
                    if ( isValidState(volunteer.getSection().getState().getName()) ) {
                        ((MaterialAutoCompleteTextView) lytStates.getEditText()).setText(volunteer.getSection().getState().getName(), false);
                        isValidState = true;
                        if ( isValidSection(volunteer.getSection().getSection()) ) {
                            ((MaterialAutoCompleteTextView) lytSections.getEditText()).setText(volunteer.getSection().getSection(), false);
                            lytSections.setVisibility(View.VISIBLE);
                            isValidSection = true;
                        } else if ( currentStateName.equals(volunteer.getSection().getState().getName()) ) {
                            ((MaterialAutoCompleteTextView) lytSections.getEditText()).setText(volunteer.getSection().getSection(), false);
                            lytSections.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }

        if ( type == VolunteerBottomSheet.TYPE_UPDATE ) {
            lytSearchSection.setVisibility(View.GONE);
        }

        if ( type == VolunteerBottomSheet.TYPE_SHOW ) {
            lytSearchSection.setVisibility(View.GONE);
            TextInputLayoutUtils.disableEditText(lytElectorKey.getEditText());
            TextInputLayoutUtils.disableEditText(lytEmail.getEditText());
            TextInputLayoutUtils.disableEditText(lytPhone.getEditText());
        }

        lytStates.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( isValidState(lytStates.getEditText().getText().toString().trim()) ) {
                    isValidState = true;
                    if ( lytStates.getEditText().getText().toString().equals(currentStateName) ) {
                        lytSections.setVisibility(View.VISIBLE);
                        isValidSection = isValidSection(lytSections.getEditText().getText().toString().trim());
                    } else {
                        lytSections.setVisibility(View.GONE);
                        isValidSection = false;
                    }
                } else {
                    isValidState = false;
                    isValidSection = false;
                    lytSections.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lytSections.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( isValidSection(lytSections.getEditText().getText().toString().trim()) ) {
                    isValidSection = true;
                } else {
                    isValidSection = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TextInputLayoutUtils.initializeFilters(lytElectorKey.getEditText(), true, ELECTORAL_KEY_SIZE);
        TextInputLayoutUtils.initializeFilters(lytPhone.getEditText(), false, PHONE_MAX_LIMIT);
        TextInputLayoutUtils.initializeFilters(lytEmail.getEditText(), false, EMAIL_MAX_LIMIT);

        return root;
    }

    public boolean isComplete() {
        // return true;
        return isElectoralKeyComplete() & isEmailComplete() & isPhoneComplete();
    }

    private boolean isElectoralKeyComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytElectorKey, "Ingrese la clave de elector", iconElectoralKey, getContext())
                && TextInputLayoutUtils.isValidElectoralKey(lytElectorKey, iconElectoralKey, getContext());
    }

    private boolean isEmailComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytEmail, "Ingrese el correo electrónico", iconEmail, getContext())
                && TextInputLayoutUtils.isValidEmail(lytEmail, iconEmail, getContext());
    }

    private boolean isPhoneComplete() {
        return TextInputLayoutUtils.isNotEmpty(lytPhone, "Ingrese el número teléfonico", iconPhone, getContext())
                && TextInputLayoutUtils.isValidNumbers(lytPhone, iconPhone, getContext());
    }

    public void setVolunteer() {
        volunteer.setElectorKey(lytElectorKey.getEditText().getText().toString().trim());
        volunteer.setEmail(lytEmail.getEditText().getText().toString().trim());
        volunteer.setPhone(lytPhone.getEditText().getText().toString().trim());
        if ( type == VolunteerBottomSheet.TYPE_INSERT ) {
            if ( isValidSection ) {
                volunteer.setSection(findSection(lytSections.getEditText().getText().toString().trim()));
            } else {
                Section section = new Section();
                if ( isValidState ) {
                    section.setState(findState(lytStates.getEditText().getText().toString().trim()));
                    volunteer.setSection(section);
                } else if ( !lytStates.getEditText().getText().toString().trim().isEmpty() ) {
                    State state = new State();
                    state.setName(lytStates.getEditText().getText().toString().trim());
                    section.setState(state);
                    volunteer.setSection(section);
                }
            }
        }
    }

    private Section findSection(String numberSection) {
        for ( Section section : localDataFileManager.getSections() ) {
            if ( section.getSection().equals(numberSection) ) {
                return section;
            }
        }
        return null;
    }

    private State findState(String nameState) {
        for ( State state : localDataFileManager.getStates() ) {
            if ( state.getName().equals(nameState) ) {
                return state;
            }
        }
        return null;
    }

    private boolean isValidSection(String numberSection) {
        for ( Section section : localDataFileManager.getSections() ) {
            if ( section.getSection().equals(numberSection) ) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidState(String nameState) {
        for ( State state : localDataFileManager.getStates() ) {
            if ( state.getName().equals(nameState) ) {
                return true;
            }
        }
        return false;
    }

    private void addSections() {
        ArrayList<Section> sections = localDataFileManager.getSections();
        Collections.sort(sections, (o1, o2) -> Integer.parseInt(o1.getSection()) - Integer.parseInt(o2.getSection()) );
        List<String> stringsSections = new ArrayList<>();
        for ( Section section : sections ) {
            stringsSections.add(section.getSection());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, stringsSections);
        ((AutoCompleteTextView) lytSections.getEditText()).setAdapter(adapter);
    }

    private void addStates() {
        List<String> stringStates = new ArrayList<>();
        for ( State state : localDataFileManager.getStates() ) {
            stringStates.add(state.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, stringStates);
        ((AutoCompleteTextView) lytStates.getEditText()).setAdapter(adapter);
    }

    public void setFocus() {
        lytElectorKey.getEditText().requestFocus();
        TextInputLayoutUtils.cursorToEnd(lytElectorKey.getEditText());
    }

}