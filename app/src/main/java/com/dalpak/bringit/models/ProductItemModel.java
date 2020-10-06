package com.dalpak.bringit.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductItemModel implements Parcelable {

    @SerializedName("id")
    private String mId;
    @SerializedName("type_id")
    private String mTypeId;
    @SerializedName("name")
    private String mName;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("delivery_price")
    private int mDeliveryPrice;
    @SerializedName("not_delivery_price")
    private int mNotDeliveryPrice;
    @SerializedName("picture")
    private String mPicture;
    @SerializedName("imageUrl")
    private String mImageUrl;
    @SerializedName("business_id")
    private String mBusinessId;
    @SerializedName("in_inventory")
    private String mInInventory;
    @SerializedName("shape")
    private String mShape;
    @SerializedName("type_name")
    private String mTypeName;
    @SerializedName("categories")
    private List<CategoryModel> mCategories;


    protected ProductItemModel(Parcel in) {
        mId = in.readString();
        mTypeId = in.readString();
        mName = in.readString();
        mDescription = in.readString();
        mDeliveryPrice = in.readInt();
        mNotDeliveryPrice = in.readInt();
        mPicture = in.readString();
        mImageUrl = in.readString();
        mBusinessId = in.readString();
        mInInventory = in.readString();
        mShape = in.readString();
        mTypeName = in.readString();
        mCategories = in.createTypedArrayList(CategoryModel.CREATOR);
    }

    public static final Creator<ProductItemModel> CREATOR = new Creator<ProductItemModel>() {
        @Override
        public ProductItemModel createFromParcel(Parcel in) {
            return new ProductItemModel(in);
        }

        @Override
        public ProductItemModel[] newArray(int size) {
            return new ProductItemModel[size];
        }
    };

    public ProductItemModel() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getInInventory() {
        return mInInventory;
    }

    public void setInInventory(String inInventory) {
        mInInventory = inInventory;
    }

    public int getDeliveryPrice() {
        return mDeliveryPrice;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        mDeliveryPrice = deliveryPrice;
    }

    public String getTypeName() {
        return mTypeName;
    }

    public void setTypeName(String objectType) {
        mTypeName = objectType;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getShape() {
        return mShape;
    }

    public void setShape(String mShape) {
        this.mShape = mShape;
    }

    public String getTypeId() {
        return mTypeId;
    }

    public void setTypeId(String mTypeId) {
        this.mTypeId = mTypeId;
    }

    public int getNotDeliveryPrice() {
        return mNotDeliveryPrice;
    }

    public void setNotDeliveryPrice(int mNotDeliveryPrice) {
        this.mNotDeliveryPrice = mNotDeliveryPrice;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String mPicture) {
        this.mPicture = mPicture;
    }

    public String getBusinessId() {
        return mBusinessId;
    }

    public void setBusinessId(String mBusinessId) {
        this.mBusinessId = mBusinessId;
    }

    public List<CategoryModel> getCategories() {
        return mCategories;
    }

    public void setCategories(List<CategoryModel> mCategories) {
        this.mCategories = mCategories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTypeId);
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeInt(mDeliveryPrice);
        dest.writeInt(mNotDeliveryPrice);
        dest.writeString(mPicture);
        dest.writeString(mImageUrl);
        dest.writeString(mBusinessId);
        dest.writeString(mInInventory);
        dest.writeString(mShape);
        dest.writeString(mTypeName);
        dest.writeTypedList(mCategories);
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
