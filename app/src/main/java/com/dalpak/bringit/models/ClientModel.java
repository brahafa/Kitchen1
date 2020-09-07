package com.dalpak.bringit.models;

import com.google.gson.annotations.SerializedName;

public class ClientModel {
        @SerializedName("f_name")
        private String mFName;
        @SerializedName("l_name")
        private String mLName;
        @SerializedName("phone")
        private String mPhone;
        @SerializedName("address")
        private AddressBean mAddress;

        public String getFName() {
            return mFName;
        }

        public void setFName(String fName) {
            mFName = fName;
        }

        public String getLName() {
            return mLName;
        }

        public void setLName(String lName) {
            mLName = lName;
        }

        public String getPhone() {
            return mPhone;
        }

        public void setPhone(String phone) {
            mPhone = phone;
        }

        public AddressBean getAddress() {
            return mAddress;
        }

        public void setAddress(AddressBean address) {
            mAddress = address;
        }

        public class AddressBean {
            @SerializedName("city_id")
            private String mCityId;
            @SerializedName("city")
            private String mCity;
            @SerializedName("street")
            private String mStreet;
            @SerializedName("house_num")
            private String mHouseNum;
            @SerializedName("floor")
            private String mFloor;
            @SerializedName("apt_num")
            private String mAptNum;
            @SerializedName("entrance")
            private String mEntrance;

            public String getCityId() {
                return mCityId;
            }

            public void setCityId(String cityId) {
                mCityId = cityId;
            }

            public String getCity() {
                return mCity;
            }

            public void setCity(String city) {
                mCity = city;
            }

            public String getStreet() {
                return mStreet;
            }

            public void setStreet(String street) {
                mStreet = street;
            }

            public String getHouseNum() {
                return mHouseNum;
            }

            public void setHouseNum(String houseNum) {
                mHouseNum = houseNum;
            }

            public String getFloor() {
                return mFloor;
            }

            public void setFloor(String floor) {
                mFloor = floor;
            }

            public String getAptNum() {
                return mAptNum;
            }

            public void setAptNum(String aptNum) {
                mAptNum = aptNum;
            }

            public String getEntrance() {
                return mEntrance;
            }

            public void setEntrance(String entrance) {
                mEntrance = entrance;
            }
        }
    }
