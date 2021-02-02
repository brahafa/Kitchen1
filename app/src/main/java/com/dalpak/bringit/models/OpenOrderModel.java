package com.dalpak.bringit.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OpenOrderModel {


    @SerializedName("id")
    private String mId;
    @SerializedName("client_id")
    private String mClientId;
    @SerializedName("address_id")
    private String mAddressId;
    @SerializedName("added_by")
    private String mAddedBy;
    @SerializedName("action_time")
    private String mActionTime;
    @SerializedName("order_time")
    private String mOrderTime;
    @SerializedName("is_delivery")
    private String mIsDelivery;
    @SerializedName("total_with_delivery")
    private String mTotalWithDelivery;
    @SerializedName("total")
    private String mTotal;
    @SerializedName("delivery_price")
    private String mDeliveryPrice;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("notes")
    private String mNotes;
    @SerializedName("order_notes")
    private String mOrderNotes;
    @SerializedName("delivery_notes")
    private String mDeliveryNotes;
    @SerializedName("table_id")
    private String mTableId;
    @SerializedName("delivery_option")
    private String mDeliveryOption;
    @SerializedName("payment_name")
    private String mPaymentName;
    @SerializedName("payment_display")
    private String mPaymentDisplay;
    @SerializedName("added_by_system")
    private String mAddedBySystem;
    @SerializedName("change_type")
    private String mChangeType;
    @SerializedName("change_for_order_id")
    private String mChangeForOrderId;
    @SerializedName("color")
    private String mColor;
    @SerializedName("is_change_confirmed")
    private boolean mIsChangeConfirmed;
    @SerializedName("startTimeStr")
    private String mStartTimeStr;
    @SerializedName("client")
    private ClientModel mClient;
    @SerializedName("products")
    private List<ItemModel> mProducts;

    public OpenOrderModel() {

    }

    public OpenOrderModel(OrderModel orderModel) {
        this.mId = orderModel.getId();
        this.mActionTime = String.valueOf(orderModel.getActionTime());
        this.mOrderTime = orderModel.getOrderTime();
        this.mStatus = orderModel.getStatus();
        this.mChangeType = orderModel.getChangeType();
        this.mDeliveryOption = orderModel.getDeliveryOption();
        this.mColor = orderModel.getColor();
        this.mIsChangeConfirmed = orderModel.isChangeConfirmed();
        this.mClient = orderModel.getClient();
        this.mStartTimeStr = orderModel.getStartTimeStr();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getClientId() {
        return mClientId;
    }

    public void setClientId(String clientId) {
        mClientId = clientId;
    }

    public String getAddressId() {
        return mAddressId;
    }

    public void setAddressId(String addressId) {
        mAddressId = addressId;
    }

    public String getAddedBy() {
        return mAddedBy;
    }

    public void setAddedBy(String addedBy) {
        mAddedBy = addedBy;
    }

    public String getActionTime() {
        return mActionTime;
    }

    public void setActionTime(String actionTime) {
        mActionTime = actionTime;
    }

    public String getOrderTime() {
        return mOrderTime;
    }

    public void setOrderTime(String orderTime) {
        mOrderTime = orderTime;
    }

    public String getIsDelivery() {
        return mIsDelivery;
    }

    public void setIsDelivery(String isDelivery) {
        mIsDelivery = isDelivery;
    }

    public String getTotalWithDelivery() {
        return mTotalWithDelivery;
    }

    public void setTotalWithDelivery(String totalWithDelivery) {
        mTotalWithDelivery = totalWithDelivery;
    }

    public String getTotal() {
        return mTotal;
    }

    public void setTotal(String total) {
        mTotal = total;
    }

    public String getDeliveryPrice() {
        return mDeliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        mDeliveryPrice = deliveryPrice;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }

    public String getOrderNotes() {
        return mOrderNotes;
    }

    public void setOrderNotes(String mOrderNotes) {
        this.mOrderNotes = mOrderNotes;
    }

    public String getDeliveryNotes() {
        return mDeliveryNotes;
    }

    public void setDeliveryNotes(String mDeliveryNotes) {
        this.mDeliveryNotes = mDeliveryNotes;
    }

    public String getTableId() {
        return mTableId;
    }

    public void setTableId(String tableId) {
        mTableId = tableId;
    }

    public String getDeliveryOption() {
        return mDeliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        mDeliveryOption = deliveryOption;
    }

    public String getPaymentName() {
        return mPaymentName;
    }

    public void setPaymentName(String paymentName) {
        mPaymentName = paymentName;
    }

    public String getPaymentDisplay() {
        return mPaymentDisplay;
    }

    public void setPaymentDisplay(String paymentDisplay) {
        mPaymentDisplay = paymentDisplay;
    }

    public ClientModel getClient() {
        return mClient;
    }

    public void setClient(ClientModel client) {
        mClient = client;
    }

    public String getAddedBySystem() {
        return mAddedBySystem;
    }

    public void setAddedBySystem(String mAddedBySystem) {
        this.mAddedBySystem = mAddedBySystem;
    }

    public List<ItemModel> getProducts() {
        return mProducts;
    }

    public void setProducts(List<ItemModel> mProducts) {
        this.mProducts = mProducts;
    }

    public String getChangeType() {
        return mChangeType;
    }

    public void setChangeType(String mChangeType) {
        this.mChangeType = mChangeType;
    }

    public String getChangeForOrderId() {
        return mChangeForOrderId;
    }

    public void setChangeForOrderId(String mChangeForOrderId) {
        this.mChangeForOrderId = mChangeForOrderId;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String mColor) {
        this.mColor = mColor;
    }


    public boolean isChangeConfirmed() {
        return mIsChangeConfirmed;
    }

    public void setIsChangeConfirmed(boolean mIsChangeConfirmed) {
        this.mIsChangeConfirmed = mIsChangeConfirmed;
    }

    public String getStartTimeStr() {
        return mStartTimeStr;
    }

    public void setStartTimeStr(String mStartTimeStr) {
        this.mStartTimeStr = mStartTimeStr;
    }
}

