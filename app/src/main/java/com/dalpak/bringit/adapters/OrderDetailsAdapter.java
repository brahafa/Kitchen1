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
import com.dalpak.bringit.models.BusinessModel;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OrderCategoryModel;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.dalpak.bringit.utils.Constants.BUSINESS_TOPPING_TYPE_LAYER;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_ADDITIONAL_OFFER;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_DEAL;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_DRINK;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_PIZZA;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_TOPPING;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_BL;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_BR;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_FULL;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_LH;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_RH;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_TL;
import static com.dalpak.bringit.utils.Constants.PIZZA_TYPE_TR;

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
        ImageView ivToppingLocation;
        CardView parent;

        OrderDetailsHolderTopping(View view) {
            super(view);

            parent = view.findViewById(R.id.parent);
            name = view.findViewById(R.id.name);
            amount = view.findViewById(R.id.amount);
            ivToppingLocation = view.findViewById(R.id.iv_topping_location);

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
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_order_details, parent, false);
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

            if (item.getLocation() == null ||
                    !BusinessModel.getInstance().getTopping_method_name().equals(BUSINESS_TOPPING_TYPE_LAYER))
                setItemPrice(holder2.amount, item);

            if (item.getLocation() != null) holder2.ivToppingLocation.setImageResource(getImageRes(item.getLocation()));

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
                case ITEM_TYPE_DEAL:
//                    imageUrl = Constants.FOOD_URL + item.getItem_picture(); //todo ask for pictures
                    placeholderRes = R.drawable.ic_ph_deal;
                    break;
            }
            Glide.with(context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(placeholderRes)
                    .into(holder1.itemImage);

            if (item.getProducts() != null) initDealRV(item.getProducts(), holder1.rvFillings);
            else if (item.getCategories().size() != 0) initRV(item.getCategories(), holder1.rvFillings);
        }
    }

    private void initRV(final List<OrderCategoryModel> categoryModels, RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);
        CategoriesDetailsAdapter categoriesDetailsAdapter = new CategoriesDetailsAdapter(context, categoryModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(categoriesDetailsAdapter);
    }

    private void initDealRV(final List<ItemModel> orderModels, RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);
        OrderDetailsAdapter orderDetailsAdapter = new OrderDetailsAdapter(context, orderModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(orderDetailsAdapter);
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

    private int getImageRes(String viewType) {
        int imageRes = R.drawable.ic_pizza_full_active;
        switch (viewType) {
            case PIZZA_TYPE_FULL:
                imageRes = R.drawable.ic_pizza_full_active;
                break;
            case PIZZA_TYPE_RH:
                imageRes = R.drawable.ic_pizza_rh_active;
                break;
            case PIZZA_TYPE_LH:
                imageRes = R.drawable.ic_pizza_lh_active;
                break;
            case PIZZA_TYPE_TR:
                imageRes = R.drawable.ic_pizza_tr_cart;
                break;
            case PIZZA_TYPE_TL:
                imageRes = R.drawable.ic_pizza_tl_cart;
                break;
            case PIZZA_TYPE_BR:
                imageRes = R.drawable.ic_pizza_br_cart;
                break;
            case PIZZA_TYPE_BL:
                imageRes = R.drawable.ic_pizza_bl_cart;
                break;
        }
        return imageRes;
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

