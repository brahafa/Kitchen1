package com.dalpak.bringit.network;

import android.content.Context;

import com.dalpak.bringit.local_db.DbHandler;
import com.dalpak.bringit.models.AllOrdersResponse;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.models.OrderModel;
import com.dalpak.bringit.models.OrdersByStatusModel;
import com.dalpak.bringit.utils.MyApp;
import com.dalpak.bringit.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class RequestHelper {

    public void getAllOrdersFromDb(final Context context, final Request.RequestAllOrdersCallBack listener) {

        if (MyApp.get().isNetworkAvailable()) {
            Request.getInstance().getAllOrders(context, response -> {
                if (response == null) {
                    response = new AllOrdersResponse();

                    response.setOrdersByStatus(geAllOrdersByStatusFromDb(context));

                } else {
                    updateLocalDB(response.getOrders(), context);
                }
                listener.onDataDone(response);
            });
        } else {
//            Utils.openAlertDialog(context, "You are now offline", "Connection is lost");
            AllOrdersResponse allOrdersResponse = new AllOrdersResponse();
            allOrdersResponse.setOrdersByStatus(geAllOrdersByStatusFromDb(context));
            listener.onDataDone(allOrdersResponse);
        }
    }

    private OrdersByStatusModel geAllOrdersByStatusFromDb(final Context context) {
        DbHandler dbHandler = new DbHandler(context);

        OrdersByStatusModel ordersByStatus = new OrdersByStatusModel();

        for (OrderModel order : dbHandler.getAllOrdersFromDb()) {
            switch (order.getStatus()) {
                case "sent":
                    ordersByStatus.getSent().add(order);
                    break;
                case "packing":
                    ordersByStatus.getPacking().add(order);
                    break;
                case "cooking":
                    ordersByStatus.getCooking().add(order);
                    break;
                case "preparing":
                    ordersByStatus.getPreparing().add(order);
                    break;
                case "received":
                    ordersByStatus.getReceived().add(order);
                    break;
            }
        }
        return ordersByStatus;
    }

    public void getOrderDetailsByIDFromDb(Context context, String orderId, Request.RequestProductsCallBack listener) {

        DbHandler dbHandler = new DbHandler(context);

        if (MyApp.get().isNetworkAvailable()) {

            Request.getInstance().getOrderDetailsByID(context, orderId, response -> {

                OpenOrderModel localOrder = dbHandler.getOrderByIdFromDb(orderId);

                if (response == null) listener.onDataDone(localOrder);
                else {
                    if (localOrder == null)
                        dbHandler.insertOrUpdateOrderDetails(response, false);
                    else if (!response.getActionTime().equals(localOrder.getActionTime()))
                        dbHandler.insertOrUpdateOrderDetails(response, true);
                    listener.onDataDone(response);
                }
            });
        } else {
            Utils.openAlertDialog(context, "You are now offline", "Connection is lost");
            OpenOrderModel localOrder = dbHandler.getOrderByIdFromDb(orderId);
            listener.onDataDone(localOrder);
        }
    }

    private void updateLocalDB(List<OrderModel> orders, Context context) {
        DbHandler dbHandler = new DbHandler(context);

        List<OpenOrderModel> orderToInsert = new ArrayList<>();
        List<OpenOrderModel> orderToUpdate = new ArrayList<>();
        for (int i = 0; i < orders.size(); i++) {
            OpenOrderModel localOrder = dbHandler.getOrderByIdFromDb(orders.get(i).getId());
            if (localOrder == null) {
                orderToInsert.add(new OpenOrderModel(orders.get(i)));
            } else {
                if (orders.get(i).getActionTime() != Integer.parseInt(localOrder.getActionTime())) {
                    orderToUpdate.add(new OpenOrderModel(orders.get(i)));
                }
            }
        }
        for (int j = 0; j < orderToInsert.size(); j++) {
            updateOrderDetailsByID(context, orderToInsert.get(j), false);
        }
        for (int j = 0; j < orderToUpdate.size(); j++) {
            updateOrderDetailsByID(context, orderToUpdate.get(j), true);
        }
    }

    private void updateOrderDetailsByID(Context context, OpenOrderModel orderModel, boolean isUpdate) {
        DbHandler dbHandler = new DbHandler(context);

        Request.getInstance().getOrderDetailsByID(context, orderModel.getId(), response -> {
                    if (response != null) {
                        response.setColor(orderModel.getColor());
                        response.setIsChangeConfirmed(orderModel.isChangeConfirmed());
                        response.setStartTimeStr(orderModel.getStartTimeStr());

                    } else {
                        response = orderModel;
                    }

                    dbHandler.insertOrUpdateOrderDetails(response, isUpdate);

                }
        );
    }

}


