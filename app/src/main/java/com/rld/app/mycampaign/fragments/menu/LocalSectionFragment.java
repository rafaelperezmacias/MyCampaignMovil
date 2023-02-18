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
import com.rld.app.mycampaign.api.API;
import com.rld.app.mycampaign.api.Client;
import com.rld.app.mycampaign.databinding.FragmentLocalSectionBinding;
import com.rld.app.mycampaign.dialogs.ProgressDialog;
import com.rld.app.mycampaign.dialogs.ProgressDialogBuilder;
import com.rld.app.mycampaign.files.FederalDistrictFileManager;
import com.rld.app.mycampaign.files.LocalDataFileManager;
import com.rld.app.mycampaign.files.LocalDistrictFileManager;
import com.rld.app.mycampaign.files.MunicipalityFileManager;
import com.rld.app.mycampaign.files.SectionFileManager;
import com.rld.app.mycampaign.files.StateFileManager;
import com.rld.app.mycampaign.models.Entity;
import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.api.EntityAPI;
import com.rld.app.mycampaign.models.api.PageSectionAPI;
import com.rld.app.mycampaign.models.api.SectionAPI;
import com.rld.app.mycampaign.secrets.AppConfig;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        Call<ArrayList<State>> statesCall = Client.getClient(AppConfig.URL_SERVER).create(API.class).getStates();
        statesCall.enqueue(new Callback<ArrayList<State>>() {
            @Override
            public void onResponse(Call<ArrayList<State>> call, Response<ArrayList<State>> response) {
                if ( response.isSuccessful() ) {
                    ArrayList<State> states = response.body();
                    JSONArray jsonArrayStates = StateFileManager.arrayListToJsonArray(states);
                    StateFileManager.writeJSON(jsonArrayStates, getContext());
                    downloadDataOfEntityByState(states, 0, progressDialog);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<State>> call, Throwable t) {
                Log.e("a", "" + t.getMessage());
            }
        });
    }

    private void downloadDataOfEntityByState(ArrayList<State> states, int index, ProgressDialog progressDialog) {
        if ( states.size() == index ) {
            downloadDataOfSectionsByState(states, 0, progressDialog);
            return;
        }
        Call<EntityAPI> federalDistrictsCall = Client.getClient(AppConfig.URL_SERVER).create(API.class)
                .getEntities("" + states.get(index).getId());
        federalDistrictsCall.enqueue(new Callback<EntityAPI>() {
            @Override
            public void onResponse(Call<EntityAPI> call, Response<EntityAPI> response) {
                if ( response.isSuccessful() ) {
                    EntityAPI entityAPI = response.body();
                    // Federal Districts
                    ArrayList<FederalDistrict> federalDistricts = entityAPI.getFederalDistricts();
                    JSONArray jsonArrayFederalDistricts = FederalDistrictFileManager.arrayListToJsonArray(federalDistricts);
                    FederalDistrictFileManager.writeJSON(jsonArrayFederalDistricts,  states.get(index).getId(), getContext());
                    // Local Districts
                    ArrayList<LocalDistrict> localDistricts = entityAPI.getLocalDistricts();
                    JSONArray jsonArrayLocalDistricts = LocalDistrictFileManager.arrayListToJsonArray(localDistricts);
                    LocalDistrictFileManager.writeJSON(jsonArrayLocalDistricts, states.get(index).getId(), getContext());
                    // Municipalities
                    ArrayList<Municipality> municipalities = entityAPI.getMunicipalities();
                    JSONArray jsonArrayMunicipalities = MunicipalityFileManager.arrayListToJsonArray(municipalities);
                    MunicipalityFileManager.writeJSON(jsonArrayMunicipalities, states.get(index).getId(), getContext());
                    //
                    downloadDataOfEntityByState(states, index + 1, progressDialog);
                }
            }
            @Override
            public void onFailure(Call<EntityAPI> call, Throwable t) {
                Log.e("a", "" + t.getMessage());
            }
        });
    }

    private void downloadDataOfSectionsByState(ArrayList<State> states, int index, ProgressDialog progressDialog) {
        if ( states.size() == index ) {
            progressDialog.dismiss();
            return;
        }
        Call<PageSectionAPI> pageSectionAPICall = Client.getClient(AppConfig.URL_SERVER).create(API.class)
                .getSections("" + states.get(index).getId());
        pageSectionAPICall.enqueue(new Callback<PageSectionAPI>() {
            @Override
            public void onResponse(Call<PageSectionAPI> call, Response<PageSectionAPI> response) {
                if ( response.isSuccessful() ) {
                    PageSectionAPI pageSectionAPI = response.body();
                    ArrayList<SectionAPI> sections = new ArrayList<>();
                    sections.addAll(pageSectionAPI.getData());
                    downloadDataOfSectionsByStatePerPage(sections, pageSectionAPI.getCurrent_page(), pageSectionAPI.getLast_page(), states.get(index).getId(), states, index, progressDialog);
                }
            }
            @Override
            public void onFailure(Call<PageSectionAPI> call, Throwable t) {
                Log.e("a", "" + t.getMessage());
            }
        });
    }

    private void downloadDataOfSectionsByStatePerPage(ArrayList<SectionAPI> sections, int currentPage, int lastPage, int stateId, ArrayList<State> states, int index, ProgressDialog progressDialog) {
        if ( currentPage > lastPage ) {
            JSONArray jsonArraySections = SectionFileManager.arrayListToJsonArray(sections);
            SectionFileManager.writeJSON(jsonArraySections, stateId, getContext());
            sections.clear();
            downloadDataOfSectionsByState(states, index + 1, progressDialog);
            return;
        }
        Call<PageSectionAPI> pageSectionAPICall = Client.getClient(AppConfig.URL_SERVER).create(API.class)
                .getSectionsByPage("" + stateId, currentPage + 1);
        pageSectionAPICall.enqueue(new Callback<PageSectionAPI>() {
            @Override
            public void onResponse(Call<PageSectionAPI> call, Response<PageSectionAPI> response) {
                if ( response.isSuccessful() ) {
                    PageSectionAPI pageSectionAPI = response.body();
                    sections.addAll(pageSectionAPI.getData());
                    downloadDataOfSectionsByStatePerPage(sections, currentPage + 1, lastPage, stateId, states, index, progressDialog);
                    return;
                }
                Log.e("a", "fallo");
            }

            @Override
            public void onFailure(Call<PageSectionAPI> call, Throwable t) {
                Log.e("a", "" + t.getMessage());
            }
        });
    }

}
