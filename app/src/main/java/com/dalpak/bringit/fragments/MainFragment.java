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

import com.dalpak.bringit.MainActivity;
import com.dalpak.bringit.R;
import com.dalpak.bringit.adapters.OrderAdapter;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.models.OrderModel;
import com.dalpak.bringit.utils.Constants;
import com.dalpak.bringit.utils.Request;
import com.google.gson.Gson;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.ColumnProperties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainFragment extends Fragment {

    private final int REQUEST_REPEAT_INTERVAL = 10 * 1000;

    private BoardView mBoardView;
    private int mColumns;

    private Gson gson;
    private final Handler mHandler = new Handler();

    private MediaPlayer mp;

    private String lastResponse = "";

    private Runnable mRunnable = () -> Request.getInstance().getAllOrders(getActivity(),
            jsonObject -> {
                if (!jsonObject.toString().equals(lastResponse)) {
                    lastResponse = jsonObject.toString();
                    updateAllRV(jsonObject);
                }
                setupBoardUpdates();
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        gson = new Gson();

        mp = MediaPlayer.create(getActivity(), R.raw.trike);
        mp.setOnCompletionListener(MediaPlayer::release);

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
                setupBoardUpdates();
                if (fromColumn != toColumn) {
                    changeStatus(
                            mBoardView.getAdapter(toColumn).getUniqueItemId(toRow),
                            5 - fromColumn,
                            5 - toColumn,
                            true,
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
                orderModels.add(gson.fromJson(jsonArray.getString(i), OrderModel.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderModels;
    }

    private void changeStatus(long order_id, int oldPos, int newPos, boolean statusChanged, String draggedToStr) {
        Request.getInstance().updateOrderStatus(getActivity(), order_id, draggedToStr, jsonObject -> { // fixme: working request
//        Request.getInstance().orderChangePos(getActivity(), order_id, oldPos, newPos, statusChanged, draggedToStr, jsonObject -> {
        });
    }

    private void initRV(final List<OrderModel> orderModels) {

        OrderAdapter bottomListAdapter = new OrderAdapter(orderModels, new OrderAdapter.AdapterCallback() {
            @Override
            public void onItemChoose(OrderModel orderModel) {
                Request.getInstance().getOrderDetailsByID(getActivity(), orderModel.getOrder_id(), jsonObject -> {
                    try {
                        OpenOrderModel openOrderModel = gson.fromJson(jsonObject.getString("order"), OpenOrderModel.class);
                        ((MainActivity) getActivity()).openOrderDialog(openOrderModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onOrderDelay() {
                playSound(Constants.ALERT_ORDER_OVERTIME);
            }
        });

        if (checkIfEdited(orderModels)) playSound(Constants.ALERT_EDIT_ORDER);

        final View header = View.inflate(getActivity(), R.layout.column_header, null);

        String[] sections = getResources().getStringArray(R.array.sections);

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
        mp.release();
        super.onDestroyView();
    }

    private int countColumnWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return (width - 60) / 5;
    }


    private void playSound(int alertType) {
        int resId = -1;
        switch (alertType) {
            case Constants.ALERT_NEW_ORDER:
                resId = R.raw.trike; // todo change to right sound
                break;
            case Constants.ALERT_ORDER_OVERTIME:
                resId = R.raw.trike; // todo change to right sound
                break;
            case Constants.ALERT_EDIT_ORDER:
                resId = R.raw.trike; // todo change to right sound
                break;
            case Constants.ALERT_FINISH_COOKING:
                resId = R.raw.trike; // todo change to right sound
                break;
        }
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(getActivity(), resId);
            }
//            mp.start();  //todo enable when work on alerts
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkIfEdited(List<OrderModel> orderModels) {
        for (OrderModel model : orderModels) {
            if (model.getOrder_has_changes().equals("1")) return true;
        }
        return false;
    }

}


