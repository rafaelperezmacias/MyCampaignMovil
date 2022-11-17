package com.rld.app.mycampaign.fragments.volunteer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.bottomsheets.VolunteerBottomSheet;
import com.rld.app.mycampaign.databinding.FragmentOtherVolunteerBsBinding;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

public class OtherFragment extends Fragment {

    private FragmentOtherVolunteerBsBinding binding;

    private TextInputLayout lytNotes;
    private RadioButton btnRadioYes;
    private RadioButton btnRadioNo;
    private RadioButton btnRadioVotingBooth;
    private RadioButton btnRadioGeneral;
    private RadioButton btnRadioOther;

    private Volunteer volunteer;
    private int type;

    public OtherFragment(Volunteer volunteer, int type)
    {
        this.volunteer = volunteer;
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOtherVolunteerBsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        lytNotes = binding.lytNotes;
        btnRadioYes = binding.btnRadioYes;
        btnRadioNo = binding.btnRadioNo;
        btnRadioVotingBooth = binding.btnVotingBooth;
        btnRadioGeneral = binding.btnRadioGeneral;
        btnRadioOther = binding.btnRadioOther;

        if ( type == VolunteerBottomSheet.TYPE_SHOW ) {
            lytNotes.getEditText().setText(volunteer.getNotes());
            TextInputLayoutUtils.setEditableEditText(lytNotes.getEditText(), false);
            btnRadioYes.setChecked(volunteer.isLocalVotingBooth());
            btnRadioNo.setChecked(!volunteer.isLocalVotingBooth());
            btnRadioVotingBooth.setChecked(volunteer.getType() == Volunteer.TYPE_VOTING_BOOTH_REPRESENTATIVE);
            btnRadioGeneral.setChecked(volunteer.getType() == Volunteer.TYPE_GENERAL_REPRESENTATIVE);
            btnRadioOther.setChecked(volunteer.getType() == Volunteer.TYPE_OTHER);
            setEditableRadioButton(btnRadioYes, false);
            setEditableRadioButton(btnRadioNo, false);
            setEditableRadioButton(btnRadioVotingBooth, false);
            setEditableRadioButton(btnRadioGeneral, false);
            setEditableRadioButton(btnRadioOther, false);
        }

        lytNotes.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if ( hasFocus ) {
                lytNotes.setHint("");
            } else {
                if ( lytNotes.getEditText().getText().toString().isEmpty() ) {
                    lytNotes.setHint(getString(R.string.fovbs_notes));
                }
            }
        });

        return root;
    }

    public boolean isComplete() {
        return true;
    }

    public void setVolunteer() {
        if ( !lytNotes.getEditText().getText().toString().isEmpty() ) {
            volunteer.setNotes(lytNotes.getEditText().getText().toString().trim());
        }
        if ( btnRadioVotingBooth.isChecked() )         {
            volunteer.setType(Volunteer.TYPE_VOTING_BOOTH_REPRESENTATIVE);
        } else if ( btnRadioGeneral.isChecked() )  {
            volunteer.setType(Volunteer.TYPE_GENERAL_REPRESENTATIVE);
        } else if ( btnRadioOther.isChecked() )  {
            volunteer.setType(Volunteer.TYPE_OTHER);
        }
        volunteer.setLocalVotingBooth(btnRadioYes.isChecked());
    }

    private void setEditableRadioButton(RadioButton btn, boolean enable) {
        btn.setSelected(enable);
        btn.setClickable(enable);
        btn.setLongClickable(enable);
        btn.setFocusable(enable);
    }

}
