package com.rld.app.mycampaign.fragments.volunteer;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.MainActivity;
import com.rld.app.mycampaign.databinding.FragmentContactVolunteerBsBinding;
import com.rld.app.mycampaign.files.LocalDataFileManager;
import com.rld.app.mycampaign.models.Section;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.R;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    private FragmentContactVolunteerBsBinding binding;

    private TextInputLayout lytElectorKey;
    private TextInputLayout lytEmail;
    private TextInputLayout lytPhone;

    private TextInputLayout lytStates;
    private TextInputLayout lytSections;

    private Volunteer volunteer;

    private LocalDataFileManager localDataFileManager;

    private boolean isValidState;
    private boolean isValidSection;

    private String currentStateName;

    public ContactFragment(Volunteer volunteer, LocalDataFileManager localDataFileManager, MainActivity mainActivity)
    {
        this.volunteer = volunteer;
        this.localDataFileManager = localDataFileManager;
        currentStateName = mainActivity.getSharedPreferences("localData", Context.MODE_PRIVATE).getString("state_name", null);
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

        lytElectorKey.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });
        lytStates.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });

        if ( localDataFileManager.isEmpty() ) {
            lytSearchSection.setVisibility(View.GONE);
        } else {
            addStates();
            addSections();
            lytSections.setVisibility(View.GONE);
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
                    } else {
                        lytSections.setVisibility(View.GONE);
                    }
                } else {
                    isValidState = false;
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

        return root;
    }

    public boolean isComplete() {
        return true;
    }

    public void setVolunteer() {
        volunteer.setElectorKey(lytElectorKey.getEditText().getText().toString().trim());
        volunteer.setEmail(lytEmail.getEditText().getText().toString().trim());
        volunteer.setPhone(lytPhone.getEditText().getText().toString().trim());
        if ( isValidSection ) {
            volunteer.setSection(findSection(lytSections.getEditText().getText().toString().trim()));
        } else {
            Section section = new Section();
            if ( isValidState ) {
                section.setState(findState(lytStates.getEditText().getText().toString().trim()));
            } else {
                State state = new State();
                state.setName("" + lytStates.getEditText().getText().toString());
            }
            volunteer.setSection(section);
        }
    }

    private void addSections() {
        List<String> stringsSections = new ArrayList<>();
        for ( Section section : localDataFileManager.getSections() ) {
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
}