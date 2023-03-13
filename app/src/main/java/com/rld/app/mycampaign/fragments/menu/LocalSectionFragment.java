package com.rld.app.mycampaign.fragments.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.rld.app.mycampaign.LoginActivity;
import com.rld.app.mycampaign.MainActivity;
import com.rld.app.mycampaign.R;
import com.rld.app.mycampaign.adapters.LocalSectionAdapter;
import com.rld.app.mycampaign.api.DownloadManager;
import com.rld.app.mycampaign.databinding.FragmentLocalSectionBinding;
import com.rld.app.mycampaign.dialogs.ErrorMessageDialog;
import com.rld.app.mycampaign.dialogs.ErrorMessageDialogBuilder;
import com.rld.app.mycampaign.dialogs.MessageDialog;
import com.rld.app.mycampaign.dialogs.MessageDialogBuilder;
import com.rld.app.mycampaign.dialogs.ProgressDialog;
import com.rld.app.mycampaign.dialogs.ProgressDialogBuilder;
import com.rld.app.mycampaign.files.LocalDataFileManager;
import com.rld.app.mycampaign.models.EntitySelect;
import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.Token;
import com.rld.app.mycampaign.preferences.DownloadManagerPreferences;
import com.rld.app.mycampaign.preferences.LocalDataPreferences;
import com.rld.app.mycampaign.preferences.TokenPreferences;
import com.rld.app.mycampaign.preferences.UserPreferences;
import com.rld.app.mycampaign.utils.Internet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class LocalSectionFragment extends Fragment {

    private FragmentLocalSectionBinding binding;

    private LinearLayout lytData;
    private LinearLayout lytErrorData;
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
        MaterialButton btnSave = binding.btnSave;
        MaterialButton btnErrorDownload = binding.btnErrorDownload;
        txtMode = binding.txtMode;
        lytStates = binding.lytStates;
        lytData = binding.lytData;
        lytErrorData = binding.lytErrorData;

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(true);

        adapter = new LocalSectionAdapter(requireContext(), entities);
        recyclerView.setAdapter(adapter);

        String[] stringStates = new String[states.size()];
        for ( int i = 0; i < states.size(); i++ ) {
            stringStates[i] = states.get(i).getName();
        }
        ((MaterialAutoCompleteTextView) lytStates.getEditText()).setSimpleItems(stringStates);

        if ( fileManager.isEmpty() ) {
            lytData.setVisibility(View.GONE);
            lytErrorData.setVisibility(View.VISIBLE);
        } else {
            showPreferences();
            lytData.setVisibility(View.VISIBLE);
            lytErrorData.setVisibility(View.GONE);
        }

        PopupMenu popupMenu = new PopupMenu(getContext(), btnOptions);
        popupMenu.inflate(R.menu.local_section_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if ( item.getItemId() == R.id.menu_local_section_update ) {
                downloadData();
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

        btnErrorDownload.setOnClickListener(v -> {
            downloadData();
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

    private void downloadData() {
        ProgressDialogBuilder builder = new ProgressDialogBuilder()
                .setTitle("Actualizando datos...")
                .setCancelable(false);
        ProgressDialog progressDialog = new ProgressDialog(requireContext(), builder);
        progressDialog.show();
        Token token = TokenPreferences.getToken(requireContext());
        if ( !Internet.isNetworkAvailable(requireContext()) || !Internet.isOnlineNetwork() ) {
            progressDialog.dismiss();
            MessageDialogBuilder messageDialogBuilder = new MessageDialogBuilder()
                    .setTitle("Sin conexión a internet")
                    .setMessage("Para actualizar los datos locales necesita de una conexión a internet")
                    .setPrimaryButtonText("Aceptar")
                    .setCancelable(true);
            MessageDialog messageDialog = new MessageDialog(requireContext(), messageDialogBuilder);
            messageDialog.setPrimaryButtonListener(v -> {
                messageDialog.dismiss();
            });
            messageDialog.show();
            return;
        }
        DownloadManager.downloadDataOfServer(requireContext(), token, new DownloadManager.OnResolveRequestListener() {
            @Override
            public void onSuccessListener() {
                DownloadManagerPreferences.setIsLocalDataSaved(requireContext(), true);
                fileManager = LocalDataFileManager.getInstanceWithAllData(requireContext(), currentIdStateSelected);
                states = fileManager.getStates();
                String[] stringStates = new String[states.size()];
                for ( int i = 0; i < states.size(); i++ ) {
                    stringStates[i] = states.get(i).getName();
                }
                ((MaterialAutoCompleteTextView) lytStates.getEditText()).setSimpleItems(stringStates);
                progressDialog.dismiss();
                MessageDialogBuilder messageDialogBuilder = new MessageDialogBuilder()
                        .setTitle("Actualización exitosa")
                        .setMessage("Los datos se han actualizado con exito!")
                        .setCancelable(false)
                        .setPrimaryButtonText("Aceptar");
                MessageDialog messageDialog = new MessageDialog(requireContext(), messageDialogBuilder);
                messageDialog.setPrimaryButtonListener(v -> messageDialog.dismiss());
                messageDialog.show();
                messageDialog.setOnDismissListener(dialog -> {
                    requireActivity().runOnUiThread(() -> {
                        lytData.setVisibility(View.VISIBLE);
                        lytErrorData.setVisibility(View.GONE);
                        showPreferences();
                    });
                });
            }
            @Override
            public void onFailureListener(int type, int code, String error)  {
                DownloadManagerPreferences.setIsLocalDataSaved(requireContext(), false);
                LocalDataPreferences.deleteLocalPreferences(requireContext());
                progressDialog.dismiss();
                ErrorMessageDialogBuilder builder = new ErrorMessageDialogBuilder();
                builder.setTitle("Error descargando datos")
                        .setButtonText("Aceptar")
                        .setCancelable(false);
                switch ( type ) {
                    case DownloadManager.TYPE_STATE_REQUEST : {
                        builder.setMessage("Ha ocurrido un error descargando los datos de los estados");
                    } break;
                    case DownloadManager.TYPE_FEDERAL_DISTRICT_REQUEST : {
                        builder.setMessage("Ha ocurrido un error descargando los datos de los distritos federales");
                    } break;
                    case DownloadManager.TYPE_LOCAL_DISTRICT_REQUEST : {
                        builder.setMessage("Ha ocurrido un error descargando los datos de los distritos locales");
                    } break;
                    case DownloadManager.TYPE_MUNICIPALITY_REQUEST : {
                        builder.setMessage("Ha ocurrido un error descargando los datos de los municipios");
                    } break;
                    case DownloadManager.TYPE_SECTION_REQUEST : {
                        builder.setMessage("Ha ocurrido un error descargando los datos de las secciones");
                    } break;
                }
                switch ( code ) {
                    case DownloadManager.BAD_REQUEST : {
                         builder.setError("400 - " + error);
                    } break;
                    case DownloadManager.UNAUTHORIZED : {
                        builder.setError("401 - " + error);
                    } break;
                    case DownloadManager.TOO_MANY_REQUEST : {
                        builder.setError("429 - " + error);
                    } break;
                    case DownloadManager.INTERNAL_ERROR : {
                        builder.setError("500 - " + error);
                    } break;
                    case DownloadManager.UNKNOWN_ERROR : {
                        builder.setError("Error desconocido - " + error);
                    } break;
                    case DownloadManager.ERROR_REQUEST : {
                        builder.setError("Error con la petición - " + error);
                    } break;
                }
                ErrorMessageDialog messageDialog = new ErrorMessageDialog(requireContext(), builder);
                messageDialog.setButtonClickListener(v -> messageDialog.dismiss());
                messageDialog.show();
                messageDialog.setOnDismissListener(dialog -> {
                    lytData.setVisibility(View.GONE);
                    lytErrorData.setVisibility(View.VISIBLE);
                    if ( code == DownloadManager.UNAUTHORIZED ) {
                        MessageDialogBuilder messageDialogBuilder = new MessageDialogBuilder();
                        messageDialogBuilder.setTitle("La sesión ha caducado")
                                .setMessage("Para seguir utilizando la aplicación, por favor vuelve a iniciar sesión.")
                                .setPrimaryButtonText("Aceptar")
                                .setCancelable(false);
                        MessageDialog closeDialog = new MessageDialog(requireContext(), messageDialogBuilder);
                        closeDialog.setPrimaryButtonListener(v -> {
                            closeDialog.dismiss();
                        });
                        closeDialog.setOnDismissListener(dialog1 -> {
                            UserPreferences.clearConnect(requireContext());
                            startActivity(new Intent(requireActivity(), LoginActivity.class));
                            requireActivity().finish();
                        });
                        closeDialog.show();
                    }
                });
            }
        });
    }

}
