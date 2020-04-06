package com.dalpak.bringit.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dalpak.bringit.MainActivity;
import com.dalpak.bringit.R;
import com.dalpak.bringit.adapters.Listener;
import com.dalpak.bringit.adapters.OrderRv;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.models.OrderModel;
import com.dalpak.bringit.utils.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainFragment extends Fragment implements Listener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    RecyclerView rVPreparing, rVReceived, rVCooking, rVPacking, rVSent;
    private OrderRv mAdapter;
    private Gson gson;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);
        gson = new Gson();
        //  openPasswordDialog();

        rVPreparing = view.findViewById(R.id.rvPreparing);
        rVReceived = view.findViewById(R.id.rvReceived);
        rVCooking = view.findViewById(R.id.rvCooking);
        rVPacking = view.findViewById(R.id.rvPacking);
        rVSent = view.findViewById(R.id.rvSent);
        initMainFragmentData(10000);
        return view;
    }

    public void initAllRV(JSONObject jsonObject) {
        try {
            if (jsonObject.has("status") && jsonObject.getBoolean("status")) {
                initRV(getOrdersList(jsonObject, "preparing"), rVPreparing);
                initRV(getOrdersList(jsonObject, "received"), rVReceived);
                initRV(getOrdersList(jsonObject, "cooking"), rVCooking);
                initRV(getOrdersList(jsonObject, "packing"), rVPacking);
                initRV(getOrdersList(jsonObject, "sent"), rVSent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initMainFragmentData(int time) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> Request.getAllOrders(getActivity(),
                jsonObject -> {
                    initAllRV(jsonObject);
                    initMainFragmentData(10000);
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

    private void initRV(final List<OrderModel> orderModels, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(
                getActivity(), LinearLayoutManager.VERTICAL, false));
        OrderRv bottomListAdapter = new OrderRv(getActivity(), orderModels, this, new OrderRv.AdapterCallback() {
            @Override
            public void onItemChoose(OrderModel orderModel) {
                Request.getOrderDetailsByID(getActivity(), orderModel.getOrder_id(), new Request.RequestJsonCallBack() {
                    @Override
                    public void onDataDone(JSONObject jsonObject) {
                        try {
                            OpenOrderModel openOrderModel = gson.fromJson(jsonObject.getString("order"), OpenOrderModel.class);
                            ((MainActivity) getActivity()).openOrderDialog(openOrderModel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        recyclerView.setAdapter(bottomListAdapter);
        recyclerView.setOnDragListener(bottomListAdapter.getDragInstance());
    }

    @Override
    public void setEmptyListTop(boolean visibility) {
        // tvEmptyListTop.setVisibility(visibility ? View.VISIBLE : View.GONE);
        //  rvTop.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setEmptyListBottom(boolean visibility) {
        //   tvEmptyListBottom.setVisibility(visibility ? View.VISIBLE : View.GONE);
        // rvBottom.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }




}


