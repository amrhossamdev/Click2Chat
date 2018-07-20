package com.amrhossam.instantchat;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.telephony.PhoneNumberUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.amrhossam.instantchatm.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.hbb20.CountryCodePicker;


public class MainActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    //Donate Me https://www.paypal.me/amrhossamdev

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CardView button = findViewById(R.id.button);
        final EditText editText = findViewById(R.id.edit);
        final LinearLayout liner = findViewById(R.id.linear);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //Donate Me https://www.paypal.me/amrhossamdev


        mAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAdFailedToLoad(int error) {
                mAdView.setVisibility(View.GONE);
            }


        });
        final InterstitialAd mInterstitialAd = new InterstitialAd(MainActivity.this);
        // Put your AdUnitId here
        mInterstitialAd.setAdUnitId("");
        AdRequest adRequestInter = new AdRequest.Builder().build();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });
        mInterstitialAd.loadAd(adRequestInter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Code = ccp.getSelectedCountryCode();
                if (editText.getText().toString().trim().length() <= 0) {

                    Snackbar snackbar = Snackbar
                            .make(liner, getResources().getString(R.string.sorry), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {


                    String smsNumber = editText.getText().toString();

                    boolean isWhatsappInstalled = whatsappInstalledOrNot("com.whatsapp");
                    if (isWhatsappInstalled) {
                        final InterstitialAd mInterstitialAd = new InterstitialAd(MainActivity.this);
                        // Put your AdUnitId here
                        mInterstitialAd.setAdUnitId("");
                        AdRequest adRequestInter = new AdRequest.Builder().build();
                        mInterstitialAd.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                mInterstitialAd.show();
                            }
                        });
                        mInterstitialAd.loadAd(adRequestInter);

                        Intent sendIntent = new Intent("android.intent.action.MAIN");
                        sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                        sendIntent.putExtra("jid", Code + PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");


                        startActivity(sendIntent);
                    } else {

                        Snackbar snackbar = Snackbar
                                .make(liner, "WhatsApp not Installed", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }

                }
            }
        });
    }

    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.rate, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rate:
                Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
                }

                break;
            case R.id.share:
                Intent sendIntent = new Intent();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.info) + "\n" + " https://play.google.com/store/apps/details?id=com.amrhossam.instantchat");
                startActivity(Intent.createChooser(sharingIntent, "Share app via"));

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();


    }
}
