package com.fiu_CaSPR.Sajib.MonkeyRocket;

/**
 * Created by Sajib on 3/24/2019.
 */

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
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
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android_examples.getinstalledappiconname_android_examplescom.R;

import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.PACKAGE_USAGE_STATS;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;


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
        //setContentView(R.layout.webview_layout);

        /*Button appListButton = (Button) findViewById(R.id.applist);
        appListButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),AppListActivity.class);
                startActivity(intent);
            }
        });
        Button snapshotButton = (Button) findViewById(R.id.snapshot);
        snapshotButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),SnapshotActivity.class);
                startActivity(intent);
            }
        });*/

        Button exitButton = (Button) findViewById(R.id.exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                exitToHome();
            }
        });

        //loadWebview();

        if(!checkPermission()) requestPermission();

        wifiScan();

        startAlert();
    }

    public void loadWebview()
    {
        url = "http://users.cis.fiu.edu/~stalu001/MonkeyRocket.htm";
        // Initialize the WebView
        wv = (WebView)findViewById(R.id.webview);
        wv.loadUrl(url);
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

        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (timeInSec * 1000), pendingIntent);
        Toast.makeText(this, "Snapshot set after " + timeInSec + " seconds", Toast.LENGTH_LONG).show();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CALL_LOG);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_WIFI_STATE);
        AppOpsManager appOps = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), this.getPackageName());


        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED && mode == MODE_ALLOWED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{GET_ACCOUNTS, READ_CALL_LOG, ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE}, PERMISSION_REQUEST_CODE);
        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean accountAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean callAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean fineLocAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean wifiAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (accountAccepted && callAccepted && fineLocAccepted && wifiAccepted) {
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
                                                    requestPermissions(new String[]{GET_ACCOUNTS, READ_CALL_LOG, ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE},
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