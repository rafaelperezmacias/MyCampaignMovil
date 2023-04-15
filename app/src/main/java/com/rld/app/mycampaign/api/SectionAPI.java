package com.rld.app.mycampaign.api;

import com.rld.app.mycampaign.models.FederalDistrict;
import com.rld.app.mycampaign.models.LocalDistrict;
import com.rld.app.mycampaign.models.Municipality;
import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.api.PageSectionAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface SectionAPI {

    @GET("api/states")
    Call<ArrayList<State>> getStates(
            @Header("Authorization") String authorization
    );

    @GET("api/federal-districts")
    Call<ArrayList<FederalDistrict>> getFederalDistricts(
            @Header("Authorization") String authorization,
            @Query("stateId") int stateId
    );

    @GET("api/local-districts")
    Call<ArrayList<LocalDistrict>> getLocalDistricts(
            @Header("Authorization") String authorization,
            @Query("stateId") int stateId
    );

    @GET("api/municipalities")
    Call<ArrayList<Municipality>> getMunicipalities(
            @Header("Authorization") String authorization,
            @Query("stateId") int stateId
    );

    @GET("api/sections")
    Call<PageSectionAPI> getSections(
            @Header("Authorization") String authorization,
            @Query("stateId") int stateId
    );

    @GET("api/sections")
    Call<PageSectionAPI> getSectionsByPage(
            @Header("Authorization") String authorization,
            @Query("stateId") int stateId,
            @Query("page") int page
    );

}
