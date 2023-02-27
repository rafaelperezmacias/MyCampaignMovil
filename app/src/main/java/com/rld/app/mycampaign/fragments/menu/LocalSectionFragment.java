package com.rld.app.mycampaign.fragments.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.adapters.LocalSectionAdapter;
import com.rld.app.mycampaign.api.DownloadManager;
import com.rld.app.mycampaign.databinding.FragmentLocalSectionBinding;
import com.rld.app.mycampaign.dialogs.ProgressDialog;
import com.rld.app.mycampaign.dialogs.ProgressDialogBuilder;
import com.rld.app.mycampaign.files.LocalDataFileManager;
import com.rld.app.mycampaign.models.EntitySelect;
import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.Token;
import com.rld.app.mycampaign.preferences.LocalDataPreferences;
import com.rld.app.mycampaign.preferences.TokenPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class LocalSectionFragment extends Fragment {

    private FragmentLocalSectionBinding binding;

    private TextInputLayout lytStates;
    private TextView txtMode;

    private LocalSectionAdapter adapter;
    private ArrayList<EntitySelect> entities;
    private ArrayList<State> states;

    private LocalDataFileManager fileManager;

    private int currentIdStateSelected;
    private int currentTypeModeSelected;
    private int idStateSelected;
    private int typeModeSelected;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entities = new ArrayList<>();
        idStateSelected = LocalDataPreferences.getIdStateSelected(requireContext());
        currentIdStateSelected = idStateSelected;
        typeModeSelected = LocalDataPreferences.getMode(requireContext());
        currentTypeModeSelected = typeModeSelected;
        fileManager = LocalDataFileManager.getInstanceWithAllData(requireContext(), idStateSelected);
        states = fileManager.getStates();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocalSectionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        ImageButton btnOptions = binding.btnOptions;
        txtMode = binding.txtMode;
        MaterialButton btnSave = binding.btnSave;
        lytStates = binding.lytStates;

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new LocalSectionAdapter(requireContext(), entities);
        recyclerView.setAdapter(adapter);

        String[] stringStates = new String[states.size()];
        for ( int i = 0; i < states.size(); i++ ) {
            stringStates[i] = states.get(i).getName();
        }
        ((MaterialAutoCompleteTextView) lytStates.getEditText()).setSimpleItems(stringStates);

        showPreferences();

        PopupMenu popupMenu = new PopupMenu(getContext(), btnOptions);
        popupMenu.inflate(R.menu.local_section_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if ( item.getItemId() == R.id.menu_local_section_update ) {
                downloadDataOfStates();
                return false;
            }
            if ( item.getItemId() == R.id.menu_local_section_municipalities ) {
                int size = entities.size();
                entities.clear();
                adapter.notifyItemRangeRemoved(0, size);
                ArrayList<Municipality> municipalities = fileManager.getMunicipalities();
                for ( Municipality municipality : municipalities ) {
                    entities.add(municipality.toEntitySelect());
                }
                Collections.sort(entities, (o1, o2) -> o1.getNumber() - o2.getNumber());
                adapter.notifyItemRangeInserted(0, entities.size());
                txtMode.setText("Municipios");
                currentTypeModeSelected = LocalDataPreferences.MODE_MUNICIPALITIES;
                if ( currentTypeModeSelected == typeModeSelected && currentIdStateSelected == idStateSelected ) {
                    setEntitiesPreferences();
                }
                return false;
            }
            if ( item.getItemId() == R.id.menu_local_section_local_districts ) {
                int size = entities.size();
                entities.clear();
                adapter.notifyItemRangeRemoved(0, size);
                ArrayList<LocalDistrict> localDistricts = fileManager.getLocalDistricts();
                for ( LocalDistrict localDistrict : localDistricts ) {
                    entities.add(localDistrict.toEntitySelect());
                }
                Collections.sort(entities, (o1, o2) -> o1.getNumber() - o2.getNumber());
                adapter.notifyItemRangeInserted(0, entities.size());
                txtMode.setText("Distritos locales");
                currentTypeModeSelected = LocalDataPreferences.MODE_LOCAL_DISTRICTS;
                if ( currentTypeModeSelected == typeModeSelected && currentIdStateSelected == idStateSelected ) {
                    setEntitiesPreferences();
                }
                return false;
            }
            if ( item.getItemId() == R.id.menu_local_section_federal_districts ) {
                int size = entities.size();
                entities.clear();
                adapter.notifyItemRangeRemoved(0, size);
                ArrayList<FederalDistrict> federalDistricts = fileManager.getFederalDistricts();
                for ( FederalDistrict federalDistrict : federalDistricts ) {
                    entities.add(federalDistrict.toEntitySelect());
                }
                Collections.sort(entities, (o1, o2) -> o1.getNumber() - o2.getNumber());
                adapter.notifyItemRangeInserted(0, entities.size());
                txtMode.setText("Distritos federales");
                currentTypeModeSelected = LocalDataPreferences.MODE_FEDERAL_DISTRICTS;
                if ( currentTypeModeSelected == typeModeSelected && currentIdStateSelected == idStateSelected ) {
                    setEntitiesPreferences();
                }
                return false;
            }
            return true;
        });
        btnOptions.setOnClickListener(v -> popupMenu.show());

        ((MaterialAutoCompleteTextView) lytStates.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProgressDialogBuilder builder = new ProgressDialogBuilder();
                builder.setTitle("Cargando datos...")
                        .setCancelable(false);
                ProgressDialog progressDialog = new ProgressDialog(requireContext(), builder);
                progressDialog.show();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        fileManager = LocalDataFileManager.getInstanceWithAllData(requireContext(), states.get(position).getId());
                        idStateSelected = states.get(position).getId();
                        requireActivity().runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                showPreferences();
                                progressDialog.dismiss();
                            }
                        });
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1000);
            }
        });

        btnSave.setOnClickListener(v -> {
            ProgressDialogBuilder builder = new ProgressDialogBuilder();
            builder.setTitle("Guardando preferencias")
                    .setCancelable(false);
            ProgressDialog progressDialog = new ProgressDialog(requireContext(), builder);
            progressDialog.show();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    ArrayList<EntitySelect> entitiesToSave = new ArrayList<>();
                    for ( EntitySelect entitySelect : entities ) {
                        if ( entitySelect.isSelected() ) {
                            entitiesToSave.add(entitySelect);
                        }
                    }
                    StringBuilder idsToSave = new StringBuilder();
                    for ( EntitySelect entitySelect : entitiesToSave ) {
                        idsToSave.append(entitySelect.getId()).append(",");
                    }
                    if ( idsToSave.length() != 0 ) {
                        idsToSave.deleteCharAt(idsToSave.length() - 1);
                    }
                    LocalDataPreferences.setSelectedIds(requireContext(), idsToSave.toString());
                    LocalDataPreferences.setMode(requireContext(), currentTypeModeSelected);
                    LocalDataPreferences.setIdStateSelected(requireContext(),currentIdStateSelected);
                    LocalDataPreferences.setNameStateSelected(requireContext(), states.get(currentIdStateSelected - 1).getName());
                    progressDialog.dismiss();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, 1000);
        });

        return root;
    }

    private void showPreferences() {
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
        switch ( currentTypeModeSelected ) {
            case LocalDataPreferences.MODE_MUNICIPALITIES: {
                int size = entities.size();
                entities.clear();
                adapter.notifyItemRangeRemoved(0, size);
                ArrayList<Municipality> municipalities = fileManager.getMunicipalities();
                for ( Municipality municipality : municipalities ) {
                    entities.add(municipality.toEntitySelect());
                }
                txtMode.setText("Municipios");
            } break;
            case LocalDataPreferences.MODE_LOCAL_DISTRICTS: {
                int size = entities.size();
                entities.clear();
                adapter.notifyItemRangeRemoved(0, size);
                ArrayList<LocalDistrict> localDistricts = fileManager.getLocalDistricts();
                for ( LocalDistrict localDistrict : localDistricts ) {
                    entities.add(localDistrict.toEntitySelect());
                }
                txtMode.setText("Distritos locales");
            } break;
            case LocalDataPreferences.MODE_FEDERAL_DISTRICTS: {
                int size = entities.size();
                entities.clear();
                adapter.notifyItemRangeRemoved(0, size);
                ArrayList<FederalDistrict> federalDistricts = fileManager.getFederalDistricts();
                for ( FederalDistrict federalDistrict : federalDistricts ) {
                    entities.add(federalDistrict.toEntitySelect());
                }
                txtMode.setText("Distritos federales");
            } break;
        }
        if ( currentIdStateSelected == idStateSelected ) {
            setEntitiesPreferences();
        }
        Collections.sort(entities, (o1, o2) -> o1.getNumber() - o2.getNumber());
        adapter.notifyItemRangeInserted(0, entities.size());
    }

    private void setEntitiesPreferences() {
        String[] selectedIds = LocalDataPreferences.getSelectedIds(requireContext());
        if ( selectedIds != null && selectedIds.length > 0 && !selectedIds[0].equals("") ) {
            for ( String id : selectedIds ) {
                int integerId = Integer.parseInt(id);
                for ( EntitySelect entitySelect : entities ) {
                    if ( entitySelect.getId() == integerId ) {
                        entitySelect.setSelected(true);
                        break;
                    }
                }
            }
        }
    }

    private void downloadDataOfStates() {
        ProgressDialogBuilder builder = new ProgressDialogBuilder()
                .setTitle("Actualizando datos...")
                .setCancelable(false);
        ProgressDialog progressDialog = new ProgressDialog(requireContext(), builder);
        progressDialog.show();
        Token token = TokenPreferences.getToken(requireContext());
        DownloadManager.downloadDataOfServer(requireContext(), token, progressDialog, null);
    }

}
