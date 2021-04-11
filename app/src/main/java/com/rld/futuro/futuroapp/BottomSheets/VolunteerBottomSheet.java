package com.rld.futuro.futuroapp.BottomSheets;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.rld.futuro.futuroapp.Fragments.VolunteerBS.ContactFragment;
import com.rld.futuro.futuroapp.Fragments.VolunteerBS.OtherFragment;
import com.rld.futuro.futuroapp.Fragments.VolunteerBS.PersonalFragment;
import com.rld.futuro.futuroapp.Fragments.VolunteerBS.SectionFragment;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = view.findViewById(R.id.volunter_bs_toolbar);
            toolbar.setElevation(12.0f);
        }

        MaterialButton btnSave = view.findViewById(R.id.fvbs_save_btn);
        ImageButton btnClose = view.findViewById(R.id.fvbs_close_btn);

        PersonalFragment personalFragment = new PersonalFragment();
        ContactFragment contactFragment = new ContactFragment();
        OtherFragment otherFragment = new OtherFragment();
        SectionFragment sectionFragment = new SectionFragment();
        currentFragment = personalFragment;

        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, personalFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, contactFragment).hide(contactFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, otherFragment).hide(otherFragment).commit();
        fragmentManager.beginTransaction().add(R.id.volunteer_bs_container, sectionFragment).hide(sectionFragment).commit();

        btnClose.setOnClickListener(v -> {
            int imageToButton = R.drawable.ic_baseline_arrow_back_24;
            if ( currentFragment == personalFragment ) {
                Toast.makeText(getContext(), "Cerrar", Toast.LENGTH_SHORT).show();
                // dismiss();
                return;
            } else if ( currentFragment == contactFragment ) {
                showFragment(personalFragment, fragmentManager);
                imageToButton = R.drawable.ic_sharp_close_24;
            } else if ( currentFragment == sectionFragment ) {
                showFragment(contactFragment, fragmentManager);
            } else if ( currentFragment == otherFragment ) {
                showFragment(sectionFragment, fragmentManager);
            }
            btnSave.setText("CONTINUAR");
            btnClose.setImageResource(imageToButton);
        });

        btnSave.setOnClickListener(v -> {
            String textToButton = "CONTINUAR";
            boolean printInfo = true;
            if ( currentFragment == personalFragment ) {

                if ( !personalFragment.isComplete() ) {
                    printInfo = false;
                    return;
                }
                showFragment(contactFragment, fragmentManager);

            } else if ( currentFragment == contactFragment ) {

                if ( !contactFragment.isComplete() ) {
                    printInfo = false;
                    return;
                }
                showFragment(sectionFragment, fragmentManager);

            } else if ( currentFragment == sectionFragment ) {
                showFragment(otherFragment, fragmentManager);
                textToButton = "FINALIZAR";
            } else if ( currentFragment == otherFragment ) {
                Toast.makeText(getContext(), "Guardar", Toast.LENGTH_SHORT).show();
                return;
            }
            if ( printInfo ) {
                btnSave.setText(textToButton);
                btnClose.setImageResource(R.drawable.ic_baseline_arrow_back_24);
            }
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
