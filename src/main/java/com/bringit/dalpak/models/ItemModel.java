package com.bringit.dalpak.models;


import java.util.ArrayList;
import java.util.List;

public class ItemModel {
    private String color;
    private String item_id;
    private String location;
    private String item_picture;
    private String item_name;
    private boolean is_compensation;
    private String position;
    private String type;
    private boolean isShortcut;
    private int uniq_for_business_id;
    private int drink_id;
    private int status;
    private int object_id;
    private int id;
    private String web_price;
    private int deal_product;
    private String dalpak_price;
    private String topping_price_on_slice;
    private String object_type;
    private String price;
    private String picture;
    private String description;
    private String date_added;
    private String name;
    private String toppingLocation;
    private String father_id;
    private String cart_id;
    private int business_id;
    private int filiingIndex;
    private String default_price;
    private  List<ItemModel>  filling;
    private  List<ItemModel>  item_filling;
    private  List<Layers> layers;
    private boolean selected;


    public String getToppingLocation() {
        return toppingLocation;
    }
    public String getFather_id() {
        return father_id;
    }
    public List<ItemModel> getItem_filling() {
        if(item_filling!=null)
            for (int i=0; i<item_filling.size(); i++){
                item_filling.get(i).setCart_id(getCart_id());
                item_filling.get(i).setFiliingIndex(i);
            }
        return item_filling;
    }

    public List<Layers> getLayers() {
        if(this.layers==null)
            this.layers=new ArrayList<>();
        return layers;
    }
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public void setLayers(List<Layers> layers) {
        if(this.layers==null)
            this.layers=new ArrayList<>();

        this.layers = layers;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public int getDeal_product(){
        return deal_product;
    }

    public void setFiliingIndex(int filiingIndex) {
        this.filiingIndex = filiingIndex;
    }
    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }
    public String getCart_id() {
        return cart_id;
    }
    public List<ItemModel> getFiling() {
        return filling;
    }

    public int getUniq_for_business_id() {
        return uniq_for_business_id;
    }

    public int getDrink_id() {
        return drink_id;
    }

    public int getStatus() {
        return status;
    }

    public int getObject_id() {
        return object_id;
    }

    public int getId() {
        return id;
    }

    public String getWeb_price() {
        return web_price;
    }

    public String getDalpak_price() {
        return dalpak_price;
    }

    public String getTopping_price_on_slice() {
        return topping_price_on_slice;
    }

    public String getObject_type() {
        return object_type;
    }

    public String getPrice() {
        return price;
    }

    public String getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }

    public String getDate_added() {
        return date_added;
    }

    public String getName() {
        return name;
    }

    public int getBusiness_id() {
        return business_id;
    }

    public String getDefault_price() {
        return default_price;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItem_picture() {
        return item_picture;
    }

    public void setItem_picture(String item_picture) {
        this.item_picture = item_picture;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public boolean isIs_compensation() {
        return is_compensation;
    }

    public void setIs_compensation(boolean is_compensation) {
        this.is_compensation = is_compensation;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isShortcut() {
        return isShortcut;
    }

    public void setShortcut(boolean shortcut) {
        isShortcut = shortcut;
    }

    public void setUniq_for_business_id(int uniq_for_business_id) {
        this.uniq_for_business_id = uniq_for_business_id;
    }

    public void setDrink_id(int drink_id) {
        this.drink_id = drink_id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWeb_price(String web_price) {
        this.web_price = web_price;
    }

    public void setDeal_product(int deal_product) {
        this.deal_product = deal_product;
    }

    public void setDalpak_price(String dalpak_price) {
        this.dalpak_price = dalpak_price;
    }

    public void setTopping_price_on_slice(String topping_price_on_slice) {
        this.topping_price_on_slice = topping_price_on_slice;
    }

    public void setObject_type(String object_type) {
        this.object_type = object_type;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setToppingLocation(String toppingLocation) {
        this.toppingLocation = toppingLocation;
    }

    public void setFather_id(String father_id) {
        this.father_id = father_id;
    }

    public void setBusiness_id(int business_id) {
        this.business_id = business_id;
    }

    public int getFiliingIndex() {
        return filiingIndex;
    }

    public void setDefault_price(String default_price) {
        this.default_price = default_price;
    }

    public List<ItemModel> getFilling() {
        return filling;
    }

    public void setFilling(List<ItemModel> filling) {
        this.filling = filling;
    }

    public void setItem_filling(List<ItemModel> item_filling) {
        this.item_filling = item_filling;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
