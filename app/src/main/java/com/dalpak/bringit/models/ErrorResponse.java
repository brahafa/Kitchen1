package com.dalpak.bringit.models;

public class ErrorResponse {


    /**
     * message : לא נמצאו הזמנות חדשות
     * errorCode : 1
     * status : false
     */

    private String message;
    private int errorCode;
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
