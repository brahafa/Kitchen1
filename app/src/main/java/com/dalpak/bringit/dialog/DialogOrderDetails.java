package com.dalpak.bringit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dalpak.bringit.R;
import com.dalpak.bringit.adapters.OrderDetailsAdapter;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OpenOrderModel;

import java.util.List;

public class DialogOrderDetails extends Dialog implements View.OnClickListener {

    Context context;
    OpenOrderModel orderModel;
    ImageView close;
    TextView tvPaymentMethod, tvPayment;
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

        tvPaymentMethod.setText(orderModel.getPayment_display());
        tvPayment.setText(String.format("  שיטת תשלום:   %s", orderModel.getPayment_display()));
        tvPaymentMethod.setText(String.format("  סך הכל:   %s%s", orderModel.getOrder_total(), context.getResources().getString(R.string.shekel)));

        initRV(orderModel.getOrder_items(), rv);

        close.setOnClickListener(v-> {
            d.dismiss();
        });
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
