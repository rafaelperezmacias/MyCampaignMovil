package com.rld.app.mycampaign.api;

import com.rld.app.mycampaign.models.State;
import com.rld.app.mycampaign.models.api.EntityAPI;
import com.rld.app.mycampaign.models.api.PageSectionAPI;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    @GET("MyCampaignWeb/public/api/states")
    Call<ArrayList<State>> getStates();

    @GET("MyCampaignWeb/public/api/entities/{stateId}")
    Call<EntityAPI> getEntities(
            @Path("stateId") String stateId
    );

    @GET("MyCampaignWeb/public/api/sections/{stateId}")
    Call<PageSectionAPI> getSections(
            @Path("stateId") String stateId
    );

    @GET("MyCampaignWeb/public/api/sections/{stateId}")
    Call<PageSectionAPI> getSectionsByPage(
            @Path("stateId") String stateId,
            @Query("page") int page
    );

}
