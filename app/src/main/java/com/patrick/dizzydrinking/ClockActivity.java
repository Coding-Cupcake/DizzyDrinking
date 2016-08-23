package com.patrick.dizzydrinking;

import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ClockActivity extends ActionBarActivity {

    Chronometer my_clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        my_clock = (Chronometer)findViewById(R.id.chronometer);

        Button startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(mStartListener);

        Button stopButton = (Button)findViewById(R.id.stop_Button);
        stopButton.setOnClickListener(mStopListener);

        Button resetButton = new Button(this);
        resetButton.setText("Reset");
        resetButton.setOnClickListener(mResetListener);
        layout.addView(resetButton);

       // setContentView(layout);
    }

    private void showElapsedTime() {
        long elapsedMillis = SystemClock.elapsedRealtime() - my_clock.getBase();
        Toast.makeText(ClockActivity.this, "Elapsed milliseconds: " + elapsedMillis,
                Toast.LENGTH_SHORT).show();
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
            showElapsedTime();
        }
    };

    View.OnClickListener mResetListener = new View.OnClickListener() {
        public void onClick(View v) {
            my_clock.setBase(SystemClock.elapsedRealtime());
            showElapsedTime();
        }
    };
}





