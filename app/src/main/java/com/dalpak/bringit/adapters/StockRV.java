package com.dalpak.bringit.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dalpak.bringit.R;
import com.dalpak.bringit.dialog.DialogWarningRemoveStock;
import com.dalpak.bringit.models.StockModel;
import com.dalpak.bringit.utils.Constants;
import com.dalpak.bringit.utils.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class StockRV extends RecyclerView.Adapter<StockRV.StockRVHolder> {

    private List<StockModel> itemList;
    private Context context;
    private View itemView;

    class StockRVHolder extends RecyclerView.ViewHolder {
        TextView name, pickupPrice, shippingPrice, inStockTvClick;
        View view;
        ImageView itemImage;

        StockRVHolder(View view) {
            super(view);
            this.view = view;
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
        if(viewType == 0){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_deal_item, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent, false);
        }
        return new StockRV.StockRVHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StockRV.StockRVHolder holder, final int position) {
        holder.name.setText(itemList.get(position).getName());
        holder.shippingPrice.setText(itemList.get(position).getDelivery_price() == null ? itemList.get(position).getDefault_price() + " שקל " : itemList.get(position).getDelivery_price() + " שקל");
        holder.pickupPrice.setText(itemList.get(position).getPickup_price() + " שקל");
        if (itemList.get(position).getObject_type().equals("food") || itemList.get(position).getObject_type().equals("deal")) {
            holder.itemImage.setVisibility(View.GONE);
        } else {
            String url = Constants.IMAGES_BASE_URL + itemList.get(position).getObject_type() + "/" + itemList.get(position).getPicture();
            Picasso.with(context).load(url).into(holder.itemImage);
            holder.itemImage.setVisibility(View.VISIBLE);

        }
        if (itemList.get(position).isObject_status()) {
            holder.inStockTvClick.setText("במלאי");
          //  holder.inStockTvClick.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green_00c37c)));
        } else {
            holder.inStockTvClick.setText("לא במלאי");
           // holder.inStockTvClick.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.red_ff7171)));
        }
        holder.inStockTvClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request.updateItemPrice(context, itemList.get(position), new Request.RequestJsonCallBack() {
                    @Override
                    public void onDataDone(JSONObject jsonObject) {
                        itemList.get(position).setObject_status(!itemList.get(position).isObject_status());
                        try {
                            if(jsonObject.has("dealsDisabled") && jsonObject.getBoolean("dealsDisabled")){
                                    FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
                                    DialogWarningRemoveStock alertDialog = DialogWarningRemoveStock.newInstance(getRemoveItems(jsonObject));
                                alertDialog.show(fm, "fragment_edit_name");

                            }
                            notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
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
        if (itemList.get(position).getObject_type().equals("deal")) {
            return 0;
        } else return 1;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
