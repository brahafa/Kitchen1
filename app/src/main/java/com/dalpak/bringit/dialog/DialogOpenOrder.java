package com.dalpak.bringit.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dalpak.bringit.R;
import com.dalpak.bringit.adapters.OpenOrderPizzaRv;
import com.dalpak.bringit.adapters.OpenOrderRv;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.utils.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DialogOpenOrder extends Dialog implements View.OnClickListener {

    Dialog d;
    Context context;
    OpenOrderModel orderModel;
    ;
    TextView orderDateTV, orderNumTV, orderNameTV, orderTypeTV, orderDetailsTV, shippingNumber, shippingTvClick;
    ImageView orderMethodIV;
    RecyclerView rvDrink, rvPizza, rvAdditional;

    public DialogOpenOrder(@NonNull final Context context, OpenOrderModel orderModel) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        this.orderModel = orderModel;
        setContentView(R.layout.open_order_dialog);
        d = this;
        initData();
        initLists();
    }

    private void initLists() {
        List<ItemModel> drinks = new ArrayList<>();
        List<ItemModel> additionals = new ArrayList<>();
        List<ItemModel> pizza = new ArrayList<>();
        for (int i = 0; i < orderModel.getOrder_items().size(); i++) {
            switch (orderModel.getOrder_items().get(i).get_ItemType()) {
                case "Drink":
                    drinks.add(orderModel.getOrder_items().get(i));
                    break;
                case "AdditionalOffer":
                    additionals.add(orderModel.getOrder_items().get(i));
                    break;
                case "Food":
                    pizza.add(orderModel.getOrder_items().get(i));
                    for (int j = i; j < orderModel.getOrder_items().size(); j++) {
                        if (orderModel.getOrder_items().get(j).getFather_id() != null && orderModel.getOrder_items().get(j).getFather_id().equals(orderModel.getOrder_items().get(i).getCart_id())) {
                            pizza.get(pizza.size() - 1).getItem_filling().add(orderModel.getOrder_items().get(j));
                        }
                    }
                    break;
            }
        }

        initRV(drinks, rvDrink);
        initRV(additionals, rvAdditional);
        initPizzaRV(pizza, rvPizza);
    }

    private void initData() {
        (findViewById(R.id.close)).setOnClickListener(this);
        orderDateTV = findViewById(R.id.orderDateTV);
        orderNameTV = findViewById(R.id.orderName);
        orderNumTV = findViewById(R.id.orderNum);
        orderTypeTV = findViewById(R.id.orderTypeTV);
        orderDetailsTV = findViewById(R.id.orderDetailsTV);
        orderMethodIV = findViewById(R.id.orderMethodIV);
        shippingNumber = findViewById(R.id.shipping_number);
        shippingTvClick = findViewById(R.id.shipping_tv_click);
        rvDrink = findViewById(R.id.rvDrink);
        rvAdditional = findViewById(R.id.rvAdditional);
        rvPizza = findViewById(R.id.rvPizza);

        orderDateTV.setText(orderModel.getOrder_time());
        orderNumTV.setText(orderModel.getOrder_id());
        orderNameTV.setText(orderModel.getF_name() + " " + orderModel.getL_name());
        initOrderMethod();
        if (orderModel.getOrder_notes() == null || orderModel.getOrder_notes().equals("")) {
            orderDetailsTV.setText("אין הערות להזמנה");
        } else {
            orderDetailsTV.setText(orderModel.getOrder_notes());
        }
    }

    private void initOrderMethod() {

        if (orderModel.getIs_delivery().equals("0")) {
            orderTypeTV.setText("איסוף עצמי");
            orderMethodIV.setImageResource(R.mipmap.takeaway);

        } else if (orderModel.getIs_delivery().equals("1")) {
            orderTypeTV.setText("משלוח");
            orderMethodIV.setImageResource(R.mipmap.delivery);
            shippingTvClick.setVisibility(View.VISIBLE);
            shippingNumber.setVisibility(View.VISIBLE);
            shippingTvClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Request.getOrderCode(context, orderModel.getOrder_id(), new Request.RequestJsonCallBack() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataDone(JSONObject jsonObject) {
                            try {
                                shippingNumber.setVisibility(View.VISIBLE);
                                if (jsonObject.has("message"))
                                    shippingNumber.setText("N " + jsonObject.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

        } else {
            orderTypeTV.setText("לשבת");
            orderMethodIV.setImageResource(R.mipmap.dinner);
        }

    }

    private void initPizzaRV(final List<ItemModel> orderModels, RecyclerView recyclerView) {
        /*
            OpenOrderPizzaRv openOrderRv = new OpenOrderPizzaRv(context, orderModels, new OpenOrderPizzaRv.AdapterCallback() {
            @Override
            public void onItemChoose(ItemModel itemModel) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(openOrderRv);
        //  recyclerView.setOnDragListener(Adapter.getDragInstance());
         */

        OpenOrderPizzaRv mAdapter = new OpenOrderPizzaRv(context, orderModels, new OpenOrderPizzaRv.AdapterCallback() {
            @Override
            public void onItemChoose(ItemModel itemModel) {

            }
        });

        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        // Initialize a new instance of RecyclerView Adapter instance


        // Set the adapter for RecyclerView
        recyclerView.setAdapter(mAdapter);


    }

    private void initRV(final List<ItemModel> orderModels, RecyclerView recyclerView) {
        OpenOrderRv openOrderRv = new OpenOrderRv(context, orderModels, new OpenOrderRv.AdapterCallback() {
            @Override
            public void onItemChoose(ItemModel itemModel) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(openOrderRv);
        //  recyclerView.setOnDragListener(Adapter.getDragInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                d.dismiss();
                break;

        }
    }
}