package com.dalpak.bringit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dalpak.bringit.R;
import com.dalpak.bringit.adapters.OrderDetailsAdapter;
import com.dalpak.bringit.databinding.OrderDetailsDialogBinding;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OpenOrderModel;

import java.util.List;
import java.util.Locale;

public class DialogOrderDetails extends Dialog implements View.OnClickListener {

    private OrderDetailsDialogBinding binding;

    Context context;
    OpenOrderModel orderModel;
    Dialog d;

    public DialogOrderDetails(@NonNull final Context context, OpenOrderModel orderModel) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = OrderDetailsDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.context = context;
        this.orderModel = orderModel;
        d = this;
        initData();

    }

    private void initData() {

        if (orderModel.getClient() != null) {
            binding.tvName.setText(Html.fromHtml(String.format("<b>שם:</b> %s %s", orderModel.getClient().getFName(), orderModel.getClient().getLName())));
            binding.tvPhone.setText(Html.fromHtml(String.format("<b>טלפון:</b> %s", orderModel.getClient().getPhone())));
            if (orderModel.getClient().getAddress() != null)
                binding.tvAddress.setText(Html.fromHtml(String.format(
                        "<b>כתובת:</b> %s, %s &emsp; <b>כניסה</b>: %s &emsp; <b>קומה:</b> %s &emsp; <b>דירה:</b> %s",
                        orderModel.getClient().getAddress().getCity() != null ? orderModel.getClient().getAddress().getCity() : "אשדוד",
                        orderModel.getClient().getAddress().getStreet(),
                        orderModel.getClient().getAddress().getEntrance(),
                        orderModel.getClient().getAddress().getFloor(),
                        orderModel.getClient().getAddress().getAptNum())));
        }
        binding.tvPaymentMethod.setText(orderModel.getPaymentDisplay());
        binding.tvPayment.setText(String.format("  שיטת תשלום:   %s", orderModel.getPaymentDisplay()));
        binding.tvPaymentMethod.setText(String.format(Locale.US, "  סך הכל:   " + "%.2f %s", Double.parseDouble(orderModel.getTotalWithDelivery()), context.getResources().getString(R.string.shekel)));
        if (!orderModel.getDeliveryPrice().equals("0")) {
            binding.tvDeliveryPrice.setVisibility(View.VISIBLE);
            binding.tvDeliveryPrice.setText(String.format(Locale.US, "  משלוח:   " + "%.2f %s", Double.parseDouble(orderModel.getDeliveryPrice()), context.getResources().getString(R.string.shekel)));
        }

        initRV(orderModel.getProducts(), binding.rvDetails);

        binding.close.setOnClickListener(v -> d.dismiss());
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
