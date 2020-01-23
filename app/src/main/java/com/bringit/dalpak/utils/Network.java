package com.bringit.dalpak.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bringit.dalpak.models.BusinessModel;


import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class Network {
    ProgressDialog pDialog;

    private RequestQueue queue;
    private NetworkCallBack listener;
    public static String BASE_URL = "https://api.bringit.co.il/?apiCtrl=";
    public static String BUSINESS = "business&do=";
    public static String DALPAK = "dalpak&do=";
    public static String PIZZIRIA = "pizziria&do=";


    public enum RequestName {
        SIGN_UP, GET_LOGGED_MANAGER, lOAD_SAVED_USER_DETAILS,
        GET_ITEMS_IN_SELECTED_FOLEDER, SETINGS_LOGIN, LOG_IN_MANAGER, GET_ALL_ORDERS,
        GET_ITEMS_SHOTR_CUT_FOLEDER, ADD_TO_CART, GET_ITEMS_BY_TYPE, GET_ORDER_DETAILS_BY_ID,
        GET_CART, CLEAR_CART, ORDER_CHANGE_POS, UPDATE_ORDER_STATUS, LOAD_BUSINES_ITEMS
    }

    ;

    Network(NetworkCallBack listener, Context context) {
        this.listener = listener;
        queue = Volley.newRequestQueue(context);
        pDialog = new ProgressDialog(context);
    }


    public void sendRequest(final Context context, final RequestName requestName, String param1) {
        String url = BASE_URL;
        switch (requestName) {
            case GET_LOGGED_MANAGER:
                url += BUSINESS + "getLoggedManager";
                break;
            case GET_ALL_ORDERS:
                url += BUSINESS + "getAllOrders&business_id=" + param1;
                break;
            case GET_ITEMS_SHOTR_CUT_FOLEDER:
                url += DALPAK + "getItemsInSelectedFolder&fav=1";
                break;
            case GET_ITEMS_IN_SELECTED_FOLEDER:
                url += DALPAK + "getItemsInSelectedFolder&folder=" + param1;
                break;
            case GET_ITEMS_BY_TYPE:
                url += DALPAK + "getItemsByType&type=" + param1 + "&linked=2";
                break;
            case GET_ORDER_DETAILS_BY_ID:

                url += BUSINESS + "getOrderDetailsByID&order_id=" + param1 + "&business_id=" + BusinessModel.getInstance().getBusiness_id();
                break;
            case LOAD_BUSINES_ITEMS:
                url += BUSINESS + "loadBusinessItems&type=" + param1 + "&business_id=" + BusinessModel.getInstance().getBusiness_id();
                break;

        }
        sendRequestObject(requestName, url, context, listener);
    }

    public void sendRequest(final Context context, final RequestName requestName) {
        String url = BASE_URL;
        switch (requestName) {
            case SIGN_UP:
                url += BUSINESS + "signup";
                break;
            case GET_CART:
                url += PIZZIRIA + "getCart";
                break;
            case CLEAR_CART:
                url += PIZZIRIA + "clearCart";

        }
        Log.d("Request url  ", url);
        sendRequestObject(requestName, url, context, listener);
    }

    private void sendRequestObject(final RequestName requestName, final String url, final Context context, final NetworkCallBack listener) {
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Request url  11  ", url);
                Log.e(TAG, "onResponse  :   " + response.toString());
                listener.onDataDone(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if (error.networkResponse != null)
                        listener.onDataError(new JSONObject(new String(error.networkResponse.data)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "Connection Error 22" + error.toString());
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                MyApp.get().checkSessionCookie(response.headers);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if (SharePref.getInstance(context).getData(Constants.TOKEN_PREF) != null) {
                    params.put("PHPSESSID", SharePref.getInstance(context).getData(Constants.TOKEN_PREF));
                    MyApp.get().addSessionCookie(params);
                }
                return params;
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void sendPostRequest(final Context context, final JSONObject params, final RequestName requestName) {
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        String url = BASE_URL;
        switch (requestName) {
            case SIGN_UP:
                url += BUSINESS + "signup";
                break;
            case lOAD_SAVED_USER_DETAILS:
                url += BUSINESS + "loadSavedUserDetails";
                break;
            case SETINGS_LOGIN:
                url += DALPAK + "settingsLogin";
                break;
            case LOG_IN_MANAGER:
                url += BUSINESS + "loginManager";
                break;
            case ADD_TO_CART:
                url += PIZZIRIA + "addToCart";
                break;
            case ORDER_CHANGE_POS:
                url += BUSINESS + "orderChangePos";
                break;
            case UPDATE_ORDER_STATUS:
                url += BUSINESS + "updateOrderStatus";


        }
        Log.d("POST url  ", url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            listener.onDataDone(response);

                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ERROR ROST REQUEST", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error  11: ", error.getMessage());
                manageErrors(error, context);
//                try {
//
//                   listener.onDataError(new JSONObject(new String(error.networkResponse.data)));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                MyApp.get().checkSessionCookie(response.headers);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if (SharePref.getInstance(context).getData(Constants.TOKEN_PREF) != null) {
                    params.put("PHPSESSID", SharePref.getInstance(context).getData(Constants.TOKEN_PREF));// BusinessModel.getInstance().getUtoken());
                    MyApp.get().addSessionCookie(params);
                }
                return params;
            }
        };
        queue.add(req);
        //   queue.start();
    }

    private void manageErrors(VolleyError error, Context context) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Utils.openAlertDialog(context, "בדוק חיבור לאינטרנט", "");

        } else if (error instanceof ParseError) {
            Toast.makeText(context, ("ParseError"), Toast.LENGTH_SHORT).show();
        } else {
            manageMsg(error, context);
        }
    }

    private void manageMsg(VolleyError error, Context context) {
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null && networkResponse.data != null) {
            try {
                JSONArray jsonArray = null;
                JSONObject jsonError = new JSONObject(new String(networkResponse.data));
                if (networkResponse.statusCode == HttpStatus.SC_FORBIDDEN) {
                    // HTTP Status Code: 403 Unauthorized
                    listener.onDataDone(jsonError);
                   Log.e("network error!!!", jsonError.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public static interface NetworkCallBack {
        void onDataDone(JSONObject json);

        void onDataError(JSONObject json);
    }
}
