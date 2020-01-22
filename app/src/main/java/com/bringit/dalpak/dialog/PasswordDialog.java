package com.bringit.dalpak.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import com.bringit.dalpak.R;
import com.bringit.dalpak.utils.NumberKeyboardView;
import com.bringit.dalpak.utils.Request;


public class PasswordDialog extends Dialog {

    String password = "";
    int passwordIndex = -1;
    TextView tv1, tv2, tv3, tv4;
    TextView[] passwordTVs;
    Context context;

    public PasswordDialog(@NonNull final Context context) {
        super(context);
        setContentView(R.layout.password_dialog);
        this.context = context;
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        passwordTVs = new TextView[]{tv1, tv2, tv3, tv4};

        NumberKeyboardView numberKeyboardView = findViewById(R.id.numberKeyboardView);
        numberKeyboardView.keyListener(new NumberKeyboardView.KeyPressListener() {
            @Override
            public void onKeyPress(String keyTxt) {
                if (!keyTxt.equals("X")) {
                    if (passwordIndex == 3) {
                        return;
                    }
                    passwordIndex++;
                    passwordTVs[passwordIndex].setText(keyTxt);
                    if (passwordIndex == 3) {
                        Request.settingsLogin(context, getThePassword(), new Request.RequestCallBackSuccess() {
                            @Override
                            public void onDataDone(boolean isDataSuccess) {
                                if (isDataSuccess) {
                                    PasswordDialog.this.dismiss();
                                } else {
                                    initErrorState();
                                }
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
}

