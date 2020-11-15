package com.dalpak.bringit.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dalpak.bringit.R;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OrderCategoryModel;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OpenOrderPizzaAdapter extends RecyclerView.Adapter<OpenOrderPizzaAdapter.OpenOrderPizzaRvHolder> {

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


    public OpenOrderPizzaAdapter(Context context, List<ItemModel> itemList, AdapterCallback adapterCallback) {
        this.itemList = itemList;
        this.context = context;
        this.adapterCallback = adapterCallback;
    }


    @Override
    public OpenOrderPizzaAdapter.OpenOrderPizzaRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.open_order_pizza_item, parent, false);
        return new OpenOrderPizzaAdapter.OpenOrderPizzaRvHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final OpenOrderPizzaAdapter.OpenOrderPizzaRvHolder holder, final int position) {
        ItemModel item = itemList.get(position);

        holder.name.setText(item.getName());


        if (item.isCanceled() || item.isDeleted()) {
            holder.tvCancel.setVisibility(View.VISIBLE);
        } else holder.tvCancel.setVisibility(View.GONE);

        if (item.isNew()) {
            holder.parent.setCardBackgroundColor(Color.parseColor("#12c395"));
            holder.name.setTextColor(Color.WHITE);
        } else {
            holder.parent.setCardBackgroundColor(Color.WHITE);
            holder.name.setTextColor(context.getResources().getColor(R.color.text_color));
        }

        if (item.getCategories().size() != 0)
            initRV(item.getCategories(), item.getShape(), holder.toppingsRv);
    }

    private void initRV(final List<OrderCategoryModel> categoryModels, String shape, RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);
        CategoriesAdapter openOrderAdapter = new CategoriesAdapter(context, categoryModels, shape, itemModel -> {
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(openOrderAdapter);
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
