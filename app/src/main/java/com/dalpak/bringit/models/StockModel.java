package com.dalpak.bringit.models;

import java.io.Serializable;

public class StockModel implements Serializable {
    String topping_id;
    String description;
    String picture;
    String default_price;
    String uniq_for_business_id;
    String id;
    String delivery_price;
    String pickup_price;
    String name;
    boolean object_status;
    String object_id;
    String object_type;
    //String filling;
    String category;
    String inInventory;
    boolean in_inventory;

    public String getTopping_id() {
        return topping_id;
    }

    public void setTopping_id(String topping_id) {
        this.topping_id = topping_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDefault_price() {
        return default_price;
    }

    public void setDefault_price(String default_price) {
        this.default_price = default_price;
    }

    public String getUniq_for_business_id() {
        return uniq_for_business_id;
    }

    public void setUniq_for_business_id(String uniq_for_business_id) {
        this.uniq_for_business_id = uniq_for_business_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDelivery_price() {
        return delivery_price;
    }

    public void setDelivery_price(String delivery_price) {
        this.delivery_price = delivery_price;
    }

    public String getPickup_price() {
        return pickup_price;
    }

    public void setPickup_price(String pickup_price) {
        this.pickup_price = pickup_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isObject_status() {
        return object_status;
    }

    public void setObject_status(boolean object_status) {
        this.object_status = object_status;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

//    public String getFilling() {
//        return filling;
//    }
//
//    public void setFilling(String filling) {
//        this.filling = filling;
//    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInInventory() {
        return inInventory;
    }

    public void setInInventory(String inInventory) {
        this.inInventory = inInventory;
    }

    public boolean getIn_inventory() {
        return in_inventory;
    }

    public void setIn_inventory(boolean in_inventory) {
        this.in_inventory = in_inventory;
    }

}
