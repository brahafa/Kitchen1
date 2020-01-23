package com.bringit.dalpak.utils;

public class Constants {
    public static String TOKEN_PREF = "token";
    public static String NAME_PREF = "name";
    public static String USER_ALREADY_CONNECTED_PREF = "connected";

    //URLS
    public static String IMAGES_BASE_URL = "https://api.bringit.co.il/public/images/";
    public static String DRINKS_URL = IMAGES_BASE_URL + "drink/";
    public static String ADDITIONAL_URL = IMAGES_BASE_URL+"loka/additionalOffer/";
    public static String FOOD_URL = IMAGES_BASE_URL + "food/";


    public static int maximumWaitTimePreparing = 10;
    public static int maximumWaitTimeReceived = 10;
    public static int maximumWaitTimeCooking = 10;
    public static int maximumWaitTimePacking = 10;
    public static int maximumWaitTimeSent = 10;

    public static enum items_types {drink, food, topping, deal}

    ;
}
