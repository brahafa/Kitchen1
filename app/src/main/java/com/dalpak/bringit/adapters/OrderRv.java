package com.dalpak.bringit.adapters;

import android.content.Context;
import android.media.MediaPlayer;
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

public class OrderRv extends DragItemAdapter<OrderModel, OrderRv.OrderHolder> {

    private List<OrderModel> orderList;
    private Context context;
    private AdapterCallback adapterCallback;

    private MediaPlayer mp;

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

    public OrderRv(Context context, List<OrderModel> orderModels, AdapterCallback adapterCallback) {
        orderList = orderModels;
        this.context = context;
        this.adapterCallback = adapterCallback;

        setItemList(orderModels);

        mp = MediaPlayer.create(context, R.raw.trike);
        mp.setOnCompletionListener(MediaPlayer::release);
    }


    @Override
    public OrderRv.OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_order_item, parent, false);
        return new OrderRv.OrderHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final OrderRv.OrderHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        if (orderList.get(position).getIs_delivery().equals("1")) {
            holder.name.setText(orderList.get(position).getStreet() + " " + orderList.get(position).getHouse_num() + ", " + orderList.get(position).getCity_name());
            holder.deliveryImage.setImageResource(R.mipmap.delivery);
        } else if (orderList.get(position).getIs_delivery().equals("0")) {
            holder.name.setText(orderList.get(position).getName());
            holder.deliveryImage.setImageResource(R.mipmap.takeaway);
        } else {
            holder.deliveryImage.setImageResource(R.mipmap.dinner);
        }

        holder.orderTime.setText(Utils.getOrderTimerStr(orderList.get(position).getOrder_time()));
        if (Utils.getOrderTimerLong(orderList.get(position).getOrder_time()) > 3) {
            playSound();
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
    }

    private void playSound() {
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(context, R.raw.trike);
            }
//            mp.start();  //todo enable when work on alerts
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


