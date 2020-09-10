package com.dalpak.bringit.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderCategoryModel {

    @SerializedName("name")
    private String mName;
    @SerializedName("is_topping_divided")
    private String mIsToppingDivided;
    @SerializedName("products")
    private List<ItemModel> mProducts;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getIsToppingDivided() {
        return mIsToppingDivided;
    }

    public void setIsToppingDivided(String isToppingDivided) {
        mIsToppingDivided = isToppingDivided;
    }

    public List<ItemModel> getProducts() {
        return mProducts;
    }

    public void setProducts(List<ItemModel> products) {
        mProducts = products;
    }

}