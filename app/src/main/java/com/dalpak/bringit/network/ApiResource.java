package com.dalpak.bringit.network;

import com.dalpak.bringit.models.AllOrdersResponse;
import com.dalpak.bringit.models.OrderChangeStatusModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiResource {


    @GET("orders/{business_id}/period/{days}")
    Call<AllOrdersResponse> getAllOrders(
            @Path("business_id") int businessId,
            @Path("days") int days);

    @PUT("orders/status")
    Call<ResponseBody> updateOrderStatus(@Body OrderChangeStatusModel model);

}
