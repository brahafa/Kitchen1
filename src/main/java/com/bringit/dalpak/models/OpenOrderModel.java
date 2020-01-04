package com.bringit.dalpak.models;

//public class OpenOrderModel {
//}


import java.util.List;

public class OpenOrderModel {
    private String order_id;
    private String business_id;
    private String client_id;
    private String address_id;
    private String address;
    private String action_time;
    private String order_time;
    private String order_sound_time;
    private String total_paid;
    private String order_site_fee;
    private String order_added_by;
    private String added_by;
    private String added_by_system;
    private String payment_method_id;
    private String order_token;
    private String status;
    private String order_is_active;
    private String order_confirmation_id;
    private String order_notes;
    private String delivery_notes;
    private String order_bringit_coupon;
    private String order_business_coupon;
    private String order_is_opened;
    private String order_user_ip;
    private String order_rate_code;
    private String order_rate_sms_sent;
    private String order_deliver_code;
    private String order_deliver_code_time;
    private String order_can_follow;
    private String order_is_paid;
    private String is_delivery;
    private String city_name;
    private String street;
    private String house_num;
    private String order_cooking_time;
    private String name;
    private String order_total;
    private String delivery_price;
    private String f_name;
    private String l_name;
    private String phone;
    private String payment_name;
    private String payment_display;
    private String pizza_count;
    private List<ItemModel> order_items;

    public String getAddress() {
        return address;
    }

    public String getPizza_count() {
        return pizza_count;
    }

    public void setPizza_count(String pizza_count) {
        this.pizza_count = pizza_count;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getAdded_by_system() {
        return added_by_system;
    }

    public void setAdded_by_system(String added_by_system) {
        this.added_by_system = added_by_system;
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public String getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(String delivery_price) {
        this.delivery_price = delivery_price;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public String getPayment_display() {
        return payment_display;
    }

    public void setPayment_display(String payment_display) {
        this.payment_display = payment_display;
    }

    public List<ItemModel> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<ItemModel> order_items) {
        this.order_items = order_items;
    }

    public  OpenOrderModel (String name){
        this.name=name;
    }
    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAction_time() {
        return action_time;
    }

    public void setAction_time(String action_time) {
        this.action_time = action_time;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_sound_time() {
        return order_sound_time;
    }

    public void setOrder_sound_time(String order_sound_time) {
        this.order_sound_time = order_sound_time;
    }

    public String getTotal_paid() {
        return total_paid;
    }

    public void setTotal_paid(String total_paid) {
        this.total_paid = total_paid;
    }

    public String getOrder_site_fee() {
        return order_site_fee;
    }

    public void setOrder_site_fee(String order_site_fee) {
        this.order_site_fee = order_site_fee;
    }

    public String getOrder_added_by() {
        return order_added_by;
    }

    public void setOrder_added_by(String order_added_by) {
        this.order_added_by = order_added_by;
    }

    public String getPayment_method_id() {
        return payment_method_id;
    }

    public void setPayment_method_id(String payment_method_id) {
        this.payment_method_id = payment_method_id;
    }

    public String getOrder_token() {
        return order_token;
    }

    public void setOrder_token(String order_token) {
        this.order_token = order_token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_is_active() {
        return order_is_active;
    }

    public void setOrder_is_active(String order_is_active) {
        this.order_is_active = order_is_active;
    }

    public String getOrder_confirmation_id() {
        return order_confirmation_id;
    }

    public void setOrder_confirmation_id(String order_confirmation_id) {
        this.order_confirmation_id = order_confirmation_id;
    }

    public String getOrder_notes() {
        return order_notes;
    }

    public void setOrder_notes(String order_notes) {
        this.order_notes = order_notes;
    }

    public String getDelivery_notes() {
        return delivery_notes;
    }

    public void setDelivery_notes(String delivery_notes) {
        this.delivery_notes = delivery_notes;
    }

    public String getOrder_bringit_coupon() {
        return order_bringit_coupon;
    }

    public void setOrder_bringit_coupon(String order_bringit_coupon) {
        this.order_bringit_coupon = order_bringit_coupon;
    }

    public String getOrder_business_coupon() {
        return order_business_coupon;
    }

    public void setOrder_business_coupon(String order_business_coupon) {
        this.order_business_coupon = order_business_coupon;
    }

    public String getOrder_is_opened() {
        return order_is_opened;
    }

    public void setOrder_is_opened(String order_is_opened) {
        this.order_is_opened = order_is_opened;
    }

    public String getOrder_user_ip() {
        return order_user_ip;
    }

    public void setOrder_user_ip(String order_user_ip) {
        this.order_user_ip = order_user_ip;
    }

    public String getOrder_rate_code() {
        return order_rate_code;
    }

    public void setOrder_rate_code(String order_rate_code) {
        this.order_rate_code = order_rate_code;
    }

    public String getOrder_rate_sms_sent() {
        return order_rate_sms_sent;
    }

    public void setOrder_rate_sms_sent(String order_rate_sms_sent) {
        this.order_rate_sms_sent = order_rate_sms_sent;
    }

    public String getOrder_deliver_code() {
        return order_deliver_code;
    }

    public void setOrder_deliver_code(String order_deliver_code) {
        this.order_deliver_code = order_deliver_code;
    }

    public String getOrder_deliver_code_time() {
        return order_deliver_code_time;
    }

    public void setOrder_deliver_code_time(String order_deliver_code_time) {
        this.order_deliver_code_time = order_deliver_code_time;
    }

    public String getOrder_can_follow() {
        return order_can_follow;
    }

    public void setOrder_can_follow(String order_can_follow) {
        this.order_can_follow = order_can_follow;
    }

    public String getOrder_is_paid() {
        return order_is_paid;
    }

    public void setOrder_is_paid(String order_is_paid) {
        this.order_is_paid = order_is_paid;
    }

    public String getIs_delivery() {
        return is_delivery;
    }

    public void setIs_delivery(String is_delivery) {
        this.is_delivery = is_delivery;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse_num() {
        return house_num;
    }

    public void setHouse_num(String house_num) {
        this.house_num = house_num;
    }

    public String getOrder_cooking_time() {
        return order_cooking_time;
    }

    public void setOrder_cooking_time(String order_cooking_time) {
        this.order_cooking_time = order_cooking_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

