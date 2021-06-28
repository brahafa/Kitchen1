package com.dalpak.bringit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.dalpak.bringit.R;
import com.dalpak.bringit.adapters.OpenOrderAdapter;
import com.dalpak.bringit.adapters.OpenOrderPizzaAdapter;
import com.dalpak.bringit.databinding.OpenOrderDialogBinding;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.models.OrderCategoryModel;
import com.dalpak.bringit.network.Request;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static com.dalpak.bringit.utils.Constants.CHANGE_TYPE_CHANGE;
import static com.dalpak.bringit.utils.Constants.DELIVERY_OPTION_DELIVERY;
import static com.dalpak.bringit.utils.Constants.DELIVERY_OPTION_TABLE;
import static com.dalpak.bringit.utils.Constants.DELIVERY_OPTION_TAKEAWAY;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_ADDITIONAL_OFFER;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_DEAL;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_DRINK;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_PIZZA;

public class DialogOpenOrder extends Dialog {

    private OpenOrderDialogBinding binding;

    private Dialog d;
    private Context context;
    public OpenOrderModel orderModel;
    private boolean ifEdited = false;

    public DialogOpenOrder(@NonNull final Context context, OpenOrderModel orderModel) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = OpenOrderDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.context = context;
        d = this;
        updateDialog(orderModel);
    }

    private void updateDialog(OpenOrderModel orderModel) {
        this.orderModel = orderModel;
        initData();
        initLists();
    }

    public void editDialog(OpenOrderModel openOrderModel) {
        ifEdited = true;
        updateDialog(openOrderModel);
    }

    private void initLists() {
        List<ItemModel> drinks = new ArrayList<>();
        List<ItemModel> additionals = new ArrayList<>();
        List<ItemModel> pizza = new ArrayList<>();

        clearDeletedItems();

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

        initRV(drinks, binding.rvDrink);
        initRV(additionals, binding.rvAdditional);
        initPizzaRV(pizza, binding.rvPizza);
    }

    private void clearDeletedItems() {
        for (int i = orderModel.getProducts().size() - 1; i >= 0; i--) {
            ItemModel item = orderModel.getProducts().get(i);
            if (item.isDeleted()) orderModel.getProducts().remove(i);
            else {
                if (item.getTypeName().equals(ITEM_TYPE_DEAL)) {
                    List<ItemModel> products = item.getProducts();
                    for (int j = products.size() - 1; j >= 0; j--) {
                        ItemModel dealItem = products.get(j);
                        if (dealItem.isDeleted()) item.getProducts().remove(i);
                        else clearCategories(dealItem.getCategories());
                    }
                } else clearCategories(item.getCategories());
            }
        }
    }

    private void clearCategories(List<OrderCategoryModel> categories) {
        for (OrderCategoryModel category : categories) {
            for (int i = category.getProducts().size() - 1; i >= 0; i--) {
                ItemModel innerItem = category.getProducts().get(i);
                if (innerItem.isDeleted()) category.getProducts().remove(i);
            }
        }
    }

    private void initData() {
        binding.close.setOnClickListener(v -> d.dismiss());

        binding.tvItemsDetails.setOnClickListener(v -> openDetailsDialog(orderModel));

        binding.tvOrderDate.setText(orderModel.getOrderTime());
        binding.tvOrderNum.setText(orderModel.getId());
        if (orderModel.getClient() != null)
            binding.tvOrderName.setText(String.format("%s %s",
                    orderModel.getClient().getFName(),
                    orderModel.getClient().getLName()));
        binding.tvPayment.setText("שיטת תשלום: " + orderModel.getPaymentDisplay());
        binding.tvOrderSrc.setText("הזמנה דרך: " + orderModel.getAddedBySystem());
        binding.tvTotal.setText(String.format(Locale.US, "  סך הכל:  " + "%.2f %s", Double.parseDouble(orderModel.getTotalWithDelivery()), context.getResources().getString(R.string.shekel)));
        binding.llChangeInOrder.setVisibility(orderModel.getChangeType().equals(CHANGE_TYPE_CHANGE) ? View.VISIBLE : View.GONE);
        binding.tvApproveChanges.setVisibility(
                orderModel.getChangeType().equals(CHANGE_TYPE_CHANGE) && !orderModel.isChangeConfirmed()
                        ? View.VISIBLE : View.GONE);
        binding.cvComment.setCardBackgroundColor(Color.parseColor(ifEdited ? "#12c395" : "#6f7888"));

        initOrderMethod();
        if (orderModel.getOrderNotes() == null || orderModel.getOrderNotes().equals("")) {
            binding.tvOrderDetails.setText("אין הערות להזמנה");
        } else {
            binding.tvOrderDetails.setText(orderModel.getOrderNotes());
        }

        if (orderModel.getDeliveryNotes() != null && !orderModel.getDeliveryNotes().equals("")) {
            binding.gDeliveryNotes.setVisibility(View.VISIBLE);
            binding.tvDeliveryDetails.setText(orderModel.getDeliveryNotes());
        }

        binding.tvApproveChanges.setOnClickListener(v -> Request.getInstance().approveOrderChanges(context, orderModel.getId(), isDataSuccess -> {
            if (isDataSuccess) {
                binding.tvApproveChanges.setBackgroundResource(R.drawable.background_change_accepted);
                binding.tvApproveChanges.setText("Changes approved");
                binding.tvApproveChanges.setEnabled(false);
                Toast.makeText(context, "Changes approved", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void initOrderMethod() {

        switch (orderModel.getDeliveryOption()) {
            case DELIVERY_OPTION_TAKEAWAY:
                binding.tvOrderType.setText("איסוף עצמי");
                binding.ivOrderType.setImageResource(R.drawable.ic_takeaway);
                break;
            case DELIVERY_OPTION_DELIVERY:
                binding.tvOrderType.setText("משלוח");
                binding.ivOrderType.setImageResource(R.drawable.ic_delivery);
                binding.llShipping.setVisibility(View.VISIBLE);
                binding.shippingTvClick.setOnClickListener(v ->
                        Request.getInstance().getOrderCode(context, orderModel.getId(), jsonObject -> {
                            try {
                                if (jsonObject.has("code")) {
                                    binding.shippingNumber.setVisibility(View.VISIBLE);
                                    binding.shippingTvClick.setVisibility(View.GONE);
                                    binding.shippingNumber.setText("N " + jsonObject.getString("code"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }));
                break;
            case DELIVERY_OPTION_TABLE:
            default:
                binding.tvOrderType.setText("לשבת");
                binding.ivOrderType.setImageResource(R.drawable.ic_dinner);
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

    private void openDetailsDialog(OpenOrderModel orderModel) {
        DialogOrderDetails d = new DialogOrderDetails(context, orderModel);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        d.show();
        d.getWindow().setAttributes(lp);
    }

}