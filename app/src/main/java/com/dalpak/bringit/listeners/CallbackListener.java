package com.dalpak.bringit.listeners;

public interface CallbackListener<T> {

    void success(T response);

    void failure(int code, String message);

    void failure(Throwable error);

}