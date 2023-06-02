package com.xeragoadobetargetapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.adobe.marketing.mobile.AdobeCallback;
import com.adobe.marketing.mobile.Analytics;
import com.adobe.marketing.mobile.Griffon;
import com.adobe.marketing.mobile.Identity;
import com.adobe.marketing.mobile.InvalidInitException;
import com.adobe.marketing.mobile.Lifecycle;
import com.adobe.marketing.mobile.LoggingMode;
import com.adobe.marketing.mobile.MobileCore;
import com.adobe.marketing.mobile.Places;
import com.adobe.marketing.mobile.PlacesMonitor;
import com.adobe.marketing.mobile.Signal;
import com.adobe.marketing.mobile.Target;
import com.adobe.marketing.mobile.UserProfile;
import com.adobe.marketing.mobile.TargetParameters;
import com.adobe.marketing.mobile.TargetRequest;
import com.adobe.marketing.mobile.VisitorID;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileCore.setApplication(this.getApplication());
        MobileCore.setLogLevel(LoggingMode.DEBUG);
        try {
            Griffon.registerExtension();
            Places.registerExtension();
            PlacesMonitor.registerExtension();
            Analytics.registerExtension();
            Target.registerExtension();
            UserProfile.registerExtension();
            Identity.registerExtension();
            Lifecycle.registerExtension();
            Signal.registerExtension();
            MobileCore.start(new AdobeCallback() {
                @Override
                public void call(Object o) {
                    //MobileCore.configureWithAppID("4fa03d1212c6/47085f890555/launch-9e0c6e9e5482"); //Xerago configureWithAppID
                    //MobileCore.configureWithAppID("2d6d124a9338/2a263dab1ce3/launch-d8b67b854e8a-development"); //BFL configureWithAppID
                    MobileCore.configureWithAppID("4fa03d1212c6/47085f890555/launch-8ad66aff00b4-development");
                }
            });
        } catch (InvalidInitException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_main);

        // create contextData map
        Map<String, String> contextData = new HashMap<>();
        // add language = en
        contextData.put("language", "en");
        MobileCore.trackState("Home", contextData);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Identity.syncIdentifier("mobile", "9789823801", VisitorID.AuthenticationState.AUTHENTICATED);
    
        //-------------- Add to Cart Button Target Activity code Start -------------------//

        Button AddtoCart = findViewById(R.id.AddtoCart);
        AddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // track this into Analytics
                MobileCore.trackAction("Add to Cart Targeting Button tapped", null);
                // now over to Target
                Snackbar.make(view, "Creating Add to Cart Target request.", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                Map<String, String> profileParameters1 = new HashMap<>();
                profileParameters1.put("isHamburgerClickedWithin30Days", "1");
                //profileParameters1.put("last_clicked", "buttonA");
                // Does not NTB user Android offer display
                //profileParameters1.put("usertype", "NTB"); // Iphone offer display
                TargetParameters ProfileParametersTest = new TargetParameters.Builder().profileParameters(profileParameters1).build();

                TargetRequest targetRequest1 = new TargetRequest("bfl_target_last_clicked", ProfileParametersTest,"{\"Offer\":[{\"featureId\":\"Welcome\",\"featurePromoText\":\"Yes\",\"status\":\"N\",\"featureIconLink\":\"88888\"},{\"featureId\":\"Welcome1\",\"featurePromoText\":\"NO\",\"status\":\"N\",\"featureIconLink\":\"99999\"}]}", new AdobeCallback<String>() {
                    /*      @Override
                           public void call(String content) {
                               if(content !=null && !content.isEmpty()){
                                   Snackbar.make(view, content, Snackbar.LENGTH_LONG).setAction("Action", null).show();

       // imageUrl now contains the URL string "https///demo"
       // You can use this URL to download and display the image as shown in the previous answer
                                   } else {
       // The regular expression didn't match the src attribute
       // Handle the error condition here
                                   }
                               }
       */
                    @Override
                    public void call(String jsonResponse) {
                        Snackbar.make(view, "Content received", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        // so this should be JSON content...
                        try {
                            JSONObject targetJSONResponse = new JSONObject(jsonResponse);
                            Log.d("ProductJsonResponse", String.valueOf(targetJSONResponse));
                            JSONArray offerArray = targetJSONResponse.getJSONArray("Offer");
                            for (int i = 0; i < offerArray.length(); i++) {
                                JSONObject productObject = offerArray.getJSONObject(0);
                                Log.d("productObjectArrayResponse", String.valueOf(productObject));
                                // replace content as needed
                                final WebView UrlWebView = findViewById(R.id.UrlWebView);
                                final String textForTextView1 = productObject.getString("featureId");
                                final TextView textView1 = findViewById(R.id.productname);
                                final String textForTextView2 = productObject.getString("featurePromoText");
                                final TextView textView2 = findViewById(R.id.productprice);
                                final String textForTextView3 = productObject.getString("status");
                                final TextView textView3 = findViewById(R.id.productdescription);
                                final String ProductImagePath = productObject.getString("featureIconLink");
                                final ImageView imageView1 = findViewById(R.id.productimage);

                                //  if (textForTextView1.length() > 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // imageView1.destroyDrawingCache(); //App local image value replacing method
                                        // imageView1.setDrawingCacheEnabled(true);//App local image value replacing method
                                        // imageView1.buildDrawingCache(true); //App local image value replacing method
                                        textView1.setText(textForTextView1);
                                        textView2.setText(textForTextView2);
                                        textView3.setText(textForTextView3);
                                        UrlWebView.setVisibility(View.INVISIBLE);
                                        Picasso.get().load(ProductImagePath).into(imageView1); // Live public url image value replacing method

                                        // Resources res = getResources(); //App local image value replacing method
                                        // int resourceId = res.getIdentifier(ProductImagePath, "url", getPackageName() ); //App local image value replacing method
                                        // imageView1.getDrawingCache(); //App local image value replacing method
                                        // imageView1.setImageResource(resourceId); //App local image value replacing method

                                        Snackbar.make(view, "Content replaced", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                });
                                //  }
                            }
                        } catch (JSONException e) {
                            Snackbar.make(view, "Content from Target not valid JSON", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        }
                    }
                });
                List<TargetRequest> requests = new ArrayList<>();
                requests.add(targetRequest1);
                // prep done, now retrieve content
                Snackbar.make(view, "Retrieving content from Target", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Target.retrieveLocationContent(requests, ProfileParametersTest);
            }
        });
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();

//-------------- Add to Cart Button Target Activity code End -------------------//

//******************* Click Here Button Target Activity code Start ********************//
        /*
        Button ClickHereBtn = findViewById(R.id.ClickHereBtn);
        ClickHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // track this into Analytics
                MobileCore.trackAction("Click Here Targeting Button tapped", null);
                // now over to Target
                Snackbar.make(view, "Creating Click Here Target request.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                TargetRequest targetRequest1 = new TargetRequest("target-global-mbox-applemobile", null
                        , "{\"productname\":\"Welcome!\",\"productprice\":\"1000rs\",\"productdescription\":\"lorum imposum\",\"productimage\":\"https://www.image.com/\""
                        , new AdobeCallback<String>() {
                    @Override
                    public void call(String jsonResponse) {
                        Snackbar.make(view, "Content received", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        // so this should be JSON content...
                        try {
                            JSONObject targetJSONResponse = new JSONObject(jsonResponse);
                            // replace content as needed
                            final String textForTextView1 = targetJSONResponse.getString("productname");
                            final TextView textView1 = findViewById(R.id.productname);
                            final String textForTextView2 = targetJSONResponse.getString("productprice");
                            final TextView textView2 = findViewById(R.id.productprice);
                            final String textForTextView3 = targetJSONResponse.getString("productdescription");
                            final TextView textView3 = findViewById(R.id.productdescription);
                            final String ProductImagePath = targetJSONResponse.getString("productimage");
                            final ImageView imageView1 = findViewById(R.id.productimage);

                            if (textForTextView1.length() > 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // imageView1.destroyDrawingCache(); //App local image value replacing method
                                        // imageView1.setDrawingCacheEnabled(true);//App local image value replacing method
                                        // imageView1.buildDrawingCache(true); //App local image value replacing method
                                        textView1.setText(textForTextView1);
                                        textView2.setText(textForTextView2);
                                        textView3.setText(textForTextView3);
                                        Picasso.get().load(ProductImagePath).into(imageView1); // Live public url image value replacing method

                                        // Resources res = getResources(); //App local image value replacing method
                                        // int resourceId = res.getIdentifier(ProductImagePath, "url", getPackageName() ); //App local image value replacing method
                                        // imageView1.getDrawingCache(); //App local image value replacing method
                                        // imageView1.setImageResource(resourceId); //App local image value replacing method

                                        Snackbar.make(view, "Content replaced", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            Snackbar.make(view, "Content from Target not valid JSON", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
   */
        //******************* Click Here Button Target Activity code End ********************//

        //-------------- Add to ClickHereBtn Button Target Activity code Start -------------------//

        Button ClickHereBtn = findViewById(R.id.ClickHereBtn);
        ClickHereBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // track this into Analytics
                final TextView textView1 = findViewById(R.id.productname);
                final TextView textView2 = findViewById(R.id.productprice);
                final TextView textView3 = findViewById(R.id.productdescription);
                final ImageView imageView1 = findViewById(R.id.productimage);
                Button AddtoCart = findViewById(R.id.AddtoCart);
                Button ClickHereBtn = findViewById(R.id.ClickHereBtn);
                textView1.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.INVISIBLE);
                textView3.setVisibility(View.INVISIBLE);
                imageView1.setVisibility(View.INVISIBLE);
                AddtoCart.setVisibility(View.INVISIBLE);
                ClickHereBtn.setVisibility(View.INVISIBLE);
                final WebView UrlwebView = findViewById(R.id.UrlWebView);
                //CookieManager cookieManager = CookieManager.getInstance();
                //cookieManager.setAcceptThirdPartyCookies(WebView, true);
                UrlwebView.setVisibility(View.VISIBLE);
                UrlwebView.getSettings().setJavaScriptEnabled(true);
                UrlwebView.getSettings().setLoadWithOverviewMode(true);
                UrlwebView.getSettings().setUseWideViewPort(true);
                WebSettings settings = UrlwebView.getSettings();
                settings.setDomStorageEnabled(true);

                /* Appending Visitor ID start */

               /* Identity.appendVisitorInfoForURL("https://bflcodepush.bajajfinserv.in/9.0.01/dist/index.html", new AdobeCallback<String>() {
                    //https://demo.xerago.com/website/uae/adobe-webtag-training/softypinko/index.html
                    @Override
                    public void call(final String urlWithAdobeVisitorInfo) {
                        //handle the new URL here
                        //For example, open the URL on the device browser
                        //
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UrlwebView.loadUrl(String.valueOf(Uri.parse(urlWithAdobeVisitorInfo)));
                                Log.d("urlWithAdobeVisitorInfo:", String.valueOf(urlWithAdobeVisitorInfo));
                                Log.d("mobile:", "8973858955");
                            }
                        });
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(urlWithAdobeVisitorInfo));
                        startActivity(intent);
                    }
                });*/

                /* Appending Visitor ID End */

                /* Set Customer ID method start */

                final String urlForWebViewAsText = "https://demo.xerago.com/website/uae/adobe-webtag-training/softypinko/index.html?mobile_number=9789823801";
                try {
                   final URL url = new URL(urlForWebViewAsText); // I like to check my URLs
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UrlwebView.loadUrl(urlForWebViewAsText);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(urlForWebViewAsText));
                            startActivity(intent);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                /* Set Customer ID method End */
            }
        });
//-------------- Add to ClickHereBtn Button Target Activity code End -------------------//
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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