package com.dalpak.bringit.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderCategoryModel {

    @SerializedName("name")
    private String mName;
    @SerializedName("is_topping_divided")
    private boolean mIsToppingDivided;
    @SerializedName("products")
    private List<ItemModel> mProducts;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public boolean isToppingDivided() {
        return mIsToppingDivided;
    }

    public void setIsToppingDivided(boolean isToppingDivided) {
        mIsToppingDivided = isToppingDivided;
    }

    public List<ItemModel> getProducts() {
        return mProducts;
    }

    public void setProducts(List<ItemModel> products) {
        mProducts = products;
    }

}