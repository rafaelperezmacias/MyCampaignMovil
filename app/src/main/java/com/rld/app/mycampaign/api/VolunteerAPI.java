package com.rld.app.mycampaign.api;

import com.rld.app.mycampaign.models.api.ServerVolunteer;
import com.rld.app.mycampaign.models.api.VolunteerRequest;
import com.rld.app.mycampaign.models.api.VolunteerResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface VolunteerAPI {

    @POST("MyCampaignWeb/public/api/volunteers/save")
    Call<VolunteerResponse> saveVolunteer(
            @Header("Authorization") String authorization,
            @Body VolunteerRequest volunteerRequest
    );

    @GET("MyCampaignWeb/public/api/volunteers/mobile")
    Call<ArrayList<ServerVolunteer>> getVolunteers(
            @Header("Authorization") String authorization
    );

}
