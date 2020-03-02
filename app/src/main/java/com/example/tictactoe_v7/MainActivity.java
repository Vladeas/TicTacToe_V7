package com.example.tictactoe_v7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        configureButtons();
    }


    /*
        - use setOnClickListener for the play button
        - if pressed call the appropriate functions
     */
    public void configureButtons(){
        Button buttonPlayGame = findViewById(R.id.buttonPlayGame);
        Button buttonPlayBest3 =  findViewById(R.id.buttonPlayBest3);
        Button buttonPlayBest5 =  findViewById(R.id.buttonPlayBest5);
        Button buttonJustPlay = findViewById(R.id.buttonPlayNormal);
        ImageButton imageButtonSettings = findViewById(R.id.imageButtonSettings);

        buttonPlayGame.setOnClickListener(this);
        buttonPlayBest3.setOnClickListener(this);
        buttonPlayBest5.setOnClickListener(this);
        buttonJustPlay.setOnClickListener(this);
        imageButtonSettings.setOnClickListener(this);
    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.buttonPlayGame:
                configurePlayButton();
                break;
            case R.id.buttonPlayBest3:
                configureBestThreeButton();
                break;
            case R.id.buttonPlayBest5:
                configureBestFiveButton();
                break;
            case R.id.buttonPlayNormal:
                configureNormalPlayButton();
                break;
            case R.id.imageButtonSettings:
                configureSettingsButton();
                break;
            default:
                break;
        }
    }

    private void configurePlayButton() {
        buttonVisibility(findViewById(R.id.buttonPlayBest3));
        buttonVisibility(findViewById(R.id.buttonPlayBest5));
        buttonVisibility(findViewById(R.id.buttonPlayNormal));
    }

    private void configureBestThreeButton() {
        Intent i = new Intent(MainActivity.this,GameActivity.class);
        i.putExtra("Extra_Game_Type",3);
        startActivity(i);
    }

    private void configureBestFiveButton() {
        Intent i = new Intent(MainActivity.this,GameActivity.class);
        i.putExtra("Extra_Game_Type",5);
        startActivity(i);
    }

    private void configureNormalPlayButton() {
        Intent i = new Intent(MainActivity.this,GameActivity.class);
        i.putExtra("Extra_Game_Type",0);
        startActivity(i);
    }

    private void configureSettingsButton(){
        Intent i = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(i);
    }

    /*
        - If The button is visible make it invisible and vice versa
     */
    private void buttonVisibility(View v){
        if(v.getVisibility() == View.VISIBLE) {
            v.setVisibility(View.INVISIBLE);
        }else{
            v.setVisibility(View.VISIBLE);
        }
    }
}
