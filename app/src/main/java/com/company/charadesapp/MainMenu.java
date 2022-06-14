package com.company.charadesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {

    Button startButton;
    String[] cardCountArray = {"50", "100", "200"};
    ArrayAdapter<String> cardCountAdapter;
    AutoCompleteTextView cardCount;
    String nbrOfCardsToPlay = "50";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        startButton = findViewById(R.id.startButton);
        cardCount = findViewById(R.id.nbrOfCardsAutoComplete);

        cardCountAdapter = new ArrayAdapter<>(this, R.layout.card_dropdown, cardCountArray);
        cardCount.setAdapter(cardCountAdapter);
        cardCount.setOnItemClickListener((adapterView, view, i, l) -> nbrOfCardsToPlay =
                adapterView
                        .getItemAtPosition(i)
                        .toString());

        startButton.setOnClickListener(v -> {
            Intent intentToStart = new Intent(MainMenu.this, WaitingActivity.class);
            intentToStart.putExtra("nbrOfCards", nbrOfCardsToPlay);
            startActivity(intentToStart);
            finish();
        });

    }
}