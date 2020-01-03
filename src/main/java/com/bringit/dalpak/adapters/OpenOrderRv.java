package com.bringit.dalpak.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bringit.dalpak.R;
import com.bringit.dalpak.models.ItemModel;

import java.util.List;

//public class OpenOrderRv  {
//}
public class OpenOrderRv extends RecyclerView.Adapter<OpenOrderRv.OpenOrderHolder> {

    private List<ItemModel> itemList;
    private Context context;
    AdapterCallback adapterCallback;

    class OpenOrderHolder extends RecyclerView.ViewHolder {
        TextView name, orderTime;
        ImageView deliveryImage;
        View view;

        OpenOrderHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            orderTime = view.findViewById(R.id.order_time);
            deliveryImage = view.findViewById(R.id.delivery_image);

        }

    }

    public OpenOrderRv(Context context, List<ItemModel> itemList, AdapterCallback adapterCallback) {
        this.itemList = itemList;
        this.context = context;
        this.adapterCallback = adapterCallback;
    }


    @Override
    public OpenOrderRv.OpenOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_order_item, parent, false);
        return new OpenOrderRv.OpenOrderHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final OpenOrderRv.OpenOrderHolder holder, final int position) {


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


