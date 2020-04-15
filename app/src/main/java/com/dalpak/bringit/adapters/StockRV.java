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
import com.dalpak.bringit.dialog.DialogWarningRemoveStock;
import com.dalpak.bringit.models.StockModel;
import com.dalpak.bringit.utils.Constants;
import com.dalpak.bringit.utils.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class StockRV extends RecyclerView.Adapter<StockRV.StockRVHolder> {

    private List<StockModel> itemList;
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

    public StockRV(Context context, List<StockModel> itemList) {
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
        StockModel item = itemList.get(position);

        holder.name.setText(item.getName());
        holder.shippingPrice.setText(item.getDelivery_price() == null ? item.getDefault_price() + " שקל " : item.getDelivery_price() + " שקל");
        holder.pickupPrice.setText(item.getPickup_price() + " שקל");
        if (item.getObject_type().equals("food") || item.getObject_type().equals("deal")) {
            holder.itemImage.setVisibility(View.GONE);
        } else {
            String url = Constants.IMAGES_BASE_URL + item.getObject_type() + "/" + item.getPicture();
            Glide.with(context)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_placeholder)
                    .centerInside()
                    .into(holder.itemImage);
            holder.itemImage.setVisibility(View.VISIBLE);

        }
        if (item.isObject_status()) {
            holder.inStockTvClick.setText("במלאי");
            //  holder.inStockTvClick.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green_00c37c)));
        } else {
            holder.inStockTvClick.setText("לא במלאי");
            // holder.inStockTvClick.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red_ff7171)));
        }
        holder.inStockTvClick.setOnClickListener(v ->
                Request.getInstance().updateItemPrice(context, item, jsonObject -> {
                    item.setObject_status(!item.isObject_status());
                    try {
                        if (jsonObject.has("dealsDisabled") && jsonObject.getBoolean("dealsDisabled")) {
                            FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                            DialogWarningRemoveStock alertDialog = DialogWarningRemoveStock.newInstance(getRemoveItems(jsonObject));
                            alertDialog.show(fm, "fragment_edit_name");

                        }
                        notifyItemChanged(position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }));
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
        if (itemList.get(position).getObject_type().equals("deal")) {
            return 0;
        } else return 1;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
