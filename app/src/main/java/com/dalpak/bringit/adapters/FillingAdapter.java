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

import androidx.recyclerview.widget.RecyclerView;

public class FillingAdapter extends RecyclerView.Adapter<FillingAdapter.ViewHolder> {

    private List<ItemModel> itemList;
    private boolean isEdited;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fillingName;

        ViewHolder(View view) {
            super(view);
            fillingName = view.findViewById(R.id.filling_name);
        }
    }

    public FillingAdapter(Context context, List<ItemModel> itemList, boolean isEdited) {
        this.context = context;
        this.itemList = itemList;
        this.isEdited = isEdited;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_item_filling, parent, false);
        return new FillingAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.fillingName.setText(itemList.get(position).getName());
        holder.fillingName.setTextColor(isEdited ? Color.WHITE : Color.BLACK);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

