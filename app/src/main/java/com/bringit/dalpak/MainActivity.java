package com.bringit.dalpak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bringit.dalpak.dialog.DialogOpenOrder;
import com.bringit.dalpak.dialog.PasswordDialog;
import com.bringit.dalpak.fragments.MainFragment;
import com.bringit.dalpak.models.OpenOrderModel;
import com.bringit.dalpak.models.OrderModel;
import com.bringit.dalpak.utils.Constants;
import com.bringit.dalpak.utils.Request;
import com.bringit.dalpak.utils.SharePref;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView nameTV;
    RelativeLayout backRL;
    private MainFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameTV = findViewById(R.id.nameTV);
        backRL = findViewById(R.id.backRL);
        fragment = new MainFragment();
        openFragment(fragment);

        backRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPasswordDialog();
            }
        });
        openPasswordDialog();
    }

    public void setName() {
        if (SharePref.getInstance(this).getData(Constants.NAME_PREF) != null) {
            nameTV.setText(SharePref.getInstance(this).getData(Constants.NAME_PREF) + " שלום ");
        }
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

    private void openFragment(MainFragment fragmentToOpen) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragmentToOpen, "Main");
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
