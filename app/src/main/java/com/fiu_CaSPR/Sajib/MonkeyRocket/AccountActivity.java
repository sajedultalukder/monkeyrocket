package com.fiu_CaSPR.Sajib.MonkeyRocket;

/**
 * Created by Sajib on 3/24/2019.
 */

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.android_examples.getinstalledappiconname_android_examplescom.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class AccountActivity extends AppCompatActivity {

    public static final int PERMS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applist_layout);

        getRegisteredEmail();
    }

    public void getRegisteredEmail() {
        //Get registered emails
        if (ContextCompat.checkSelfPermission(AccountActivity.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AccountActivity.this, Manifest.permission.GET_ACCOUNTS)) {
                ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, PERMS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(AccountActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, PERMS_REQUEST_CODE);
            }
        } else {
            //do some stuff
            ArrayList<String> emails = new ArrayList<>();

            Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
            Account[] accounts = AccountManager.get(this).getAccounts();
            for (Account account : accounts) {
                if (gmailPattern.matcher(account.name).matches()) {
                    emails.add(account.name);
                    Log.d("Account: ",account.name);
                }
            }

            Toast.makeText(this, "Total Registered Email Address " + emails.size() + ": " + emails.get(0), Toast.LENGTH_LONG).show();
        }
    }
}

