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

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_ADDITIONAL_OFFER;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_DRINK;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_PIZZA;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_TOPPING;

public class OpenOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemModel> itemList;
    private Context context;
    AdapterCallback adapterCallback;
    private View itemView;

    class OpenOrderHolder extends RecyclerView.ViewHolder {
        TextView name, amount;
        ImageView itemImage;
        RecyclerView rvFillings;
        TextView tvCancel;
        CardView parent;

        OpenOrderHolder(View view) {
            super(view);
            tvCancel = view.findViewById(R.id.tv_cancel);
            parent = view.findViewById(R.id.parent);
            name = view.findViewById(R.id.name);
            amount = view.findViewById(R.id.order_time);
            itemImage = view.findViewById(R.id.itemImage);
            rvFillings = view.findViewById(R.id.rv_fillings);
        }
    }

    class OpenOrderHolderTopping extends RecyclerView.ViewHolder {
        TextView name, amount;
        ImageView itemImage, tl, tr, bl, br;
        CardView parent;
        TextView tvCancel;

        OpenOrderHolderTopping(View view) {
            super(view);

            tvCancel = view.findViewById(R.id.tv_cancel);
            parent = view.findViewById(R.id.parent);
            name = view.findViewById(R.id.name);
            amount = view.findViewById(R.id.order_time);
            itemImage = view.findViewById(R.id.itemImage);
            tl = view.findViewById(R.id.tl);
            tr = view.findViewById(R.id.tr);
            bl = view.findViewById(R.id.bl);
            br = view.findViewById(R.id.br);

        }
    }

    public OpenOrderAdapter(Context context, List<ItemModel> itemList, AdapterCallback adapterCallback) {
        this.itemList = itemList;
        this.context = context;
        this.adapterCallback = adapterCallback;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {// topping type
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topping_item, parent, false);
            return new OpenOrderAdapter.OpenOrderHolderTopping(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.open_order_item, parent, false);
            return new OpenOrderAdapter.OpenOrderHolder(itemView);
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ItemModel item = itemList.get(position);

        OpenOrderHolder holder1;
        OpenOrderHolderTopping holder2;

        if (holder.getItemViewType() == 0) {
            holder2 = (OpenOrderHolderTopping) holder;
            holder2.name.setText(item.getName());

            initToppingColor(item, holder2);

//            todo waiting for change types
//            if (item.getChange_type() != null)
//                switch (item.getChange_type()) {
//                    case "DELETED":
//                        holder2.tvCancel.setVisibility(View.VISIBLE);
//                        break;
//                    case "NEW":
//                        holder2.parent.setCardBackgroundColor(Color.parseColor("#12c395"));
//                        holder2.name.setTextColor(Color.WHITE);
//                        break;
//                }
        } else {
            holder1 = (OpenOrderHolder) holder;

//            todo waiting for change types
//            if (item.getChange_type() != null)
//                switch (item.getChange_type()) {
//                    case "DELETED":
//                        holder1.tvCancel.setVisibility(View.VISIBLE);
//                        break;
//                    case "NEW":
//                        holder1.parent.setCardBackgroundColor(Color.parseColor("#12c395"));
//                        holder1.name.setTextColor(Color.WHITE);
//                        break;
//                }

            holder1.name.setText(item.getName());

            if (item.getCategories() != null && item.getCategories().size() != 0) {
                holder1.rvFillings.setVisibility(View.VISIBLE);
                holder1.rvFillings.setLayoutManager(new LinearLayoutManager(context));
                FillingAdapter fillingAdapter =
                        new FillingAdapter(context, item.getCategories().get(0).getProducts(),
                                /*item.getChange_type() != null && item.getChange_type().equals("NEW")*/false); //todo waiting for change types

                holder1.rvFillings.setAdapter(fillingAdapter);
            }

            String imageUrl = "";
            int placeholderRes = R.drawable.ic_placeholder;
            switch (item.getTypeName()) {
                case ITEM_TYPE_DRINK:
//                    imageUrl = Constants.DRINKS_URL + item.getItem_picture(); //todo ask for pictures
                    placeholderRes = R.drawable.ic_ph_drink;
                    break;
                case ITEM_TYPE_ADDITIONAL_OFFER:
//                    imageUrl = Constants.ADDITIONAL_URL + item.getItem_picture(); //todo ask for pictures
                    placeholderRes = R.drawable.ic_ph_food;
                    break;
                case ITEM_TYPE_PIZZA:
//                    imageUrl = Constants.FOOD_URL + item.getItem_picture(); //todo ask for pictures
                    placeholderRes = R.drawable.ic_ph_pizza;
                    break;
            }
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(placeholderRes)
                    .into(holder1.itemImage);
        }


    }

    private void initToppingColor(ItemModel itemModel, OpenOrderHolderTopping holder) {
        if (itemModel.getLocation() != null)
            switch (itemModel.getLocation()) {
                case "full":
                    holder.tl.setImageResource(R.mipmap.squart_red1);
                    holder.tr.setImageResource(R.mipmap.squart_red2);
                    holder.bl.setImageResource(R.mipmap.squart_red4);
                    holder.br.setImageResource(R.mipmap.squart_red3);

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
        if (itemList.get(position).getTypeName().equals(ITEM_TYPE_TOPPING)) {
            return 0;
        } else return 1;
    }

    List<ItemModel> getList() {
        return itemList;
    }

}

