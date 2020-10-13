package com.dalpak.bringit.models;

import com.google.gson.annotations.SerializedName;

public class WorkerResponse {

    @SerializedName("user")
    private WorkerModel mUser;
    @SerializedName("status")
    private boolean mStatus;

    public WorkerModel getUser() {
        return mUser;
    }

    public void setUser(WorkerModel user) {
        mUser = user;
    }

    public boolean isStatus() {
        return mStatus;
    }

    public void setStatus(boolean status) {
        mStatus = status;
    }

}
