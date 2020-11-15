package com.dalpak.bringit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dalpak.bringit.R;
import com.dalpak.bringit.models.BusinessModel;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OrderCategoryModel;

import java.util.ArrayList;
import java.util.List;

import static com.dalpak.bringit.utils.Constants.BUSINESS_TOPPING_TYPE_LAYER;

public class CategoriesDetailsAdapter extends RecyclerView.Adapter<CategoriesDetailsAdapter.CategoriesDetailsAdapterRvHolder> {

    private List<OrderCategoryModel> itemList;
    private Context context;
    private final String shape;

    class CategoriesDetailsAdapterRvHolder extends RecyclerView.ViewHolder {
        TextView name;
        RecyclerView toppingsRv;
        RecyclerView layersRv;

        CategoriesDetailsAdapterRvHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            toppingsRv = view.findViewById(R.id.rvTopping);
            layersRv = view.findViewById(R.id.rvLayers);
        }

    }


    public CategoriesDetailsAdapter(Context context, List<OrderCategoryModel> itemList, String shape) {
        this.itemList = itemList;
        this.context = context;
        this.shape = shape;
    }


    @Override
    public CategoriesDetailsAdapterRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoriesDetailsAdapterRvHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final CategoriesDetailsAdapterRvHolder holder, final int position) {
        OrderCategoryModel item = itemList.get(position);

        holder.name.setText(item.getName());

        if (item.getProducts().size() != 0)
            initRV(item.getProducts(), shape, item.isToppingDivided(), holder.toppingsRv);

        if (item.getProducts().get(0).getLocation() != null && item.isToppingDivided() &&
                BusinessModel.getInstance().getTopping_method_display().equals(BUSINESS_TOPPING_TYPE_LAYER))
            initLayerRV(item.getProducts(), holder.layersRv);
    }

    private void initRV(final List<ItemModel> orderModels, String shape, boolean isToppingDivided, RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);

        List<ItemModel> groupedList = new ArrayList<>();
        if (!isToppingDivided) {
            for (ItemModel oldItem : orderModels) {
//                oldItem.setCount(1);
                boolean isNew = true;
                for (ItemModel groupItem : groupedList) {
                    if (groupItem.getName().equals(oldItem.getName()) &&
                            groupItem.getPrice().equals(oldItem.getPrice())) {
//                        groupItem.setCount(groupItem.getCount() + 1);
                        isNew = false;
                        break;
                    }
                }
                if (isNew) groupedList.add(oldItem);
            }
        } else groupedList = orderModels;

        OrderDetailsAdapter openOrderAdapter = new OrderDetailsAdapter(context, groupedList, shape, isToppingDivided);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(openOrderAdapter);
    }

    private void initLayerRV(final List<ItemModel> orderModels, RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);

        ArrayList<String> layerPrices = new ArrayList<>();
        for (ItemModel itemModel : orderModels)
            if (!itemModel.getPrice().equals("0.00") && !itemModel.getPrice().equals("0")) layerPrices.add(itemModel.getPrice());
        LayerAdapter layerAdapter = new LayerAdapter(context, layerPrices);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(layerAdapter);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
