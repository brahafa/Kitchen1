package com.dalpak.bringit.fragments;

import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dalpak.bringit.MainActivity;
import com.dalpak.bringit.R;
import com.dalpak.bringit.adapters.OrderAdapter;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.models.OrderCategoryModel;
import com.dalpak.bringit.models.OrderModel;
import com.dalpak.bringit.utils.Constants;
import com.dalpak.bringit.utils.Request;
import com.dalpak.bringit.utils.Utils;
import com.google.gson.Gson;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.ColumnProperties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.dalpak.bringit.utils.Utils.CHANGE_TYPE_CANCELED;
import static com.dalpak.bringit.utils.Utils.CHANGE_TYPE_CHANGE;
import static com.dalpak.bringit.utils.Utils.createNotificationChannel;

public class MainFragment extends Fragment {

    private final int REQUEST_REPEAT_INTERVAL = 10 * 1000;
    private final long DELAY_TIME_IN_SECONDS = 20;

    private BoardView mBoardView;
    private int mColumns;

    private Gson gson;
    private final Handler mHandler = new Handler();

    private MediaPlayer mp;

    private String lastResponse = "";
    private int lastNewOrdersSize = 0;
    private int lastCookingOrdersSize = 0;

    private Runnable mRunnable = () -> Request.getInstance().getAllOrders(getActivity(),
            jsonObject -> {
                if (mp != null) mp.stop();
                if (hasDelay(jsonObject)) mp = playSound(Constants.ALERT_ORDER_OVERTIME);

                if (!jsonObject.toString().equals(lastResponse)) {
                    checkIfOrderHasBeenUpdated(lastResponse, jsonObject);
                    lastResponse = jsonObject.toString();
                    updateAllRV(jsonObject);
                }
                setupBoardUpdates();
            });

    private void checkIfOrderHasBeenUpdated(String lastResponse, JSONObject jsonObject) {
        try {
            JSONArray lastJsonArray = (new JSONObject(lastResponse)).getJSONArray("orders");
            JSONArray currentJsonArray = jsonObject.getJSONArray("orders");
            JSONObject lastJsonObj, currentJsonObj;
            for (int i = 0; i < lastJsonArray.length(); i++) {
                for (int j = 0; j < currentJsonArray.length(); j++) {
                    lastJsonObj = lastJsonArray.getJSONObject(i);
                    currentJsonObj = currentJsonArray.getJSONObject(j);
                    if (lastJsonObj.getString("id").equals(currentJsonObj.get("change_for_order_id")) && !lastJsonObj.getString("change_type").equals(currentJsonObj.getString("change_type"))) {
                        String msg;
                        if (lastJsonArray.getJSONObject(i) == null || !lastJsonArray.getJSONObject(i).has("client")) {
                            msg = "יש עדכון בהזמנות";
                        } else {
                            msg = " הזמנה של " + lastJsonArray.getJSONObject(i).getJSONObject("client").getString("f_name") + " עודכנה";
                        }
                        playSound(Constants.ALERT_EDIT_ORDER);
                        createNotificationChannel(Objects.requireNonNull(getContext()), msg);
                        if (((MainActivity) Objects.requireNonNull(getActivity())).dialogOpenOrder != null &&
                                ((MainActivity) Objects.requireNonNull(getActivity())).dialogOpenOrder.isShowing() &&
                                (((MainActivity) getActivity()).dialogOpenOrder).orderModel.getId().equals(lastJsonObj.getString("id"))) {
                            updateOrderDetailsDialog(currentJsonObj.getString("id"));
                        }

                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateOrderDetailsDialog(String id) {
        Request.getInstance().getOrderDetailsByID(getActivity(), id, jsonObject -> {
            try {
                OpenOrderModel openOrderModel = gson.fromJson(jsonObject.getString("order"), OpenOrderModel.class);
                prepareOrder(openOrderModel);
                openOrderModel.setTotal(Utils.getTotalOrder(openOrderModel) + "");
                ((MainActivity) Objects.requireNonNull(getActivity())).dialogOpenOrder.editDialog(openOrderModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void prepareOrder(OpenOrderModel openOrderModel) {
        for (int i = 0; i < openOrderModel.getProducts().size(); i++) {
            List<OrderCategoryModel> categoryModels = openOrderModel.getProducts().get(i).getCategories();
            for (int j = 0; j < categoryModels.size(); j++) {
                if (categoryModels.get(j).getCategoryHasFixedPrice()) {
                    sortToppingsRec(categoryModels.get(j).getProducts());
                    //if productFixPrice = 0 it is mean that all the product will get the fix price
                    int productsFixPrice = categoryModels.get(j).getProductsFixedPrice() == 0 ? 100000 : categoryModels.get(j).getProductsFixedPrice();
                    for (int k = 0; k < categoryModels.get(j).getProducts().size(); k++) {
                        if (productsFixPrice > 0) {
                            categoryModels.get(j).getProducts().get(k).setPrice(categoryModels.get(j).getFixedPrice() + "");
                            productsFixPrice--;
                        } else {
                            return;
                        }

                    }
                }
            }
        }
    }

    private static void sortToppingsRec(List<ItemModel> toppingModels) {
        Collections.sort(toppingModels, (u1, u2) -> (Integer.parseInt(u1.getPrice()) - Integer.parseInt(u2.getPrice())));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        gson = new Gson();

        String[] statuses = getResources().getStringArray(R.array.statuses);

        mBoardView = view.findViewById(R.id.board_view);
        mBoardView.setSnapToColumnsWhenScrolling(true);
        mBoardView.setSnapToColumnWhenDragging(true);
        mBoardView.setSnapDragItemToTouch(true);
        mBoardView.setSnapToColumnInLandscape(false);
        mBoardView.setColumnSnapPosition(BoardView.ColumnSnapPosition.CENTER);
        mBoardView.setBoardListener(new BoardView.BoardListenerAdapter() {

            @Override
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
//                setupBoardUpdates();
                if (fromColumn != toColumn) {
                    changeStatus(
                            mBoardView.getAdapter(toColumn).getUniqueItemId(toRow),
                            statuses[toColumn]);
                    changePosition(
                            mBoardView.getAdapter(toColumn).getUniqueItemId(toRow),
                            fromRow + 1,
                            toRow + 1,
                            true,
                            statuses[fromColumn],
                            statuses[toColumn]);
                    if (fromColumn == 2 && toColumn == 1) lastCookingOrdersSize--;
                } else if (fromRow != toRow) {
                    changePosition(
                            mBoardView.getAdapter(toColumn).getUniqueItemId(toRow),
                            fromRow + 1,
                            toRow + 1,
                            true,
                            statuses[fromColumn],
                            statuses[toColumn]);
                }
            }

            @Override
            public void onItemDragStarted(int column, int row) {
                removeBoardUpdates();
            }

        });

//        setupBoardUpdates();
//        mRunnable.run();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAllRV();
    }

    public void initAllRV() {
        initRV(new ArrayList<>());
        initRV(new ArrayList<>());
        initRV(new ArrayList<>());
        initRV(new ArrayList<>());
        initRV(new ArrayList<>());
    }

    private void updateAllRV(JSONObject jsonObject) {

        try {
            if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                mBoardView.clearBoard();
                mColumns = 0;
                initRV(getOrdersList(jsonObject, "sent"));
                initRV(getOrdersList(jsonObject, "packing"));
                initRV(getOrdersList(jsonObject, "cooking"));
                initRV(getOrdersList(jsonObject, "preparing"));
                initRV(getOrdersList(jsonObject, "received"));

                int newOrdersSize = getOrdersList(jsonObject, "received").size();
                if (lastNewOrdersSize < newOrdersSize) playSound(Constants.ALERT_NEW_ORDER);

                if (isEdited(jsonObject)) playSound(Constants.ALERT_EDIT_ORDER);

                int cookingOrdersSize = getOrdersList(jsonObject, "cooking").size();
                if (lastCookingOrdersSize > cookingOrdersSize)
                    playSound(Constants.ALERT_FINISH_COOKING);

                lastNewOrdersSize = newOrdersSize;
                lastCookingOrdersSize = cookingOrdersSize;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startBoardUpdates() {
        mRunnable.run();
    }

    private void setupBoardUpdates() {
        mHandler.postDelayed(mRunnable, REQUEST_REPEAT_INTERVAL);
    }

    private void removeBoardUpdates() {
        mHandler.removeCallbacks(mRunnable);
    }

    private List<OrderModel> getOrdersList(JSONObject jsonObject, String orderStatus) {
        Gson gson = new Gson();
        List<OrderModel> orderModels = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            if (jsonObject.getJSONObject("ordersByStatus").has(orderStatus))
                jsonArray = jsonObject.getJSONObject("ordersByStatus").getJSONArray(orderStatus);
            for (int i = 0; i < jsonArray.length(); i++) {
                if (!((gson.fromJson(jsonArray.getString(i), OrderModel.class)).getChangeType().equals(CHANGE_TYPE_CANCELED)))
                    orderModels.add(gson.fromJson(jsonArray.getString(i), OrderModel.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderModels;
    }

    private void changeStatus(long order_id, String draggedToStr) {
        Request.getInstance().updateOrderStatus(getActivity(), order_id, draggedToStr, jsonObject -> {
        });
    }

    private void changePosition(long order_id, int oldPos, int newPos, boolean statusChanged, String draggedFromStr, String draggedToStr) {
        Request.getInstance().orderChangePos(getActivity(), order_id, oldPos, newPos, statusChanged, draggedFromStr, draggedToStr, jsonObject -> {
            startBoardUpdates();
        });
    }

    private void initRV(final List<OrderModel> orderModels) {

        OrderAdapter bottomListAdapter = new OrderAdapter(orderModels, orderModel ->
                Request.getInstance().getOrderDetailsByID(getActivity(), orderModel.getId(), jsonObject -> {
                    try {
                        OpenOrderModel openOrderModel = gson.fromJson(jsonObject.getString("order"), OpenOrderModel.class);
                        prepareOrder(openOrderModel);
                        openOrderModel.setTotal(Utils.getTotalOrder(openOrderModel) + "");
                        ((MainActivity) getActivity()).openOrderDialog(openOrderModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }));


        final View header = View.inflate(getActivity(), R.layout.column_header, null);

        String[] sections = getResources().getStringArray(R.array.sections_iw);

        ((TextView) header.findViewById(R.id.tv_column_title)).setText(sections[mColumns]);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ColumnProperties columnProperties = ColumnProperties.Builder.newBuilder(bottomListAdapter)
                .setLayoutManager(layoutManager)
                .setHasFixedItemSize(true)
                .setColumnBackgroundColor(Color.TRANSPARENT)
                .setItemsSectionBackgroundColor(Color.TRANSPARENT)
                .setHeader(header)
                .build();

        int columnWidth = countColumnWidth();

        mBoardView.setColumnWidth(columnWidth);
        mBoardView.setColumnSpacing(10);
        mBoardView.setBoardEdge(10);
        mBoardView.addColumn(columnProperties);
        mColumns++;
    }

    @Override
    public void onDestroyView() {
        removeBoardUpdates();
        if (mp != null) mp.release();
        super.onDestroyView();
    }

    private int countColumnWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return (width - 60) / 5;
    }


    private MediaPlayer playSound(int alertType) {
        switch (alertType) {
            default:
            case Constants.ALERT_NEW_ORDER:
                MediaPlayer mp1 = MediaPlayer.create(getActivity(), R.raw.new_order);
                mp1.setOnPreparedListener(MediaPlayer::start);
                break;
            case Constants.ALERT_ORDER_OVERTIME:
                MediaPlayer mp2 = MediaPlayer.create(getActivity(), R.raw.ping);
                mp2.setOnPreparedListener(MediaPlayer::start);
                mp2.setOnCompletionListener(MediaPlayer::start);
                return mp2;
            case Constants.ALERT_EDIT_ORDER:
                MediaPlayer mp3 = MediaPlayer.create(getActivity(), R.raw.order_edit);
                mp3.setOnPreparedListener(MediaPlayer::start);
                break;
            case Constants.ALERT_FINISH_COOKING:
                MediaPlayer mp4 = MediaPlayer.create(getActivity(), R.raw.exit_from_cooking);
                mp4.setOnPreparedListener(MediaPlayer::start);
                break;
        }
        return null;

    }

    private boolean isEdited(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject.getJSONObject("ordersByStatus").getJSONArray("sent"));
            jsonArray.put(jsonObject.getJSONObject("ordersByStatus").getJSONArray("packing"));
            jsonArray.put(jsonObject.getJSONObject("ordersByStatus").getJSONArray("cooking"));
            jsonArray.put(jsonObject.getJSONObject("ordersByStatus").getJSONArray("preparing"));
            jsonArray.put(jsonObject.getJSONObject("ordersByStatus").getJSONArray("received"));
            for (int i = 0; i < jsonArray.length(); i++) {
                for (int j = 0; j < jsonArray.getJSONArray(i).length(); j++) {
                    if (jsonArray.getJSONArray(i).getJSONObject(j).has("is_change_confirmed")) {
                        boolean isConfirmed = jsonArray.getJSONArray(i).getJSONObject(j).getBoolean("is_change_confirmed");
                        String changeType = jsonArray.getJSONArray(i).getJSONObject(j).getString("change_type");
                        if (changeType.equals(CHANGE_TYPE_CHANGE) && !isConfirmed) return true;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean hasDelay(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONObject("ordersByStatus").getJSONArray("received");
            for (int i = 0; i < jsonArray.length(); i++) {
                String orderTime = jsonArray.getJSONObject(i).getString("order_time");
                if (Utils.getOrderTimerLong(orderTime) > DELAY_TIME_IN_SECONDS) return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}


