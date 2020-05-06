package com.tictactoevladdanciu.tictactoe_v7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private Switch switchSoundOnOff, switchHighContrastOnOff;
    private boolean switchSoundOnOffTruth, switchHighContrastOnOffTruth;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCHSOUND = "switchsoundonoff";
    public static final String SWITCHCONTRAST = "switchhighcontrastonoff";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);

        configureButtons();

        loadData();
        updateViews();
    }

    // use findViewById & setOnClickListener's for the buttons
    public void configureButtons(){
        Button buttonGoBack = findViewById(R.id.buttonGoBack);
        switchHighContrastOnOff = findViewById(R.id.switchHighContrastOnOff);
        switchSoundOnOff = findViewById(R.id.switchSoundOnOff);

        buttonGoBack.setOnClickListener(this);
        switchHighContrastOnOff.setOnClickListener(this);
        switchSoundOnOff.setOnClickListener(this);
    }

    // call each button method when button is pressed
    @Override
    public void onClick(View v){
        buttonSoundMethod();
        switch (v.getId()){
            case R.id.buttonGoBack:
                configureGoBackButton();
                break;
            case R.id.switchSoundOnOff:
                saveData();
                break;
            case R.id.switchHighContrastOnOff:
                saveData();
                break;
            default:
                break;
        }
    }

    //end the intent and return to the previous activity
    private void configureGoBackButton(){
        finish();
    }

    //Play sound for pressing a button
    private void buttonSoundMethod(){
        loadData();
        if(switchSoundOnOffTruth) {
            MediaPlayer buttonSound = MediaPlayer.create(this, R.raw.swipe_sound_button);
            buttonSound.start();
        }
    }

    //Use SharedPreferences to save the Sound State
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCHSOUND, switchSoundOnOff.isChecked());
        editor.putBoolean(SWITCHCONTRAST, switchHighContrastOnOff.isChecked());
        editor.apply();
    }

    // Get the saved preference for sound in the game (saved in the settings activity)
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchSoundOnOffTruth = sharedPreferences.getBoolean(SWITCHSOUND, true);
        switchHighContrastOnOffTruth = sharedPreferences.getBoolean(SWITCHCONTRAST, false);
    }

    // Update the switch to show the same value as saved in shared preferences
    private void updateViews() {
        switchSoundOnOff.setChecked(switchSoundOnOffTruth);
        switchHighContrastOnOff.setChecked(switchHighContrastOnOffTruth);
    }
}
