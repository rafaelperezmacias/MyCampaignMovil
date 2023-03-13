package com.rld.app.mycampaign.fragments.menu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rld.app.mycampaign.MainActivity;
import com.rld.app.mycampaign.adapters.VolunteersAdapter;
import com.rld.app.mycampaign.databinding.FragmentVolunteerBinding;
import com.rld.app.mycampaign.models.Volunteer;

import java.util.ArrayList;

public class VolunteerFragment extends Fragment {

    private FragmentVolunteerBinding binding;

    private static OnClickMenuVolunteerListener onClickMenuVolunteerListener;
    private ArrayList<Volunteer> volunteers;
    private VolunteersAdapter adapter;
    private RecyclerView volunteersRecyclerview;

    private MainActivity mainActivity;

    private FloatingActionButton btnMenuVolunteer;
    private MaterialCardView cardNoData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volunteers = new ArrayList<>();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentVolunteerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Activity parentActivity = getActivity();
        if ( parentActivity instanceof MainActivity ) {
            mainActivity = (MainActivity) parentActivity;
        }

        MaterialButton btnErrorAdd = binding.btnErrorAdd;
        btnMenuVolunteer = binding.btnMenuVolunteer;
        volunteersRecyclerview = binding.volunteersRecyclerview;
        cardNoData = binding.cardNoData;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        volunteersRecyclerview.setLayoutManager(linearLayoutManager);
        volunteersRecyclerview.setHasFixedSize(true);

        adapter = new VolunteersAdapter(mainActivity, volunteers);
        volunteersRecyclerview.setAdapter(adapter);

        btnMenuVolunteer.setOnClickListener(v -> {
            if ( onClickMenuVolunteerListener != null ) {
                onClickMenuVolunteerListener.showMenuVolunteer();
                btnMenuVolunteer.setVisibility(View.GONE);
            }
        });

        if ( onClickMenuVolunteerListener != null ) {
            onClickMenuVolunteerListener.initializeVolunteersList(VolunteerFragment.this);
        }

        btnErrorAdd.setOnClickListener(v -> {
            if ( onClickMenuVolunteerListener != null ) {
                onClickMenuVolunteerListener.showRegisterForm();
            }
        });

        return root;
    }

    public interface OnClickMenuVolunteerListener {
        void initializeVolunteersList(VolunteerFragment volunteerFragment);
        void showMenuVolunteer();
        void showRegisterForm();
    }

    public static void setOnClickAddVolunteer(OnClickMenuVolunteerListener onClickMenuVolunteerListener) {
        VolunteerFragment.onClickMenuVolunteerListener = onClickMenuVolunteerListener;
    }

    public void updateVolunteers(ArrayList<Volunteer> volunteers) {
        adapter = new VolunteersAdapter(mainActivity, volunteers);
        volunteersRecyclerview.setAdapter(adapter);
        if ( adapter.getItemCount() == 0 ) {
            cardNoData.setVisibility(View.VISIBLE);
            volunteersRecyclerview.setVisibility(View.GONE);
            btnMenuVolunteer.setVisibility(View.GONE);
        } else {
            volunteersRecyclerview.setVisibility(View.VISIBLE);
            btnMenuVolunteer.setVisibility(View.VISIBLE);
            cardNoData.setVisibility(View.GONE);
        }
    }

    public void notifyChangeOnRecyclerView() {
        if ( adapter != null ) {
            adapter.notifyDataSetChanged();
        }
    }

    public void notifyChangeOnRecyclerView(int index) {
        if ( adapter != null ) {
            adapter.notifyItemChanged(index);
        }
    }

    public void showMenuVolunteer() {
        if ( btnMenuVolunteer != null ) {
            btnMenuVolunteer.setVisibility(View.VISIBLE);
        }
    }

}