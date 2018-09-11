package com.drkeironbrown.lifecoach.api;


import com.drkeironbrown.lifecoach.helper.AppConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static Retrofit restAdapterBusiness;
    /**
     * instance of restApi interface
     */
    private static AppApi REST_CLIENT;
    /**
     * static instance of retrofit adapter
     */
    private static Retrofit restAdapter;

    /**
     * this static block init the retrofit in MYApplication class.
     */
    static {
        setupRestClient();
    }

    public static void setupRestClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);
        builder.connectTimeout(2000, TimeUnit.SECONDS);
        builder.readTimeout(80, TimeUnit.SECONDS);
        builder.writeTimeout(90, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder().setLenient().create();
        restAdapter = new Retrofit.Builder()
                .baseUrl(AppConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build();
    }

    /**
     * @return the instance of Restclient interface
     */
    public static AppApi get() {
        if (REST_CLIENT == null) {
            REST_CLIENT = restAdapter.create(AppApi.class);
        }
        return REST_CLIENT;
    }

}
