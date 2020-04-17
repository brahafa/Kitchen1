package com.dalpak.bringit.utils;

import android.content.Context;
import android.util.Log;

import com.dalpak.bringit.models.BusinessModel;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.StockModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.dalpak.bringit.utils.SharedPrefs.saveData;

public class Request {

    private static Request sRequest;

    public static Request getInstance() {
        if (sRequest == null) {
            sRequest = new Request();
        }
        return sRequest;
    }

    public void logIn(final Context context, String password, String email, final RequestCallBackSuccess listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password);
            jsonObject.put("email", email);

            Log.d("send data: ", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("response: ", json.toString());
                try {
                    BusinessModel.getInstance().initData(json);
                    saveData(Constants.TOKEN_PREF, json.getString("utoken"));
                    saveData(Constants.LOG_IN_JSON_PREF, json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (json.has("errorCode") && json.getInt("errorCode") == 1) {
                        listener.onDataDone(false);
                    } else {
                        listener.onDataDone(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onDataError(JSONObject json) {
                openAlertMsg(context, json);
            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.LOG_IN_MANAGER);
    }

    public void settingsLogin(final Context context, String password, final RequestCallBackSuccess listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password);
            jsonObject.put("phone", "0501112222");
            Log.d("send data: ", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                try {
                    if (json.has("status") && json.getBoolean("status")) {
                        if (json.getJSONObject("user").has("name")) {
                            saveData(Constants.NAME_PREF, (json.getJSONObject("user")).getString("name"));
                        }
                        listener.onDataDone(true);
                    } else {
                        listener.onDataDone(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("GET_ALL_ORDERS", json.toString());
            }

            @Override
            public void onDataError(JSONObject json) {
                openAlertMsg(context, json);
            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.SETINGS_LOGIN);
    }

    public void getAllOrders(final Context context, final RequestJsonCallBack listener) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                listener.onDataDone(json);
            }

            @Override
            public void onDataError(JSONObject json) {
                //{"message":"לא נמצאו הזמנות חדשות","errorCode":1,"status":false}
                listener.onDataDone(json);
                try {
                    if (json.has("errorCode") && json.getInt("errorCode") == 1) {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // openAlertMsg(context, json);
            }
        });
        network.sendRequest(context, Network.RequestName.GET_ALL_ORDERS, Integer.toString(BusinessModel.getInstance().getBusiness_id()));
    }

    public void updateItemPrice(Context context, StockModel stockModel, final RequestJsonCallBack listener) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        try {
            // jsonObject.put("object_status", !stockModel.isObject_status());
            jsonObject.put("object_status", !stockModel.isObject_status());
            jsonObject.put("id", stockModel.getId());
            jsonObject.put("object_id", stockModel.getObject_id());
            jsonObject.put("object_type", stockModel.getObject_type());
            jsonObject.put(stockModel.getObject_type() + "_id", stockModel.getObject_id());

            // jsonObject.put("object_type",stockModel.getObject_type());
            jsonArray.put((jsonObject));
            jsonObject1.put("items", jsonArray);
            jsonObject1.put("business_id", BusinessModel.getInstance().getBusiness_id());
            Log.d("send data: ", jsonObject1.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("UPDATE_ITEM_PRICE", json.toString());
                listener.onDataDone(json);
            }

            @Override
            public void onDataError(JSONObject json) {
                openAlertMsg(context, json);
            }
        });
        network.sendPostRequest(context, jsonObject1, Network.RequestName.UPDATE_ITEM_PRICE);
    }

    public void updateOrderStatus(Context context, long order_id, String status, final RequestJsonCallBack listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", order_id);
            jsonObject.put("oStatus", status);
            jsonObject.put("business_id", BusinessModel.getInstance().getBusiness_id());

            Log.d("send data: ", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("UPDATE_ORDER_STATUS", jsonObject.toString());
            }

            @Override
            public void onDataError(JSONObject json) {
                openAlertMsg(context, json);
            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.UPDATE_ORDER_STATUS);
    }

    public void getOrderDetailsByID(Context context, String orderId, RequestJsonCallBack requestJsonCallBack) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("getOrderDetailsByID", json.toString());
                requestJsonCallBack.onDataDone(json);
//               try {
//                  // requestItemsListCallBack.onDataDone(getListGlobalFromJsonArr(json.getJSONObject("Message")));
//               } catch (JSONException e) {
//                   e.printStackTrace();
//               }
            }

            @Override
            public void onDataError(JSONObject json) {

            }
        });
        network.sendRequest(context, Network.RequestName.GET_ORDER_DETAILS_BY_ID, orderId);
    }

    public void loadBusinessItems(Context context, String type, RequestJsonCallBack requestJsonCallBack) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("getOrderDetailsByID", json.toString());
                requestJsonCallBack.onDataDone(json);

            }

            @Override
            public void onDataError(JSONObject json) {

            }
        });
        network.sendRequest(context, Network.RequestName.LOAD_BUSINES_ITEMS, type);
    }

    public void getOrderCode(Context context, String orderId, RequestJsonCallBack requestJsonCallBack) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("getOrderCode ", json.toString());
                requestJsonCallBack.onDataDone(json);
//               try {
//                  // requestItemsListCallBack.onDataDone(getListGlobalFromJsonArr(json.getJSONObject("Message")));
//               } catch (JSONException e) {
//                   e.printStackTrace();
//               }
            }

            @Override
            public void onDataError(JSONObject json) {

            }
        });
        network.sendRequest(context, Network.RequestName.GET_ORDER_CODE, orderId);
    }

    public void orderChangePos(Context context, long order_id, int oldPosition, int newPosition, boolean statusChanged, String draggedFromStatus, String draggedToStatus, final RequestJsonCallBack listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", order_id);
            jsonObject.put("oldPosition", oldPosition);
            jsonObject.put("position", newPosition);
            jsonObject.put("statusChanged", statusChanged);
            jsonObject.put("prevStatus", draggedFromStatus);
            jsonObject.put("status", draggedToStatus);
            Log.d("send data: ", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("orderChangePos", jsonObject.toString());
            }

            @Override
            public void onDataError(JSONObject json) {
                openAlertMsg(context, json);
            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.ORDER_CHANGE_POS);
    }

    public void getItemsInSelectedFolder(Context context, String param, Network.RequestName getItemsInSelectedFoleder, final RequestJsonCallBack listener) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("getSelectedFolder", json.toString());
                listener.onDataDone(json);
            }

            @Override
            public void onDataError(JSONObject json) {
                Log.d("getSelectedFolder", json.toString());
            }
        });
        network.sendRequest(context, getItemsInSelectedFoleder, param);
    }

    public void getItemsByType(Context context, String type) {
        final Gson gson = new Gson();
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("GET_ITEMS_BY_TYPE", json.toString());
                try {
                    List<ItemModel> itemModels = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        itemModels.add(gson.fromJson(jsonArray.getString(i), ItemModel.class));
                    }
                    BusinessModel.getInstance().setDrinkList(itemModels);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ;
            }

            @Override
            public void onDataError(JSONObject json) {

            }
        });
        network.sendRequest(context, Network.RequestName.GET_ITEMS_BY_TYPE, type);
    }

    public void addToCart(final Context context, ItemModel itemModel, final RequestItemsListCallBack listener) {
        JSONObject params = new JSONObject();
        try {
            params.put("business_id", 1);
            params.put("type", itemModel.getObject_type());
            params.put("o_id", itemModel.getObject_id());
            params.put("addBy", "dalpak");
            //   getParamsFillings(params);
            if (!itemModel.getFather_id().equals("")) {
                params.put("f_id", itemModel.getFather_id());
            }
            Log.d("addDrink", params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("ADD_TO_CART ", json.toString());
                getCart(context, listener);
            }

            @Override
            public void onDataError(JSONObject json) {
                Log.d("ADD_TO_CART_ERR", json.toString());
            }
        });
        network.sendPostRequest(context, params, Network.RequestName.ADD_TO_CART);
    }

    private void getCart(Context context, final RequestItemsListCallBack listener) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                try {
                    listener.onDataDone(getListGlobalFromJsonArr(json.getJSONObject("Message")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataError(JSONObject json) {

            }
        });
        network.sendRequest(context, Network.RequestName.GET_CART);
    }

    public void clearCart(Context context, final RequestCallBack callBack) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                callBack.onDataDone();
            }

            @Override
            public void onDataError(JSONObject json) {

            }
        });
        network.sendRequest(context, Network.RequestName.CLEAR_CART);
    }

    public List<ItemModel> getListGlobalFromJsonArr(JSONObject object) {
        List<ItemModel> globalObjList = new ArrayList<>();
        Gson gson = new Gson();
        try {
            if (object == null) {
                return null;
            }
            Iterator x = object.keys();
            //  if(x.next()!="Message")
            while (x.hasNext()) {
                String key = (String) x.next();
                globalObjList.add(gson.fromJson(String.valueOf(object.get(key)), ItemModel.class));
            }
        } catch (JSONException e) {
            Log.e("GET CART ERROR", e.toString());
            e.printStackTrace();
        }
        return globalObjList;
    }

    private void openAlertMsg(Context context, JSONObject json) {
        try {
            Utils.openAlertDialog(context, json.getString("message"), "");
            Log.d("response failed: ", json.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface RequestCallBack {
        void onDataDone();
    }

    public interface RequestCallBackSuccess {
        void onDataDone(boolean isDataSuccess);
    }

    public interface RequestJsonCallBack {
        void onDataDone(JSONObject jsonObject);
    }

    public interface RequestItemsListCallBack {
        void onDataDone(List<ItemModel> itemModels);
    }
}
