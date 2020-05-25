package com.dalpak.bringit;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.dalpak.bringit.databinding.ActivityMainBinding;
import com.dalpak.bringit.dialog.DialogOpenOrder;
import com.dalpak.bringit.dialog.PasswordDialog;
import com.dalpak.bringit.fragments.MainFragment;
import com.dalpak.bringit.fragments.StockFragment;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.models.StockModel;
import com.dalpak.bringit.utils.Constants;
import com.dalpak.bringit.utils.Request;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.dalpak.bringit.utils.SharedPrefs.getData;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainFragment fragment;
    List<StockModel> stockModelList;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        initUI();
        initListeners();
        openPasswordDialog(false);
    }

    private void initUI() {

        fragment = new MainFragment();
        stockModelList = new ArrayList<>();
        gson = new Gson();
        openFragment(fragment, "Main");
        checkBusinessStatus();
    }

    private void checkBusinessStatus(){
        Request.getInstance().checkBusinessStatus(this, isBusinessOpen -> {
            changeBusinessStatus(!isBusinessOpen);
        });
    }

    private void initListeners() {
        binding.stockMenu.sailStock.setOnClickListener(v -> openStockFragment("deal"));
        binding.stockMenu.extraStock.setOnClickListener(v -> openStockFragment("topping"));
        binding.stockMenu.spacialAdditionsStock.setOnClickListener(v -> openStockFragment("special"));
        binding.stockMenu.additionalStock.setOnClickListener(v -> openStockFragment("additionalOffer"));
        binding.stockMenu.drinkStock.setOnClickListener(v -> openStockFragment("drink"));
        binding.stockMenu.pizzaStock.setOnClickListener(v -> openStockFragment("food"));

        binding.stockTvClick.setOnClickListener(v -> {
            if (binding.stockMenu.getRoot().getVisibility() == View.VISIBLE) {
                closeStockMenu();
            } else {
                openStockMenu();
            }
        });

        binding.swLayout.setOnClickListener(v -> {
            Request.getInstance().changeBusinessStatus(this, binding.swWebsite.isChecked() ? "close" : "open", () -> {
                changeBusinessStatus(binding.swWebsite.isChecked());
            });
        });

        binding.backToMain.setOnClickListener(v -> {
            popBackStackTillEntry(1);
        });

        binding.backRL.setOnClickListener(v -> {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (f instanceof MainFragment) {
                openPasswordDialog(true);
            } else {
                getSupportFragmentManager().popBackStack();
            }
        });
        binding.nameTV.setOnClickListener(v -> {
            openPasswordDialog(true);
        });
        binding.coverView.setOnClickListener(v -> {
            if (binding.stockMenu.getRoot().getVisibility() == View.VISIBLE) {
                closeStockMenu();
            }
        });
    }

    private void changeBusinessStatus(boolean status) {
        if (status) {
            binding.titleSwitch.setText("אתר לא פעיל");
            binding.swWebsite.setChecked(false);

        } else {
            binding.titleSwitch.setText("אתר פעיל");
            binding.swWebsite.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (f instanceof MainFragment) {
            openPasswordDialog(true);
        } else {
            super.onBackPressed();
        }
    }

    private void closeStockMenu() {
        binding.stockMenu.getRoot().setVisibility(View.GONE);
        binding.coverView.setVisibility(View.GONE);
    }

    private void openStockMenu() {
        binding.stockMenu.getRoot().setVisibility(View.VISIBLE);
        binding.coverView.setVisibility(View.VISIBLE);
    }


    public void setName() {
        if (!getData(Constants.NAME_PREF).equals("")) {
            binding.nameTV.setText(getData(Constants.NAME_PREF) + " שלום ");
        }
    }

    private void openStockFragment(String type) {
        Request.getInstance().loadBusinessItems(getApplication(), type, jsonObject -> {
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

    public void openPasswordDialog(boolean needLogout) {
        if (needLogout) {
            Request.getInstance().workerLogout(this);
        }
        PasswordDialog passwordDialog = new PasswordDialog(this);
        passwordDialog.setCancelable(false);
        passwordDialog.show();

        passwordDialog.setOnDismissListener(dialog -> {
            setName();
            fragment.startBoardUpdates();
            checkBusinessStatus();
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
        closeStockMenu();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, fragmentToOpen, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void popBackStackTillEntry(int entryIndex) {

        if (getSupportFragmentManager().getBackStackEntryCount() <= entryIndex) {
            return;
        }
        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(entryIndex);
        getSupportFragmentManager().popBackStackImmediate(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
