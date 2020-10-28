package com.dalpak.bringit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.dalpak.bringit.R;
import com.dalpak.bringit.adapters.OpenOrderAdapter;
import com.dalpak.bringit.adapters.OpenOrderPizzaAdapter;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.utils.Request;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.dalpak.bringit.utils.Constants.DELIVERY_OPTION_DELIVERY;
import static com.dalpak.bringit.utils.Constants.DELIVERY_OPTION_TABLE;
import static com.dalpak.bringit.utils.Constants.DELIVERY_OPTION_TAKEAWAY;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_ADDITIONAL_OFFER;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_DEAL;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_DRINK;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_PIZZA;

public class DialogOpenOrder extends Dialog implements View.OnClickListener {

    private Dialog d;
    private Context context;
    private OpenOrderModel orderModel;
    private View viewOrderChanged;
    private CardView cvComment;
    private TextView orderDateTV, orderNumTV, orderNameTV, orderTypeTV, orderDetailsTV, deliveryDetailsTV, shippingNumber, shippingTvClick, tvOrderSrc, tvPayment;
    private TextView tvTotal, tvItemsDetails;
    private View gDeliveryNotes;
    private ImageView orderMethodIV;
    private RecyclerView rvDrink, rvPizza, rvAdditional;
    private LinearLayout shippingHolder;

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
        for (ItemModel orderItem : orderModel.getProducts()) {
            switch (orderItem.getTypeName()) {
                case ITEM_TYPE_DRINK:
                    drinks.add(orderItem);
                    break;
                case ITEM_TYPE_ADDITIONAL_OFFER:
                    additionals.add(orderItem);
                    break;
                case ITEM_TYPE_PIZZA:
                    pizza.add(orderItem);
                    break;
                case ITEM_TYPE_DEAL:
                    for (ItemModel dealItem : orderItem.getProducts()) {
                        switch (dealItem.getTypeName()) {
                            case ITEM_TYPE_DRINK:
                                drinks.add(dealItem);
                                break;
                            case ITEM_TYPE_ADDITIONAL_OFFER:
                                additionals.add(dealItem);
                                break;
                            case ITEM_TYPE_PIZZA:
                                pizza.add(dealItem);
                                break;
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
        viewOrderChanged = findViewById(R.id.ll_change_in_order);
        cvComment = findViewById(R.id.cv_comment);

        orderDateTV = findViewById(R.id.tv_order_date);
        orderNameTV = findViewById(R.id.tv_order_name);
        orderNumTV = findViewById(R.id.tv_order_num);
        orderTypeTV = findViewById(R.id.tv_order_type);
        orderDetailsTV = findViewById(R.id.tv_order_details);
        deliveryDetailsTV = findViewById(R.id.tv_delivery_details);
        gDeliveryNotes = findViewById(R.id.g_delivery_notes);
        orderMethodIV = findViewById(R.id.iv_order_type);
        shippingHolder = findViewById(R.id.ll_shipping);
        shippingNumber = findViewById(R.id.shipping_number);
        shippingTvClick = findViewById(R.id.shipping_tv_click);
        rvDrink = findViewById(R.id.rvDrink);
        rvAdditional = findViewById(R.id.rvAdditional);
        rvPizza = findViewById(R.id.rvPizza);
        tvOrderSrc = findViewById(R.id.tv_order_src);
        tvPayment = findViewById(R.id.tv_payment);
        tvTotal = findViewById(R.id.tv_total);
        tvItemsDetails = findViewById(R.id.tv_items_details);

        tvItemsDetails.setOnClickListener(v -> openDetailsDialog(orderModel));

        orderDateTV.setText(orderModel.getOrderTime());
        orderNumTV.setText(orderModel.getId());
        if (orderModel.getClient() != null)
            orderNameTV.setText(String.format("%s %s",
                    orderModel.getClient().getFName(),
                    orderModel.getClient().getLName()));
        tvPayment.setText("שיטת תשלום: " + orderModel.getPaymentDisplay());
        tvOrderSrc.setText("הזמנה דרך: " + orderModel.getAddedBySystem());
        tvTotal.setText(String.format("  סך הכל:  %s%s", orderModel.getTotal(), context.getResources().getString(R.string.shekel)));
        viewOrderChanged.setVisibility(checkIfEdited() ? View.VISIBLE : View.GONE);
        cvComment.setCardBackgroundColor(Color.parseColor(checkIfEdited() ? "#12c395" : "#6f7888"));

        initOrderMethod();
        if (orderModel.getOrderNotes() == null || orderModel.getOrderNotes().equals("")) {
            orderDetailsTV.setText("אין הערות להזמנה");
        } else {
            orderDetailsTV.setText(orderModel.getOrderNotes());
        }

        if (orderModel.getDeliveryNotes() != null && !orderModel.getDeliveryNotes().equals("")) {
            gDeliveryNotes.setVisibility(View.VISIBLE);
            deliveryDetailsTV.setText(orderModel.getDeliveryNotes());
        }
    }

    private void initOrderMethod() {

        switch (orderModel.getDeliveryOption()) {
            case DELIVERY_OPTION_TAKEAWAY:
                orderTypeTV.setText("איסוף עצמי");
                orderMethodIV.setImageResource(R.drawable.ic_takeaway);
                break;
            case DELIVERY_OPTION_DELIVERY:
                orderTypeTV.setText("משלוח");
                orderMethodIV.setImageResource(R.drawable.ic_delivery);
                shippingHolder.setVisibility(View.VISIBLE);
                shippingTvClick.setOnClickListener(v ->
                        Request.getInstance().getOrderCode(context, orderModel.getId(), jsonObject -> {
                            try {
                                if (jsonObject.has("code")) {
                                    shippingNumber.setVisibility(View.VISIBLE);
                                    shippingTvClick.setVisibility(View.GONE);
                                    shippingNumber.setText("N " + jsonObject.getString("code"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }));
                break;
            case DELIVERY_OPTION_TABLE:
            default:
                orderTypeTV.setText("לשבת");
                orderMethodIV.setImageResource(R.drawable.ic_dinner);
                break;
        }

    }

    private void initPizzaRV(final List<ItemModel> orderModels, RecyclerView recyclerView) {

        OpenOrderPizzaAdapter mAdapter = new OpenOrderPizzaAdapter(context, orderModels, itemModel -> {
        });
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        // Set the adapter for RecyclerView
        recyclerView.setAdapter(mAdapter);


    }

    private void initRV(final List<ItemModel> orderModels, RecyclerView recyclerView) {
        OpenOrderAdapter openOrderAdapter = new OpenOrderAdapter(context, orderModels, itemModel -> {
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(openOrderAdapter);
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

    private void openDetailsDialog(OpenOrderModel orderModel) {
        DialogOrderDetails d = new DialogOrderDetails(context, orderModel);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        d.show();
        d.getWindow().setAttributes(lp);
    }

    private boolean checkIfEdited() {
//        for (ItemModel model : orderModel.getOrder_items()) { //todo order items missing
//            if (model.getChange_type() != null) return true;
//        }
        return false;
    }
}