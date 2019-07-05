package com.fiu_CaSPR.Sajib.MonkeyRocket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android_examples.getinstalledappiconname_android_examplescom.R;

public class Consent extends Activity {

    public static String country="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consent);
        Button agreebutton = (Button) findViewById(R.id.agree);
        Button disagreebutton = (Button) findViewById(R.id.disagree);

        WebView wv;
        wv=(WebView)findViewById(R.id.webView1);
        //wv.setInitialScale(1);
        //wv.getSettings().setJavaScriptEnabled(true);
        //wv.getSettings().setLoadWithOverviewMode(true);
        //wv.getSettings().setUseWideViewPort(true);
        //wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        //wv.setScrollbarFadingEnabled(false);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.loadUrl("http://www.monkeyrocket.review/PRIVACY_POLICY_MR.html");
        //wv.loadUrl("http://users.cis.fiu.edu/~stalu001/PRIVACY_POLICY_MR.html");

        agreebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });
        disagreebutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                exitToHome();
            }
        });

    }
    public void exitToHome(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
