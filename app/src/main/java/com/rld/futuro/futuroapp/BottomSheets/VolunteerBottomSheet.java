package com.rld.futuro.futuroapp.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rld.futuro.futuroapp.Fragments.VolunteerBS.PersonalFragment;
import com.rld.futuro.futuroapp.R;

public class VolunteerBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetBehavior bottomSheetBehavior;

    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    public VolunteerBottomSheet() {

    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.fragment_volunter_bottom_sheet, null);
        bottomSheet.setContentView(view);
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if(BottomSheetBehavior.STATE_HIDDEN == i) dismiss();
                if(BottomSheetBehavior.STATE_DRAGGING == i) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override public void onSlide(@NonNull View view, float v) { }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Toolbar toolbar = view.findViewById(R.id.volunter_bs_toolbar);
            toolbar.setElevation(12.0f);
        }

        PersonalFragment personalFragment = new PersonalFragment();
        currentFragment = personalFragment;

        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, personalFragment).commit();
        // fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, addressFragment).hide(addressFragment).commit();;

        ((ImageButton) view.findViewById(R.id.volunteer_bs_ib_close))
                .setOnClickListener(v -> {
                    dismiss();
                });

        setCancelable(false);
        return bottomSheet;
    }

    private void showFragment(Fragment fragment, FragmentManager fragmentManager) {
        fragmentManager.beginTransaction().hide(currentFragment).show(fragment).commit();
        currentFragment = fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
