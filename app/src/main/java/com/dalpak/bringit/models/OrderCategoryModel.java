package com.dalpak.bringit.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderCategoryModel {

    @SerializedName("name")
    private String mName;
    @SerializedName("is_topping_divided")
    private boolean mIsToppingDivided;
    @SerializedName("products_fixed_price")
    private int mProductsFixedPrice;
    @SerializedName("category_has_fixed_price")
    private boolean mCategoryHasFixedPrice;
    @SerializedName("fixed_price")
    private int mFixedPrice;
    @SerializedName("products")
    private List<ItemModel> mProducts;

    public int getProductsFixedPrice() {
        return mProductsFixedPrice;
    }

    public void setProductsFixedPrice(int productsFixedPrice) {
        mProductsFixedPrice = productsFixedPrice;
    }

    public boolean getCategoryHasFixedPrice() {
        return mCategoryHasFixedPrice;
    }

    public void setCategoryHasFixedPrice(boolean categoryHasFixedPrice) {
        mCategoryHasFixedPrice = categoryHasFixedPrice;
    }

    public int getFixedPrice() {
        return mFixedPrice;
    }

    public void setFixedPrice(int fixedPrice) {
        mFixedPrice = fixedPrice;
    }


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