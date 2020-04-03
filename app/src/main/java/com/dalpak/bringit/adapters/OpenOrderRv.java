package com.dalpak.bringit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dalpak.bringit.R;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.utils.Constants;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//public class OpenOrderRv  {
//}
public class OpenOrderRv extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemModel> itemList;
    private Context context;
    AdapterCallback adapterCallback;
    private View itemView;

    class OpenOrderHolder extends RecyclerView.ViewHolder {
        TextView name, amount;
        ImageView itemImage, tl, tr, bl, br;
        RecyclerView rvFillings;
        View view;

        OpenOrderHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            amount = view.findViewById(R.id.order_time);
            itemImage = view.findViewById(R.id.itemImage);
            rvFillings = view.findViewById(R.id.rv_fillings);
        }
    }

    class OpenOrderHolderTopping extends RecyclerView.ViewHolder {
        TextView name, amount;
        ImageView itemImage, tl, tr, bl, br;
        View view;

        OpenOrderHolderTopping(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            amount = view.findViewById(R.id.order_time);
            itemImage = view.findViewById(R.id.itemImage);
            tl = view.findViewById(R.id.tl);
            tr = view.findViewById(R.id.tr);
            bl = view.findViewById(R.id.bl);
            br = view.findViewById(R.id.br);

        }
    }

    public OpenOrderRv(Context context, List<ItemModel> itemList, AdapterCallback adapterCallback) {
        this.itemList = itemList;
        this.context = context;
        this.adapterCallback = adapterCallback;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {// topping type
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topping_item, parent, false);
            return new OpenOrderRv.OpenOrderHolderTopping(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.open_order_item, parent, false);
            return new OpenOrderRv.OpenOrderHolder(itemView);
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        OpenOrderHolder holder1;
        OpenOrderHolderTopping holder2;
        if (holder.getItemViewType() == 0) {
            holder2 = (OpenOrderHolderTopping) holder;
            holder2.name.setText(itemList.get(position).getName());
            // holder2.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.topping_background)));
            initToppingColor(itemList.get(position), holder2);
        } else {
            holder1 = (OpenOrderHolder) holder;
            holder1.name.setText(itemList.get(position).getName());

            if (itemList.get(position).getItem_filling() != null) {
                holder1.rvFillings.setLayoutManager(new LinearLayoutManager(context));
                FillingAdapter fillingAdapter =
                        new FillingAdapter(context, itemList.get(position).getItem_filling());
                holder1.rvFillings.setAdapter(fillingAdapter);
            }

            String imageUrl = "";
            switch (itemList.get(position).get_ItemType()) {
                case "Drink":
                    imageUrl = Constants.DRINKS_URL + itemList.get(position).getItem_picture();
                    break;
                case "AdditionalOffer":
                    //   holder1.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                    imageUrl = Constants.ADDITIONAL_URL + itemList.get(position).getItem_picture();
                    break;
                case "Food":
                    //  holder1.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                    imageUrl = Constants.FOOD_URL + itemList.get(position).getItem_picture();
                    break;
            }
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder1.itemImage);
        }


    }

    private void initToppingColor(ItemModel itemModel, OpenOrderHolderTopping holder) {
        switch (itemModel.getLocation()) {
            case "full":
                holder.tl.setImageResource(R.mipmap.squart_red1);
                holder.tr.setImageResource(R.mipmap.squart_red2);
                holder.bl.setImageResource(R.mipmap.squart_red4);
                holder.br.setImageResource(R.mipmap.squart_red3);//

                break;
            case "rightHalfPizza":
                holder.tr.setImageResource(R.mipmap.squart_red2);
                holder.br.setImageResource(R.mipmap.squart_red3);
                holder.bl.setImageResource(R.mipmap.squart4);
                holder.tl.setImageResource(R.mipmap.squart1);
                break;
            case "leftHalfPizza":
                holder.tl.setImageResource(R.mipmap.squart_red1);
                holder.bl.setImageResource(R.mipmap.squart_red4);
                holder.tr.setImageResource(R.mipmap.squart2);
                holder.br.setImageResource(R.mipmap.squart3);
                break;
            case "br":
                holder.br.setImageResource(R.mipmap.squart_red3);
                holder.bl.setImageResource(R.mipmap.squart4);
                holder.tr.setImageResource(R.mipmap.squart2);
                holder.tl.setImageResource(R.mipmap.squart1);


                break;
            case "bl":
                holder.br.setImageResource(R.mipmap.squart3);
                holder.bl.setImageResource(R.mipmap.squart_red4);
                holder.tr.setImageResource(R.mipmap.squart2);
                holder.tl.setImageResource(R.mipmap.squart1);
                break;
            case "tr":
                holder.br.setImageResource(R.mipmap.squart3);
                holder.bl.setImageResource(R.mipmap.squart4);
                holder.tr.setImageResource(R.mipmap.squart_red2);
                holder.tl.setImageResource(R.mipmap.squart1);
                break;
            case "tl":
                holder.br.setImageResource(R.mipmap.squart3);
                holder.bl.setImageResource(R.mipmap.squart4);
                holder.tr.setImageResource(R.mipmap.squart2);
                holder.tl.setImageResource(R.mipmap.squart_red1);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface AdapterCallback {
        void onItemChoose(ItemModel itemModel);
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position).get_ItemType().equals("Topping")) {
            return 0;
        } else return 1;
    }

    List<ItemModel> getList() {
        return itemList;
    }

}

