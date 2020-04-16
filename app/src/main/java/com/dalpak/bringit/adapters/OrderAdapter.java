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

public class OrderAdapter extends DragItemAdapter<OrderModel, OrderAdapter.OrderHolder> {

    private List<OrderModel> orderList;
    private AdapterCallback adapterCallback;


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

        if (orderList.get(position).getIs_delivery().equals("1")) {
            holder.name.setText(
                    String.format("%s %s, %s",
                            orderList.get(position).getStreet(),
                            orderList.get(position).getHouse_num(),
                            orderList.get(position).getCity_name()));
            holder.deliveryImage.setImageResource(R.drawable.ic_delivery);
        } else if (orderList.get(position).getIs_delivery().equals("0")) {
            holder.name.setText(orderList.get(position).getName());
            holder.deliveryImage.setImageResource(R.drawable.ic_takeaway);
        } else {
            holder.deliveryImage.setImageResource(R.drawable.ic_dinner);
        }

        holder.orderTime.setText(orderList.get(position).getStartTimeStr());
        if (Utils.getOrderTimerLong(orderList.get(position).getOrder_time()) > 3) {
            adapterCallback.onOrderDelay();
            holder.warningImg.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> adapterCallback.onItemChoose(orderList.get(position)));

    }

    @Override
    public long getUniqueItemId(int position) {
        return Long.parseLong(orderList.get(position).getOrder_id());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public interface AdapterCallback {
        void onItemChoose(OrderModel orderModel);

        void onOrderDelay();
    }

}


