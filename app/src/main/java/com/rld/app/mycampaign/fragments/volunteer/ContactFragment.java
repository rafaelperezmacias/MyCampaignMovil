package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.databinding.FragmentContactVolunteerBsBinding;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.R;

public class ContactFragment extends Fragment {

    private FragmentContactVolunteerBsBinding binding;

    private TextInputLayout lytElectorKey;
    private TextInputLayout lytEmail;
    private TextInputLayout lytPhone;

    private TextInputLayout lytStates;
    private TextInputLayout lytSectionsAuto;

    private MaterialCardView cardError;
    private TextView txtCardError;
    private AppCompatImageView imgCardIcon;

    private Volunteer volunteer;

    public ContactFragment(Volunteer volunteer)
    {
        this.volunteer = volunteer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContactVolunteerBsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lytElectorKey = binding.lytElectoralKey;
        lytEmail = binding.lytEmail;
        lytPhone = binding.lytPhone;
        lytStates = binding.lytStates;
        lytSectionsAuto = binding.lytSections;
        cardError = binding.cardError;
        txtCardError = binding.cardErrorText;
        imgCardIcon = binding.cardErrorIcon;

        lytElectorKey.getEditText().setFilters(new InputFilter[] { new InputFilter.AllCaps() });

        lytStates.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }

    private void showCardError1() {
        txtCardError.setText(getString(R.string.fcvbs_error_1));
        cardError.setBackgroundColor(getContext().getResources().getColor(R.color.error_y_background));
        cardError.setCardBackgroundColor(getContext().getResources().getColor(R.color.error_y_background));
        cardError.setStrokeColor(getContext().getResources().getColor(R.color.error_y_text));
        imgCardIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.error_y));
        txtCardError.setTextColor(getContext().getResources().getColor(R.color.error_y_text));
    }

    private void showCardError2() {
        txtCardError.setText(getString(R.string.fcvbs_error_2));
        cardError.setBackgroundColor(getContext().getResources().getColor(R.color.error_r_background));
        cardError.setCardBackgroundColor(getContext().getResources().getColor(R.color.error_r_background));
        cardError.setStrokeColor(getContext().getResources().getColor(R.color.error_r_text));
        imgCardIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.error_r));
        txtCardError.setTextColor(getContext().getResources().getColor(R.color.error_r_text));
    }

    public boolean isComplete() {
        return true;
    }

    public void setVolunteer() {

    }

}