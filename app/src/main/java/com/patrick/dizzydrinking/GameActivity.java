package com.patrick.dizzydrinking;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class GameActivity extends ActionBarActivity {

    private String[] allPlayers;
    private boolean mod;
    private int category;
    private String currentGame;
    private String currentPlayer;

    private static ArrayList<String> gamesClassicAll;
    private static ArrayList<String> gamesDaringAll;

    private ArrayList<String> gamesClassic;
    private ArrayList<String> gamesDaring;

    private ArrayList<String> gamesClassic_Time;
    private ArrayList<String> gamesDaring_Time;
    private static ArrayList<String> gamesDaringAll_Time;

    ImageButton startWatch;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Set the icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_solo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Set the font
        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/steelfish_rg.ttf");

        //Retrieve data
        allPlayers = getIntent().getStringArrayExtra("players");
        mod = getIntent().getBooleanExtra("mode", true);
        category = getIntent().getIntExtra("category",0);
        gamesClassic = getIntent().getStringArrayListExtra("gamesClassic");
        gamesDaring = getIntent().getStringArrayListExtra("gamesDaring");

        final TextView players = (TextView) findViewById(R.id.players);
        final Button challenge = (Button)findViewById(R.id.currentChallenge);
        challenge.setTypeface(myFont);

        startWatch = (ImageButton)findViewById(R.id.startWatch);
        startWatch.setVisibility(View.INVISIBLE);

        if(category == 0)
        challenge.setText(R.string.gameClassic);
        else challenge.setText(R.string.gameDaring);

        String randomPlayer = (allPlayers[new Random().nextInt(allPlayers.length)]);

        //Display the challenge
        if(getIntent().getStringExtra("Caller").equals("INFO")) {

            currentPlayer = getIntent().getStringExtra("lastPlayer");
            currentGame = getIntent().getStringExtra("lastGame");

            TextView challengeText = (TextView) findViewById(R.id.description);

            challengeText.setText(currentGame);
            players.setText(currentPlayer);

        } else {

            int show = showGame(category);

            if (show == 0) {
                players.setText(randomPlayer);
                currentPlayer = randomPlayer;
            }

            else {
                players.setText("");
                currentPlayer = "";
            }

        }
        //Next challenge
        TextView nextCh = (TextView) findViewById(R.id.nextChallenge);
        nextCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                intent.putExtra("players", allPlayers);
                intent.putExtra("mode", mod);
                intent.putExtra("gamesClassic", gamesClassic);
                intent.putExtra("gamesDaring", gamesDaring);
                intent.putExtra("category", category);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
        inflater.inflate(R.menu.menu_popup_game, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.toString()) {

                    case "Main Menu":
                    case "Hauptmenü":
                        Intent intentStart = new Intent(getApplicationContext(), StartActivity.class);
                        intentStart.putExtra("mode", mod);
                        startActivity(intentStart);
                        finish();
                        break;

                    case "Information":
                    case "Informationen":
                        Intent intentInfo = new Intent(getApplicationContext(), InfoActivity.class);
                        intentInfo.putExtra("Caller", "GAME");
                        intentInfo.putExtra("players", allPlayers);
                        intentInfo.putExtra("mode", mod);
                        intentInfo.putExtra("gamesClassic", gamesClassic);
                        intentInfo.putExtra("gamesDaring", gamesDaring);
                        intentInfo.putExtra("category", category);
                        intentInfo.putExtra("lastGame", currentGame);
                        intentInfo.putExtra("lastPlayer", currentPlayer);
                        startActivity(intentInfo);
                        finish();
                        break;

                }

                return false;
            }
        });
    }

    public int showGame(int category) {

        TextView challenge = (TextView) findViewById(R.id.description);

        if(mod){

            if (category == 0) {

                int result;

                ArrayList<String> classicGamesAll = new ArrayList<String>();
                ArrayList<String> classicGamesCombined = new ArrayList<String>();

                classicGamesAll.addAll(Arrays.asList(getResources().getStringArray(R.array.gamesForAll_Classic)));
                classicGamesCombined.addAll(Arrays.asList(getResources().getStringArray(R.array.classics)));
                classicGamesCombined.addAll(Arrays.asList(getResources().getStringArray(R.array.gamesForAll_Classic)));

                String randomGame = (classicGamesCombined.get(new Random().nextInt(classicGamesCombined.size())));
                currentGame = randomGame;
                challenge.setText(randomGame);
                mod = true;

                if(classicGamesAll.contains(randomGame))
                    result = 1;
                else result = 0;
                check_time(challenge.getText().toString());
                return result;

            }

            else {

                int result;

                ArrayList<String> daringGamesAll = new ArrayList<String>();
                ArrayList<String> daringGamesCombined = new ArrayList<String>();

                daringGamesAll.addAll(Arrays.asList(getResources().getStringArray(R.array.gamesForAll_Daring)));
                daringGamesCombined.addAll(Arrays.asList(getResources().getStringArray(R.array.daring)));
                daringGamesCombined.addAll(Arrays.asList(getResources().getStringArray(R.array.gamesForAll_Daring)));


                String randomGame = (daringGamesCombined.get(new Random().nextInt(daringGamesCombined.size())));
                currentGame = randomGame;
                challenge.setText(randomGame);
                mod = true;

                if(daringGamesAll.contains(randomGame))
                    result = 1;
                else result = 0;
                check_time(challenge.getText().toString());
                return result;

            }

        } else {

            mod = false;

            if(category == 0) {

                if(gamesClassic == null) {
                    String[] temp = getResources().getStringArray(R.array.classics);
                    String[] temp2 =  getResources().getStringArray(R.array.gamesForAll_Classic);

                    gamesClassic = new ArrayList();
                    gamesClassicAll = new ArrayList<>();

                    gamesClassicAll.addAll(Arrays.asList(temp2));
                    gamesClassic.addAll(Arrays.asList(temp));
                    gamesClassic.addAll(Arrays.asList(temp2));
                }


                if(gamesClassic.size() > 0) {

                    int result;
                    String randomGame = (gamesClassic.get(new Random().nextInt(gamesClassic.size())));
                    currentGame = randomGame;
                    challenge.setText(randomGame);
                    gamesClassic.remove(randomGame);

                    if(gamesClassicAll.contains(randomGame))
                        result = 1;
                    else result = 0;
                    check_time(challenge.getText().toString());
                    return result;

                }



            } else {

                if(gamesDaring == null) {
                    String[] temp = getResources().getStringArray(R.array.daring);
                    String[] temp2 =  getResources().getStringArray(R.array.gamesForAll_Daring);

                    gamesDaring = new ArrayList();
                    gamesDaringAll = new ArrayList<>();

                    gamesDaringAll.addAll(Arrays.asList(temp2));
                    gamesDaring.addAll(Arrays.asList(temp));
                    gamesDaring.addAll(Arrays.asList(temp2));
                }

                if(gamesDaring.size() > 0) {

                    int result;

                    String randomGame = (gamesDaring.get(new Random().nextInt(gamesDaring.size())));
                    currentGame = randomGame;
                    challenge.setText(randomGame);
                    gamesDaring.remove(randomGame);

                    if(gamesDaringAll.contains(randomGame))
                        result = 1;
                    else result = 0;
                    check_time(challenge.getText().toString());
                    return result;
                }

            }

        }

            return 0;
    }

    public void check_time (String text) {

        if (text.startsWith("Zeitspiel")) {

            startWatch.setVisibility(View.VISIBLE);

            startWatch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent in_tent = new Intent(getApplicationContext(), ClockActivity.class);
                in_tent.putExtra("Caller", "GAME");
                in_tent.putExtra("players", allPlayers);
                in_tent.putExtra("mode", mod);
                in_tent.putExtra("gamesClassic", gamesClassic);
                in_tent.putExtra("gamesDaring", gamesDaring);
                in_tent.putExtra("category", category);
                in_tent.putExtra("lastGame", currentGame);
                in_tent.putExtra("lastPlayer", currentPlayer);
                startActivity(in_tent);
                finish();

            }
        });

    } }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putStringArrayList("classicGames", gamesClassic);
        savedInstanceState.putStringArrayList("daringGames", gamesDaring);
        savedInstanceState.putBoolean("mode", mod);

        // etc.
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
       gamesClassic = savedInstanceState.getStringArrayList("classicGames");
       gamesDaring = savedInstanceState.getStringArrayList("daringGames");
       mod = savedInstanceState.getBoolean("mode");
    }

    @Override
    public void onBackPressed() {}

}
