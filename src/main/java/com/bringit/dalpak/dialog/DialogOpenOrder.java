package com.bringit.dalpak.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bringit.dalpak.R;
import com.bringit.dalpak.models.OrderModel;

import java.util.List;

public class DialogOpenOrder  extends Dialog implements View.OnClickListener {

    Dialog d;
    Context context;
    OrderModel orderModel;

    public DialogOpenOrder(@NonNull final Context context, OrderModel orderModel) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = context;
        this.orderModel = orderModel;
        setContentView(R.layout.open_order_dialog);
        d = this;
        (findViewById(R.id.close)).setOnClickListener(this);
    }

    private void initRV(final List<OrderModel> orderModels, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false));

       // recyclerView.setAdapter(Adapter);
      //  recyclerView.setOnDragListener(Adapter.getDragInstance());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                d.dismiss();
                break;

        }
    }
}