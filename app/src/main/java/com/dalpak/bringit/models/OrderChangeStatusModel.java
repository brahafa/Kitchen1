package com.dalpak.bringit.models;

public class OrderChangeStatusModel {

    private final int business_id;
    private final long order_id;
    private final String status;
    private final int position;

    public OrderChangeStatusModel(int business_id, long order_id, String newStatus, int newPos) {
        this.business_id = business_id;
        this.order_id = order_id;
        this.status = newStatus;
        this.position = newPos;
    }
}
