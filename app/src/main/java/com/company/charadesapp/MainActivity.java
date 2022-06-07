package com.company.charadesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView questionMark;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.app_title);
        questionMark = findViewById(R.id.question_mark);

        //Loads the animation created in anim folder into the question mark picture and title at the opening page;
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.transition);
        title.startAnimation(animation);
        questionMark.startAnimation(animation);

        //Waits 5 seconds before jumping to next page;
        new Handler().postDelayed(() -> {
            Intent i = new Intent(MainActivity.this, MainMenu.class);
            startActivity(i);
            finish();
        }, 5000);

    }
}