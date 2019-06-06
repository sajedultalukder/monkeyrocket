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


public class AccountTracker {

    public static int count=0;

    public static String getAccounts(Context mContext)
    {
        String possibleEmail="";

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return "\n Account Permission Denied\n";
        }

        /*try{
            possibleEmail += " Gmail Accounts:\n";
            Account[] accounts = AccountManager.get(mContext).getAccountsByType("com.google");

            for (Account account : accounts) {

                possibleEmail += account.name+" : "+account.type+" , \n";
                possibleEmail += " \n";
                count++;
            }
        }
        catch(Exception e)
        {
            Log.i("Exception", "Exception:"+e) ;
            //Toast.makeText(this, "Exception:"+e, Toast.LENGTH_SHORT).show();
        }
*/

        try{

            Account[] accounts = AccountManager.get(mContext).getAccounts();
            for (Account account : accounts) {

                //possibleEmail += " "+account.name+" : "+account.type+" \n";
                possibleEmail += " "+account.name+" \n";
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

        Log.i("Exception", "mails:"+possibleEmail) ;

        possibleEmail = "\n Account Count: "+count+"\n"+possibleEmail;

        return possibleEmail;
    }
}