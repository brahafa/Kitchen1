package com.dalpak.bringit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.dalpak.bringit.R;
import com.dalpak.bringit.adapters.OrderDetailsAdapter;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OpenOrderModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_DEAL;

public class DialogOrderDetails extends Dialog implements View.OnClickListener {

    Context context;
    OpenOrderModel orderModel;
    ImageView close;
    TextView tvPaymentMethod, tvPayment;
    TextView tvName, tvAddress, tvPhone;
    private RecyclerView rv;
    Dialog d;

    public DialogOrderDetails(@NonNull final Context context, OpenOrderModel orderModel) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        this.orderModel = orderModel;
        setContentView(R.layout.order_details_dialog);
        d = this;
        initData();

    }

    private void initData() {
        rv = findViewById(R.id.rv_details);
        close = findViewById(R.id.close);
        tvPaymentMethod = findViewById(R.id.tv_payment_method);
        tvPayment = findViewById(R.id.tv_payment);

        tvName = findViewById(R.id.tv_name);
        tvAddress = findViewById(R.id.tv_address);
        tvPhone = findViewById(R.id.tv_phone);

        tvName.setText(Html.fromHtml(String.format("<b>שם:</b> %s %s", orderModel.getClient().getFName(), orderModel.getClient().getLName())));
        tvPhone.setText(Html.fromHtml(String.format("<b>טלפון:</b> %s", orderModel.getClient().getPhone())));
        if (orderModel.getClient().getAddress() != null)
            tvAddress.setText(Html.fromHtml(String.format(
                    "<b>כתובת:</b> %s, %s &emsp; <b>כניסה</b>: %s &emsp; <b>קומה:</b> %s &emsp; <b>דירה:</b> %s",
                    orderModel.getClient().getAddress().getCity(),
                    orderModel.getClient().getAddress().getStreet(),
                    orderModel.getClient().getAddress().getEntrance(),
                    orderModel.getClient().getAddress().getFloor(),
                    orderModel.getClient().getAddress().getAptNum())));

        tvPaymentMethod.setText(orderModel.getPaymentDisplay());
        tvPayment.setText(String.format("  שיטת תשלום:   %s", orderModel.getPaymentDisplay()));
        tvPaymentMethod.setText(String.format("  סך הכל:   %s%s", orderModel.getTotal(), context.getResources().getString(R.string.shekel)));

        List<ItemModel> separatedItems = new ArrayList<>();
        for (ItemModel item : orderModel.getItems()) {
            if (item.getTypeName().equals(ITEM_TYPE_DEAL)) separatedItems.addAll(item.getItems());
            else separatedItems.add(item);
        }

        initRV(separatedItems, rv);

        close.setOnClickListener(v -> d.dismiss());
    }

    private void initRV(final List<ItemModel> orderModels, RecyclerView recyclerView) {
        OrderDetailsAdapter openOrderAdapter = new OrderDetailsAdapter(context, orderModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(openOrderAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}
