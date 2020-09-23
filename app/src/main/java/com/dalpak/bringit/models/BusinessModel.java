package com.dalpak.bringit.models;


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
    private String topping_method_id;
    private String topping_method_name;

    private List<ItemModel> drinkList;


    public static BusinessModel getInstance() {
        if (instance == null) {
            instance = new BusinessModel();
        }
        return instance;
    }

    public void initData(BusinessModel businessModel) {
        this.business_id = businessModel.business_id;
        this.business_name_formal = businessModel.business_name_formal;
        this.business_name_commercial = businessModel.business_name_commercial;
        this.business_email = businessModel.business_email;
        this.business_register_date = businessModel.business_register_date;
        this.business_last_login = businessModel.business_last_login;
        this.business_phone = businessModel.business_phone;
        this.business_address = businessModel.business_address;
        this.business_delivery_time = businessModel.business_delivery_time;
        this.additional_delivery_time_in_minute = businessModel.additional_delivery_time_in_minute;
        this.logo_url = businessModel.logo_url;
        this.topping_method_id = businessModel.topping_method_id;
        this.topping_method_name = businessModel.topping_method_name;
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

    public String getTopping_method_id() {
        return topping_method_id;
    }

    public void setTopping_method_id(String topping_method_id) {
        this.topping_method_id = topping_method_id;
    }

    public String getTopping_method_name() {
        return topping_method_name;
    }

    public void setTopping_method_name(String topping_method_name) {
        this.topping_method_name = topping_method_name;
    }
}
