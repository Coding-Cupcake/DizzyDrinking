package com.patrick.dizzydrinking;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

public class ClockActivity extends ActionBarActivity {

    Chronometer my_clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        //Set the icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_solo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        my_clock = (Chronometer)findViewById(R.id.chronometer);

        Button startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(mStartListener);

        Button stopButton = (Button)findViewById(R.id.stop_Button);
        stopButton.setOnClickListener(mStopListener);

        Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(mResetListener);

    }


    View.OnClickListener mStartListener = new View.OnClickListener() {
        public void onClick(View v) {
            int stoppedMilliseconds = 0;

            String chronoText = my_clock.getText().toString();
            String array[] = chronoText.split(":");
            if (array.length == 2) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                        + Integer.parseInt(array[1]) * 1000;
            } else if (array.length == 3) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                        + Integer.parseInt(array[1]) * 60 * 1000
                        + Integer.parseInt(array[2]) * 1000;
            }

            my_clock.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
            my_clock.start();
        }
    };

    View.OnClickListener mStopListener = new View.OnClickListener() {
        public void onClick(View v) {
            my_clock.stop();
        }
    };

    View.OnClickListener mResetListener = new View.OnClickListener() {
        public void onClick(View v) {
            my_clock.setBase(SystemClock.elapsedRealtime());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clock, menu);
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
        inflater.inflate(R.menu.menu_popup_clock, popup.getMenu());
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
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        intent.putExtra("players", getIntent().getStringArrayExtra("players"));
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





