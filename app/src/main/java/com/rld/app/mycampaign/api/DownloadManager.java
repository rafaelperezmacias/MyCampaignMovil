package com.rld.app.mycampaign.api;

import android.content.Context;
import android.util.Log;

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
import com.rld.app.mycampaign.preferences.TokenPreferences;
import com.rld.app.mycampaign.secrets.AppConfig;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadManager {

    public static final int UNKNOWN_ERROR = 0;
    public static final int ERROR_REQUEST = 1;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int TOO_MANY_REQUEST = 429;
    public static final int INTERNAL_ERROR = 500;

    public static final int TYPE_STATE_REQUEST = 1;
    public static final int TYPE_FEDERAL_DISTRICT_REQUEST = 2;
    public static final int TYPE_LOCAL_DISTRICT_REQUEST = 3;
    public static final int TYPE_MUNICIPALITY_REQUEST = 4;
    public static final int TYPE_SECTION_REQUEST = 5;

    private Context context;
    private Token token;
    private OnResolveRequestListener onResolveRequestListener;

    private DownloadManager()
    {

    }

    public static void downloadDataOfServer(Context context, Token token, OnResolveRequestListener onResolveRequestListener) {
        DownloadManager downloadManager = new DownloadManager();
        downloadManager.context = context;
        downloadManager.token = token;
        downloadManager.onResolveRequestListener = onResolveRequestListener;
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
                    return;
                }
                if ( response.code() == 400 ) {
                    onResolveRequestListener.onFailureListener(TYPE_STATE_REQUEST, BAD_REQUEST, response.message());
                    return;
                }
                if ( response.code() == 401 ) {
                    onResolveRequestListener.onFailureListener(TYPE_STATE_REQUEST, UNAUTHORIZED, response.message());
                    return;
                }
                if ( response.code() == 429 ) {
                    onResolveRequestListener.onFailureListener(TYPE_STATE_REQUEST, TOO_MANY_REQUEST, response.message());
                    return;
                }
                if ( response.code() == 500 ) {
                    onResolveRequestListener.onFailureListener(TYPE_STATE_REQUEST, INTERNAL_ERROR, response.message());
                    return;
                }
                onResolveRequestListener.onFailureListener(TYPE_STATE_REQUEST, UNKNOWN_ERROR, response.message());
            }
            @Override
            public void onFailure(Call<ArrayList<State>> call, Throwable t) {
                onResolveRequestListener.onFailureListener(TYPE_STATE_REQUEST, ERROR_REQUEST, t.getMessage());
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
                    return;
                }
                if ( response.code() == 400 ) {
                    onResolveRequestListener.onFailureListener(TYPE_FEDERAL_DISTRICT_REQUEST, BAD_REQUEST, response.message());
                    return;
                }
                if ( response.code() == 401 ) {
                    onResolveRequestListener.onFailureListener(TYPE_FEDERAL_DISTRICT_REQUEST, UNAUTHORIZED, response.message());
                    return;
                }
                if ( response.code() == 429 ) {
                    onResolveRequestListener.onFailureListener(TYPE_FEDERAL_DISTRICT_REQUEST, TOO_MANY_REQUEST, response.message());
                    return;
                }
                if ( response.code() == 500 ) {
                    onResolveRequestListener.onFailureListener(TYPE_FEDERAL_DISTRICT_REQUEST, INTERNAL_ERROR, response.message());
                    return;
                }
                onResolveRequestListener.onFailureListener(TYPE_FEDERAL_DISTRICT_REQUEST, UNKNOWN_ERROR, response.message());
            }
            @Override
            public void onFailure(Call<ArrayList<FederalDistrict>> call, Throwable t) {
                onResolveRequestListener.onFailureListener(TYPE_FEDERAL_DISTRICT_REQUEST, ERROR_REQUEST, t.getMessage());
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
                    return;
                }
                if ( response.code() == 400 ) {
                    onResolveRequestListener.onFailureListener(TYPE_LOCAL_DISTRICT_REQUEST, BAD_REQUEST, response.message());
                    return;
                }
                if ( response.code() == 401 ) {
                    onResolveRequestListener.onFailureListener(TYPE_LOCAL_DISTRICT_REQUEST, UNAUTHORIZED, response.message());
                    return;
                }
                if ( response.code() == 429 ) {
                    onResolveRequestListener.onFailureListener(TYPE_LOCAL_DISTRICT_REQUEST, TOO_MANY_REQUEST, response.message());
                    return;
                }
                if ( response.code() == 500 ) {
                    onResolveRequestListener.onFailureListener(TYPE_LOCAL_DISTRICT_REQUEST, INTERNAL_ERROR, response.message());
                    return;
                }
                onResolveRequestListener.onFailureListener(TYPE_LOCAL_DISTRICT_REQUEST, UNKNOWN_ERROR, response.message());
            }
            @Override
            public void onFailure(Call<ArrayList<LocalDistrict>> call, Throwable t) {
                onResolveRequestListener.onFailureListener(TYPE_LOCAL_DISTRICT_REQUEST, ERROR_REQUEST, t.getMessage());
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
                    return;
                }
                if ( response.code() == 400 ) {
                    onResolveRequestListener.onFailureListener(TYPE_MUNICIPALITY_REQUEST, BAD_REQUEST, response.message());
                    return;
                }
                if ( response.code() == 401 ) {
                    onResolveRequestListener.onFailureListener(TYPE_MUNICIPALITY_REQUEST, UNAUTHORIZED, response.message());
                    return;
                }
                if ( response.code() == 429 ) {
                    onResolveRequestListener.onFailureListener(TYPE_MUNICIPALITY_REQUEST, TOO_MANY_REQUEST, response.message());
                    return;
                }
                if ( response.code() == 500 ) {
                    onResolveRequestListener.onFailureListener(TYPE_MUNICIPALITY_REQUEST, INTERNAL_ERROR, response.message());
                    return;
                }
                onResolveRequestListener.onFailureListener(TYPE_MUNICIPALITY_REQUEST, UNKNOWN_ERROR, response.message());
            }
            @Override
            public void onFailure(Call<ArrayList<Municipality>> call, Throwable t) {
                onResolveRequestListener.onFailureListener(TYPE_MUNICIPALITY_REQUEST, ERROR_REQUEST, t.getMessage());
            }
        });
    }

    private void downloadDataOfSectionsByState(ArrayList<State> states, int index) {
        if ( states.size() == index ) {
            onResolveRequestListener.onSuccessListener();
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
                    return;
                }
                if ( response.code() == 400 ) {
                    onResolveRequestListener.onFailureListener(TYPE_SECTION_REQUEST, BAD_REQUEST, response.message());
                    return;
                }
                if ( response.code() == 401 ) {
                    onResolveRequestListener.onFailureListener(TYPE_SECTION_REQUEST, UNAUTHORIZED, response.message());
                    return;
                }
                if ( response.code() == 429 ) {
                    onResolveRequestListener.onFailureListener(TYPE_SECTION_REQUEST, TOO_MANY_REQUEST, response.message());
                    return;
                }
                if ( response.code() == 500 ) {
                    onResolveRequestListener.onFailureListener(TYPE_SECTION_REQUEST, INTERNAL_ERROR, response.message());
                    return;
                }
                onResolveRequestListener.onFailureListener(TYPE_SECTION_REQUEST, UNKNOWN_ERROR, response.message());
            }
            @Override
            public void onFailure(Call<PageSectionAPI> call, Throwable t) {
                onResolveRequestListener.onFailureListener(TYPE_SECTION_REQUEST, ERROR_REQUEST, t.getMessage());
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
                if ( response.code() == 400 ) {
                    onResolveRequestListener.onFailureListener(TYPE_SECTION_REQUEST, BAD_REQUEST, response.message());
                    return;
                }
                if ( response.code() == 401 ) {
                    onResolveRequestListener.onFailureListener(TYPE_SECTION_REQUEST, UNAUTHORIZED, response.message());
                    return;
                }
                if ( response.code() == 429 ) {
                    onResolveRequestListener.onFailureListener(TYPE_SECTION_REQUEST, TOO_MANY_REQUEST, response.message());
                    return;
                }
                if ( response.code() == 500 ) {
                    onResolveRequestListener.onFailureListener(TYPE_SECTION_REQUEST, INTERNAL_ERROR, response.message());
                    return;
                }
                onResolveRequestListener.onFailureListener(TYPE_SECTION_REQUEST, UNKNOWN_ERROR, response.message());
            }
            @Override
            public void onFailure(Call<PageSectionAPI> call, Throwable t) {
                onResolveRequestListener.onFailureListener(TYPE_SECTION_REQUEST, ERROR_REQUEST, t.getMessage());
            }
        });
    }

    public interface OnResolveRequestListener {
        void onSuccessListener();
        void onFailureListener(int type, int code, String error);
    }

}
