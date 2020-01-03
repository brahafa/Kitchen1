package com.bringit.dalpak.models;


import java.util.ArrayList;
import java.util.List;

public class ItemModel {

    private String color;
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
}
