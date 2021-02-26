package com.dalpak.bringit.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.dalpak.bringit.models.OrdersByStatusModel;
import com.dalpak.bringit.network.Request;
import com.dalpak.bringit.network.RequestHelper;
import com.dalpak.bringit.utils.Constants;
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

import static com.dalpak.bringit.utils.Constants.CHANGE_TYPE_CANCELED;
import static com.dalpak.bringit.utils.Constants.CHANGE_TYPE_CHANGE;

public class MainFragment extends Fragment {

    private final int REQUEST_REPEAT_INTERVAL = 10 * 1000;
    private final long DELAY_TIME_IN_SECONDS = 20;

    private Context mContext;

    private BoardView mBoardView;
    private int mColumns;

    private final Handler mHandler = new Handler();

    private MediaPlayer mp;

    private String lastResponse = "";
    private int lastNewOrdersSize = 0;
    private int lastCookingOrdersSize = 0;

    private onBusinessStatusCheckListener listener;

    RequestHelper requestHelper = new RequestHelper();

    private Runnable mRunnable = () -> requestHelper.getAllOrdersFromDb(getActivity(),
            response -> {
                if (mp != null) mp.release();
                if (hasDelay(response.getOrdersByStatus().getReceived()))
                    mp = playSound(Constants.ALERT_ORDER_OVERTIME);

                if (!response.toString().equals(lastResponse)) {
                    try {
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(gson.toJson(response));
                        checkIfOrderHasBeenUpdated(lastResponse, jsonObject);
                        lastResponse = gson.toJson(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.w("update error", e.toString());
                    }

                    updateAllRV(response.getOrdersByStatus());
                }
                setupBoardUpdates();
                listener.onBusinessStatusCheck();
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
                    if (!lastJsonObj.getString("change_type").equals(currentJsonObj.getString("change_type")) && lastJsonObj.getString("id").equals(currentJsonObj.get("change_for_order_id"))) {
                        String msg;
                        if (lastJsonArray.getJSONObject(i) == null || !lastJsonArray.getJSONObject(i).has("client")) {
                            msg = "יש עדכון בהזמנות";
                        } else {
                            msg = " הזמנה של " + lastJsonArray.getJSONObject(i).getJSONObject("client").getString("f_name") + " עודכנה";
                        }
                        playSound(Constants.ALERT_EDIT_ORDER);
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
        requestHelper.getOrderDetailsByIDFromDb(getActivity(), id, response -> {
            prepareOrder(response);
            response.setTotal(Utils.getTotalOrder(response) + "");
            ((MainActivity) Objects.requireNonNull(getActivity())).dialogOpenOrder.editDialog(response);
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

    private void updateAllRV(OrdersByStatusModel ordersByStatus) {

        mBoardView.clearBoard();
        mColumns = 0;
        initRV(clearSentOrdersList(clearOrdersList(ordersByStatus.getSent())));
        initRV(clearOrdersList(ordersByStatus.getPacking()));
        initRV(clearOrdersList(ordersByStatus.getCooking()));
        initRV(clearOrdersList(ordersByStatus.getPreparing()));
        initRV(clearOrdersList(ordersByStatus.getReceived()));

        int newOrdersSize = clearOrdersList(ordersByStatus.getReceived()).size();
        if (lastNewOrdersSize < newOrdersSize) playSound(Constants.ALERT_NEW_ORDER);

        if (isEdited(ordersByStatus)) playSound(Constants.ALERT_EDIT_ORDER);

        int cookingOrdersSize = clearOrdersList(ordersByStatus.getCooking()).size();
        if (lastCookingOrdersSize > cookingOrdersSize)
            playSound(Constants.ALERT_FINISH_COOKING);

        lastNewOrdersSize = newOrdersSize;
        lastCookingOrdersSize = cookingOrdersSize;
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

    private List<OrderModel> clearOrdersList(List<OrderModel> orders) {
        List<OrderModel> clearOrders = new ArrayList<>();
        for (OrderModel order : orders)
            if (!order.getChangeType().equals(CHANGE_TYPE_CANCELED)) clearOrders.add(order);
        return clearOrders;
    }

    private List<OrderModel> clearSentOrdersList(List<OrderModel> orders) {
        List<OrderModel> clearSentOrders = new ArrayList<>();
        for (OrderModel order : orders)
            if (!(order.getStartTimeStr().contains("day") ||
                    order.getStartTimeStr().contains("week") ||
                    order.getStartTimeStr().contains("month")))
                clearSentOrders.add(order);
        return clearSentOrders;
    }

    private void changeStatus(long order_id, String draggedToStr) {
        removeBoardUpdates();
        Request.getInstance().updateOrderStatus(getActivity(), order_id, draggedToStr, jsonObject -> {
        });
    }

    private void changePosition(long order_id, int oldPos, int newPos, boolean statusChanged, String draggedFromStr, String draggedToStr) {
        removeBoardUpdates();
        Request.getInstance().orderChangePos(getActivity(), order_id, oldPos, newPos, statusChanged, draggedFromStr, draggedToStr,
                jsonObject -> startBoardUpdates());
    }

    private void initRV(final List<OrderModel> orderModels) {

        OrderAdapter bottomListAdapter = new OrderAdapter(orderModels, orderModel ->
                requestHelper.getOrderDetailsByIDFromDb(getActivity(), orderModel.getId(), response -> {
                    prepareOrder(response);
                    response.setTotal(Utils.getTotalOrder(response) + "");
                    ((MainActivity) getActivity()).openOrderDialog(response);
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

    private boolean isEdited(OrdersByStatusModel ordersByStatus) {

        List<OrderModel> allOrders = new ArrayList<>();

        allOrders.addAll(ordersByStatus.getReceived());
        allOrders.addAll(ordersByStatus.getPreparing());
        allOrders.addAll(ordersByStatus.getCooking());
        allOrders.addAll(ordersByStatus.getPacking());
        allOrders.addAll(clearSentOrdersList(ordersByStatus.getSent()));

        for (OrderModel order : allOrders) {
            if (order.getChangeType().equals(CHANGE_TYPE_CHANGE) && !order.isChangeConfirmed())
                return true;
        }
        return false;
    }

    private boolean hasDelay(List<OrderModel> receivedOrders) {
        for (OrderModel order : receivedOrders) {
            if (!order.getChangeType().equals(CHANGE_TYPE_CANCELED)) {
                String orderTime = order.getOrderTime();
                if (Utils.getOrderTimerLong(orderTime) > DELAY_TIME_IN_SECONDS) return true;
            }
        }
        return false;
    }

    public interface onBusinessStatusCheckListener {
        void onBusinessStatusCheck();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            listener = (onBusinessStatusCheckListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnLoggedInManagerListener");
        }
    }

}


