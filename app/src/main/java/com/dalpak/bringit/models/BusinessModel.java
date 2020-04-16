package com.dalpak.bringit.models;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BusinessModel {

    private static BusinessModel instance = null;

    private int business_id;
    private String business_name_formal;
    private String business_name_commercial;
    private String business_email;
    private String business_register_date;
    private String business_last_login;
    private String business_phone;
    private String business_address;
    private String business_delivery_time;
    private String additional_delivery_time_in_minute;
    private String logo_url;
    private String utoken;

    private List<ItemModel> drinkList;


    public static BusinessModel getInstance() {
        if (instance == null) {
            instance = new BusinessModel();
        }
        return instance;
    }

    public void initData(JSONObject JSONbusinessModel) throws JSONException {

        JSONObject jsonObjectMsg = JSONbusinessModel.getJSONObject("message");

        business_id = jsonObjectMsg.getInt("business_id");
        business_name_formal = jsonObjectMsg.getString("business_name_formal");
        business_name_commercial = jsonObjectMsg.getString("business_name_commercial");
        business_email = jsonObjectMsg.getString("business_email");
        business_register_date = jsonObjectMsg.getString("business_register_date");
        business_last_login = jsonObjectMsg.getString("business_last_login");
        business_phone = jsonObjectMsg.getString("business_phone");
        business_address = jsonObjectMsg.getString("business_address");
        business_delivery_time = jsonObjectMsg.getString("business_delivery_time");
        additional_delivery_time_in_minute = jsonObjectMsg.getString("additional_delivery_time_in_minute");
        logo_url = jsonObjectMsg.getString("logo_url");
        utoken = JSONbusinessModel.getString("utoken");


    }

    public void setDrinkList(List<ItemModel> drinkList) {
        this.drinkList = drinkList;
    }

    public List<ItemModel> getDrinkList() {
        return drinkList;
    }

    public int getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(int business_id) {
        this.business_id = business_id;
    }

    public String getBusiness_name_formal() {
        return business_name_formal;
    }

    public void setBusiness_name_formal(String business_name_formal) {
        this.business_name_formal = business_name_formal;
    }

    public String getBusiness_name_commercial() {
        return business_name_commercial;
    }

    public void setBusiness_name_commercial(String business_name_commercial) {
        this.business_name_commercial = business_name_commercial;
    }

    public String getBusiness_email() {
        return business_email;
    }

    public void setBusiness_email(String business_email) {
        this.business_email = business_email;
    }

    public String getBusiness_register_date() {
        return business_register_date;
    }

    public void setBusiness_register_date(String business_register_date) {
        this.business_register_date = business_register_date;
    }

    public String getBusiness_last_login() {
        return business_last_login;
    }

    public void setBusiness_last_login(String business_last_login) {
        this.business_last_login = business_last_login;
    }

    public String getBusiness_phone() {
        return business_phone;
    }

    public void setBusiness_phone(String business_phone) {
        this.business_phone = business_phone;
    }

    public String getBusiness_address() {
        return business_address;
    }

    public void setBusiness_address(String business_address) {
        this.business_address = business_address;
    }

    public String getBusiness_delivery_time() {
        return business_delivery_time;
    }

    public void setBusiness_delivery_time(String business_delivery_time) {
        this.business_delivery_time = business_delivery_time;
    }

    public String getAdditional_delivery_time_in_minute() {
        return additional_delivery_time_in_minute;
    }

    public void setAdditional_delivery_time_in_minute(String additional_delivery_time_in_minute) {
        this.additional_delivery_time_in_minute = additional_delivery_time_in_minute;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getUtoken() {
        return utoken;
    }

    public void setUtoken(String utoken) {
        this.utoken = utoken;
    }
}
