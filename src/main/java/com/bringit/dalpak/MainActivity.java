package com.bringit.dalpak;

import androidx.appcompat.app.AppCompatActivity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.bringit.dalpak.dialog.DialogOpenOrder;
import com.bringit.dalpak.dialog.PasswordDialog;
import com.bringit.dalpak.fragments.MainFragment;
import com.bringit.dalpak.models.OrderModel;
import com.bringit.dalpak.utils.Constants;
import com.bringit.dalpak.utils.SharePref;

public class MainActivity extends AppCompatActivity {
    TextView nameTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nameTV = findViewById(R.id.nameTV);
        openFragment(new MainFragment());
    }

    public void setName(){
        if(SharePref.getInstance(this).getData(Constants.NAME_PREF) != null){
            nameTV.setText(SharePref.getInstance(this).getData(Constants.NAME_PREF) + " שלום ");
        }
    }

    public void openOrderDialog(OrderModel orderModel){
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
