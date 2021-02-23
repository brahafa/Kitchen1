package com.dalpak.bringit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dalpak.bringit.databinding.ActivityMainBinding;
import com.dalpak.bringit.dialog.DialogOpenOrder;
import com.dalpak.bringit.dialog.ExitDialog;
import com.dalpak.bringit.dialog.PasswordDialog;
import com.dalpak.bringit.fragments.MainFragment;
import com.dalpak.bringit.fragments.StockFragment;
import com.dalpak.bringit.models.OpenOrderModel;
import com.dalpak.bringit.models.ProductItemModel;
import com.dalpak.bringit.network.Request;
import com.dalpak.bringit.utils.Constants;
import com.dalpak.bringit.utils.MyExceptionHandler;
import com.dalpak.bringit.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static com.dalpak.bringit.utils.SharedPrefs.getData;
import static com.dalpak.bringit.utils.SharedPrefs.saveData;

public class MainActivity extends AppCompatActivity implements MainFragment.onBusinessStatusCheckListener {
    private ActivityMainBinding binding;
    private MainFragment fragment;
    ArrayList<ProductItemModel> stockModelList;
    private Gson gson;

    private final int TYPE_SWITCH_BUSINESS = 1;
    public DialogOpenOrder dialogOpenOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        setContentView(binding.getRoot());
        initUI();
        initListeners();
        openPasswordDialog(false);
    }

    private void initUI() {
        binding.version.setText(Utils.getVersionApp(this));
        fragment = new MainFragment();
        stockModelList = new ArrayList<>();
        gson = new Gson();
        openFragment(fragment, "Main");
        checkBusinessStatus();
    }

    private void initListeners() {
        binding.stockMenu.sailStock.setOnClickListener(v -> openStockFragment("deal"));
        binding.stockMenu.extraStock.setOnClickListener(v -> openStockFragment("topping"));
        binding.stockMenu.spacialAdditionsStock.setOnClickListener(v -> openStockFragment("special"));
        binding.stockMenu.additionalStock.setOnClickListener(v -> openStockFragment("additionalOffer"));
        binding.stockMenu.drinkStock.setOnClickListener(v -> openStockFragment("drink"));
        binding.stockMenu.pizzaStock.setOnClickListener(v -> openStockFragment("pizza"));

        binding.stockTvClick.setOnClickListener(v -> {
            if (binding.stockMenu.getRoot().getVisibility() == View.VISIBLE) {
                closeStockMenu();
            } else {
                openStockMenu();
            }
        });

        binding.swLayout.setOnClickListener(v -> openPasswordDialog(false, TYPE_SWITCH_BUSINESS));

        binding.backToMain.setOnClickListener(v -> popBackStackTillEntry(1));

        binding.backRL.setOnClickListener(v -> {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (f instanceof MainFragment) {
                openPasswordDialog(true);
            } else {
                getSupportFragmentManager().popBackStack();
            }
        });
        binding.nameTV.setOnClickListener(v -> openPasswordDialog(true));
        binding.exitTV.setOnClickListener(v -> openExitDialog());

        binding.coverView.setOnClickListener(v -> {
            if (binding.stockMenu.getRoot().getVisibility() == View.VISIBLE) {
                closeStockMenu();
            }
        });
    }


    private void checkBusinessStatus() {
        Request.getInstance().checkBusinessStatus(this, this::setBusinessStatus);
    }

    private void changeBusinessStatus(boolean isOpen) {
        Request.getInstance().changeBusinessStatus(this, isOpen, isDataSuccess -> setBusinessStatus(isOpen));
    }

    private void setBusinessStatus(boolean isBusinessOpen) {
        binding.swWebsite.setChecked(isBusinessOpen);
        binding.titleSwitch.setText(isBusinessOpen ? "אתר פעיל" : "אתר לא פעיל");

        binding.swWebsite.setOnCheckedChangeListener((buttonView, isChecked) -> changeBusinessStatus(isChecked));
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
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                for (int i = 0; i < jsonArray.length(); i++) {
                    stockModelList.add(gson.fromJson(jsonArray.getString(i), ProductItemModel.class));
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("valuesArray", stockModelList);
                StockFragment stockFragment = new StockFragment();
                stockFragment.setArguments(bundle);
                openFragment(stockFragment, "stock");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    public void openPasswordDialog(boolean needLogout) {
        openPasswordDialog(needLogout, 0);
    }

    public void openPasswordDialog(boolean needLogout, int type) {
        if (needLogout) {
            Request.getInstance().workerLogout(this);
        }
        PasswordDialog passwordDialog = new PasswordDialog(this);
        passwordDialog.setCancelable(type == TYPE_SWITCH_BUSINESS);
        passwordDialog.setCancelButton(type == TYPE_SWITCH_BUSINESS);
        passwordDialog.show();

        passwordDialog.setOnDismissListener(dialog -> {
            if (type == TYPE_SWITCH_BUSINESS) {
                if (passwordDialog.getWorker() != null)
                    if (passwordDialog.getWorker().getPermissions().getOpenCloseBusiness().equals("1"))
                        binding.swWebsite.setChecked(!binding.swWebsite.isChecked());
                    else Utils.openPermissionAlertDialog(this);
            } else {
                setName();
                fragment.startBoardUpdates();
            }
        });
    }

    public void openExitDialog() {
        ExitDialog exitDialog = new ExitDialog(this, new ExitDialog.ExitListener() {
            @Override
            public void onExit() {
                finish();
            }

            @Override
            public void onLogout() {
                saveData(Constants.TOKEN_PREF, "");

                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        exitDialog.show();
    }

    public void openOrderDialog(OpenOrderModel orderModel) {
        dialogOpenOrder = new DialogOpenOrder(this, orderModel);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialogOpenOrder.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialogOpenOrder.show();
        dialogOpenOrder.getWindow().setAttributes(lp);
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


    @Override
    public void onBusinessStatusCheck() {
        checkBusinessStatus();
    }

}
