package com.dalpak.bringit.models;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BusinessModel {

    private static BusinessModel instance = null;

    private static int business_id;
    private static String business_name_formal;
    private static String business_name_commercial;
    private static String business_email;
    private static String business_register_date;
    private static String business_last_login;
    private static String business_phone;
    private static String business_address;
    private static String business_delivery_time;
    private static String additional_delivery_time_in_minute;
    private static String logo_url;
    private static String utoken;

    private static List<ItemModel> drinkList ;


    public static BusinessModel getInstance() {
        if(instance == null) {
            instance = new BusinessModel();
        }
        return instance;
    }

    public  void initData(JSONObject JSONbusinessModel) throws JSONException {

        JSONObject jsonObjectMsg=JSONbusinessModel.getJSONObject("message");

        BusinessModel.business_id = jsonObjectMsg.getInt("business_id");
        BusinessModel.business_name_formal = jsonObjectMsg.getString("business_name_formal");
        BusinessModel.business_name_commercial = jsonObjectMsg.getString("business_name_commercial");
        BusinessModel.business_email =jsonObjectMsg.getString("business_email");
        BusinessModel.business_register_date = jsonObjectMsg.getString("business_register_date");
        BusinessModel.business_last_login = jsonObjectMsg.getString("business_last_login");
        BusinessModel.business_phone = jsonObjectMsg.getString("business_phone");
        BusinessModel.business_address = jsonObjectMsg.getString("business_address");
        BusinessModel.business_delivery_time = jsonObjectMsg.getString("business_delivery_time");
        BusinessModel.additional_delivery_time_in_minute = jsonObjectMsg.getString("additional_delivery_time_in_minute");
        BusinessModel.logo_url = jsonObjectMsg.getString("logo_url");
        BusinessModel.utoken = JSONbusinessModel.getString("utoken");


    }

    public void setDrinkList(List<ItemModel> drinkList){
        BusinessModel.drinkList =drinkList;
    }
    public List<ItemModel> getDrinkList( ){
        return drinkList;
    }
    public  int getBusiness_id() {
        return business_id;
    }

    public  void setBusiness_id(int business_id) {
        BusinessModel.business_id = business_id;
    }

    public  String getBusiness_name_formal() {
        return business_name_formal;
    }

    public  void setBusiness_name_formal(String business_name_formal) {
        BusinessModel.business_name_formal = business_name_formal;
    }

    public  String getBusiness_name_commercial() {
        return business_name_commercial;
    }

    public  void setBusiness_name_commercial(String business_name_commercial) {
        BusinessModel.business_name_commercial = business_name_commercial;
    }

    public  String getBusiness_email() {
        return business_email;
    }

    public  void setBusiness_email(String business_email) {
        BusinessModel.business_email = business_email;
    }

    public  String getBusiness_register_date() {
        return business_register_date;
    }

    public  void setBusiness_register_date(String business_register_date) {
        BusinessModel.business_register_date = business_register_date;
    }

    public  String getBusiness_last_login() {
        return business_last_login;
    }

    public  void setBusiness_last_login(String business_last_login) {
        BusinessModel.business_last_login = business_last_login;
    }

    public  String getBusiness_phone() {
        return business_phone;
    }

    public  void setBusiness_phone(String business_phone) {
        BusinessModel.business_phone = business_phone;
    }

    public  String getBusiness_address() {
        return business_address;
    }

    public  void setBusiness_address(String business_address) {
        BusinessModel.business_address = business_address;
    }

    public  String getBusiness_delivery_time() {
        return business_delivery_time;
    }

    public  void setBusiness_delivery_time(String business_delivery_time) {
        BusinessModel.business_delivery_time = business_delivery_time;
    }

    public  String getAdditional_delivery_time_in_minute() {
        return additional_delivery_time_in_minute;
    }

    public  void setAdditional_delivery_time_in_minute(String additional_delivery_time_in_minute) {
        BusinessModel.additional_delivery_time_in_minute = additional_delivery_time_in_minute;
    }

    public  String getLogo_url() {
        return logo_url;
    }

    public  void setLogo_url(String logo_url) {
        BusinessModel.logo_url = logo_url;
    }

    public  String getUtoken() {
        return utoken;
    }

    public  void setUtoken(String utoken) {
        BusinessModel.utoken = utoken;
    }
}
