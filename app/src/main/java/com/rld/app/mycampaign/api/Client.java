package com.rld.app.mycampaign.api;

import android.annotation.SuppressLint;

import com.rld.app.mycampaign.secrets.AppConfig;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Retrofit retrofit = null;

    private static final OkHttpClient client;

    static {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        try {
            httpClient.sslSocketFactory(getSSLSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException ignored) { }
        httpClient.hostnameVerifier((hostname, session) -> true);
        httpClient.connectTimeout(10, TimeUnit.SECONDS);
        httpClient.readTimeout(10, TimeUnit.SECONDS);
        httpClient.writeTimeout(10, TimeUnit.SECONDS);
        client = httpClient.build();
    }

    private static SSLSocketFactory getSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        @SuppressLint("CustomX509TrustManager") TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }
                    @SuppressLint("TrustAllX509TrustManager")
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        return sslContext.getSocketFactory();
    }

    public static Retrofit getClient() {
        if ( retrofit == null ) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConfig.URL_SERVER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
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
