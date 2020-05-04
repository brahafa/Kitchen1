package com.dalpak.bringit.utils;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dalpak.bringit.models.BusinessModel;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;
import static com.dalpak.bringit.utils.SharedPrefs.getData;

public class Network {

    private final String SET_COOKIE_KEY = "Set-Cookie";
    private final String COOKIE_KEY = "Cookie";
    private final String SESSION_COOKIE = "PHPSESSID";

    private NetworkCallBack listener;
    private final String BASE_URL = "https://api.bringit.co.il/?apiCtrl=";
    private final String BUSINESS = "business&do=";
    private final String DALPAK = "dalpak&do=";
    private final String PIZZIRIA = "pizziria&do=";


    public enum RequestName {
        SIGN_UP, GET_LOGGED_MANAGER, lOAD_SAVED_USER_DETAILS,
        GET_ITEMS_IN_SELECTED_FOLEDER, WORKER_LOGIN, LOG_IN_MANAGER, GET_ALL_ORDERS,
        GET_ITEMS_SHOTR_CUT_FOLEDER, ADD_TO_CART, GET_ITEMS_BY_TYPE, GET_ORDER_DETAILS_BY_ID,
        GET_CART, CLEAR_CART, ORDER_CHANGE_POS, UPDATE_ORDER_STATUS, LOAD_BUSINES_ITEMS, UPDATE_ITEM_PRICE, GET_ORDER_CODE,
        WORKER_LOGOUT
    }

    Network(NetworkCallBack listener) {
        this.listener = listener;
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
            case GET_ORDER_CODE:
                url += BUSINESS + "getOrderCode" + "&order_id=" + param1;

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
        JsonObjectRequest jsonArrayRequest =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        response -> {
                            Log.d("Request url  11  ", url);
                            Log.d(TAG, "onResponse  :   " + response.toString());
                            listener.onDataDone(response);
                        }, error -> {
                    try {
                        if (error.networkResponse != null)
                            listener.onDataError(new JSONObject(new String(error.networkResponse.data)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "Connection Error 22" + error.toString());
                }) {

                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        checkSessionCookie(response.headers);
                        return super.parseNetworkResponse(response);
                    }

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<String, String>();
                        if (!getData(Constants.TOKEN_PREF).equals("")) {
                            params.put(SESSION_COOKIE, getData(Constants.TOKEN_PREF));
                            Log.d(TAG, "token is: " + getData(Constants.TOKEN_PREF));

                            addSessionCookie(params);
                        }
                        return params;
                    }
                };
        RequestQueueSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
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
            case WORKER_LOGIN:
                url += DALPAK + "workerLogin";
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
                break;
            case UPDATE_ITEM_PRICE:
                url += BUSINESS + "updateItemPrice";
                break;
            case WORKER_LOGOUT:
                url += DALPAK + "workerLogout";


        }
        Log.d("POST url  ", url);
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, params,
                response -> {
                    try {
                        listener.onDataDone(response);
                        VolleyLog.v("Response:%n %s", response.toString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ERROR POST REQUEST", e.toString());
                    }
                }, error -> {
            VolleyLog.e("Error  11: ", error.getMessage());
            manageErrors(error, context);
            //                try {
            //
            //                   listener.onDataError(new JSONObject(new String(error.networkResponse.data)));
            //                } catch (JSONException e) {
            //                    e.printStackTrace();
            //                }

        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                checkSessionCookie(response.headers);
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                if (!getData(Constants.TOKEN_PREF).equals("")) {
                    params.put(SESSION_COOKIE, getData(Constants.TOKEN_PREF));// BusinessModel.getInstance().getUtoken());
                    addSessionCookie(params);
                }
                return params;
            }
        };
        RequestQueueSingleton.getInstance(context).addToRequestQueue(req);
    }

    private void manageErrors(VolleyError error, Context context) {
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Utils.openAlertDialog(context, "בדוק חיבור לאינטרנט", "");

        } else if (error instanceof ParseError) {
           // Toast.makeText(context, ("ParseError"), Toast.LENGTH_SHORT).show();
            error.printStackTrace();
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

    private void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
            String cookie = headers.get(SET_COOKIE_KEY);
            if (cookie.length() > 0) {
                String[] splitCookie = cookie.split(";");
                String[] splitSessionId = splitCookie[0].split("=");
                cookie = splitSessionId[1];
                SharedPrefs.saveData(SESSION_COOKIE, cookie);
            }
        }
    }

    private void addSessionCookie(Map<String, String> headers) {
        String sessionId = getData(Constants.TOKEN_PREF);
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            headers.put(COOKIE_KEY, builder.toString());
        }
    }


    public interface NetworkCallBack {
        void onDataDone(JSONObject json);

        void onDataError(JSONObject json);
    }
}
