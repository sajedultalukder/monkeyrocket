package com.fiu_CaSPR.Sajib.MonkeyRocket;

/**
 * Created by Sajib on 3/24/2019.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Snapshot Activated", Toast.LENGTH_LONG).show();
        Log.d("ALARM TAG", "Snapshot Activated");
        intent = new Intent(context,SnapshotActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}