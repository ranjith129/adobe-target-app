package com.xeragoadobetargetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.InvalidInitException;
import com.adobe.marketing.mobile.Lifecycle;
import com.adobe.marketing.mobile.LoggingMode;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Signal;
import com.adobe.marketing.mobile.Target;
import com.adobe.marketing.mobile.TargetRequest;
import com.adobe.marketing.mobile.UserProfile;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TargetContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileCore.setApplication(this.getApplication());
        MobileCore.setLogLevel(LoggingMode.DEBUG);

        setContentView(R.layout.activity_target_content);

        Button buttonAction = findViewById(R.id.firstButton);
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // track this into Analytics
                MobileCore.trackAction("Targeting Button tapped", null);
                // now over to Target
                Snackbar.make(view, "Creating Target request.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                TargetRequest targetRequest1 = new TargetRequest("target-global-mbox-mobile", null
                        , "{\"text\":\"Welcome!\",\"url\":\"https://www.xerago.com/\""
                        , new AdobeCallback<String>() {
                    @Override
                    public void call(String jsonResponse) {
                        Snackbar.make(view, "Content received", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        // so this should be JSON content...
                        try {
                            JSONObject targetJSONResponse = new JSONObject(jsonResponse);
                            // replace content as needed
                            final String textForTextView = targetJSONResponse.getString("description");
                            final TextView Banner1Text = findViewById(R.id.BannerText);
                            //final String Banner1Image = targetJSONResponse.getString("src");
                            //final ImageView BannerOne = findViewById(R.id.BannerOne);

                            final String urlForWebViewAsText = targetJSONResponse.getString("url");
                            URL url = new URL(urlForWebViewAsText); // I like to check my URLs
                            final WebView webView = findViewById(R.id.webView);

                            if (urlForWebViewAsText.length() > 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Banner1Text.setText(textForTextView);
                                        webView.loadUrl(urlForWebViewAsText);
                                        //BannerOne.setImageURI(uri)
                                        //BannerOne.setImageIcon(Icon.createWithContentUri(Banner1Image));
                                        Snackbar.make(view, "Content replaced", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            Snackbar.make(view, "Content from Target not valid JSON", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        } catch (MalformedURLException e) {
                            Snackbar.make(view, "Target returned invalid URL", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
                });
                List<TargetRequest> requests = new ArrayList<>();
                requests.add(targetRequest1);
                // prep done, now retrieve content
                Snackbar.make(view, "Retrieving content from Target", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Target.retrieveLocationContent(requests, null);
            }
        });
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();


    }

    @Override
    public void onResume() {
        super.onResume();
        MobileCore.setApplication(getApplication());
        MobileCore.lifecycleStart(null);
    }

    @Override
    public void onPause() {
        MobileCore.lifecyclePause();
        super.onPause();
    }
}