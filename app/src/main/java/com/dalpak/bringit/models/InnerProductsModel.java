package com.dalpak.bringit.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class InnerProductsModel implements Parcelable {
    @SerializedName("id")
    private String mId;
    @SerializedName("category_id")
    private String mCategoryId;
    @SerializedName("name")
    private String mName;
    @SerializedName("price")
    private int mPrice;
    @SerializedName("in_inventory")
    private String mInInventory;

    protected InnerProductsModel(Parcel in) {
        mId = in.readString();
        mCategoryId = in.readString();
        mName = in.readString();
        mPrice = in.readInt();
        mInInventory = in.readString();
    }

    public static final Creator<InnerProductsModel> CREATOR = new Creator<InnerProductsModel>() {
        @Override
        public InnerProductsModel createFromParcel(Parcel in) {
            return new InnerProductsModel(in);
        }

        @Override
        public InnerProductsModel[] newArray(int size) {
            return new InnerProductsModel[size];
        }
    };


    public int getId() {
        return Integer.parseInt(mId);
    }

    public String getStringId() {
        return mId;
    }

    public void setId(int id) {
        mId = String.valueOf(id);
    }

    public String getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(String categoryId) {
        mCategoryId = categoryId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public String getInInventory() {
        return mInInventory;
    }

    public void setInInventory(String inInventory) {
        mInInventory = inInventory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mCategoryId);
        dest.writeString(mName);
        dest.writeInt(mPrice);
        dest.writeString(mInInventory);
    }
}
