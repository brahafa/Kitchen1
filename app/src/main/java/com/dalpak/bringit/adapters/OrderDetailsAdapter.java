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
import androidx.recyclerview.widget.RecyclerView;

import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_ADDITIONAL_OFFER;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_DRINK;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_PIZZA;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_TOPPING;

public class OrderDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemModel> itemList;
    private Context context;
    private View itemView;

    class OrderDetailsHolder extends RecyclerView.ViewHolder {
        TextView name, amount;
        ImageView itemImage;
        RecyclerView rvFillings;
        TextView tvCancel;
        CardView parent;

        OrderDetailsHolder(View view) {
            super(view);
            tvCancel = view.findViewById(R.id.tv_cancel);
            parent = view.findViewById(R.id.parent);
            name = view.findViewById(R.id.name);
            amount = view.findViewById(R.id.amount);
            itemImage = view.findViewById(R.id.itemImage);
            rvFillings = view.findViewById(R.id.rv_fillings);
        }
    }

    class OrderDetailsHolderTopping extends RecyclerView.ViewHolder {
        TextView name, amount;
        ImageView itemImage, tl, tr, bl, br;
        CardView parent;

        OrderDetailsHolderTopping(View view) {
            super(view);

            parent = view.findViewById(R.id.parent);
            name = view.findViewById(R.id.name);
            amount = view.findViewById(R.id.amount);
            itemImage = view.findViewById(R.id.itemImage);
            tl = view.findViewById(R.id.tl);
            tr = view.findViewById(R.id.tr);
            bl = view.findViewById(R.id.bl);
            br = view.findViewById(R.id.br);

        }
    }

    public OrderDetailsAdapter(Context context, List<ItemModel> itemList) {
        this.itemList = itemList;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {// topping type
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topping_item, parent, false);
            return new OrderDetailsAdapter.OrderDetailsHolderTopping(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.open_order_item, parent, false);
            return new OrderDetailsAdapter.OrderDetailsHolder(itemView);
        }

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ItemModel item = itemList.get(position);

        OrderDetailsHolder holder1;
        OrderDetailsHolderTopping holder2;

        if (holder.getItemViewType() == 0) {

            holder2 = (OrderDetailsHolderTopping) holder;
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder2.parent.getLayoutParams();
            layoutParams.setMargins(0, 0, 60, 0);
            holder2.parent.requestLayout();
            holder2.name.setText(item.getName());
            setItemPrice(holder2.amount, item);
            initToppingColor(item, holder2);

        } else {
            holder1 = (OrderDetailsHolder) holder;
//            fixme father id
//            if (item.getFather_id() != null) {
//                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder1.parent.getLayoutParams();
//                layoutParams.setMargins(0, 0, 0, 0);
//                holder1.parent.requestLayout();
//            }

            holder1.parent.setBackground(null);
            setItemPrice(holder1.amount, item);

            holder1.name.setText(item.getName());
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

    private void setItemPrice(TextView amount, ItemModel item) {
        amount.setVisibility(View.VISIBLE);
        if (item.getPrice().equals("0")) {
            amount.setText("במבצע");
            amount.setTextColor(context.getResources().getColor(R.color.red_F52E2E));
        } else {
            amount.setText(String.format("%s %s", item.getPrice(), context.getResources().getString(R.string.shekel)));
            amount.setTextColor(context.getResources().getColor(R.color.blue_2060e5));
        }

    }

    private void initToppingColor(ItemModel itemModel, OrderDetailsHolderTopping holder) {
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

