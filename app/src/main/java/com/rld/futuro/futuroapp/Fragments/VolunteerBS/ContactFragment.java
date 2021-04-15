package com.rld.futuro.futuroapp.Fragments.VolunteerBS;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.futuro.futuroapp.MainActivity;
import com.rld.futuro.futuroapp.Models.FileManager;
import com.rld.futuro.futuroapp.Models.Section;
import com.rld.futuro.futuroapp.Models.Volunteer;
import com.rld.futuro.futuroapp.R;
import com.rld.futuro.futuroapp.Models.State;
import com.rld.futuro.futuroapp.Request.AppConfig;
import com.rld.futuro.futuroapp.Utils.TextInputLayoutUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    private TextInputLayout lytElectorKey;
    private TextInputLayout lytEmail;
    private TextInputLayout lytPhone;

    private TextInputLayout lytStates;
    private TextInputLayout lytSectionsAuto;

    private MaterialCardView cardError;
    private TextView txtError;
    private AppCompatImageView imgCardIcon;

    private List<State> states;

    private boolean isJaliscoSelected;
    private boolean isStateValid;
    private boolean isSectionValid;
    private boolean isLocal;

    private Volunteer volunteer;

    private ArrayList<Section> sections;


    public ContactFragment(Volunteer volunteer, MainActivity mainActivity)
    {
        this.volunteer = volunteer;
        sections = mainActivity.getSections();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_volunteer_bs, container, false);

        lytElectorKey = view.findViewById(R.id.fcvbs_electorKey_lyt);
        lytEmail = view.findViewById(R.id.fcvbs_email_lyt);
        lytPhone = view.findViewById(R.id.fcvbs_phone_lyt);

        lytStates = view.findViewById(R.id.fcvbs_states_lyt);
        lytSectionsAuto = view.findViewById(R.id.fcvbs_sections_auto_lyt);

        cardError = view.findViewById(R.id.card_error);
        txtError = view.findViewById(R.id.fcvbs_cardError_txt);
        imgCardIcon = view.findViewById(R.id.fbvbs_cardIcon_img);

        states = State.getStates(getContext());
        addStates();
        addSections();
        sections.clear();
        if ( sections.isEmpty() || sections.size() != AppConfig.SECTIONS_SIZE) {
            isLocal = true;
            showCardError2();
        } else {
            isLocal = false;
            showCardError1();
        }

        lytStates.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ( isValidState(lytStates.getEditText().getText().toString().trim()) ) {
                    isStateValid = true;
                    if ( lytStates.getEditText().getText().toString().equals(State.STATE_JALISCO) ) {
                        if ( !isLocal ) {
                            lytSectionsAuto.setVisibility(View.VISIBLE);
                        }
                        isJaliscoSelected = true;
                    } else {
                        lytSectionsAuto.setVisibility(View.GONE);
                        if ( !isLocal )
                        isJaliscoSelected = false;
                    }
                } else {
                    isStateValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lytSectionsAuto.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int section = Integer.parseInt(lytSectionsAuto.getEditText().getText().toString().trim());
                    if ( isValidSection(section) ) {
                        isSectionValid = true;
                    } else {
                        isSectionValid = false;
                    }
                } catch (Exception ex) {
                    isSectionValid = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void showCardError1() {
        txtError.setText(getString(R.string.fcvbs_error_1));
        cardError.setBackgroundColor(getContext().getResources().getColor(R.color.error_y_background));
        cardError.setCardBackgroundColor(getContext().getResources().getColor(R.color.error_y_background));
        cardError.setStrokeColor(getContext().getResources().getColor(R.color.error_y_text));
        imgCardIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.error_y));
        txtError.setTextColor(getContext().getResources().getColor(R.color.error_y_text));
    }

    private void showCardError2() {
        txtError.setText(getString(R.string.fcvbs_error_2));
        cardError.setBackgroundColor(getContext().getResources().getColor(R.color.error_r_background));
        cardError.setCardBackgroundColor(getContext().getResources().getColor(R.color.error_r_background));
        cardError.setStrokeColor(getContext().getResources().getColor(R.color.error_r_text));
        imgCardIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.error_r));
        txtError.setTextColor(getContext().getResources().getColor(R.color.error_r_text));
    }

    private void addSections() {
        List<String> stringsSections = new ArrayList<>();
        for ( Section section : sections ) {
            stringsSections.add("" + section.getSection());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, stringsSections);
        ((AutoCompleteTextView) lytSectionsAuto.getEditText()).setAdapter(adapter);
    }

    private void addStates() {
        List<String> stringStates = new ArrayList<>();
        for ( State state : states ) {
            stringStates.add(state.getState());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.list_item, stringStates);
        ((AutoCompleteTextView) lytStates.getEditText()).setAdapter(adapter);
    }

    public void getState() {
        for ( State state : states ) {
            if ( state.getState().equals(lytStates.getEditText().getText().toString().trim()) ) {
                volunteer.setJalisco(state.getState().equals(State.STATE_JALISCO));
                volunteer.setState(state.getState());
                volunteer.setStateNumber(state.getStateNumber());
                break;
            }
        }
    }

    public boolean isComplete() {
        if (!TextInputLayoutUtils.isValid(lytElectorKey, getString(R.string.fcvbs_key_error))
                | !TextInputLayoutUtils.isValid(lytEmail, getString(R.string.fcvbs_email_error))
                | !TextInputLayoutUtils.isValid(lytPhone, getString(R.string.fcvbs_phone_error))
                | !isStateSelected()
                | ( isJaliscoSelected && !isLocal && !isSectionSelected()) ) {
            return false;
        }
        return true;
    }

    public void setVolunteer() {
        volunteer.setElectorKey(lytElectorKey.getEditText().getText().toString().trim());
        volunteer.setEmail(lytEmail.getEditText().getText().toString().trim());
        volunteer.setPhone(lytPhone.getEditText().getText().toString().trim());
        volunteer.setLocal(isLocal);
        if ( isJaliscoSelected && !isLocal ) {
            int s = Integer.parseInt(lytSectionsAuto.getEditText().getText().toString().trim());
            for ( Section section : sections ) {
                if ( section.getSection() ==  s) {
                    volunteer.setSectionObject(section);
                    break;
                }
            }
        }
    }

    private boolean isSectionSelected() {
        if ( lytSectionsAuto.getEditText().getText().length() == 0 || !isSectionValid ) {
            lytSectionsAuto.setError("Ingrese una sección valida");
            return false;
        }
        lytSectionsAuto.setError(null);
        return true;
    }

    private boolean isStateSelected() {
        if ( lytStates.getEditText().getText().length() == 0 || !isStateValid ) {
            lytStates.setError(getString(R.string.fcvbs_state_error));
            return false;
        }
        lytStates.setError(null);
        return true;
    }

    private boolean isValidSection(int mySection) {
        for ( Section section : sections ) {
            if ( section.getSection() == mySection ) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidState(String myState) {
        for ( State state : states ) {
            if ( state.getState().equals(myState) ) {
                return true;
            }
        }
        return false;
    }
}