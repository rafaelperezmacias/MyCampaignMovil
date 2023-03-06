package com.rld.app.mycampaign.api;

import com.rld.app.mycampaign.secrets.AppConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url) {
        if ( retrofit == null ) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientForImage() {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.IMAGE_SERVER)
                .build();
    }

}
