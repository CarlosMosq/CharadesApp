package com.company.charadesapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daprlabs.cardstack.SwipeDeck;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GamePlay extends AppCompatActivity {
    private SwipeDeck swipeDeck;
    TextView team1, team2, secondsOnTimer, phaseTextView;
    int pointsTeam1, pointsTeam2, winnerPoints, loserPoints, cardCount;
    boolean currentTeam = true;
    int currentPhase = 1;
    String winner, loser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        cardCount = Integer.parseInt(getIntent().getStringExtra("nbrOfCards"));
        team1 = findViewById(R.id.teamID);
        team2 = findViewById(R.id.teamID2);
        phaseTextView = findViewById(R.id.phase);
        secondsOnTimer = findViewById(R.id.seconds);
        swipeDeck = findViewById(R.id.swipeDeck);

        adjustTextViews();
        countDown();
        setUpDeck(currentPhase);
//end of onCreate()
    }

    //Uses retrofit to call the list of cards from GitHub
    private void callRetrofit(CardListLoadable CardListLoadable) {
        CompletableFuture.runAsync(() -> {
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl("https://raw.githubusercontent.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitAPICard retrofitAPICard = retrofit.create(RetrofitAPICard.class);

            Call<List<ModelClassCard>> call = retrofitAPICard
                    .getModelClass(getString(R.string.gitHub));
            call.enqueue(new Callback<List<ModelClassCard>>() {
                @Override
                public void onResponse(@NonNull Call<List<ModelClassCard>> call
                        , @NonNull Response<List<ModelClassCard>> response) {
                    CardListLoadable.onCardListLoaded(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<List<ModelClassCard>> call
                        , @NonNull Throwable t) {

                }
            });
        });
//end of callRetrofit()
    }

    //Set up deck of cards and loads the deck to the appropriate view;
    private void setUpDeck(int phase) {
        callRetrofit(data -> {
            Collections.shuffle(data);
            List<ModelClassCard> deckForUse = data.subList(0, cardCount);
            phaseTextView.setText(String.format("%s %s", getString(R.string.phase), phase));
            DeckAdapter deckAdapter = new DeckAdapter(deckForUse, getApplicationContext());
            swipeDeck.setAdapter(deckAdapter);
            swipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
                @Override
                public void cardSwipedLeft(int position) {
                /*if card is swiped left, card was not guessed, so it returns to the end of
                the deck*/
                    deckForUse.add(deckForUse.get(position));
                }

                @Override
                public void cardSwipedRight(int position) {
                    if (currentTeam) {
                        pointsTeam1 += Integer.parseInt(deckForUse.get(position).getPoints());
                    } else {
                        pointsTeam2 += Integer.parseInt(deckForUse.get(position).getPoints());
                    }
                    adjustTextViews();
                }

                @Override
                public void cardsDepleted() {
                    if(currentPhase < 3) {
                        currentPhase++;
                        DeckAdapter deckAdapter = new DeckAdapter(deckForUse, getApplicationContext());
                        swipeDeck.setAdapter(deckAdapter);
                        phaseTextView.setText(String.format("%s %s"
                                , getString(R.string.phase)
                                , currentPhase));
                    }
                    else {
                        if (pointsTeam1 > pointsTeam2) {
                            winner = getString(R.string.team1);
                            loser = getString(R.string.team2);
                            winnerPoints = pointsTeam1;
                            loserPoints = pointsTeam2;
                        }
                        else if (pointsTeam2 > pointsTeam1){
                            winner = getString(R.string.team2);
                            loser = getString(R.string.team1);
                            winnerPoints = pointsTeam2;
                            loserPoints = pointsTeam1;
                        }
                        else {
                            winner = getString(R.string.tie);
                            loser = "";
                            winnerPoints = pointsTeam1;
                            loserPoints = pointsTeam2;
                        }
                        Intent i = new Intent(GamePlay.this, Game_Over.class);
                        i.putExtra("winner", winner);
                        i.putExtra("loser", loser);
                        i.putExtra("winnerPoints", winnerPoints);
                        i.putExtra("loserPoints", loserPoints);
                        startActivity(i);
                    }
                }

                @Override
                public void cardActionDown() {

                }

                @Override
                public void cardActionUp() {

                }
            });
        });
    }

    /*Responsible for the clock on the bottom of the page and for calling the adjustment of
    the current player*/
    private void countDown() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                secondsOnTimer.setTypeface(Typeface.DEFAULT);
                secondsOnTimer.setText(String.format("%ss", l/1000));
            }

            @Override
            public void onFinish() {
                playSound(R.raw.error);
                Toast.makeText(GamePlay.this
                        , getText(R.string.nextPlayer)
                        , Toast.LENGTH_SHORT).show();
                currentTeam = !currentTeam;
                adjustTextViews();
                countDownForTeamSwitch();
            }
        }.start();
    }

    //Clock for 5s to allow the user to handle the phone to another player;
    private void countDownForTeamSwitch() {
        new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long l) {
                playSound(R.raw.seconds);
                secondsOnTimer.setText(String.format("%ss", l/1000));
                secondsOnTimer.setTypeface(Typeface.DEFAULT_BOLD);
            }

            @Override
            public void onFinish() {
                countDown();
            }
        }.start();
    }

    private void playSound(int sound) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, sound);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
    }

    /*Adjust color of the current player to green and bold and the other player to red and default
    style*/
    private void adjustTextViews() {
        team1.setText(String.format("%s: %s", getString(R.string.team1), pointsTeam1));
        team2.setText(String.format("%s: %s", getString(R.string.team2), pointsTeam2));

        if(currentTeam) {
            team1.setTextColor(getColor(R.color.game_green));
            team1.setTypeface(Typeface.DEFAULT_BOLD);
            team2.setTextColor(getColor(R.color.game_red));
            team2.setTypeface(Typeface.DEFAULT);
        }
        else {
            team1.setTextColor(getColor(R.color.game_red));
            team1.setTypeface(Typeface.DEFAULT);
            team2.setTextColor(getColor(R.color.game_green));
            team2.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

}