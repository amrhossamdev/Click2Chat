package com.amrhossam.instantchat;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;


public class MainActivity extends AppCompatActivity {
    CountryCodePicker ccp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CardView button = findViewById(R.id.button);
        final EditText editText = findViewById(R.id.edit);
        final LinearLayout liner = findViewById(R.id.linear);
        ccp = findViewById(R.id.ccp);


        button.setOnClickListener(v -> {
            final String Code = ccp.getSelectedCountryCode();
            if (editText.getText().toString().trim().length() == 0) {
                Snackbar snackbar = Snackbar.make(liner, getResources().getString(R.string.sorry), Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                String smsNumber = editText.getText().toString();
                boolean isWhatsappInstalled = isWhatsAppInstalled(this);
                if (isWhatsappInstalled) {
                    try {
                        openChat(Code + smsNumber);
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(liner, "WhatsApp not Installed", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });
    }

    private void openChat(String num) {
        String smsNumber = num + "@s.whatsapp.net";
        Uri uri = Uri.parse("smsto:" + smsNumber);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.putExtra("jid", smsNumber);
        i.setPackage("com.whatsapp");
        this.startActivity(i);
    }

    public static boolean isWhatsAppInstalled(Context context) {
        PackageManager packageManager = context.getPackageManager();
        boolean isInstalled;
        try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isInstalled = false;
        }
        return isInstalled;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.rate, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.rate) {
            Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            }
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName())));
            }
        } else if (itemId == R.id.share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.info) + "\n" + " https://play.google.com/store/apps/details?id=com.example.app"); // Adjusted for clarity
            startActivity(Intent.createChooser(sharingIntent, "Share app via"));
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;

    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}