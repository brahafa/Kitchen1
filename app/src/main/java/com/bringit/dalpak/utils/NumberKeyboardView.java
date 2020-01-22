package com.bringit.dalpak.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bringit.dalpak.R;

public class NumberKeyboardView extends LinearLayout implements View.OnClickListener {
    private View view;
    private Button key_0, key_1, key_2, key_3, key_4, key_5, key_6, key_7 , key_8, key_9, key_delete;
    KeyPressListener keyPressListener;

    public NumberKeyboardView(Context context) {
        super(context);
        init();
    }

    public NumberKeyboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NumberKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void keyListener(KeyPressListener keyPressListener) {
        this.keyPressListener = keyPressListener;
    }

    private void init(){
        view =  inflate(getContext(), R.layout.number_keyboard, this);
        initViews();
    }

    private void initViews() {
        key_0 = view.findViewById(R.id.key_0);
        key_1 = view.findViewById(R.id.key_1);
        key_2 = view.findViewById(R.id.key_2);
        key_3 = view.findViewById(R.id.key_3);
        key_4 = view.findViewById(R.id.key_4);
        key_5 = view.findViewById(R.id.key_5);
        key_6 = view.findViewById(R.id.key_6);
        key_7 = view.findViewById(R.id.key_7);
        key_8 = view.findViewById(R.id.key_8);
        key_9 = view.findViewById(R.id.key_9);
        key_delete = view.findViewById(R.id.key_delete);

        key_0.setOnClickListener(this);
        key_1.setOnClickListener(this);
        key_2.setOnClickListener(this);
        key_3.setOnClickListener(this);
        key_4.setOnClickListener(this);
        key_5.setOnClickListener(this);
        key_6.setOnClickListener(this);
        key_7.setOnClickListener(this);
        key_8.setOnClickListener(this);
        key_9.setOnClickListener(this);
        key_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        keyPressListener.onKeyPress(((Button)v).getText().toString());
    }

    public interface KeyPressListener{
        void onKeyPress(String keyTxt);
    }
}
