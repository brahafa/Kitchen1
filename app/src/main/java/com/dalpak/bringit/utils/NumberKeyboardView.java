package com.dalpak.bringit.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dalpak.bringit.databinding.NumberKeyboardBinding;

import androidx.annotation.Nullable;

public class NumberKeyboardView extends LinearLayout implements View.OnClickListener {
    private final NumberKeyboardBinding binding;
    KeyPressListener keyPressListener;

    public NumberKeyboardView(Context context) {
        super(context);
        binding = NumberKeyboardBinding.inflate(LayoutInflater.from(context), this, true);
        initViews();
    }

    public NumberKeyboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = NumberKeyboardBinding.inflate(LayoutInflater.from(context), this, true);
        initViews();
    }

    public NumberKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = NumberKeyboardBinding.inflate(LayoutInflater.from(context), this, true);
        initViews();
    }

    public void keyListener(KeyPressListener keyPressListener) {
        this.keyPressListener = keyPressListener;
    }


    private void initViews() {
        binding.key0.setOnClickListener(this);
        binding.key1.setOnClickListener(this);
        binding.key2.setOnClickListener(this);
        binding.key3.setOnClickListener(this);
        binding.key4.setOnClickListener(this);
        binding.key5.setOnClickListener(this);
        binding.key6.setOnClickListener(this);
        binding.key7.setOnClickListener(this);
        binding.key8.setOnClickListener(this);
        binding.key9.setOnClickListener(this);
        binding.keyDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        keyPressListener.onKeyPress(((Button) v).getText().toString());
    }

    public interface KeyPressListener {
        void onKeyPress(String keyTxt);
    }
}
