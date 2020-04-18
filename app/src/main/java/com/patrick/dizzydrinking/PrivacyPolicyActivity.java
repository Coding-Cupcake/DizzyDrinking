package com.patrick.dizzydrinking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.PopupMenu;

import java.util.Locale;

/**
 * Created by patrick on 07.03.20.
 */

public class PrivacyPolicyActivity extends AppCompatActivity {

    WebView web;


    private String caller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        //Set the icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_solo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        web = (WebView) findViewById(R.id.webView);

        if (Locale.getDefault().getLanguage().equals("de"))
            web.loadUrl("file:///android_asset/datenschutz.html");
        else web.loadUrl("file:///android_asset/privacy_policy.html");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_privacy, menu);

        caller = getIntent().getExtras().getString("Caller");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        final View menuItemView = findViewById(item.getItemId());

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            showPopup(menuItemView);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showPopup(View v) {

        PopupMenu popup = new PopupMenu(this, v);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup_privacy, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.toString()) {

                    case "Back":
                    case "Zurück":
                        switching();


                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        switching();
    }

    public void switching() {

        if (caller.equals("START")) {
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            intent.putExtra("currentPlayerList", getIntent().getStringArrayListExtra("currentPlayerList"));
            startActivity(intent);
            finish();

        } else {
            Intent intent = new Intent(getApplicationContext(), GameActivity.class);
            intent.putExtra("players", getIntent().getStringArrayListExtra("players"));
            intent.putExtra("mode", getIntent().getBooleanExtra("mode", true));
            intent.putExtra("gamesClassic", getIntent().getStringArrayListExtra("gamesClassic"));
            intent.putExtra("gamesDaring", getIntent().getStringArrayListExtra("gamesDaring"));
            intent.putExtra("category", getIntent().getIntExtra("category", 0));
            intent.putExtra("Caller", "INFO");
            intent.putExtra("lastGame", getIntent().getStringExtra("lastGame"));
            intent.putExtra("lastPlayer", getIntent().getStringExtra("lastPlayer"));
            startActivity(intent);
            finish();
        }

    }

}
