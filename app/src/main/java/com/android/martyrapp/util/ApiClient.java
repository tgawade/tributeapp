package com.android.martyrapp.util;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    public static Retrofit getClient() {
        AuthenticationInterceptor interceptor = new AuthenticationInterceptor();
        httpClient.addInterceptor(interceptor);
        if (retrofit==null) {
            retrofit = new Retrofit.Builder().baseUrl(Constants.API_URL).
                    addConverterFactory(GsonConverterFactory.create()).
                    client(httpClient.build())
                    .build();
            //retrofit = retrofit.create(Retrofit.class);
        }
        return retrofit;
    }
}
