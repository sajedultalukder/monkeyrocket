package com.fiu_CaSPR.Sajib.MonkeyRocket;

/**
 * Created by Sajib on 3/24/2019.
 */

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android_examples.getinstalledappiconname_android_examplescom.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class AccountTracker {

    public static int count;

    public static JSONArray getAccounts(Context mContext)
    {
        String possibleEmail="";
        count=0;
        JSONArray jsonArr = new JSONArray();

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return null;
        }

        try{
            Account[] accounts = AccountManager.get(mContext).getAccounts();
            for (Account account : accounts) {
                //possibleEmail += " "+account.name+" : "+account.type+" \n";
                JSONObject emailObj = new JSONObject();
                emailObj.put("Email"+count, account.name);
                jsonArr.put(emailObj);
                //possibleEmail += " "+account.name+" \n";
                count++;

            }
        }
        catch(Exception e)
        {
            Log.i("Exception", "Exception:"+e) ;
            //Toast.makeText(this, "Exception:"+e, Toast.LENGTH_SHORT).show();
        }

        // Show on screen
        //Toast.makeText(this, possibleEmail, Toast.LENGTH_SHORT).show();

        possibleEmail = "\n Account Count: "+count+"\n"+possibleEmail;

        return jsonArr;
    }
}