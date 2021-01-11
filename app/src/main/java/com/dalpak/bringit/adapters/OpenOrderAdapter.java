package com.dalpak.bringit.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dalpak.bringit.R;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_ADDITIONAL_OFFER;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_DRINK;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_PIZZA;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_TOPPING;
import static com.dalpak.bringit.utils.Utils.getImageRes;
import static com.dalpak.bringit.utils.Utils.getImageResRect;

public class OpenOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemModel> itemList;
    private Context context;
    private String shape;
    private boolean isToppingDivided = true;
    private boolean isParentDeleted;
    AdapterCallback adapterCallback;

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
        ImageView ivToppingLocation;
        ImageView ivToppingLocationRect;
        CardView parent;
        TextView tvCancel;

        OpenOrderHolderTopping(View view) {
            super(view);

            tvCancel = view.findViewById(R.id.tv_cancel);
            parent = view.findViewById(R.id.parent);
            name = view.findViewById(R.id.name);
            amount = view.findViewById(R.id.order_time);
            ivToppingLocation = view.findViewById(R.id.iv_topping_location);
            ivToppingLocationRect = view.findViewById(R.id.iv_topping_location_rect);

        }
    }

    public OpenOrderAdapter(Context context, List<ItemModel> itemList, AdapterCallback adapterCallback) {
        this.itemList = itemList;
        this.context = context;
        this.adapterCallback = adapterCallback;
    }

    public OpenOrderAdapter(Context context, List<ItemModel> itemList, String shape, boolean isToppingDivided, boolean isParentDeleted, AdapterCallback adapterCallback) {
        this.itemList = itemList;
        this.context = context;
        this.shape = shape;
        this.isToppingDivided = isToppingDivided;
        this.isParentDeleted = isParentDeleted;
        this.adapterCallback = adapterCallback;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
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

        if (holder.getItemViewType() == 0) {// topping type
            holder2 = (OpenOrderHolderTopping) holder;

            String name = item.getName();
            if (item.getCount() > 1) name = name.concat(" x" + item.getCount());
            holder2.name.setText(name);

            switch (shape) {
                case Constants.PIZZA_TYPE_CIRCLE:
                    holder2.ivToppingLocation.setVisibility(isToppingDivided ? View.VISIBLE : View.GONE);
                    if (item.getLocation() != null)
                        holder2.ivToppingLocation.setImageResource(getImageRes(item.getLocation()));
                    break;
                case Constants.PIZZA_TYPE_RECTANGLE:
                    holder2.ivToppingLocationRect.setVisibility(isToppingDivided ? View.VISIBLE : View.GONE);
                    if (item.getLocation() != null)
                        holder2.ivToppingLocationRect.setImageResource(getImageResRect(item.getLocation()));
                    break;
                case Constants.PIZZA_TYPE_ONE_SLICE:
                    holder2.ivToppingLocation.setVisibility(isToppingDivided ? View.VISIBLE : View.GONE);
                    holder2.ivToppingLocation.setImageResource(R.drawable.ic_pizza_slice_active);
                    break;
            }

            if (!isParentDeleted) {
                if (item.isCanceled() || item.isDeleted()) {
                    holder2.tvCancel.setVisibility(View.VISIBLE);
                } else if (item.isNew()) {
                    holder2.parent.setCardBackgroundColor(Color.parseColor("#12c395"));
                    holder2.name.setTextColor(Color.BLACK);
                }
            }

        } else {
            holder1 = (OpenOrderHolder) holder;


            if (item.isCanceled() || item.isDeleted()) {
                holder1.tvCancel.setVisibility(View.VISIBLE);
            } else {
                holder1.tvCancel.setVisibility(View.GONE);
            }
            if (item.isNew()) {
                holder1.parent.setCardBackgroundColor(Color.parseColor("#12c395"));
                holder1.name.setTextColor(Color.BLACK);
            } else {
                holder1.parent.setCardBackgroundColor(Color.WHITE);
                holder1.name.setTextColor(context.getResources().getColor(R.color.text_color));
            }

            holder1.name.setText(item.getName());

            if (item.getCategories() != null && item.getCategories().size() != 0) {
                holder1.rvFillings.setVisibility(View.VISIBLE);
                holder1.rvFillings.setLayoutManager(new LinearLayoutManager(context));

                ArrayList<ItemModel> groupedList = new ArrayList<>();
                for (ItemModel oldItem : item.getCategories().get(0).getProducts()) {
                    boolean isNew = true;
                    for (ItemModel groupItem : groupedList) {
                        if (groupItem.getName().equals(oldItem.getName()) &&
                                groupItem.getPrice().equals(oldItem.getPrice())) {
                            groupItem.setCount(groupItem.getCount() + 1);
                            isNew = false;
                        }
                    }
                    if (isNew) groupedList.add(oldItem);
                }
                FillingAdapter fillingAdapter =
                        new FillingAdapter(context, groupedList,
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

