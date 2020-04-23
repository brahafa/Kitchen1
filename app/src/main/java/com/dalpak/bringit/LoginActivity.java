package com.dalpak.bringit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.dalpak.bringit.databinding.ActivityLoginBinding;
import com.dalpak.bringit.utils.Constants;
import com.dalpak.bringit.utils.Request;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import static com.dalpak.bringit.utils.SharedPrefs.saveData;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        /*
        if(SharePref.getInstance(this).getBooleanData(Constants.USER_ALREADY_CONNECTED_PREF)){
            try {
                JSONObject jsonObject = new JSONObject(SharePref.getInstance(this).getData(Constants.LOG_IN_JSON_PREF));
                BusinessModel.getInstance().initData(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            openMainActivity();
            this.finish();
        }

         */
        initListener();
    }

    private void initListener() {
        binding.ivClearName.setOnClickListener(v -> {
            binding.edtUsername.setText("");
            correctUsername();
        });

        binding.ivClearPassword.setOnClickListener(v -> {
            binding.edtPassword.setText("");
            correctPassword();
        });
        binding.tvGo.setOnClickListener(v ->
                Request.getInstance().logIn(this,
                        binding.edtPassword.getText().toString(),
                        binding.edtUsername.getText().toString(),
                        isDataSuccess -> {
                            if (isDataSuccess) {
                                saveData(Constants.USER_ALREADY_CONNECTED_PREF, true);
                                openMainActivity();
                            } else {
                                errorInPassword();
                                errorInUsername();
                            }

                        }));

        binding.edtPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.tvGo.performClick();
                return true;
            }
            return false;
        });
    }

    private void errorInPassword() {
        binding.ivClearPassword.setVisibility(View.VISIBLE);
        binding.edtPassword.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_edittext_error));
    }

    private void errorInUsername() {
        binding.ivClearName.setVisibility(View.VISIBLE);
        binding.edtUsername.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_edittext_error));
    }

    public void correctPassword() {
        binding.ivClearPassword.setVisibility(View.INVISIBLE);
        binding.edtPassword.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_edittext));
    }

    public void correctUsername() {
        binding.ivClearName.setVisibility(View.INVISIBLE);
        binding.edtUsername.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_edittext));
    }

    private void openMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
