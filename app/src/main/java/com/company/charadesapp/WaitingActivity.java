package com.company.charadesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class WaitingActivity extends AppCompatActivity {
    private TextView countdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        countdown = findViewById(R.id.countdown);
        String nbrOfCardsNextView = getIntent().getStringExtra("nbrOfCards");
        countDown(nbrOfCardsNextView);
    }

    //Displays 5s period before game starts;
    private void countDown(String nbrOfCards) {
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
                playSound(R.raw.seconds);
                countdown.setText(String.format("%s", l/1000));
            }

            @Override
            public void onFinish() {
                Intent i = new Intent(WaitingActivity.this, GamePlay.class);
                i.putExtra("nbrOfCards", nbrOfCards);
                startActivity(i);
            }
        }.start();
    }

    private void playSound(int sound) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, sound);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
    }

}