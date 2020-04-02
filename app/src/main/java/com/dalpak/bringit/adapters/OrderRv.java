package com.dalpak.bringit.adapters;

import android.content.ClipData;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dalpak.bringit.R;
import com.dalpak.bringit.models.OrderModel;
import com.dalpak.bringit.utils.Request;
import com.dalpak.bringit.utils.Utils;

import org.json.JSONObject;

import java.util.List;

public class OrderRv extends RecyclerView.Adapter<OrderRv.OrderHolder> {

    private List<OrderModel> orderList;
    private Context context;
    private Listener listener;
    AdapterCallback adapterCallback;

    class OrderHolder extends RecyclerView.ViewHolder {
        TextView name, orderTime;
        ImageView deliveryImage, warningImg;
        View view;

        OrderHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            warningImg = view.findViewById(R.id.warning_image);
            orderTime = view.findViewById(R.id.order_time);
            deliveryImage = view.findViewById(R.id.delivery_image);

        }

    }

    public OrderRv(Context context, List<OrderModel> orderModels, Listener listener, AdapterCallback adapterCallback) {
        orderList = orderModels;
        this.context = context;
        this.listener = listener;
        this.adapterCallback=adapterCallback;
    }


    @Override
    public OrderRv.OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_order_item, parent, false);
        return new OrderRv.OrderHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final OrderRv.OrderHolder holder, final int position) {

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
        if(Utils.getOrderTimerLong(orderList.get(position).getOrder_time()) > 3){
            holder.warningImg.setVisibility(View.VISIBLE);
        }
        holder.view.setTag(position);
        //holder.view.setOnTouchListener(this);
        holder.view.setOnDragListener(new DragListener(listener));

        holder.view.setOnClickListener(v -> adapterCallback.onItemChoose(orderList.get(position)));

        holder.view.setOnLongClickListener(v -> {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                v.startDragAndDrop(data, shadowBuilder, v, 0);
            } else {
                v.startDrag(data, shadowBuilder, v, 0);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        float x, y;
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                ClipData data = ClipData.newPlainText("", "");
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    v.startDragAndDrop(data, shadowBuilder, v, 0);
//                } else {
//                    v.startDrag(data, shadowBuilder, v, 0);
//                }
//                return true;
//        }
//        return true;
//    }

    public DragListener getDragInstance() {
        if (listener != null) {
            return new DragListener(listener);
        } else {
            Log.e("ListAdapter", "Listener wasn't initialized!");
            return null;
        }
    }

    public interface AdapterCallback {
         void onItemChoose(OrderModel orderModel);
    }


    List<OrderModel> getList() {
        return orderList;
    }

    void changeStatus(String order_id, int positionSource, int positionTarget, boolean b, String draggedToStr){
        Request.updateOrderStatus(context, order_id, draggedToStr, new Request.RequestJsonCallBack() {
            @Override
            public void onDataDone(JSONObject jsonObject) {

            }
        });
    }

    void updateList(List<OrderModel> orderModels) {
        this.orderList = orderModels;
    }
}


