package com.rld.app.mycampaign.fragments.menu;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.adapters.LocalSectionAdapter;
import com.rld.app.mycampaign.api.DownloadManager;
import com.rld.app.mycampaign.databinding.FragmentLocalSectionBinding;
import com.rld.app.mycampaign.dialogs.ProgressDialog;
import com.rld.app.mycampaign.dialogs.ProgressDialogBuilder;
import com.rld.app.mycampaign.files.LocalDataFileManager;
import com.rld.app.mycampaign.models.Entity;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.Token;
import com.rld.app.mycampaign.preferences.TokenPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LocalSectionFragment extends Fragment {

    private static final int MODE_MUNICIPALITIES = 1;
    private static final int MODE_LOCAL_DISTRICTS = 2;
    private static final int MODE_FEDERAL_DISTRICTS = 3;

    private FragmentLocalSectionBinding binding;

    private TextInputLayout lytStates;

    private LocalSectionAdapter adapter;
    private ArrayList<Entity> entities;

    private ArrayList<State> states;
    private ArrayList<Entity> selectedEntities;

    private RequestQueue requestQueue;

    private LocalDataFileManager fileManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entities = new ArrayList<>();
        selectedEntities = new ArrayList<>();
        int idStateSelected = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE).getInt("state_selected",14);
        fileManager = LocalDataFileManager.getInstance(getContext(), idStateSelected);
        states = fileManager.getStates();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocalSectionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        ImageButton btnOptions = binding.btnOptions;
        lytStates = binding.lytStates;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new LocalSectionAdapter(getContext(), entities, (this::onSelectItemOnRecyclerViewListener));
        recyclerView.setAdapter(adapter);

        String[] stringStates = new String[states.size()];
        for ( int i = 0; i < states.size(); i++ ) {
            stringStates[i] = states.get(i).getName();
        }
        ((MaterialAutoCompleteTextView) lytStates.getEditText()).setSimpleItems(stringStates);

        Cache cache = new DiskBasedCache(getActivity().getCacheDir(), 10 * 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        showPreferences();

        PopupMenu popupMenu = new PopupMenu(getContext(), btnOptions);
        popupMenu.inflate(R.menu.local_section_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if ( item.getItemId() == R.id.menu_local_section_update ) {
                downloadDataOfStates();
                return false;
            }
            if ( item.getItemId() == R.id.menu_local_section_municipalities ) {
                return false;
            }
            if ( item.getItemId() == R.id.menu_local_section_local_districts ) {
                return false;
            }
            if ( item.getItemId() == R.id.menu_local_section_federal_districts ) {
                return false;
            }
            return true;
        });
        btnOptions.setOnClickListener(v -> popupMenu.show());

        return root;
    }

    private void onSelectItemOnRecyclerViewListener(int position, boolean selected) {
        if ( selected ) {
            selectedEntities.add(entities.get(position));
            Log.e("1", "" + selectedEntities.size());
            return;
        }
        Entity removeEntity = entities.get(position);
        selectedEntities.remove(removeEntity);
        Log.e("1", "" + selectedEntities.size());
    }

    private void showPreferences() {
        int idStateSelected = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE).getInt("state_selected",14);
        State stateSelected = null;
        for ( State state : states ) {
            if ( state.getId() == idStateSelected ) {
                stateSelected = state;
                break;
            }
        }
        if ( stateSelected == null ) {
            return;
        }
        ((MaterialAutoCompleteTextView) lytStates.getEditText()).setText(stateSelected.getName(), false);
        int mode = getContext().getSharedPreferences("preferences", Context.MODE_PRIVATE).getInt("mode", MODE_MUNICIPALITIES);
        switch ( mode ) {
            case MODE_MUNICIPALITIES: {
                int size = entities.size();
                entities.clear();
                adapter.notifyItemRangeRemoved(0, size);
                entities.addAll(fileManager.getMunicipalities());
            } break;
            case MODE_LOCAL_DISTRICTS: {
                int size = entities.size();
                entities.clear();
                adapter.notifyItemRangeRemoved(0, size);
                entities.addAll(fileManager.getLocalDistricts());
            } break;
            case MODE_FEDERAL_DISTRICTS: {
                int size = entities.size();
                entities.clear();
                adapter.notifyItemRangeRemoved(0, size);
                entities.addAll(fileManager.getFederalDistricts());
            } break;
        }
        Collections.sort(entities, (Comparator<Entity>) (o1, o2) -> o1.getName().compareTo(o2.getName()));
        adapter.notifyItemRangeInserted(0, entities.size());
    }

    private void downloadDataOfStates() {
        ProgressDialogBuilder builder = new ProgressDialogBuilder()
                .setTitle("Actualizando datos...")
                .setCancelable(false);
        ProgressDialog progressDialog = new ProgressDialog(getActivity(), builder);
        progressDialog.show();
        Token token = TokenPreferences.getToken(getContext());
        DownloadManager.downloadDataOfServer(getActivity().getApplicationContext(), token, progressDialog, null);
    }

}
