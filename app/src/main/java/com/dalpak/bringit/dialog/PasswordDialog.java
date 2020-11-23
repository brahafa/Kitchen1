package com.dalpak.bringit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.dalpak.bringit.R;
import com.dalpak.bringit.models.WorkerModel;
import com.dalpak.bringit.utils.Constants;
import com.dalpak.bringit.utils.NumberKeyboardView;
import com.dalpak.bringit.utils.Request;
import com.dalpak.bringit.utils.SharedPrefs;
import com.dalpak.bringit.utils.Utils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


public class PasswordDialog extends Dialog {

    int passwordIndex = -1;
    TextView tv1, tv2, tv3, tv4;
    TextView[] passwordTVs;
    Context context;
    private WorkerModel mWorker;

    public PasswordDialog(@NonNull final Context context) {
        super(context);
        setContentView(R.layout.password_dialog);
        this.context = context;
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        passwordTVs = new TextView[]{tv1, tv2, tv3, tv4};
        (findViewById(R.id.tv_version)).setOnLongClickListener(v ->{((TextView)findViewById(R.id.tv_version)).setText(Utils.getVersionApp(getContext())); return false;});

        NumberKeyboardView numberKeyboardView = findViewById(R.id.numberKeyboardView);
        numberKeyboardView.keyListener(keyTxt -> {
            if (!keyTxt.equals("X")) {
                if (passwordIndex == 3) {
                    return;
                }
                passwordIndex++;
                passwordTVs[passwordIndex].setText(keyTxt);
                if (passwordIndex == 3) {
                    Request.getInstance().settingsLogin(context, getThePassword(), response -> {
                        if (response.isStatus()) {
                            mWorker = response.getUser();
                            if (response.getUser().getPermissions().getKitchen().equals("1")) {
                                SharedPrefs.saveData(Constants.NAME_PREF, response.getUser().getName());
//                                  SharedPrefs.saveData(Constants.ROLE_PREF, response.getUser().getRole());
                                PasswordDialog.this.dismiss();
                            } else
                                Utils.openPermissionAlertDialog(context);
                        } else {
                            initErrorState();
                        }
                    });
                }
            } else if (passwordIndex >= 0) {
                if (passwordIndex == 3) {
                    initSuccessState();
                }
                passwordTVs[passwordIndex].setText("");
                passwordIndex--;
            }
        });

    }

    private String getThePassword() {
        return tv1.getText().toString()
                + tv2.getText().toString()
                + tv3.getText().toString()
                + tv4.getText().toString();
    }

    private void initErrorState() {
        tv1.setBackground(ContextCompat.getDrawable(context, R.drawable.background_edittext_error));
        tv2.setBackground(ContextCompat.getDrawable(context, R.drawable.background_edittext_error));
        tv3.setBackground(ContextCompat.getDrawable(context, R.drawable.background_edittext_error));
        tv4.setBackground(ContextCompat.getDrawable(context, R.drawable.background_edittext_error));
        tv1.setTextColor(context.getResources().getColor(R.color.error));
        tv2.setTextColor(context.getResources().getColor(R.color.error));
        tv3.setTextColor(context.getResources().getColor(R.color.error));
        tv4.setTextColor(context.getResources().getColor(R.color.error));
    }

    private void initSuccessState() {
        tv1.setBackground(ContextCompat.getDrawable(context, R.drawable.background_edittext));
        tv2.setBackground(ContextCompat.getDrawable(context, R.drawable.background_edittext));
        tv3.setBackground(ContextCompat.getDrawable(context, R.drawable.background_edittext));
        tv4.setBackground(ContextCompat.getDrawable(context, R.drawable.background_edittext));
        tv1.setTextColor(context.getResources().getColor(R.color.text_color));
        tv2.setTextColor(context.getResources().getColor(R.color.text_color));
        tv3.setTextColor(context.getResources().getColor(R.color.text_color));
        tv4.setTextColor(context.getResources().getColor(R.color.text_color));
    }

    public WorkerModel getWorker() {
        return mWorker;
    }
}

