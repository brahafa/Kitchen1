package com.dalpak.bringit.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dalpak.bringit.R;
import com.dalpak.bringit.models.ItemModel;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OpenOrderPizzaRv extends RecyclerView.Adapter<OpenOrderPizzaRv.OpenOrderPizzaRvHolder> {

    private List<ItemModel> itemList;
    private Context context;
    AdapterCallback adapterCallback;

    class OpenOrderPizzaRvHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView tvCancel;
        RecyclerView toppingsRv;
        CardView parent;

        OpenOrderPizzaRvHolder(View view) {
            super(view);
            tvCancel = view.findViewById(R.id.tv_cancel);
            parent = view.findViewById(R.id.parent);
            name = view.findViewById(R.id.name);
            toppingsRv = view.findViewById(R.id.rvTopping);
        }

    }


    public OpenOrderPizzaRv(Context context, List<ItemModel> itemList, AdapterCallback adapterCallback) {
        this.itemList = itemList;
        this.context = context;
        this.adapterCallback = adapterCallback;
    }


    @Override
    public OpenOrderPizzaRv.OpenOrderPizzaRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.open_order_pizza_item, parent, false);
        return new OpenOrderPizzaRv.OpenOrderPizzaRvHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final OpenOrderPizzaRv.OpenOrderPizzaRvHolder holder, final int position) {
        ItemModel item = itemList.get(position);

        holder.name.setText(item.getName());

        if (item.getChange_type() != null)
            switch (item.getChange_type()) {
                case "DELETE":
                    holder.tvCancel.setVisibility(View.VISIBLE);
                    break;
                case "NEW":
                    holder.parent.setCardBackgroundColor(Color.parseColor("#12c395"));
                    holder.name.setTextColor(Color.WHITE);
                    break;
            }
        if (item.getItem_filling().size() != 0) initRV(item.getItem_filling(), holder.toppingsRv);
    }

    private void initRV(final List<ItemModel> orderModels, RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);
        OpenOrderRv openOrderRv = new OpenOrderRv(context, orderModels, itemModel -> {
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(openOrderRv);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface AdapterCallback {
        void onItemChoose(ItemModel itemModel);
    }


    List<ItemModel> getList() {
        return itemList;
    }

}
