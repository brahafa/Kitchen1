package com.dalpak.bringit.adapters;

import android.content.Context;
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
import com.dalpak.bringit.models.BusinessModel;
import com.dalpak.bringit.models.ItemModel;
import com.dalpak.bringit.models.OrderCategoryModel;
import com.dalpak.bringit.utils.Constants;

import java.util.List;

import static com.dalpak.bringit.utils.Constants.BUSINESS_TOPPING_TYPE_LAYER;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_ADDITIONAL_OFFER;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_DEAL;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_DRINK;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_PIZZA;
import static com.dalpak.bringit.utils.Constants.ITEM_TYPE_TOPPING;
import static com.dalpak.bringit.utils.Utils.getImageRes;
import static com.dalpak.bringit.utils.Utils.getImageResRect;

public class OrderDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemModel> itemList;
    private Context context;
    private String shape;
    private boolean isToppingDivided;

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
        ImageView ivToppingLocationRect;
        CardView parent;

        OrderDetailsHolderTopping(View view) {
            super(view);

            parent = view.findViewById(R.id.parent);
            name = view.findViewById(R.id.name);
            amount = view.findViewById(R.id.amount);
            ivToppingLocation = view.findViewById(R.id.iv_topping_location);
            ivToppingLocationRect = view.findViewById(R.id.iv_topping_location_rect);

        }
    }

    public OrderDetailsAdapter(Context context, List<ItemModel> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    public OrderDetailsAdapter(Context context, List<ItemModel> itemList, String shape, boolean isToppingDivided) {
        this.itemList = itemList;
        this.context = context;
        this.shape = shape;
        this.isToppingDivided = isToppingDivided;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
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

            String name = item.getName();
            if (item.getCount() > 1) name = name.concat(" x" + item.getCount());
            holder2.name.setText(name);

            if (item.getLocation() == null ||
                    !(BusinessModel.getInstance().getTopping_method_display().equals(BUSINESS_TOPPING_TYPE_LAYER) && isToppingDivided))
                setItemPrice(holder2.amount, item);

            if (shape != null)
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
            else if (item.getCategories().size() != 0)
                initRV(item.getCategories(), item.getShape(), holder1.rvFillings);
        }
    }

    private void initRV(final List<OrderCategoryModel> categoryModels, String shape, RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);
        CategoriesDetailsAdapter categoriesDetailsAdapter = new CategoriesDetailsAdapter(context, categoryModels, shape);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(categoriesDetailsAdapter);
    }

    private void initDealRV(final List<ItemModel> orderModels, RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);
        OrderDetailsAdapter orderDetailsAdapter = new OrderDetailsAdapter(context, orderModels, shape, isToppingDivided);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(orderDetailsAdapter);
    }

    private void setItemPrice(TextView amount, ItemModel item) {
        amount.setVisibility(View.VISIBLE);
        if (item.getPrice().equals("0")) {
            amount.setText("במבצע");
            amount.setTextColor(context.getResources().getColor(R.color.red_F52E2E));
        } else {
            String multiplier = item.getCount() > 1 ? "x" + item.getCount() + " " : "";

            amount.setText(String.format("%s%s %s", multiplier, context.getResources().getString(R.string.shekel), item.getPrice()));
            amount.setTextColor(context.getResources().getColor(R.color.blue_2060e5));
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

