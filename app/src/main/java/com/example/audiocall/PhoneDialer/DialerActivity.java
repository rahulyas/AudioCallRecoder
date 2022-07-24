package com.example.audiocall.PhoneDialer;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.telecom.TelecomManager.ACTION_CHANGE_DEFAULT_DIALER;
import static android.telecom.TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.example.audiocall.R;


public class DialerActivity extends AppCompatActivity {

    EditText phoneNumberInput;

    public static int REQUEST_PERMISSION = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);

        phoneNumberInput = findViewById(R.id.phoneNumberInput);

        if (getIntent() != null && getIntent().getData() != null)
            phoneNumberInput.setText(getIntent().getData().getSchemeSpecificPart());
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        //offerReplacingDefaultDialer();

        phoneNumberInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                makeCall();
                return true;
            }
        });
    }

    private void makeCall() {
        if (ContextCompat.checkSelfPermission(DialerActivity.this,
                Manifest.permission.CALL_PHONE) != PERMISSION_GRANTED) {
            Uri uri = Uri.parse("tel:"+phoneNumberInput.getText().toString().trim());
            startActivity(new Intent(Intent.ACTION_CALL, uri));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 || requestCode == 300 || requestCode == 400 || requestCode == 500 || requestCode == 600) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    makeCall();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}