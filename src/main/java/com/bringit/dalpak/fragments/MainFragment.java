package com.bringit.dalpak.fragments;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bringit.dalpak.MainActivity;
import com.bringit.dalpak.R;
import com.bringit.dalpak.adapters.Listener;
import com.bringit.dalpak.adapters.OrderRv;
import com.bringit.dalpak.dialog.PasswordDialog;
import com.bringit.dalpak.models.OrderModel;
import com.bringit.dalpak.utils.Request;
import com.bringit.dalpak.utils.Utils;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

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
        openPasswordDialog();

        rVPreparing = view.findViewById(R.id.rvPreparing);
        rVReceived = view.findViewById(R.id.rvReceived);
        rVCooking = view.findViewById(R.id.rvCooking);
        rVPacking = view.findViewById(R.id.rvPacking);
        rVSent = view.findViewById(R.id.rvSent);

        return view;
    }


    private void openPasswordDialog() {
        PasswordDialog passwordDialog = new PasswordDialog(getActivity());
        passwordDialog.setCancelable(false);
        passwordDialog.show();

        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((MainActivity)getActivity()).setName();
                Request.getAllOrders(getActivity(), new Request.RequestJsonCallBack() {
                    @Override
                    public void onDataDone(JSONObject jsonObject) {
                        initRV(getOrdersList(jsonObject, "preparing"), rVPreparing);
                        initRV(getOrdersList(jsonObject, "received"), rVReceived);
                        initRV(getOrdersList(jsonObject, "cooking"), rVCooking);
                        initRV(getOrdersList(jsonObject, "packing"), rVPacking);
                        initRV(getOrdersList(jsonObject, "sent"), rVSent);
                    }
                });
            }
        });
    }

    private List<OrderModel> getOrdersList(JSONObject jsonObject, String orderStatus) {
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
                ((MainActivity)getActivity()).openOrderDialog(orderModel);
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


