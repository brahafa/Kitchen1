package com.bringit.dalpak.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bringit.dalpak.R;
import com.bringit.dalpak.models.ItemModel;

import java.util.List;

public class OpenOrderPizzaRv extends RecyclerView.Adapter<OpenOrderPizzaRv.OpenOrderPizzaRvHolder> {

    private List<ItemModel> itemList;
    private Context context;
    AdapterCallback adapterCallback;

    class OpenOrderPizzaRvHolder extends RecyclerView.ViewHolder {
        TextView name;
        View view;
        RecyclerView toppingsRv;
        OpenOrderPizzaRvHolder(View view) {
            super(view);
            this.view = view;
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
        holder.name.setText(itemList.get(position).getName());
        initRV(itemList.get(position).getItem_filling(),holder.toppingsRv);
    }

    private void initRV(final List<ItemModel> orderModels, RecyclerView recyclerView) {
        OpenOrderRv openOrderRv = new OpenOrderRv(context, orderModels,new OpenOrderRv.AdapterCallback() {
            @Override
            public void onItemChoose(ItemModel itemModel) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(openOrderRv);
        //  recyclerView.setOnDragListener(Adapter.getDragInstance());
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
