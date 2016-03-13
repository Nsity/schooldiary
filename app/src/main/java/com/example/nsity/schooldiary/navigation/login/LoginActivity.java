package com.example.nsity.schooldiary.navigation.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.nsity.schooldiary.R;
import com.example.nsity.schooldiary.navigation.MainActivity;
import com.example.nsity.schooldiary.system.CommonFunctions;
import com.example.nsity.schooldiary.system.SyncManager;
import com.example.nsity.schooldiary.system.gcm.GCMIntentService;
import com.example.nsity.schooldiary.system.gcm.ServiceRegister;
import com.example.nsity.schooldiary.system.network.CallBack;
import com.example.nsity.schooldiary.system.network.Server;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import javax.crypto.spec.GCMParameterSpec;

/**
 * Created by nsity on 15.11.15.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mLoginView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginView = (EditText) findViewById(R.id.login);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        if (!Server.isOnline(this)) {
            Toast.makeText(this, getString(R.string.internet_problem), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mLoginView.setError(null);
        mPasswordView.setError(null);

        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            CommonFunctions.showProgress(true, this, mLoginFormView, mProgressView);
            UserManager.login(getApplicationContext(), login, password, new CallBack() {
                @Override
                public void onSuccess() {
                    CommonFunctions.showProgress(false, getApplicationContext(), mLoginFormView, mProgressView);
                    sync();
                }

                @Override
                public void onFail(String message) {
                    CommonFunctions.showProgress(false, getApplicationContext(), mLoginFormView, mProgressView);
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void sync() {
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(getString(R.string.action_load));
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();

        SyncManager.sync(getApplicationContext(), new CallBack() {
            @Override
            public void onSuccess() {
                if (ServiceRegister.checkPlayServices(getApplicationContext())) {
                    Intent intent = new Intent(getApplicationContext(), GCMIntentService.class);
                    intent.putExtra(GCMIntentService.KEY, GCMIntentService.REGISTER);
                    startService(intent);
                }
                mProgressDialog.dismiss();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }



}


