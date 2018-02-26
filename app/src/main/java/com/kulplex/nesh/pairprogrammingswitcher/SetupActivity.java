package com.kulplex.nesh.pairprogrammingswitcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class SetupActivity extends AppCompatActivity {

    NumberPicker durationMinutesNP, durationSecondsNP, intervalMinutesNP, intervalSecondsNP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);


        durationMinutesNP = findViewById(R.id.DurationMinutesNp);
        durationSecondsNP = findViewById(R.id.DurationSecondsNp);
        intervalMinutesNP = findViewById(R.id.IntervalMinutesNp);
        intervalSecondsNP = findViewById(R.id.IntervalSecondsNp);

        durationMinutesNP.setMinValue(0);
        durationMinutesNP.setMaxValue(59);
        durationSecondsNP.setMinValue(0);
        durationSecondsNP.setMaxValue(59);
        intervalMinutesNP.setMinValue(0);
        intervalMinutesNP.setMaxValue(59);
        intervalSecondsNP.setMinValue(0);
        intervalSecondsNP.setMaxValue(59);

        if(getIntent().getExtras() != null) {
            durationMinutesNP.setValue(getIntent().getExtras().getInt("duration_minutes"));
            durationSecondsNP.setValue(getIntent().getExtras().getInt("duration_seconds"));
            intervalMinutesNP.setValue(getIntent().getExtras().getInt("interval_minutes"));
            intervalSecondsNP.setValue(getIntent().getExtras().getInt("interval_seconds"));
        }
    }

    void OnStartTimer(View v) {
        Intent i = new Intent(v.getContext(), TimerActivity.class);
        i.putExtra("duration_minutes", durationMinutesNP.getValue());
        i.putExtra("duration_seconds", durationSecondsNP.getValue());
        i.putExtra("interval_minutes", intervalMinutesNP.getValue());
        i.putExtra("interval_seconds", intervalSecondsNP.getValue());
        startActivity(i);
    }
}
