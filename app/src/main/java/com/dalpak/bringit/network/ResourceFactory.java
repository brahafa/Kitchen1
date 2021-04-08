package com.dalpak.bringit.network;

import com.dalpak.bringit.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.dalpak.bringit.utils.SharedPrefs.getData;

class ResourceFactory {

    private static Retrofit mRootResourceFactory;

    public static Retrofit getRootResourceFactory() {
        return mRootResourceFactory;
    }

    static <T> T createResource(Class<T> resourceClass, String BASE_URL_2) {
        if (mRootResourceFactory == null) {
            String DEFAULT_DATE_FORMAT = "dd'-'MM'-'yyyy'T'HH':'mm':'ss";
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
//                    .callTimeout(10, TimeUnit.SECONDS)
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(chain -> {
                        Request request = chain.request();
                        Request newRequest;
                        newRequest = request.newBuilder().addHeader("Apikey", getData(Constants.TOKEN_PREF)).build();
                        return chain.proceed(newRequest);
                    }).build();
            Gson gson = new GsonBuilder().setLenient().setDateFormat(DEFAULT_DATE_FORMAT).create();
            mRootResourceFactory = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .baseUrl(BASE_URL_2)
                    .build();
        }

        return mRootResourceFactory.create(resourceClass);
    }
}