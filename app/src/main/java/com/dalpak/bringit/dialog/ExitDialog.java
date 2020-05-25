package com.dalpak.bringit.dialog;

import android.app.Dialog;
import android.content.Context;

import com.dalpak.bringit.databinding.DialogExitBinding;

import androidx.annotation.NonNull;


public class ExitDialog extends Dialog {

    public ExitDialog(@NonNull final Context context, ExitListener listener) {
        super(context);
        DialogExitBinding binding = DialogExitBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.tvExit.setOnClickListener(v -> {
            listener.onExit();
            dismiss();
        });
        binding.tvLogout.setOnClickListener(v -> {
            listener.onLogout();
            dismiss();
        });

    }

    public interface ExitListener {
        void onExit();

        void onLogout();
    }

}

