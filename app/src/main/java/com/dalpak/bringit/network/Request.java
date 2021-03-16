package com.dalpak.bringit.network;

import android.content.Context;
import android.util.Log;

import com.dalpak.bringit.models.AllOrdersResponse;
import com.dalpak.bringit.models.BusinessModel;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.models.ProductItemModel;
import com.dalpak.bringit.models.WorkerResponse;
import com.dalpak.bringit.utils.Constants;
import com.dalpak.bringit.utils.SharedPrefs;
import com.dalpak.bringit.utils.Utils;
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
                    Gson gson = new Gson();
                    BusinessModel businessModel = gson.fromJson(json.getJSONObject("message").toString(), BusinessModel.class);
                    BusinessModel.getInstance().initData(businessModel);

                    SharedPrefs.saveData(Constants.TOKEN_PREF, json.getString("utoken"));
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

    public void settingsLogin(final Context context, String password, final RequestWorkerCallBack listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("password", password);
            Log.d("send data: ", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Gson gson = new Gson();
                WorkerResponse response = gson.fromJson(json.toString(), WorkerResponse.class);
                listener.onDataDone(response);
                Log.d("settingsLogin", json.toString());
            }

            @Override
            public void onDataError(JSONObject json) {
                Log.e("settingsLogin error", json.toString());
                openAlertMsg(context, json);
                listener.onDataDone(new WorkerResponse());
            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.WORKER_LOGIN);
    }


    public void workerLogout(final Context context) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("GET_ALL_ORDERS", json.toString());
            }

            @Override
            public void onDataError(JSONObject json) {

            }
        });
        network.sendPostRequest(context, null, Network.RequestName.WORKER_LOGOUT);
    }

    public void getAllOrders(final Context context, final RequestAllOrdersCallBack listener) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                if (json == null) {
                    listener.onDataDone(new AllOrdersResponse());
                } else {
                    Gson gson = new Gson();
                    AllOrdersResponse response = gson.fromJson(json.toString(), AllOrdersResponse.class);
                    listener.onDataDone(response);
                }
            }

            @Override
            public void onDataError(JSONObject json) {
                //{"message":"לא נמצאו הזמנות חדשות","errorCode":1,"status":false}
                listener.onDataDone(null);
            }
        });
        network.sendRequest(context, Network.RequestName.GET_ALL_ORDERS, "14", true);
    }

    public void updateProductStatus(Context context, ProductItemModel itemModel, final RequestCallBackSuccess listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", itemModel.getId());
            jsonObject.put("status", 1 ^ Integer.parseInt(itemModel.getInInventory()));
            Log.d("send data: ", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("update status", json.toString());
                listener.onDataDone(true);
            }

            @Override
            public void onDataError(JSONObject json) {
                openAlertMsg(context, json);
            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.UPDATE_PRODUCT_STATUS, true);
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
                listener.onDataDone(json);
                Log.d("updateOrderStatus", json.toString());
            }

            @Override
            public void onDataError(JSONObject json) {
                openAlertMsg(context, json);
            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.UPDATE_ORDER_STATUS);
    }

    public void getOrderDetailsByID(Context context, String orderId, RequestProductsCallBack listener) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Gson gson = new Gson();
                OpenOrderModel response = null;
                try {
                    response = gson.fromJson(json.getString("order"), OpenOrderModel.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listener.onDataDone(response);
            }

            @Override
            public void onDataError(JSONObject json) {
                listener.onDataDone(null);
                Log.d("ERROR OrderDetailsByID", json.toString());
            }
        });
        network.sendRequest(context, Network.RequestName.GET_ORDER_DETAILS_BY_ID, orderId, true);
    }

    public void loadBusinessItems(Context context, String type, RequestJsonCallBack requestJsonCallBack) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("load products " + type, json.toString());
                requestJsonCallBack.onDataDone(json);

            }

            @Override
            public void onDataError(JSONObject json) {

            }
        });
        network.sendRequest(context, Network.RequestName.LOAD_PRODUCTS, type, true);
    }

    public void getOrderCode(Context context, String orderId, RequestJsonCallBack requestJsonCallBack) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("getOrderCode", json.toString());
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
        network.sendRequest(context, Network.RequestName.GET_ORDER_CODE, orderId, true);
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
                Log.d("orderChangePos", json.toString());
                listener.onDataDone(json);
            }

            @Override
            public void onDataError(JSONObject json) {
                openAlertMsg(context, json);
            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.ORDER_CHANGE_POS);
    }

    public void checkBusinessStatus(Context context, RequestCallBackSuccess requestCallBackSuccess) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("checkBusinessStatus ", json.toString());
                try {
                    requestCallBackSuccess.onDataDone(json.getBoolean("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDataError(JSONObject json) {
                Log.d("getLoggedManager Err", json.toString());
            }
        });
        network.sendRequest(context, Network.RequestName.CHECK_BUSINESS_STATUS, null);
    }

    public void changeBusinessStatus(Context context, boolean isOpen, final RequestCallBackSuccess listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("business_id", BusinessModel.getInstance().getBusiness_id());
            jsonObject.put("status", isOpen ? "open" : "close");
            Log.d("send data: ", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("changeBusinessStatus ", json.toString());
                listener.onDataDone(true);
            }

            @Override
            public void onDataError(JSONObject json) {
                Log.e("changeStatus Err", json.toString());
            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.CHANGE_BUSINESS_STATUS);
    }

    public void approveOrderChanges(Context context, String orderId, final RequestCallBackSuccess listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("business_id", BusinessModel.getInstance().getBusiness_id());
            jsonObject.put("order_id", orderId);
            Log.d("send data: ", jsonObject.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                Log.d("approveOrderChanges ", json.toString());
                listener.onDataDone(true);
            }

            @Override
            public void onDataError(JSONObject json) {
                Log.e("approveOrderChanges Err", json.toString());
            }
        });
        network.sendPostRequest(context, jsonObject, Network.RequestName.APPROVE_ORDER_CHANGES, true);
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
//            params.put("type", itemModel.getObject_type());
//            params.put("o_id", itemModel.getObject_id());
            params.put("addBy", "dalpak");
            //   getParamsFillings(params);
//            if (!itemModel.getFather_id().equals("")) {
//                params.put("f_id", itemModel.getFather_id());
//            }
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

    public void checkToken(Context context, final RequestCallBackSuccess listener) {
        Network network = new Network(new Network.NetworkCallBack() {
            @Override
            public void onDataDone(JSONObject json) {
                try {
                    Gson gson = new Gson();
                    BusinessModel businessModel = gson.fromJson(json.getJSONObject("user").toString(), BusinessModel.class);
                    BusinessModel.getInstance().initData(businessModel);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listener.onDataDone(true);
            }

            @Override
            public void onDataError(JSONObject json) {
                listener.onDataDone(false);
            }
        });
        network.sendRequest(context, Network.RequestName.GET_LOGGED_MANAGER, "");
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

    public interface RequestProductsCallBack {
        void onDataDone(OpenOrderModel response);
    }

    public interface RequestAllOrdersCallBack {
        void onDataDone(AllOrdersResponse response);
    }

    public interface RequestJsonCallBack {
        void onDataDone(JSONObject jsonObject);
    }

    public interface RequestItemsListCallBack {
        void onDataDone(List<ItemModel> itemModels);
    }

    public interface RequestWorkerCallBack {
        void onDataDone(WorkerResponse response);
    }
}
