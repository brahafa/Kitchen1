package com.dalpak.bringit.models;


import com.google.gson.annotations.SerializedName;

public class OrderModel {

    @SerializedName("id")
    private String mId;
    @SerializedName("business_id")
    private String mBusinessId;
    @SerializedName("action_time")
    private int mActionTime;
    @SerializedName("order_time")
    private String mOrderTime;
    @SerializedName("is_delivery")
    private boolean mIsDelivery;
    @SerializedName("is_change_confirmed")
    private boolean mIsChangeConfirmed;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("change_type")
    private String mChangeType;
    @SerializedName("total_paid")
    private int mTotalPaid;
    @SerializedName("is_paid")
    private int mIsPaid;
    @SerializedName("position")
    private int mPosition;
    @SerializedName("has_changes")
    private boolean mHasChanges;
    @SerializedName("table_id")
    private String mTableId;
    @SerializedName("delivery_option")
    private String mDeliveryOption;
    @SerializedName("cooking_time")
    private int mCookingTime;
    @SerializedName("startTimeStr")
    private String mStartTimeStr;
    @SerializedName("color")
    private String mColor;
    @SerializedName("client")
    private ClientModel mClient;

    public String getChangeType() {
        return mChangeType;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getActionTime() {
        return mActionTime;
    }

    public void setActionTime(int actionTime) {
        mActionTime = actionTime;
    }

    public boolean isDelivery() {
        return mIsDelivery;
    }

    public void setIsDelivery(boolean isDelivery) {
        mIsDelivery = isDelivery;
    }

    public int getTotalPaid() {
        return mTotalPaid;
    }

    public void setTotalPaid(int totalPaid) {
        mTotalPaid = totalPaid;
    }

    public int getIsPaid() {
        return mIsPaid;
    }
    public void setIsPaid(int isPaid) {
        mIsPaid = isPaid;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public boolean isHasChanges() {
        return mHasChanges;
    }

    public void setHasChanges(boolean hasChanges) {
        mHasChanges = hasChanges;
    }

    public int getCookingTime() {
        return mCookingTime;
    }

    public void setCookingTime(int cookingTime) {
        mCookingTime = cookingTime;
    }

    public ClientModel getClient() {
        return mClient;
    }

    public void setClient(ClientModel client) {
        mClient = client;
    }

    public String getOrderTime() {
        return mOrderTime;
    }

    public void setOrderTime(String mOrderTime) {
        this.mOrderTime = mOrderTime;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getStartTimeStr() {
        return mStartTimeStr;
    }

    public void setStartTimeStr(String mStartTimeStr) {
        this.mStartTimeStr = mStartTimeStr;
    }

    public String getTableId() {
        return mTableId;
    }

    public void setTableId(String mTableId) {
        this.mTableId = mTableId;
    }

    public String getBusinessId() {
        return mBusinessId;
    }

    public void setBusinessId(String mBusinessId) {
        this.mBusinessId = mBusinessId;
    }

    public String getDeliveryOption() {
        return mDeliveryOption;
    }

    public void setDeliveryOption(String mDeliveryOption) {
        this.mDeliveryOption = mDeliveryOption;
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

    public void setChangeType(String mChangeType) {
        this.mChangeType = mChangeType;
    }
}
