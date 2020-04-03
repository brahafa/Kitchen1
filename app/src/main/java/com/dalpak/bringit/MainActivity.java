package com.dalpak.bringit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dalpak.bringit.dialog.DialogOpenOrder;
import com.dalpak.bringit.dialog.PasswordDialog;
import com.dalpak.bringit.fragments.MainFragment;
import com.dalpak.bringit.fragments.StockFragment;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.models.StockModel;
import com.dalpak.bringit.utils.Constants;
import com.dalpak.bringit.utils.Request;
import com.dalpak.bringit.utils.SharePref;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView nameTV, stockTvClick, pizzaStock, sailStock, drinkStock, additionsStock, spacialStock, additionalStock;
    RelativeLayout backRL;
    LinearLayout menuStockLayout;
    private MainFragment fragment;
    List<StockModel> stockModelList;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initListeners();
        openPasswordDialog();
    }

    private void initUI() {
        nameTV = findViewById(R.id.nameTV);
        sailStock = findViewById(R.id.sail_stock);
        additionsStock = findViewById(R.id.additions_stock);
        spacialStock = findViewById(R.id.spacial_additions_stock);
        additionalStock = findViewById(R.id.additional_stock);
        drinkStock = findViewById(R.id.drink_stock);
        pizzaStock = findViewById(R.id.pizza_stock);
        stockTvClick = findViewById(R.id.stock_tv_click);
        menuStockLayout = findViewById(R.id.menu_stock);
        backRL = findViewById(R.id.backRL);
        fragment = new MainFragment();
        stockModelList = new ArrayList<>();
        gson = new Gson();
        openFragment(fragment, "Main");
    }

    private void initListeners() {
        sailStock.setOnClickListener(v -> openStockFragment("deal"));
        additionsStock.setOnClickListener(v -> openStockFragment("topping")); // fixme: change to open right fragment
        spacialStock.setOnClickListener(v -> openStockFragment("topping"));
        additionalStock.setOnClickListener(v -> openStockFragment("additionalOffer"));
        drinkStock.setOnClickListener(v -> openStockFragment("drink"));
        pizzaStock.setOnClickListener(v -> openStockFragment("food"));

        stockTvClick.setOnClickListener(v -> {
            if (menuStockLayout.getVisibility() == View.VISIBLE) {
                menuStockLayout.setVisibility(View.GONE);
            } else {
                menuStockLayout.setVisibility(View.VISIBLE);
            }
        });

        backRL.setOnClickListener(v -> {
            Fragment f = getFragmentManager().findFragmentById(R.id.fragment_container);
            if (f instanceof MainFragment) {
                openPasswordDialog();
            } else {
                getFragmentManager().popBackStack();
            }


        });
    }

    public void setName() {
        if (SharePref.getInstance(this).getData(Constants.NAME_PREF) != null) {
            nameTV.setText(SharePref.getInstance(this).getData(Constants.NAME_PREF) + " שלום ");
        }
    }

    private void openStockFragment(String type) {
        Request.loadBusinessItems(getApplication(), type, jsonObject -> {
            stockModelList.clear();
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("message");
                for (int i = 0; i < jsonArray.length(); i++) {
                    stockModelList.add(gson.fromJson(jsonArray.getString(i), StockModel.class));
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("valuesArray", (Serializable) stockModelList);
                StockFragment stockFragment = new StockFragment();
                stockFragment.setArguments(bundle);
                openFragment(stockFragment, "stock");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void openPasswordDialog() {
        PasswordDialog passwordDialog = new PasswordDialog(this);
        passwordDialog.setCancelable(false);
        passwordDialog.show();

        passwordDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setName();
                Request.getAllOrders(getApplication(), new Request.RequestJsonCallBack() {
                    @Override
                    public void onDataDone(JSONObject jsonObject) {
                        fragment.initAllRV(jsonObject);
                    }
                });
            }
        });
    }

    public void openOrderDialog(OpenOrderModel orderModel) {
        DialogOpenOrder d = new DialogOpenOrder(this, orderModel);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        d.show();
        d.getWindow().setAttributes(lp);
    }

    private void openFragment(Fragment fragmentToOpen, String tag) {
        menuStockLayout.setVisibility(View.GONE);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragmentToOpen, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
