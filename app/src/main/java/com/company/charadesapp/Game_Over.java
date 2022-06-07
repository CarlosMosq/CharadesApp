package com.company.charadesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Game_Over extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView winningTeam = findViewById(R.id.winningTeam);
        TextView winningTeamPoints = findViewById(R.id.winningTeamPoints);
        TextView losingTeam = findViewById(R.id.losingTeam);
        TextView losingTeamPoints = findViewById(R.id.losingTeamPoints);
        Button restartButton = findViewById(R.id.restartButton);
        String winner = getIntent().getStringExtra("winner");

        if(winner.equals(getString(R.string.tie))) {
            losingTeam.setVisibility(View.INVISIBLE);
            losingTeamPoints.setVisibility(View.INVISIBLE);
        }

        winningTeam.setText(winner);
        winningTeamPoints.setText(getIntent().getStringExtra("winnerPoints"));
        losingTeam.setText(getIntent().getStringExtra("loser"));
        losingTeamPoints.setText(getIntent().getStringExtra("loserPoints"));

        restartButton.setOnClickListener(view -> {
            Intent i = new Intent(Game_Over.this, MainMenu.class);
            startActivity(i);
            finish();
        });

    }
}