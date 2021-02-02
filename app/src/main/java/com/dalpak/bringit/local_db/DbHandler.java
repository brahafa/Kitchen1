package com.dalpak.bringit.local_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dalpak.bringit.models.ClientModel;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.models.OrderModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.dalpak.bringit.utils.Constants.PATTERN_DATE_FROM_SERVER;

public class DbHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "ordersdb";
    private static final String TABLE_Orders = "orderdetails";
    private static final String KEY_LOCAL_ID = "local_id";
    private static final String KEY_ID = "id";
    private static final String KEY_ACTION_TIME = "action_time";
    private static final String KEY_ORDER_TIME = "order_time";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CHANGE_TYPE = "change_type";
    private static final String KEY_DELIVERY_OPTION = "delivery_option";
    private static final String KEY_COLOR = "color";
    private static final String KEY_IS_CHANGE_CONFIRMED = "is_change_confirmed";
    private static final String KEY_CLIENT = "client";
    private static final String KEY_ADDED_BY_SYSTEM = "added_by_system";
    private static final String KEY_START_TIME_STR = "startTimeStr";

    private static final String KEY_TOTAL_WITH_DELIVERY = "total_with_delivery";
    private static final String KEY_DELIVERY_PRICE = "delivery_price";
    private static final String KEY_ORDER_NOTES = "order_notes";
    private static final String KEY_DELIVERY_NOTES = "delivery_notes";
    private static final String KEY_PAYMENT_DISPLAY = "payment_display";
    private static final String KEY_PRODUCTS = "products";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_Orders + "("
                + KEY_LOCAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ID + " INTEGER,"
//                + KEY_BUSINESS_ID + " TEXT,"
                + KEY_ACTION_TIME + " INTEGER,"
                + KEY_ORDER_TIME + " TEXT,"
//                + KEY_ADDED_BY + " TEXT,"
//                + KEY_IS_DELIVERY + " BOOLEAN,"
                + KEY_STATUS + " TEXT,"
//                + KEY_TOTAL_PAID + " INTEGER,"
//                + KEY_IS_PAID + " BOOLEAN,"
//                + KEY_POSITION + " INTEGER,"
                + KEY_CHANGE_TYPE + " TEXT,"
//                + KEY_TABLE_ID + " TEXT,"
                + KEY_DELIVERY_OPTION + " TEXT,"
                + KEY_COLOR + " TEXT,"
//                + KEY_COOKING_TIME + " INTEGER,"
//                + KEY_IS_CANCELED + " BOOLEAN,"
//                + KEY_IS_CHANGED + " BOOLEAN,"
                + KEY_IS_CHANGE_CONFIRMED + " BOOLEAN,"
//                + KEY_TABLE_IS_ACTIVE + " TEXT,"
                + KEY_CLIENT + " TEXT,"
                + KEY_START_TIME_STR + " TEXT,"

                + KEY_TOTAL_WITH_DELIVERY + " TEXT,"
                + KEY_DELIVERY_PRICE + " TEXT,"
                + KEY_ORDER_NOTES + " TEXT,"
                + KEY_DELIVERY_NOTES + " TEXT,"
                + KEY_PAYMENT_DISPLAY + " TEXT,"
                + KEY_ADDED_BY_SYSTEM + " TEXT,"
                + KEY_PRODUCTS + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Orders);
        // Create tables again
        onCreate(db);
    }

    // **** CRUD (Create, Read, Update, Delete) Operations ***** //
    // Adding new Order Details
    public void insertOrUpdateOrderDetails(OpenOrderModel orderModel, boolean isUpdate) {
        //Get the Data Repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        //Create a new map of values, where column names are the keys
        ContentValues cValues = new ContentValues();
        cValues.put(KEY_ID, orderModel.getId());
        cValues.put(KEY_ACTION_TIME, orderModel.getActionTime());
        cValues.put(KEY_ORDER_TIME, orderModel.getOrderTime());
        cValues.put(KEY_STATUS, orderModel.getStatus());
        cValues.put(KEY_CHANGE_TYPE, orderModel.getChangeType());
        cValues.put(KEY_DELIVERY_OPTION, orderModel.getDeliveryOption());
        cValues.put(KEY_COLOR, orderModel.getColor());
        cValues.put(KEY_IS_CHANGE_CONFIRMED, orderModel.isChangeConfirmed());
        cValues.put(KEY_START_TIME_STR, orderModel.getStartTimeStr());

        cValues.put(KEY_TOTAL_WITH_DELIVERY, orderModel.getTotalWithDelivery());
        cValues.put(KEY_DELIVERY_PRICE, orderModel.getDeliveryPrice());
        cValues.put(KEY_ORDER_NOTES, orderModel.getOrderNotes());
        cValues.put(KEY_DELIVERY_NOTES, orderModel.getDeliveryNotes());
        cValues.put(KEY_PAYMENT_DISPLAY, orderModel.getPaymentDisplay());
        cValues.put(KEY_ADDED_BY_SYSTEM, orderModel.getAddedBySystem());

        Gson gson = new Gson();
        try {
            JSONObject userInfo = new JSONObject(gson.toJson(orderModel.getClient()));
            cValues.put(KEY_CLIENT, userInfo.toString());

            JSONArray products = new JSONArray(gson.toJson(orderModel.getProducts()));
            cValues.put(KEY_PRODUCTS, products.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isUpdate)
            db.update(TABLE_Orders, cValues, KEY_ID + " = ?", new String[]{orderModel.getId()});
        else db.insert(TABLE_Orders, null, cValues);

        db.close();
    }

    // Get User Details
    public List<OrderModel> getAllOrdersFromDb() {
        List<OrderModel> orderModelList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_Orders;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            if (getDaysFromCreateOrder(cursor.getString(cursor.getColumnIndex(KEY_ORDER_TIME))) > 3) {
                deleteOrderFromDb(cursor.getString(cursor.getColumnIndex(KEY_ID)));
            } else {
                OrderModel orderModel = new OrderModel();
                orderModel.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                orderModel.setActionTime(cursor.getInt(cursor.getColumnIndex(KEY_ACTION_TIME)));
                orderModel.setOrderTime(cursor.getString(cursor.getColumnIndex(KEY_ORDER_TIME)));
                orderModel.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
                orderModel.setChangeType(cursor.getString(cursor.getColumnIndex(KEY_CHANGE_TYPE)));
                orderModel.setDeliveryOption(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_OPTION)));
                orderModel.setColor(cursor.getString(cursor.getColumnIndex(KEY_COLOR)));
                orderModel.setIsChangeConfirmed(cursor.getInt(cursor.getColumnIndex(KEY_IS_CHANGE_CONFIRMED)) == 1);
                orderModel.setStartTimeStr(cursor.getString(cursor.getColumnIndex(KEY_START_TIME_STR)));

                Gson gson = new Gson();
                ClientModel client = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_CLIENT)), ClientModel.class);
                orderModel.setClient(client);

                orderModelList.add(orderModel);
            }
        }
        cursor.close();
        return orderModelList;
    }

    private long getDaysFromCreateOrder(String orderTime) {
        Calendar calendarStart = Calendar.getInstance();
        SimpleDateFormat sdfIn = new SimpleDateFormat(PATTERN_DATE_FROM_SERVER);
        try {
            calendarStart.setTime(sdfIn.parse(orderTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return TimeUnit.MILLISECONDS.toDays(Calendar.DATE - calendarStart.getTimeInMillis());

    }

    //Get order details by id
    public OpenOrderModel getOrderByIdFromDb(String orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_Orders + " where " + KEY_ID + "=?", new String[]{orderId});
        OpenOrderModel orderModel = null;
        if (cursor.moveToNext()) {
            orderModel = new OpenOrderModel();
            orderModel.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
            orderModel.setActionTime(cursor.getString(cursor.getColumnIndex(KEY_ACTION_TIME)));
            orderModel.setOrderTime(cursor.getString(cursor.getColumnIndex(KEY_ORDER_TIME)));
            orderModel.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
            orderModel.setChangeType(cursor.getString(cursor.getColumnIndex(KEY_CHANGE_TYPE)));
            orderModel.setDeliveryOption(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_OPTION)));
            orderModel.setColor(cursor.getString(cursor.getColumnIndex(KEY_COLOR)));
            orderModel.setIsChangeConfirmed(cursor.getInt(cursor.getColumnIndex(KEY_IS_CHANGE_CONFIRMED)) == 1);
            orderModel.setStartTimeStr(cursor.getString(cursor.getColumnIndex(KEY_START_TIME_STR)));

            orderModel.setTotalWithDelivery(cursor.getString(cursor.getColumnIndex(KEY_TOTAL_WITH_DELIVERY)));
            orderModel.setDeliveryPrice(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_PRICE)));
            orderModel.setOrderNotes(cursor.getString(cursor.getColumnIndex(KEY_ORDER_NOTES)));
            orderModel.setDeliveryNotes(cursor.getString(cursor.getColumnIndex(KEY_DELIVERY_NOTES)));
            orderModel.setPaymentDisplay(cursor.getString(cursor.getColumnIndex(KEY_PAYMENT_DISPLAY)));
            orderModel.setAddedBySystem(cursor.getString(cursor.getColumnIndex(KEY_ADDED_BY_SYSTEM)));

            Gson gson = new Gson();
            ClientModel client = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_CLIENT)), ClientModel.class);
            orderModel.setClient(client);

            Type productsType = new TypeToken<ArrayList<ItemModel>>() {
            }.getType();
            List<ItemModel> products = gson.fromJson(cursor.getString(cursor.getColumnIndex(KEY_PRODUCTS)), productsType);
            orderModel.setProducts(products);

        }
        cursor.close();
        db.close();
        return orderModel;
    }

    // Delete Order Details
    public void deleteOrderFromDb(String orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Orders, KEY_ID + " = ?", new String[]{orderId});
        db.close();
    }

}