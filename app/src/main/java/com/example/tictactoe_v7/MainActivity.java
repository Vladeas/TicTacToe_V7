package com.example.tictactoe_v7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final static String three = "3", five = "5", unDefined = "0";
    private boolean switchOnOffTruth;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCHSOUND = "switchsoundonoff";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        loadSoundPreference();
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
        loadSoundPreference();
        buttonSoundMethod();
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


    //Use intent to start the new game activity and send a string representative of the chosen game type
    private void configureBestThreeButton() {
        Intent i = new Intent(MainActivity.this,GameActivity.class);
        i.putExtra("Extra_Game_Type",three);
        startActivity(i);
    }
    //Use intent to start the new game activity and send a string representative of the chosen game type
    private void configureBestFiveButton() {
        Intent i = new Intent(MainActivity.this,GameActivity.class);
        i.putExtra("Extra_Game_Type",five);
        startActivity(i);
    }
    //Use intent to start the new game activity and send a string representative of the chosen game type
    private void configureNormalPlayButton() {
        Intent i = new Intent(MainActivity.this,GameActivity.class);
        i.putExtra("Extra_Game_Type",unDefined);
        startActivity(i);
    }
    //Use intent to start the new settings activity
    private void configureSettingsButton(){
        Intent i = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(i);
    }
    // Make the other play types(Buttons) visible
    private void configurePlayButton() { // show/hide play options
        buttonVisibility(findViewById(R.id.buttonPlayBest3));
        buttonVisibility(findViewById(R.id.buttonPlayBest5));
        buttonVisibility(findViewById(R.id.buttonPlayNormal));
    }

    //If The button is visible make it invisible and vice versa
    private void buttonVisibility(View v){
        if(v.getVisibility() == View.VISIBLE) {
            v.setVisibility(View.INVISIBLE);
        }else{
            v.setVisibility(View.VISIBLE);
        }
    }

    // Play the sound effect for pressing a button, also check if the sound is enabled or not
    private void buttonSoundMethod(){
        loadSoundPreference();
        if(switchOnOffTruth) {
            MediaPlayer buttonSound = MediaPlayer.create(this, R.raw.swipe_sound_button);
            buttonSound.start();
        }
    }

    // Get the saved preference for sound in the game (taken from the settings activity)
    public void loadSoundPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchOnOffTruth = sharedPreferences.getBoolean(SWITCHSOUND, true);
    }
}
