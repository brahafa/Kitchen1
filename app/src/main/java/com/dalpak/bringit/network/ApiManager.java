package com.dalpak.bringit.network;

import com.dalpak.bringit.BuildConfig;
import com.dalpak.bringit.listeners.CallbackListener;
import com.dalpak.bringit.models.AllOrdersResponse;
import com.dalpak.bringit.models.BusinessModel;
import com.dalpak.bringit.models.ErrorResponse;
import com.dalpak.bringit.models.OrderChangeStatusModel;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

import static com.dalpak.bringit.network.ResourceFactory.getRootResourceFactory;

public class ApiManager {


    private String BASE_URL;
    private String BASE_URL_2;

    private final String BASE_URL_DEV = "https://api.dev.bringit.org.il/?apiCtrl=";
    private final String BASE_URL_2_DEV = "https://api2.dev.bringit.org.il/";

    private final String BASE_URL_TEST = "https://api.test.bringit.org.il/?apiCtrl=";
    private final String BASE_URL_2_TEST = "https://api2.test.bringit.org.il/";

    private final String BASE_URL_STAGE = "https://api.stg.bringit.co.il/?apiCtrl=";
    private final String BASE_URL_2_STAGE = "https://api2.stg.bringit.co.il/";

    private final String BASE_URL_PROD = "https://api.bringit.co.il/?apiCtrl=";
    private final String BASE_URL_2_PROD = "https://api2.bringit.co.il/";

    private final String BASE_URL_LOCAL = "http://192.168.5.7:80/bringit_backend/?apiCtrl=";
    private final String BASE_URL_2_LOCAL = "http://192.168.5.7:80/api2/";


    private static ApiManager sApiManager;
    private ApiResource mApiResource;
    private Converter<ResponseBody, ErrorResponse> converter;

    public static ApiManager getInstance() {
        if (sApiManager == null) {
            sApiManager = new ApiManager();
        }
        return sApiManager;
    }

    private ApiManager() {
        setBaseUrl();
        mApiResource = ResourceFactory.createResource(ApiResource.class, BASE_URL_2);
        converter = getRootResourceFactory().responseBodyConverter(ErrorResponse.class, new Annotation[0]);
    }

    private void setBaseUrl() {
        switch (BuildConfig.BUILD_TYPE) {
            case "debug":
            default:
                BASE_URL = BASE_URL_DEV;
                BASE_URL_2 = BASE_URL_2_DEV;
                break;
            case "debugTest":
                BASE_URL = BASE_URL_TEST;
                BASE_URL_2 = BASE_URL_2_TEST;
                break;
            case "debugStage":
                BASE_URL = BASE_URL_STAGE;
                BASE_URL_2 = BASE_URL_2_STAGE;
                break;
            case "localHost":
                BASE_URL = BASE_URL_LOCAL;
                BASE_URL_2 = BASE_URL_2_LOCAL;
                break;
            case "release":
            case "debugLive":
                BASE_URL = BASE_URL_PROD;
                BASE_URL_2 = BASE_URL_2_PROD;
                break;
        }
    }

    public void getAllOrders(CallbackListener<AllOrdersResponse> callback) {
        mApiResource.getAllOrders(BusinessModel.getInstance().getBusiness_id(), 14)
                .enqueue(new Callback<AllOrdersResponse>() {
                    @Override
                    public void onResponse(Call<AllOrdersResponse> call, Response<AllOrdersResponse> response) {
                        if (response.isSuccessful()) callback.success(response.body());
                        else {
                            try {
                                ErrorResponse errorResponse = converter.convert(response.errorBody());
                                callback.failure(response.code(), errorResponse.getMessage());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AllOrdersResponse> responseCall, Throwable throwable) {
                        if (throwable instanceof SocketTimeoutException)
                            callback.failure(throwable);
                    }
                });
    }

    public void updateOrderStatus(OrderChangeStatusModel model, CallbackListener<ResponseBody> callback) {
        mApiResource.updateOrderStatus(model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) callback.success(response.body());
                else {
                    try {
                        ErrorResponse errorResponse = converter.convert(response.errorBody());
//                        callback.failure(response.code(), errorResponse.getError());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> responseCall, Throwable throwable) {
//                callback.failure(throwable);
            }
        });
    }

}