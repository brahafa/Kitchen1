package com.dalpak.bringit.models;

import com.google.gson.annotations.SerializedName;

public class WorkerModel {
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("phone")
    private String mPhone;
    @SerializedName("permissions")
    private PermissionsBean mPermissions;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public PermissionsBean getPermissions() {
        return mPermissions;
    }

    public void setPermissions(PermissionsBean permissions) {
        mPermissions = permissions;
    }

    public class PermissionsBean {
        @SerializedName("moneybox")
        private String mMoneybox;
        @SerializedName("kitchen")
        private String mKitchen;
        @SerializedName("management")
        private String mManagement;
        @SerializedName("delivering")
        private String mDelivering;
        @SerializedName("openCloseBusiness")
        private String mOpenCloseBusiness;

        public String getMoneybox() {
            return mMoneybox;
        }

        public void setMoneybox(String moneybox) {
            mMoneybox = moneybox;
        }

        public String getKitchen() {
            return mKitchen;
        }

        public void setKitchen(String kitchen) {
            mKitchen = kitchen;
        }

        public String getManagement() {
            return mManagement;
        }

        public void setManagement(String management) {
            mManagement = management;
        }

        public String getDelivering() {
            return mDelivering;
        }

        public void setDelivering(String delivering) {
            mDelivering = delivering;
        }

        public String getOpenCloseBusiness() {
            return mOpenCloseBusiness;
        }

        public void setOpenCloseBusiness(String openCloseBusiness) {
            mOpenCloseBusiness = openCloseBusiness;
        }
    }
}
