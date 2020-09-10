package com.dalpak.bringit.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemModel {

    @SerializedName("id")
    private String mId;
    @SerializedName("order_id")
    private String mOrderId;
    @SerializedName("name")
    private String mName;
    @SerializedName("type_name")
    private String mTypeName;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("location")
    private String mLocation;
    @SerializedName("is_compensation")
    private String mIsCompensation;
    @SerializedName("products")
    private List<ItemModel> mProducts;
    @SerializedName("categories")
    private List<OrderCategoryModel> mCategories;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getOrderId() {
        return mOrderId;
    }

    public void setOrderId(String orderId) {
        mOrderId = orderId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTypeName() {
        return mTypeName;
    }

    public void setTypeName(String typeName) {
        mTypeName = typeName;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getIsCompensation() {
        return mIsCompensation;
    }

    public void setIsCompensation(String isCompensation) {
        mIsCompensation = isCompensation;
    }

    public List<ItemModel> getProducts() {
        return mProducts;
    }

    public void setProducts(List<ItemModel> products) {
        mProducts = products;
    }

    public List<OrderCategoryModel> getCategories() {
        return mCategories;
    }

    public void setCategories(List<OrderCategoryModel> mCategories) {
        this.mCategories = mCategories;
    }
}
