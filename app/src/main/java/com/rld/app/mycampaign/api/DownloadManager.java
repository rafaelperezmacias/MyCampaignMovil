package com.rld.app.mycampaign.api;

import android.content.Context;
import android.util.Log;

import com.rld.app.mycampaign.dialogs.ProgressDialog;
import com.rld.app.mycampaign.files.FederalDistrictFileManager;
import com.rld.app.mycampaign.files.LocalDistrictFileManager;
import com.rld.app.mycampaign.files.MunicipalityFileManager;
import com.rld.app.mycampaign.files.SectionFileManager;
import com.rld.app.mycampaign.files.StateFileManager;
import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.Token;
import com.rld.app.mycampaign.models.api.PageSectionAPI;
import com.rld.app.mycampaign.models.api.SectionResponse;
import com.rld.app.mycampaign.secrets.AppConfig;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadManager {

    private Context context;
    private Token token;
    private ProgressDialog progressDialog;
    private OnResolveRequestListener onResolveRequestListener;

    private DownloadManager()
    {

    }

    public static void downloadDataOfServer(Context context, Token token, ProgressDialog progressDialog, OnResolveRequestListener onResolveRequestListener) {
        DownloadManager downloadManager = new DownloadManager();
        downloadManager.context = context;
        downloadManager.token = token;
        downloadManager.progressDialog = progressDialog;
        downloadManager.onResolveRequestListener = onResolveRequestListener;
        progressDialog.show();
        downloadManager.downloadDataOfStates();
    }

    private void downloadDataOfStates() {
        Call<ArrayList<State>> statesCall = Client.getClient(AppConfig.URL_SERVER).create(SectionAPI.class)
                .getStates(token.toAPIRequest());
        statesCall.enqueue(new Callback<ArrayList<State>>() {
            @Override
            public void onResponse(Call<ArrayList<State>> call, Response<ArrayList<State>> response) {
                if ( response.isSuccessful() ) {
                    ArrayList<State> states = response.body();
                    JSONArray jsonArrayStates = StateFileManager.arrayListToJsonArray(states);
                    StateFileManager.writeJSON(jsonArrayStates, context);
                    downloadDataOfFederalDistrictsByState(states, 0);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<State>> call, Throwable t) {
                Log.e("a0", "" + t.getMessage());
            }
        });
    }

    private void downloadDataOfFederalDistrictsByState(ArrayList<State> states, int index) {
        if ( states.size() == index ) {
            downloadDataOfLocalDistrictsByState(states, 0);
            return;
        }
        Call<ArrayList<FederalDistrict>> federalDistrictsCall = Client.getClient(AppConfig.URL_SERVER).create(SectionAPI.class)
                .getFederalDistricts(
                        token.toAPIRequest(),
                        states.get(index).getId()
                );
        federalDistrictsCall.enqueue(new Callback<ArrayList<FederalDistrict>>() {
            @Override
            public void onResponse(Call<ArrayList<FederalDistrict>> call, Response<ArrayList<FederalDistrict>> response) {
                if ( response.isSuccessful() ) {
                    ArrayList<FederalDistrict> federalDistricts = response.body();
                    JSONArray jsonArrayFederalDistricts = FederalDistrictFileManager.arrayListToJsonArray(federalDistricts);
                    FederalDistrictFileManager.writeJSON(jsonArrayFederalDistricts,  states.get(index).getId(), context);
                    downloadDataOfFederalDistrictsByState(states, index + 1);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<FederalDistrict>> call, Throwable t) {
                Log.e("a1", "" + t.getMessage());
            }
        });
    }

    private void downloadDataOfLocalDistrictsByState(ArrayList<State> states, int index) {
        if ( states.size() == index ) {
            downloadDataOfMunicipalitiesByState(states, 0);
            return;
        }
        Call<ArrayList<LocalDistrict>> localDistrictsCall = Client.getClient(AppConfig.URL_SERVER).create(SectionAPI.class)
                .getLocalDistricts(
                        token.toAPIRequest(),
                        states.get(index).getId()
                );
        localDistrictsCall.enqueue(new Callback<ArrayList<LocalDistrict>>() {
            @Override
            public void onResponse(Call<ArrayList<LocalDistrict>> call, Response<ArrayList<LocalDistrict>> response) {
                if ( response.isSuccessful() ) {
                    ArrayList<LocalDistrict> localDistricts = response.body();
                    JSONArray jsonArrayLocalDistricts = LocalDistrictFileManager.arrayListToJsonArray(localDistricts);
                    LocalDistrictFileManager.writeJSON(jsonArrayLocalDistricts, states.get(index).getId(), context);
                    downloadDataOfLocalDistrictsByState(states, index + 1);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<LocalDistrict>> call, Throwable t) {
                Log.e("a2", "" + t.getMessage());
            }
        });
    }

    private void downloadDataOfMunicipalitiesByState(ArrayList<State> states, int index) {
        if ( states.size() == index ) {
            downloadDataOfSectionsByState(states, 0);
            return;
        }
        Call<ArrayList<Municipality>> municipalitiesCall = Client.getClient(AppConfig.URL_SERVER).create(SectionAPI.class)
                .getMunicipalities(
                        token.toAPIRequest(),
                        states.get(index).getId()
                );
        municipalitiesCall.enqueue(new Callback<ArrayList<Municipality>>() {
            @Override
            public void onResponse(Call<ArrayList<Municipality>> call, Response<ArrayList<Municipality>> response) {
                if ( response.isSuccessful() ) {
                    ArrayList<Municipality> municipalities = response.body();
                    JSONArray jsonArrayMunicipalities = MunicipalityFileManager.arrayListToJsonArray(municipalities);
                    MunicipalityFileManager.writeJSON(jsonArrayMunicipalities, states.get(index).getId(), context);
                    downloadDataOfMunicipalitiesByState(states, index + 1);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Municipality>> call, Throwable t) {
                Log.e("a3", "" + t.getMessage());
            }
        });
    }

    private void downloadDataOfSectionsByState(ArrayList<State> states, int index) {
        if ( states.size() == index ) {
            if ( onResolveRequestListener != null ) {
                onResolveRequestListener.onSuccessListener();
            }
            progressDialog.dismiss();
            return;
        }
        Call<PageSectionAPI> pageSectionAPICall = Client.getClient(AppConfig.URL_SERVER).create(SectionAPI.class)
                .getSections(
                        token.toAPIRequest(),
                        states.get(index).getId()
                );
        pageSectionAPICall.enqueue(new Callback<PageSectionAPI>() {
            @Override
            public void onResponse(Call<PageSectionAPI> call, Response<PageSectionAPI> response) {
                if ( response.isSuccessful() ) {
                    PageSectionAPI pageSectionAPI = response.body();
                    ArrayList<SectionResponse> sections = new ArrayList<>();
                    sections.addAll(pageSectionAPI.getData());
                    downloadDataOfSectionsByStatePerPage(sections, pageSectionAPI.getCurrent_page(), pageSectionAPI.getLast_page(), states.get(index).getId(), states, index);
                }
            }
            @Override
            public void onFailure(Call<PageSectionAPI> call, Throwable t) {
                Log.e("a4", "" + t.getMessage());
            }
        });
    }

    private void downloadDataOfSectionsByStatePerPage(ArrayList<SectionResponse> sections, int currentPage, int lastPage, int stateId, ArrayList<State> states, int index) {
        if ( currentPage > lastPage ) {
            JSONArray jsonArraySections = SectionFileManager.arrayListToJsonArray(sections);
            SectionFileManager.writeJSON(jsonArraySections, stateId, context);
            sections.clear();
            downloadDataOfSectionsByState(states, index + 1);
            return;
        }
        Call<PageSectionAPI> pageSectionAPICall = Client.getClient(AppConfig.URL_SERVER).create(SectionAPI.class)
                .getSectionsByPage(
                        token.toAPIRequest(),
                        states.get(index).getId(),
                        currentPage + 1
                );
        pageSectionAPICall.enqueue(new Callback<PageSectionAPI>() {
            @Override
            public void onResponse(Call<PageSectionAPI> call, Response<PageSectionAPI> response) {
                if ( response.isSuccessful() ) {
                    PageSectionAPI pageSectionAPI = response.body();
                    sections.addAll(pageSectionAPI.getData());
                    downloadDataOfSectionsByStatePerPage(sections, currentPage + 1, lastPage, stateId, states, index);
                    return;
                }
            }
            @Override
            public void onFailure(Call<PageSectionAPI> call, Throwable t) {
                Log.e("a5", "" + t.getMessage());
            }
        });
    }

    public interface OnResolveRequestListener {
        void onSuccessListener();
        void onFailureListener();
    }

}
