package com.company.charadesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class WaitingActivity extends AppCompatActivity {
    private TextView countdown;
    private final String nbrOfCardsNextView = getIntent().getStringExtra("nbrOfCards");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        countdown = findViewById(R.id.countdown);
        countDown();
    }

    private void countDown() {
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
                countdown.setText(String.format("%s", l/1000));
            }

            @Override
            public void onFinish() {
                Intent i = new Intent(WaitingActivity.this, GamePlay.class);
                i.putExtra("nbrOfCards", nbrOfCardsNextView);
                startActivity(i);
            }
        }.start();
    }

}