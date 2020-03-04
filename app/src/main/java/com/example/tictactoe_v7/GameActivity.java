package com.example.tictactoe_v7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Double.*;



public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private int boardLength = 3;
    private int moveCount, roundCountPlayerOne, roundCountPlayerTwo, playerOnePoints, playerTwoPoints;
    private boolean playerOneTurn = true;
    private TextView textViewPlayerOne;
    private TextView textViewPlayerTwo;
    private ImageButton[][] imageButtons = new ImageButton[boardLength][boardLength];
    boolean popUpVisible = false;

    /*
        - initialize the Used Views (by Id)
        - set on clickListener for each ImageButton(From which the board is composed)
        - set on clickListener for the two[2] reset Buttons(From which the board is composed)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        textViewPlayerOne = findViewById(R.id.textViewPlayerOne);
        textViewPlayerTwo = findViewById(R.id.textViewPlayerTwo);

        configureButtons();

    }

    private void configureButtons(){
        Button buttonMainMenu = findViewById(R.id.buttonMainMenu);
        Button buttonResetGame = findViewById(R.id.buttonResetGame);
        Button buttonResetBoard = findViewById(R.id.buttonResetBoard);

        buttonMainMenu.setOnClickListener(this);
        buttonResetGame.setOnClickListener(this);
        buttonResetBoard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.buttonMainMenu:
                finish();
                break;
            case R.id.buttonResetGame:
                break;
            case R.id.buttonResetBoard:
                break;
            default:
                break;
        }
    }


}
