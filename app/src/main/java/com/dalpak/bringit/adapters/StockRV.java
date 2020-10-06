package com.dalpak.bringit.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dalpak.bringit.R;
import com.dalpak.bringit.models.ProductItemModel;
import com.dalpak.bringit.utils.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class StockRV extends RecyclerView.Adapter<StockRV.StockRVHolder> {

    private ArrayList<ProductItemModel> itemList;
    private Context context;
    private View itemView;

    class StockRVHolder extends RecyclerView.ViewHolder {
        TextView name, pickupPrice, shippingPrice, inStockTvClick;
        ImageView itemImage;

        StockRVHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            itemImage = view.findViewById(R.id.stock_image);
            pickupPrice = view.findViewById(R.id.pickup_price);
            shippingPrice = view.findViewById(R.id.shipping_price);
            inStockTvClick = view.findViewById(R.id.in_stock_tv_click);

        }
    }

    public StockRV(Context context, ArrayList<ProductItemModel> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public StockRV.StockRVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_deal_item, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent, false);
        }
        return new StockRV.StockRVHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StockRV.StockRVHolder holder, final int position) {
        ProductItemModel item = itemList.get(position);

        holder.name.setText(item.getName());
        holder.shippingPrice.setText(item.getDeliveryPrice() + " שקל");
        holder.pickupPrice.setText(item.getNotDeliveryPrice() + " שקל");
        if (item.getTypeName().equals("food") || item.getTypeName().equals("deal")) {
            holder.itemImage.setVisibility(View.GONE);
        } else {

            if (item.getTypeName().equals("topping")) {
                int id = context.getResources().getIdentifier(item.getPicture().replace(".png", ""), "mipmap", context.getPackageName());
                if (id > 0) {
                    Drawable drawable = context.getResources().getDrawable(id);
                    holder.itemImage.setImageDrawable(drawable);
                }
            } else {
//                String url = Constants.IMAGES_BASE_URL + item.getTypeName() + "/" + item.getPicture();
                Glide.with(context)
                        .load(item.getImageUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(item.getTypeName().equals("drink")
                                ? R.drawable.ic_ph_drink
                                : R.drawable.ic_ph_food)
                        .centerInside()
                        .into(holder.itemImage);
                holder.itemImage.setVisibility(View.VISIBLE);
            }


        }

        if (item.getInInventory().equals("1")) {
            holder.inStockTvClick.setText("במלאי");
            holder.inStockTvClick.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_background));
        } else {
            holder.inStockTvClick.setText("לא במלאי");
            holder.inStockTvClick.setBackground(ContextCompat.getDrawable(context, R.drawable.btn_background_not_in_stock));
        }
        holder.inStockTvClick.setOnClickListener(v -> {

            Request.getInstance().updateProductStatus(context, item, isDataSuccess -> {
                item.setInInventory(String.valueOf(1 ^ Integer.parseInt(item.getInInventory())));

//                FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
//                DialogWarningRemoveStock alertDialog = DialogWarningRemoveStock.newInstance(getRemoveItems(jsonObject));
//                alertDialog.show(fm, "fragment_edit_name");

                notifyItemChanged(position);
            });
        });

    }

    private String getRemoveItems(JSONObject jsonObject) {
        String itemsList = "";

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("changedItems");
            for (int i = 0; i < jsonArray.length(); i++) {
                itemsList += ((JSONObject) jsonArray.get(i)).getString("name");
                itemsList += "\n";
                itemsList += "\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return itemsList;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position).getTypeName().equals("deal")) {
            return 0;
        } else return 1;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
