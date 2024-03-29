package com.dalpak.bringit.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryModel implements Parcelable {

    @SerializedName("id")
    private String mId;
    @SerializedName("product_id")
    private String mProductId;
    @SerializedName("name")
    private String mName;
    @SerializedName("list_order")
    private String mListOrder;
    @SerializedName("products_limit")
    private String mProductsLimit;
    @SerializedName("products_fixed_price")
    private int mProductsFixedPrice;
    @SerializedName("category_has_fixed_price")
    private boolean mCategoryHasFixedPrice;
    @SerializedName("fixed_price")
    private int mFixedPrice;
    @SerializedName("is_topping_divided")
    private String mIsToppingDivided;
    @SerializedName("is_mandatory")
    private String mIsMandatory;
    @SerializedName("products")
    private List<InnerProductsModel> mProducts;

    protected CategoryModel(Parcel in) {
        mId = in.readString();
        mProductId = in.readString();
        mName = in.readString();
        mListOrder = in.readString();
        mProductsLimit = in.readString();
        mProductsFixedPrice = in.readInt();
        mCategoryHasFixedPrice = in.readByte() != 0;
        mFixedPrice = in.readInt();
        mIsToppingDivided = in.readString();
        mIsMandatory = in.readString();
        mProducts = in.createTypedArrayList(InnerProductsModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mProductId);
        dest.writeString(mName);
        dest.writeString(mListOrder);
        dest.writeString(mProductsLimit);
        dest.writeInt(mProductsFixedPrice);
        dest.writeByte((byte) (mCategoryHasFixedPrice ? 1 : 0));
        dest.writeInt(mFixedPrice);
        dest.writeString(mIsToppingDivided);
        dest.writeString(mIsMandatory);
        dest.writeTypedList(mProducts);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getProductId() {
        return mProductId;
    }

    public void setProductId(String productId) {
        mProductId = productId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getListOrder() {
        return mListOrder;
    }

    public void setListOrder(String listOrder) {
        mListOrder = listOrder;
    }

    public String getProductsLimit() {
        return mProductsLimit;
    }

    public void setProductsLimit(String productsLimit) {
        mProductsLimit = productsLimit;
    }

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

    public String getIsToppingDivided() {
        return mIsToppingDivided;
    }

    public void setIsToppingDivided(String isToppingDivided) {
        mIsToppingDivided = isToppingDivided;
    }

    public String getIsMandatory() {
        return mIsMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        mIsMandatory = isMandatory;
    }

    public List<InnerProductsModel> getProducts() {
        return mProducts;
    }

    public void setProducts(List<InnerProductsModel> products) {
        mProducts = products;
    }

}
