package com.android.martyrapp.util;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rgundal on 26-09-2017.
 */
public class AuthenticationInterceptor implements Interceptor {


    public AuthenticationInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder builder = original.newBuilder()
                .header(Constants.API_KEY, Constants.API_KEY_VAL);

        Request request = builder.build();
        return chain.proceed(request);
    }
}