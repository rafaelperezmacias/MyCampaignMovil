package com.rld.app.mycampaign.fragments.menu;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.MainActivity;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.adapters.LocalSectionAdapter;
import com.rld.app.mycampaign.databinding.FragmentLocalSectionBinding;
import com.rld.app.mycampaign.files.FederalDistrictFileManager;
import com.rld.app.mycampaign.files.LocalDataFileManager;
import com.rld.app.mycampaign.files.LocalDistrictFileManager;
import com.rld.app.mycampaign.files.MunicipalityFileManager;
import com.rld.app.mycampaign.files.SectionFileManager;
import com.rld.app.mycampaign.files.StateFileManager;
import com.rld.app.mycampaign.models.Entity;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.secrets.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocalSectionFragment extends Fragment {

    private static final int MODE_MUNICIPALITIES = 1;
    private static final int MODE_LOCAL_DISTRICTS = 2;
    private static final int MODE_FEDERAL_DISTRICTS = 3;

    private FragmentLocalSectionBinding binding;

    private TextInputLayout lytStates;

    private LocalSectionAdapter adapter;
    private ArrayList<Entity> entities;

    private ArrayList<State> states;

    private RequestQueue requestQueue;

    private LocalDataFileManager fileManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entities = new ArrayList<>();
        fileManager = LocalDataFileManager.getInstance(getContext());
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

        adapter = new LocalSectionAdapter(getContext(), entities);
        recyclerView.setAdapter(adapter);

        String[] stringStates = new String[states.size()];
        for ( int i = 0; i < states.size(); i++ ) {
            stringStates[i] = states.get(i).getName();
        }
        ((MaterialAutoCompleteTextView) lytStates.getEditText()).setSimpleItems(stringStates);

        requestQueue = Volley.newRequestQueue(getActivity());
        showPreferences();

        PopupMenu popupMenu = new PopupMenu(getContext(), btnOptions);
        popupMenu.inflate(R.menu.local_section_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_local_section_update) {
                downloadDataOfSections();
                return false;
            }
            if (item.getItemId() == R.id.menu_local_section_municipalities) {
                return false;
            }
            if (item.getItemId() == R.id.menu_local_section_local_districts) {
                return false;
            }
            if (item.getItemId() == R.id.menu_local_section_federal_districts) {
                return false;
            }
            return true;
        });
        btnOptions.setOnClickListener(v -> popupMenu.show());

        return root;
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

    }

    private void downloadDataOfSections() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, AppConfig.GET_SECTIONS, null, response -> {
            try {
                if ( response.getInt("code") == 200 ) {
                    StateFileManager.writeJSON(response.getJSONArray("states"), getContext());
                    JSONObject state = response.getJSONObject("state");
                    getActivity().getSharedPreferences("localData", Context.MODE_PRIVATE).edit().putInt("state_id", state.getInt("id")).apply();
                    getActivity().getSharedPreferences("localData", Context.MODE_PRIVATE).edit().putString("state_name", state.getString("name")).apply();
                    FederalDistrictFileManager.writeJSON(state.getJSONArray("federal_districts"), state.getInt("id"), getContext());
                    LocalDistrictFileManager.writeJSON(state.getJSONArray("local_districts"), state.getInt("id"), getContext());
                    MunicipalityFileManager.writeJSON(state.getJSONArray("municipalities"), state.getInt("id"), getContext());
                    SectionFileManager.writeJSON(state.getJSONArray("sections"), getContext());
                    getActivity().getSharedPreferences("localData", Context.MODE_PRIVATE).edit().putBoolean("saved", true).apply();
                }
            } catch ( JSONException ex ) {
                Log.e("Error", "" + ex.getMessage());
            }
        }, error -> {
            Log.e("Error", "" + error.getMessage());
        });
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

}
