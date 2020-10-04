package com.patrick.dizzydrinking;

import android.content.Intent;
import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;


public class CategoryActivity extends AppCompatActivity {

    private String[] players;
    public  boolean mod;
    private ArrayList<String> gamesClassic;
    private ArrayList<String> gamesDaring;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //Set the icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_solo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Retrieve player list
        players = getIntent().getStringArrayExtra("players");

        //Set the font
        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/steelfish_rg.ttf");

        final Button cat = (Button)findViewById(R.id.button2);
        cat.setTypeface(myFont);

        //Retrieve booleans
        mod = getIntent().getBooleanExtra("mode", true);

        //Retrieve modified game list
        gamesClassic = getIntent().getStringArrayListExtra("gamesClassic");
        gamesDaring = getIntent().getStringArrayListExtra("gamesDaring");

        //Classic
        ImageView classic = (ImageView)findViewById(R.id.classic);
        classic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gamesClassic != null && gamesClassic.size() == 0) {
                    Toast.makeText(getApplicationContext(),R.string.noClassics, Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                    intent.putExtra("category", 0);
                    intent.putExtra("players", players);
                    intent.putExtra("mode", mod);
                    intent.putExtra("gamesClassic", gamesClassic);
                    intent.putExtra("gamesDaring", gamesDaring);
                    intent.putExtra("Caller", "CAT");
                    startActivity(intent);
                }

            }
        });

        //Daring
        ImageView daring = (ImageView)findViewById(R.id.daring);
        daring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(gamesDaring != null && gamesDaring.size() == 0) {
                    Toast.makeText(getApplicationContext(),R.string.noDaring, Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(),GameActivity.class);
                    intent.putExtra("category", 1);
                    intent.putExtra("players", players);
                    intent.putExtra("mode", mod);
                    intent.putExtra("gamesClassic", gamesClassic);
                    intent.putExtra("gamesDaring", gamesDaring);
                    intent.putExtra("Caller", "CAT");
                    startActivity(intent);
                }

            }
        });

        if(gamesDaring != null && gamesDaring.size() == 0 && gamesClassic != null && gamesClassic.size() == 0) {
            classic.setClickable(false);
            daring.setClickable(false);

            for(int i = 0; i < 2; i++)
                Toast.makeText(getApplicationContext(),R.string.noGames, Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category, menu);
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
        inflater.inflate(R.menu.menu_popup_home, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.toString()) {


                    case "Main Menu":
                    case "Hauptmenü" :  Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                                        startActivity(intent);
                                        finish();
                                        break;

                }

                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {}
}
