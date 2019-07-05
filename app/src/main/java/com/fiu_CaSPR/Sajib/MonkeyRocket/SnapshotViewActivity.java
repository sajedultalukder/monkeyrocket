package com.fiu_CaSPR.Sajib.MonkeyRocket;

/**
 * Created by Sajib on 3/24/2019.
 */

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SnapshotViewActivity extends AppCompatActivity {

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
        setContentView(R.layout.snapshot_layout);

        newString+=" Device ID: "+getDeviceID()+"\n Battery Level: "+getBatteryPercentage()+"\n";
        newString+=" Device Name: "+ DeviceTracker.getDeviceName()+"\n Model: "+ Build.MODEL+"\n Serial: "+ Build.SERIAL+"\n Wlan0: "+ IPTracker.getMACAddress("wlan0")+"\n Eth0: "+ IPTracker.getMACAddress("eth0")+"\n IPv4: "+ IPTracker.getIPAddress(true)+"\n IPv6: "+ IPTracker.getIPAddress(false)+"\n";
        newString+=" Total External: "+ StorageTracker.totalExternalValue+" MB\n";
        newString+=" Free External: "+ StorageTracker.freeExternalValue+" MB\n";
        newString+=" Total Internal: "+ StorageTracker.totalInternalValue+" MB\n";
        newString+=" Free Internal: "+ StorageTracker.freeInternalValue+" MB\n";
        newString+=AccountTracker.getAccounts(this);
        newString+=installedApps();

        TextView snapshot = (TextView) findViewById(R.id.snapshot);
        snapshot.setText("Current Snapshot:\n\n"+newString);
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
}

