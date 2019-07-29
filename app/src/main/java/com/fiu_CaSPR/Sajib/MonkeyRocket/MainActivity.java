package com.fiu_CaSPR.Sajib.MonkeyRocket;

/**
 * Created by Sajib on 3/24/2019.
 */

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.android_examples.getinstalledappiconname_android_examplescom.R;

import java.util.List;

import static android.Manifest.permission.GET_ACCOUNTS;


public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    public static WebView wv;
    public String url;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    static StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.playstore_layout);

        Button exitButton = (Button) findViewById(R.id.exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                exitToHome();
            }
        });

        Button snapshotButton = (Button) findViewById(R.id.snapshot);
        snapshotButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),SnapshotViewActivity.class);
                startActivity(intent);
            }
        });


        if(!checkPermission()) requestPermission();

        //wifiScan();

        startAlert();
    }

    public void exitToHome(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public void wifiScan()
    {
        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (mainWifi.isWifiEnabled() == false)
        {
            // If wifi disabled then enable it
            Toast.makeText(getApplicationContext(), "Enabling wifi...",
                    Toast.LENGTH_LONG).show();
            mainWifi.setWifiEnabled(true);
        }

        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();

    }

    public void startAlert() {
        int timeInSec = 1;

        Intent intent = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (timeInSec * 1000), pendingIntent);
        //Toast.makeText(this, "Snapshot set after " + timeInSec + " seconds", Toast.LENGTH_LONG).show();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{GET_ACCOUNTS}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean accountAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (accountAccepted) {
                        //Toast.makeText(this, "Permission Granted, Now you can use the app.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Toast.makeText(this, "Permission Denied, You may experience problem using the app.", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(GET_ACCOUNTS)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{GET_ACCOUNTS},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    class WifiReceiver extends BroadcastReceiver {

        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {

            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults();

            for (int i = 0; i < wifiList.size(); i++){
                sb.append(" "+new Integer(i+1).toString() + ".");
                sb.append((wifiList.get(i)).SSID+" ("+wifiList.get(i).level+")");
                sb.append("\n");
            }
            System.out.print(sb);
        }

    }
}