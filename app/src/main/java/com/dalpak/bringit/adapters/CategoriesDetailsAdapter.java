package com.dalpak.bringit.adapters;

import android.content.Context;
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

public class CategoriesDetailsAdapter extends RecyclerView.Adapter<CategoriesDetailsAdapter.CategoriesAdapterRvHolder> {

    private List<OrderCategoryModel> itemList;
    private Context context;

    class CategoriesAdapterRvHolder extends RecyclerView.ViewHolder {
        TextView name;
        RecyclerView toppingsRv;
        CardView parent;

        CategoriesAdapterRvHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            toppingsRv = view.findViewById(R.id.rvTopping);
        }

    }


    public CategoriesDetailsAdapter(Context context, List<OrderCategoryModel> itemList) {
        this.itemList = itemList;
        this.context = context;
    }


    @Override
    public CategoriesDetailsAdapter.CategoriesAdapterRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoriesDetailsAdapter.CategoriesAdapterRvHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final CategoriesDetailsAdapter.CategoriesAdapterRvHolder holder, final int position) {
        OrderCategoryModel item = itemList.get(position);

        holder.name.setText(item.getName());

        if (item.getProducts().size() != 0) initRV(item.getProducts(), item.isToppingDivided(), holder.toppingsRv);
    }

    private void initRV(final List<ItemModel> orderModels, boolean isToppingDivided, RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);
        OrderDetailsAdapter openOrderAdapter = new OrderDetailsAdapter(context, orderModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(openOrderAdapter);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
