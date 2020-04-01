package com.example.tictactoe_v7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    private Switch switchSoundOnOff;
    private boolean switchOnOffTruth;

    public static final String SHARED_PREFS_SOUND = "sharedPrefsSound";
    public static final String SWITCHSOUND = "switchsoundonoff";

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
        switchSoundOnOff = findViewById(R.id.switchSoundOnOff);

        buttonGoBack.setOnClickListener(this);
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
        if(switchOnOffTruth) {
            MediaPlayer buttonSound = MediaPlayer.create(this, R.raw.swipe_sound_button);
            buttonSound.start();
        }
    }

    //Use SharedPreferences to save the Sound State
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_SOUND, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SWITCHSOUND, switchSoundOnOff.isChecked());
        editor.apply();
    }

    // Get the saved preference for sound in the game (saved in the settings activity)
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_SOUND, MODE_PRIVATE);
        switchOnOffTruth = sharedPreferences.getBoolean(SWITCHSOUND, true);
    }

    // Update the switch to show the same value as saved in shared preferences
    private void updateViews() {
        switchSoundOnOff.setChecked(switchOnOffTruth);
    }
}
