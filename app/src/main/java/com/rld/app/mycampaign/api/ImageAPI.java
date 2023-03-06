package com.rld.app.mycampaign.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ImageAPI {

    @GET("api/")
    Call<ResponseBody> getProfileImage(
            @Query("name") String name,
            @Query("color") String color,
            @Query("background") String background,
            @Query("size") String size,
            @Query("font-size") String fontSize
    );

}
