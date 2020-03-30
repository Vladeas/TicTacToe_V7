package com.example.tictactoe_v7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);

        configureButtons();
    }


    public void configureButtons(){ // use findViewById & setOnClickListener's for the buttons
        Button buttonGoBack = findViewById(R.id.buttonGoBack);

        buttonGoBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View v){ // call each button method when button is pressed
        buttonSoundMethod();
        switch (v.getId()){
            case R.id.buttonGoBack:
                configureGoBackButton();
                break;
            default:
                break;
        }
    }


    private void configureGoBackButton() //end the intent and return to the previous activity
    {
        finish();
    }

    private void buttonSoundMethod(){
        MediaPlayer buttonSound = MediaPlayer.create(this, R.raw.swipe_sound_button);
        buttonSound.start();
    }
}
