package com.bringit.dalpak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bringit.dalpak.utils.Constants;
import com.bringit.dalpak.utils.Request;
import com.bringit.dalpak.utils.SharePref;

public class LoginActivity extends AppCompatActivity {
    ImageView userNameDeleteIV, passwordDeleteIV;
    private EditText usernameET, passwordET;
    TextView goTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        userNameDeleteIV = findViewById(R.id.clearName);
        passwordDeleteIV = findViewById(R.id.clearPassword);
        goTV = findViewById(R.id.go);

        initListener();
    }

    private void initListener() {
        userNameDeleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameET.setText("");
                correctUsername();
            }
        });

        passwordDeleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordET.setText("");
                correctPassword();
            }
        });
        goTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Request.logIn(getApplicationContext(), passwordET.getText().toString(), usernameET.getText().toString(), new Request.RequestCallBackSuccess() {
                    @Override
                    public void onDataDone(boolean isDataSuccess) {
                        if (isDataSuccess) {
                            SharePref.getInstance(getApplicationContext()).saveData(Constants.USER_ALREADY_CONNECTED_PREF, true);
                            openMainActivity();
                        } else {
                            errorInPassword();
                            errorInUsername();
                        }

                    }
                });
            }
        });

        passwordET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    goTV.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void errorInPassword() {
        passwordDeleteIV.setVisibility(View.VISIBLE);
        passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_edittext_error));
    }

    private void errorInUsername() {
        userNameDeleteIV.setVisibility(View.VISIBLE);
        usernameET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_edittext_error));
    }

    public void correctPassword() {
        passwordDeleteIV.setVisibility(View.INVISIBLE);
        passwordET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_edittext));
    }

    public void correctUsername() {
        userNameDeleteIV.setVisibility(View.INVISIBLE);
        usernameET.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_edittext));
    }

    private void openMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
