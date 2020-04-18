package com.patrick.dizzydrinking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.patrick.dizzydrinking.R.string;


public class StartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int PERMISSION_REQUEST_READ_CONTACTS = 0;
    private static boolean PERMISSION_GRANTED = false;

    public static ArrayList<String> playerList;
    public static boolean normalMod = false;

    private View mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mLayout = findViewById(R.id.contactPicker);

        //Set the icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_solo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //Set the font
        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/steelfish_rg.ttf");

        playerList = getIntent().getStringArrayListExtra("currentPlayerList");

        if (playerList == null)
            playerList = new ArrayList<>();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Set list items
        displayList();

        normalMod = getIntent().getBooleanExtra("mode", false);

        //Contact button
        ImageButton getContacts = (ImageButton) findViewById(R.id.contactPicker);
        getContacts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showContactActivity();
            }
        });

        //Send button
        ImageButton sendNames = (ImageButton) findViewById(R.id.sendInput);
        sendNames.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                initPlayerList();

                EditText edit = (EditText) findViewById(R.id.editText);
                String input = edit.getText().toString();
                String[] values = input.split(", ");

                ArrayList<String> cleanValues = new ArrayList<>(Arrays.asList(values));
                for (String val : cleanValues)
                    if (val.equals("")) cleanValues.remove(val);


                for (String player : cleanValues) {
                    if (!playerList.contains(player))
                        playerList.add(player);
                }

                if (playerList.isEmpty()) {

                    setDefault();
                    Toast.makeText(getApplicationContext(), getString(string.playersNr), Toast.LENGTH_SHORT).show();

                } else {

                    final ListAdapter adapter;
                    Collections.sort(playerList);
                    adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, playerList);

                    final ListView allContacts = (ListView) findViewById(R.id.listView);
                    allContacts.setAdapter(adapter);
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    EditText playersEnteredManually = findViewById(R.id.editText);
                    playersEnteredManually.getText().clear();
                }

            }
        });

        //Start game
        final TextView startGame = (TextView) findViewById(R.id.nextChallenge);
        startGame.setTypeface(myFont);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initPlayerList();

                if (playerList.size() < 2) {
                    Toast.makeText(getApplicationContext(), getString(string.emptyList), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                    intent.putExtra("players", playerList);
                    intent.putExtra("mode", normalMod);
                    startActivity(intent);
                }
            }
        });

        //Choose names
        final TextView chooseNames = (TextView) findViewById(R.id.enterNames);
        chooseNames.setTypeface(myFont);

        //players
        final TextView players = (TextView) findViewById(R.id.playersParticipating);
        players.setTypeface(myFont);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);

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

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {

    }

    public void showPopup(View v) {

        final View myView = v;
        PopupMenu popup = new PopupMenu(this, v);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.toString()) {

                    case "Reset Players":
                    case "Spieler zurücksetzen":
                        setDefault();
                        playerList = null;
                        break;

                    case "Information":
                    case "Informationen":
                        Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                        intent.putExtra("Caller", "START");
                        intent.putExtra("currentPlayerList", playerList);
                        startActivity(intent);
                        break;

                    case "Change Mode":
                        showMods(myView, true);
                        break;

                    case "Modus wechseln":
                        showMods(myView, false);
                        break;

                    case "Datenschutz":
                    case "Privacy Policy":
                        Intent privacyIntent = new Intent(getApplicationContext(), PrivacyPolicyActivity.class);
                        privacyIntent.putExtra("Caller", "START");
                        privacyIntent.putExtra("currentPlayerList", playerList);
                        startActivity(privacyIntent);
                        break;

                    case "Quit":
                    case "Beenden":
                        finish();
                }

                return false;
            }
        });
    }

    public void showMods(View v, final boolean english) {

        PopupMenu popup = new PopupMenu(this, v);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup_mods, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.toString()) {

                    case "Multi":
                        normalMod = true;
                        if (english)
                            Toast.makeText(getApplicationContext(), "Chosen Mode: Multi", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Gewählter Modus: Multi", Toast.LENGTH_SHORT).show();
                        break;

                    case "Unique":
                        normalMod = false;
                        if (english)
                            Toast.makeText(getApplicationContext(), "Chosen Mode: Unique", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getApplicationContext(), "Gewählter Modus: Unique", Toast.LENGTH_SHORT).show();
                }


                return false;
            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void displayList() {

        //List View for Players
        final ListView allContacts = (ListView) findViewById(R.id.listView);


        //Players Content List
        final ListAdapter adapter;
        Collections.sort(playerList);
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, playerList);


        if (!playerList.isEmpty()) {

            allContacts.setAdapter(adapter);

        } else setDefault();

        //Remove players
        allContacts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long arg3) {

                if (!playerList.isEmpty()) {
                    playerList.remove(position);

                    if (!playerList.isEmpty()) {
                        final ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, playerList);
                        //Assign new adapter to ListView
                        allContacts.setAdapter(adapter);
                    } else setDefault();
                }
                return false;
            }

        });

        //ListView Item Click Listener
        allContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });
    }

    public void setDefault() {

        String[] defaultList = {getString(string.player1), getString(string.player2), getString(string.dots), getString(string.playerN)};

        final ListAdapter def = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, defaultList);

        final ListView allContacts = (ListView) findViewById(R.id.listView);

        //Assign default adapter to ListView
        allContacts.setAdapter(def);

        //Reset players
        playerList = new ArrayList<>();

    }

    private void startContactActivity() {
        Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
        intent.putExtra("ownList", playerList);
        startActivity(intent);
    }


    private void showContactActivity() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            startContactActivity();
        } else {
            requestReadContactsPermission();
        }
    }

    public void requestReadContactsPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(StartActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},
                    PERMISSION_REQUEST_READ_CONTACTS);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_READ_CONTACTS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.PERMISSION_GRANTED = true;
                } else {
                    this.PERMISSION_GRANTED = false;
                }
                return;
            }
        }
    }

    public void initPlayerList(){
        if (playerList == null)
            playerList = new ArrayList<>();
    }

}


