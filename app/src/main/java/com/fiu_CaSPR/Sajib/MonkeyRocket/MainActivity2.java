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
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android_examples.getinstalledappiconname_android_examplescom.R;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;


public class MainActivity2 extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    static StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.playstore_layout);

        Button appListButton = (Button) findViewById(R.id.applist);
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
        });

        ImageView imageView11 = (ImageView) findViewById(R.id.imageview11);
        ImageView imageView12 = (ImageView) findViewById(R.id.imageview12);
        ImageView imageView13 = (ImageView) findViewById(R.id.imageview13);
        ImageView imageView14 = (ImageView) findViewById(R.id.imageview14);

        ImageView imageView21 = (ImageView) findViewById(R.id.imageview21);
        ImageView imageView22 = (ImageView) findViewById(R.id.imageview22);
        ImageView imageView23 = (ImageView) findViewById(R.id.imageview23);
        ImageView imageView24 = (ImageView) findViewById(R.id.imageview24);

        ImageView imageView31 = (ImageView) findViewById(R.id.imageview31);
        ImageView imageView32 = (ImageView) findViewById(R.id.imageview32);
        ImageView imageView33 = (ImageView) findViewById(R.id.imageview33);
        ImageView imageView34 = (ImageView) findViewById(R.id.imageview34);

        ImageView imageView41 = (ImageView) findViewById(R.id.imageview41);
        ImageView imageView42 = (ImageView) findViewById(R.id.imageview42);
        ImageView imageView43 = (ImageView) findViewById(R.id.imageview43);
        ImageView imageView44 = (ImageView) findViewById(R.id.imageview44);

        imageView11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView13.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView14.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView21.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView22.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView23.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView24.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView31.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView32.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView33.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView34.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView41.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView42.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView43.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        imageView44.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(getApplicationContext(),RatingActivity.class);
                startActivity(intent);
            }
        });

        if(!checkPermission()) requestPermission();

        wifiScan();

        startAlert();
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

                    if (accountAccepted && callAccepted && fineLocAccepted && wifiAccepted)
                        Toast.makeText(this, "Permission Granted, Now you can use the app.", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(this, "Permission Denied, You may experience problem using the app.", Toast.LENGTH_SHORT).show();

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
        new AlertDialog.Builder(MainActivity2.this)
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