package com.dalpak.bringit.models;

import java.util.List;

public class AllOrdersResponse {

    private OrdersByStatusModel ordersByStatus;
    private List<OrderModel> orders;

    public OrdersByStatusModel getOrdersByStatus() {
        return ordersByStatus;
    }

    public void setOrdersByStatus(OrdersByStatusModel ordersByStatus) {
        this.ordersByStatus = ordersByStatus;
    }

    public List<OrderModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
    }


}
