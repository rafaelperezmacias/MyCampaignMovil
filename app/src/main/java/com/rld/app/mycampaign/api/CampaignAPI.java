package com.rld.app.mycampaign.api;

import com.rld.app.mycampaign.models.Campaign;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CampaignAPI {

    @GET("MyCampaignWeb/public/api/campaigns")
    Call<Campaign> getCurrentCampaign(
            @Header("Authorization") String authorization
    );

}
