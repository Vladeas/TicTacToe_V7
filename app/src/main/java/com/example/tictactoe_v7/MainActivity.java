package com.example.tictactoe_v7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final static String three = "3", five = "5", unDefined = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        configureButtons();
    }



    public void configureButtons(){ // use findViewById & setOnClickListener's for the buttons
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
    public void onClick(View v){ // call each button method when button is pressed
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



    private void configureBestThreeButton() {
        Intent i = new Intent(MainActivity.this,GameActivity.class);
        i.putExtra("Extra_Game_Type",three);
        startActivity(i);
    }

    private void configureBestFiveButton() {
        Intent i = new Intent(MainActivity.this,GameActivity.class);
        i.putExtra("Extra_Game_Type",five);
        startActivity(i);
    }

    private void configureNormalPlayButton() {
        Intent i = new Intent(MainActivity.this,GameActivity.class);
        i.putExtra("Extra_Game_Type",unDefined);
        startActivity(i);
    }

    private void configureSettingsButton(){
        Intent i = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(i);
    }


    private void configurePlayButton() { // show/hide play options
        buttonVisibility(findViewById(R.id.buttonPlayBest3));
        buttonVisibility(findViewById(R.id.buttonPlayBest5));
        buttonVisibility(findViewById(R.id.buttonPlayNormal));
    }


    private void buttonVisibility(View v){ //If The button is visible make it invisible and vice versa
        if(v.getVisibility() == View.VISIBLE) {
            v.setVisibility(View.INVISIBLE);
        }else{
            v.setVisibility(View.VISIBLE);
        }
    }
}
