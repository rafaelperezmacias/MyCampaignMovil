package com.rld.app.mycampaign.fragments.volunteer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.bottomsheets.VolunteerBottomSheet;
import com.rld.app.mycampaign.databinding.FragmentOtherVolunteerBsBinding;
import com.rld.app.mycampaign.models.Volunteer;
import com.rld.app.mycampaign.utils.TextInputLayoutUtils;

public class OtherFragment extends Fragment {

    private static final int NOTES_MAX_LIMIT = 512;

    private FragmentOtherVolunteerBsBinding binding;

    private TextInputLayout lytNotes;
    private RadioButton btnRadioYes;
    private RadioButton btnRadioNo;
    private RadioButton btnRadioVotingBooth;
    private RadioButton btnRadioGeneral;
    private RadioButton btnRadioOther;

    private AppCompatImageView iconNotes;

    private Volunteer volunteer;
    private int type;
    private VolunteerBottomSheet parent;

    public OtherFragment(Volunteer volunteer, int type, VolunteerBottomSheet parent)
    {
        this.volunteer = volunteer;
        this.type = type;
        this.parent = parent;
    }

    @SuppressLint("ClickableViewAccessibility")
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

        iconNotes = binding.iconNotes;

        if ( type == VolunteerBottomSheet.TYPE_SHOW || type == VolunteerBottomSheet.TYPE_UPDATE ) {
            lytNotes.getEditText().setText(volunteer.getNotes());
            btnRadioYes.setChecked(volunteer.isLocalVotingBooth());
            btnRadioNo.setChecked(!volunteer.isLocalVotingBooth());
            btnRadioVotingBooth.setChecked(volunteer.getType() == Volunteer.TYPE_VOTING_BOOTH_REPRESENTATIVE);
            btnRadioGeneral.setChecked(volunteer.getType() == Volunteer.TYPE_GENERAL_REPRESENTATIVE);
            btnRadioOther.setChecked(volunteer.getType() == Volunteer.TYPE_OTHER);
        }

        if ( type == VolunteerBottomSheet.TYPE_SHOW ) {
            TextInputLayoutUtils.disableEditText(lytNotes.getEditText());
            setEditableRadioButton(btnRadioYes, false);
            setEditableRadioButton(btnRadioNo, false);
            setEditableRadioButton(btnRadioVotingBooth, false);
            setEditableRadioButton(btnRadioGeneral, false);
            setEditableRadioButton(btnRadioOther, false);
        }

        lytNotes.getEditText().setOnTouchListener((view, motionEvent) -> {
            parent.setScrollingEnable(true);
            if ( ( motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                parent.setScrollingEnable(false);
            }
            return false;
        });

        TextInputLayoutUtils.initializeFilters(lytNotes.getEditText(), false, NOTES_MAX_LIMIT);

        return root;
    }

    public boolean isComplete() {
        if ( !lytNotes.getEditText().getText().toString().trim().isEmpty() ) {
            return TextInputLayoutUtils.isValidNotes(lytNotes, iconNotes, getContext());
        }
        return true;
    }

    public void setVolunteer() {
        if ( !lytNotes.getEditText().getText().toString().isEmpty() ) {
            volunteer.setNotes(lytNotes.getEditText().getText().toString().trim());
        }
        if ( btnRadioVotingBooth.isChecked() ) {
            volunteer.setType(Volunteer.TYPE_VOTING_BOOTH_REPRESENTATIVE);
        } else if ( btnRadioGeneral.isChecked() ) {
            volunteer.setType(Volunteer.TYPE_GENERAL_REPRESENTATIVE);
        } else if ( btnRadioOther.isChecked() ) {
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

    public void setFocus() {
        lytNotes.getEditText().requestFocus();
        TextInputLayoutUtils.cursorToEnd(lytNotes.getEditText());
    }

}
