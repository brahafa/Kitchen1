package com.dalpak.bringit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dalpak.bringit.R;
import com.dalpak.bringit.models.OrderModel;
import com.dalpak.bringit.utils.Utils;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.List;

import static com.dalpak.bringit.utils.Constants.DELIVERY_OPTION_DELIVERY;
import static com.dalpak.bringit.utils.Constants.DELIVERY_OPTION_TABLE;
import static com.dalpak.bringit.utils.Constants.DELIVERY_OPTION_TAKEAWAY;

public class OrderAdapter extends DragItemAdapter<OrderModel, OrderAdapter.OrderHolder> {

    private List<OrderModel> orderList;
    private AdapterCallback adapterCallback;
    private final long DELAY_TIME_IN_SECONDS = 20;


    class OrderHolder extends DragItemAdapter.ViewHolder {
        TextView name, orderTime;
        ImageView deliveryImage, warningImg;

        OrderHolder(View view) {
            super(view, R.id.parent, true);

            name = view.findViewById(R.id.name);
            warningImg = view.findViewById(R.id.warning_image);
            orderTime = view.findViewById(R.id.order_time);
            deliveryImage = view.findViewById(R.id.delivery_image);

        }

    }

    public OrderAdapter(List<OrderModel> orderModels, AdapterCallback adapterCallback) {
        orderList = orderModels;
        this.adapterCallback = adapterCallback;

        setItemList(orderModels);

    }


    @Override
    public OrderAdapter.OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_order_item, parent, false);
        return new OrderAdapter.OrderHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final OrderAdapter.OrderHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        switch (orderList.get(position).getDeliveryOption()) {
            case DELIVERY_OPTION_DELIVERY:
                if (orderList.get(position).getClient() != null)
                    holder.name.setText(
                            String.format("%s %s, %s",
                                    orderList.get(position).getClient().getAddress().getStreet(),
                                    orderList.get(position).getClient().getAddress().getHouseNum(),
                                    orderList.get(position).getClient().getAddress().getCity()));
                holder.deliveryImage.setImageResource(R.drawable.ic_delivery);
                break;
            case DELIVERY_OPTION_TAKEAWAY:
                holder.name.setText(orderList.get(position).getClient().getFName());
                holder.deliveryImage.setImageResource(R.drawable.ic_takeaway);
                break;
            case DELIVERY_OPTION_TABLE:
            default:
                holder.deliveryImage.setImageResource(R.drawable.ic_dinner);
                break;
        }

        holder.orderTime.setText(orderList.get(position).getStartTimeStr());
        if (orderList.get(position).getStatus().equals("received") &&
                Utils.getOrderTimerLong(orderList.get(position).getOrderTime()) > DELAY_TIME_IN_SECONDS) {
            holder.warningImg.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> adapterCallback.onItemChoose(orderList.get(position)));

    }

    @Override
    public long getUniqueItemId(int position) {
        return Long.parseLong(orderList.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public interface AdapterCallback {
        void onItemChoose(OrderModel orderModel);
    }

}


