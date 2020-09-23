package com.dalpak.bringit.utils;

public class Constants {
    public static String LOG_IN_JSON_PREF = "loginJson";
    public static String TOKEN_PREF = "token";
    public static String NAME_PREF = "name";
    public static String USER_ALREADY_CONNECTED_PREF = "connected";

    //URLS
    public static String IMAGES_BASE_URL = "https://api.bringit.co.il/public/images/";
    public static String DRINKS_URL = IMAGES_BASE_URL + "drink/";
    public static String ADDITIONAL_URL = IMAGES_BASE_URL + "loka/additionalOffer/";
    public static String FOOD_URL = IMAGES_BASE_URL + "food/";

    //    delivery options
    public static final String DELIVERY_OPTION_TAKEAWAY = "pickup";
    public static final String DELIVERY_OPTION_DELIVERY = "delivery";
    public static final String DELIVERY_OPTION_TABLE = "table";

    //    alerts
    public static final int ALERT_NEW_ORDER = 0;
    public static final int ALERT_ORDER_OVERTIME = 1;
    public static final int ALERT_EDIT_ORDER = 2;
    public static final int ALERT_FINISH_COOKING = 3;

    //    item types
    public static final String ITEM_TYPE_PIZZA = "pizza";
    public static final String ITEM_TYPE_TOPPING = "topping";
    public static final String ITEM_TYPE_DRINK = "drink";
    public static final String ITEM_TYPE_ADDITIONAL_OFFER = "additionalOffer";
    public static final String ITEM_TYPE_SPECIAL = "special";
    public static final String ITEM_TYPE_DEAL = "deal";

    //    pizza topping locations
    public static final String PIZZA_TYPE_FULL = "full";
    public static final String PIZZA_TYPE_RH = "rightHalfPizza";
    public static final String PIZZA_TYPE_LH = "leftHalfPizza";
    public static final String PIZZA_TYPE_TL = "tl";
    public static final String PIZZA_TYPE_TR = "tr";
    public static final String PIZZA_TYPE_BL = "bl";
    public static final String PIZZA_TYPE_BR = "br";

    //     pizza topping methods
    public static final String BUSINESS_TOPPING_TYPE_QUARTER = "quarter";
    public static final String BUSINESS_TOPPING_TYPE_LAYER = "layer";
    public static final String BUSINESS_TOPPING_TYPE_FIXED = "fixed";

    public static int maximumWaitTimePreparing = 10;
    public static int maximumWaitTimeReceived = 10;
    public static int maximumWaitTimeCooking = 10;
    public static int maximumWaitTimePacking = 10;
    public static int maximumWaitTimeSent = 10;

    public static enum items_types {drink, food, topping, deal}

    ;
}
