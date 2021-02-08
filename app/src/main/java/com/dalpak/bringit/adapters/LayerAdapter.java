package com.dalpak.bringit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dalpak.bringit.R;

import java.util.List;
import java.util.Locale;

public class LayerAdapter extends RecyclerView.Adapter<LayerAdapter.ViewHolder> {

    private List<String> itemList;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView layerName;
        TextView layerPrice;

        ViewHolder(View view) {
            super(view);
            layerName = view.findViewById(R.id.name);
            layerPrice = view.findViewById(R.id.amount);
        }
    }

    public LayerAdapter(Context context, List<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_item_layer, parent, false);
        return new LayerAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.layerName.setText(String.format("שכבת תוספות %d", position + 1));
        holder.layerPrice.setText(String.format(Locale.US, "%s %.2f", context.getResources().getString(R.string.shekel), Double.parseDouble(itemList.get(position))));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

