package com.company.charadesapp;

import android.content.Intent;
import android.graphics.Typeface;
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
    int pointsTeam1, pointsTeam2, winnerPoints, loserPoints;
    int cardCount = Integer.parseInt(getIntent().getStringExtra("nbrOfCards"));
    boolean currentTeam = true;
    int currentPhase = 1;
    String winner, loser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        team1 = findViewById(R.id.teamID);
        team2 = findViewById(R.id.teamID2);
        phaseTextView = findViewById(R.id.phase);
        secondsOnTimer = findViewById(R.id.seconds);
        adjustTextViews();
        countDown();

        swipeDeck = findViewById(R.id.swipeDeck);
        callRetrofit(data -> {
            Collections.shuffle(data);
            List<ModelClassCard> deckForUse = data.subList(0, cardCount);
            setUpDeck(deckForUse, currentPhase);
        });

//end of onCreate()
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void callRetrofit(onCardListLoaded onCardListLoaded) {
        CompletableFuture.runAsync(() -> {
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl("https://raw.githubusercontent.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitAPICard retrofitAPICard = retrofit.create(RetrofitAPICard.class);

            Call<List<ModelClassCard>> call = retrofitAPICard.getModelClass();
            call.enqueue(new Callback<List<ModelClassCard>>() {
                @Override
                public void onResponse(@NonNull Call<List<ModelClassCard>> call, @NonNull Response<List<ModelClassCard>> response) {
                    onCardListLoaded.onCardListLoaded(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<List<ModelClassCard>> call, @NonNull Throwable t) {

                }
            });
        });
//end of callRetrofit()
    }

    private void setUpDeck(List<ModelClassCard> list, int phase) {
        CompletableFuture.runAsync(() -> {
            phaseTextView.setText(String.format("%s %s", getString(R.string.phase), phase));
            DeckAdapter deckAdapter = new DeckAdapter(list, getApplicationContext());
            swipeDeck.setAdapter(deckAdapter);
            swipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
                @Override
                public void cardSwipedLeft(int position) {
                    //if card is swiped left, card was not guessed, so it returns to the end of the deck
                    list.add(list.get(position));
                    //put cards on shared preferences?
                }

                @Override
                public void cardSwipedRight(int position) {
                    //add cards to a second list to be used on next round?
                    if (currentTeam) {
                        pointsTeam1++;
                    } else {
                        pointsTeam2++;
                    }
                    adjustTextViews();
                }

                @Override
                public void cardsDepleted() {
                    if(currentPhase < 3) {
                        currentPhase++;
                        Collections.shuffle(list);
                        setUpDeck(list, phase);
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
                    //check what is happening to cards
                }

                @Override
                public void cardActionUp() {

                }
            });
        });
    }

    //responsible for the clock on the bottom of the page and for calling the adjustment of the current player
    private void countDown() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                secondsOnTimer.setTypeface(Typeface.DEFAULT);
                secondsOnTimer.setText(String.format("%ss", l/1000));
            }

            @Override
            public void onFinish() {
                Toast.makeText(GamePlay.this, getText(R.string.nextPlayer), Toast.LENGTH_SHORT).show();
                currentTeam = !currentTeam;
                adjustTextViews();
                countDownForTeamSwitch();
            }
        }.start();
    }

    private void countDownForTeamSwitch() {
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
                secondsOnTimer.setText(String.format("%ss", l/1000));
                secondsOnTimer.setTypeface(Typeface.DEFAULT_BOLD);
            }

            @Override
            public void onFinish() {
                countDown();
            }
        }.start();
    }

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