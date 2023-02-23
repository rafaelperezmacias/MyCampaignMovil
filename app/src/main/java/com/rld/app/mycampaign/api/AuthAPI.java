package com.rld.app.mycampaign.api;

import com.rld.app.mycampaign.models.api.LoginResponse;
import com.rld.app.mycampaign.models.api.UserRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {

    @POST("MyCampaignWeb/public/api/auth/login")
    Call<LoginResponse> login(@Body UserRequest userRequest);

}
