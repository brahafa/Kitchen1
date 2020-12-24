package com.dalpak.bringit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dalpak.bringit.R;
import com.dalpak.bringit.databinding.PasswordDialogBinding;
import com.dalpak.bringit.models.WorkerModel;
import com.dalpak.bringit.utils.Constants;
import com.dalpak.bringit.utils.NumberKeyboardView;
import com.dalpak.bringit.utils.Request;
import com.dalpak.bringit.utils.SharedPrefs;
import com.dalpak.bringit.utils.Utils;


public class PasswordDialog extends Dialog {

    private final PasswordDialogBinding binding;
    int passwordIndex = -1;
    TextView[] passwordTVs;
    Context context;
    private WorkerModel mWorker;

    public PasswordDialog(@NonNull final Context context) {
        super(context);
        binding = PasswordDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.context = context;
        passwordTVs = new TextView[]{binding.tv1, binding.tv2, binding.tv3, binding.tv4};

        binding.tvVersion.setOnLongClickListener(v -> {
            binding.tvVersion.setText(Utils.getVersionApp(getContext()));
            return false;
        });

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

    public void setCancelButton(boolean isVisible) {
        binding.ivClose.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private String getThePassword() {
        return binding.tv1.getText().toString()
                + binding.tv2.getText().toString()
                + binding.tv3.getText().toString()
                + binding.tv4.getText().toString();
    }

    private void initErrorState() {
        binding.tv1.setActivated(true);
        binding.tv2.setActivated(true);
        binding.tv3.setActivated(true);
        binding.tv4.setActivated(true);
    }

    private void initSuccessState() {
        binding.tv1.setActivated(false);
        binding.tv2.setActivated(false);
        binding.tv3.setActivated(false);
        binding.tv4.setActivated(false);
    }

    public WorkerModel getWorker() {
        return mWorker;
    }
}

