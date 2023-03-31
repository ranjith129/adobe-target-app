package com.xeragoadobetargetapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.adobe.marketing.mobile.Analytics;
import com.adobe.marketing.mobile.Griffon;
import com.adobe.marketing.mobile.Places;
import com.adobe.marketing.mobile.PlacesMonitor;
import com.adobe.marketing.mobile.TargetRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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
import com.adobe.marketing.mobile.UserProfile;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
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
                    //MobileCore.configureWithAppID("4fa03d1212c6/47085f890555/launch-9e0c6e9e5482");
                    MobileCore.configureWithAppID("2d6d124a9338/2a263dab1ce3/launch-d8b67b854e8a-development");
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

        //-------------- Add to Cart Button Target Activity code Start -------------------//

        Button AddtoCart = findViewById(R.id.AddtoCart);
        AddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // track this into Analytics
                MobileCore.trackAction("Add to Cart Targeting Button tapped", null);
                // now over to Target
                Snackbar.make(view, "Creating Add to Cart Target request.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                TargetRequest targetRequest1 = new TargetRequest("target-global-mbox-app", null
                        , "{\"color\":\"#000000\""
                        , new AdobeCallback<String>() {
                    @Override
                    public void call(String jsonResponse) {
                        Snackbar.make(view, "Content received", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        // so this should be JSON content...
                        try {
                            JSONObject targetJSONResponse = new JSONObject(jsonResponse);
                            // replace content as needed
                            final String textForTextView1 = targetJSONResponse.getString("color");
                            final TextView textView1 = findViewById(R.id.productname);

                            //final String textForTextView2 = targetJSONResponse.getString("productprice");
                           // final TextView textView2 = findViewById(R.id.productprice);
                          //  final String textForTextView3 = targetJSONResponse.getString("productdescription");
                          //  final TextView textView3 = findViewById(R.id.productdescription);
                          //  final String ProductImagePath = targetJSONResponse.getString("productimage");
                           // final ImageView imageView1 = findViewById(R.id.productimage);

                            if (textForTextView1.length() > 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // imageView1.destroyDrawingCache(); //App local image value replacing method
                                        // imageView1.setDrawingCacheEnabled(true);//App local image value replacing method
                                        // imageView1.buildDrawingCache(true); //App local image value replacing method
                                        textView1.setText(textForTextView1);
                                        //textView2.setText(textForTextView2);
                                       // textView3.setText(textForTextView3);
                                       // Picasso.get().load(ProductImagePath).into(imageView1); // Live public url image value replacing method

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
                TargetRequest targetRequest2 = new TargetRequest("target-global-mbox-applemobile", null
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
                requests.add(targetRequest2);
                // prep done, now retrieve content
                Snackbar.make(view, "Retrieving content from Target", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Target.retrieveLocationContent(requests, null);
            }
        });
*/
        //******************* Click Here Button Target Activity code End ********************//
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
