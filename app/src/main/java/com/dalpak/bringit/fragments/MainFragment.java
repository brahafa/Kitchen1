package com.dalpak.bringit.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dalpak.bringit.MainActivity;
import com.dalpak.bringit.R;
import com.dalpak.bringit.adapters.OrderRv;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.models.OrderModel;
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
import androidx.recyclerview.widget.LinearLayoutManager;

public class MainFragment extends Fragment {

    private BoardView mBoardView;
    private int mColumns;

    private Gson gson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        gson = new Gson();
        //  openPasswordDialog();

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
                if (fromColumn != toColumn) {
                    changeStatus(
                            mBoardView.getAdapter(toColumn).getUniqueItemId(toRow),
                            5 - fromColumn,
                            5 - toColumn,
                            true,
                            statuses[toColumn]);
                }
            }
        });

        initMainFragmentData(10 * 1000);
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

    public void updateAllRV(JSONObject jsonObject) {

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

    private void initMainFragmentData(int time) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> Request.getInstance().getAllOrders(getActivity(),
                jsonObject -> {
                    updateAllRV(jsonObject);
                    initMainFragmentData(10 * 1000);
                }), time);
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
//        Request.getInstance().updateOrderStatus(getActivity(), order_id, draggedToStr, jsonObject -> { // fixme: working request
        Request.getInstance().orderChangePos(getActivity(), order_id, oldPos, newPos, statusChanged, draggedToStr, jsonObject -> {
        });
    }

    private void initRV(final List<OrderModel> orderModels) {

        OrderRv bottomListAdapter = new OrderRv(getActivity(), orderModels,
                orderModel -> Request.getInstance().getOrderDetailsByID(getActivity(), orderModel.getOrder_id(), jsonObject -> {
                    try {
                        OpenOrderModel openOrderModel = gson.fromJson(jsonObject.getString("order"), OpenOrderModel.class);
                        ((MainActivity) getActivity()).openOrderDialog(openOrderModel);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }));

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

    private int countColumnWidth() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return (width - 60) / 5;
    }

}


