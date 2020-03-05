package com.example.tictactoe_v7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);


        configureTextViewsScore();
        configureButtons();

    }


    private void configureTextViewsScore(){
        textViewPlayerOne = findViewById(R.id.textViewPlayerOne);
        textViewPlayerTwo = findViewById(R.id.textViewPlayerTwo);
    }

    private void configureButtons(){
        Button buttonMainMenu = findViewById(R.id.buttonMainMenu);
        Button buttonResetGame = findViewById(R.id.buttonResetGame);
        Button buttonResetBoard = findViewById(R.id.buttonResetBoard);
        Button buttonMainMenuEnd = findViewById(R.id.buttonGoMainMenuEnd);
        Button buttonPlayAgainEnd = findViewById(R.id.buttonPlayAgainEnd);


        buttonMainMenu.setOnClickListener(this);
        buttonResetGame.setOnClickListener(this);
        buttonResetBoard.setOnClickListener(this);
        buttonMainMenuEnd.setOnClickListener(this);
        buttonPlayAgainEnd.setOnClickListener(this);

        for(int i = 0; i < boardLength;i++){ // setOnClickListener for all boardGame buttons
            for(int j = 0; j < boardLength;j++){
                int resourceID = getResources().getIdentifier(("imageButton" + i) + j,"id",getPackageName()); //
                imageButtons[i][j] = findViewById(resourceID);
                imageButtons[i][j].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v){
        if(!popUpVisible) { // Buttons only accessible if the Game won window is not visible
            switch (v.getId()) {
                case R.id.buttonMainMenu:
                    finish();
                    break;
                case R.id.buttonResetGame:
                    configureButtonResetScore();
                    break;
                case R.id.buttonResetBoard:
                    configureButtonResetBoard();
                    break;
                default:
                    if (!(v).getTag().toString().equals("")) {
                        break;
                    }
                    if (playerOneTurn) {
                        (v).setBackgroundResource(R.drawable.roman_helmet_512px_white);
                        (v).setTag("x");
                        setPlayerTurnIcon("PlayerOne");
                    } else {
                        (v).setBackgroundResource(R.drawable.viking_helmet_512px_grey);
                        (v).setTag("o");
                        setPlayerTurnIcon("PlayerTwo");
                    }
                    moveCount++;
                    winConditionsConfiguration();
                    break;
            }
        } else {// Buttons only accessible if the Game won window is visible
            if (v.getId() == R.id.buttonGoMainMenuEnd)
                finish();
            if(v.getId() == R.id.buttonPlayAgainEnd)
                configureButtonResetScore();
        }
    }

    private void setPlayerTurnIcon(String player){
        ImageView imageViewPlayerTurn = findViewById(R.id.imageViewPlayerTurn);
        if(!player.equals("PlayerOne"))
            imageViewPlayerTurn.setBackgroundResource(R.drawable.roman_helmet_512px_white);
        else
            imageViewPlayerTurn.setBackgroundResource(R.drawable.viking_helmet_512px_white);
    }


    private void winConditionsConfiguration(){
        if (checkForWin()) {
            if (playerOneTurn) {
                playerOnePoints++;
                roundCountPlayerOne++;
                toastMessage("Player One Wins !");
                if(!gameEnd()) {
                    configureButtonResetBoard();
                }
            } else {
                playerTwoPoints++;
                roundCountPlayerTwo++;
                toastMessage("Player Two Wins !");
                if(!gameEnd()) {
                    configureButtonResetBoard();
                }
            }
        } else if (moveCount == 9) {
            toastMessage("Draw !");
        } else {
            playerOneTurn = !playerOneTurn;
        }
        updatePointsText();
        gameEnd();
    }


    private boolean gameEnd(){
        Double gameLength = 0.;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                gameLength = Double.parseDouble(extras.getString("Extra_Game_Type")); //The key argument here must match the one used in the other activity
            } catch(NullPointerException e) {
                Log.i("Error", "NullPointerException");
            }

            if(gameLength != 0) {
                if (roundCountPlayerOne >= Math.ceil(gameLength / 2.0)) {
                    setEndGameWindowVisibility(true);
                    playerEndGameWin(1);
                    return true;
                } else if (roundCountPlayerTwo >= Math.ceil(gameLength / 2.0)) {
                    setEndGameWindowVisibility(true);
                    playerEndGameWin(2);
                    return true;
                }
            }
        }
        return false;

    }


    private void playerEndGameWin(int player){
        TextView textViewEndGame = findViewById(R.id.textViewEndGame);
        String message = "Player " + player + " won!";
        textViewEndGame.setText(message);
    }


    public boolean checkForWin(){
        String[][] field = new String[boardLength][boardLength];

        for (int i = 0; i < boardLength;i++){
            for(int j = 0 ; j < boardLength;j++){
                field[i][j] = imageButtons[i][j].getTag().toString();
            }
        }

        if ( checkForLineWin(field) || checkForColumnWin(field) || checkForPrimaryDiagonalWin(field) || checkForSecondaryDiagonalWin(field)){
            return true;
        }
        return false;
    }

    private boolean checkForLineWin(String[][] field){
        for (int i = 0; i < boardLength;i++){
            if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals("")){
                return true;
            }
        }
        return false;
    }
    private boolean checkForColumnWin(String[][] field){
        for (int i = 0; i < 3;i++){
            if(field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals("")){
                return true;
            }
        }
        return false;
    }
    private boolean checkForPrimaryDiagonalWin(String[][] field) {
        if(field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals("")){
            return true;
        }
        return false;
    }
    private boolean checkForSecondaryDiagonalWin(String[][] field) {
        if(field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals("")){
            return true;
        }
        return false;
    }

    private void toastMessage(String message){ // show a toast message with the outcome of the round
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
        configureButtonResetBoard();
    }

    private void configureButtonResetBoard(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                imageButtons[i][j].setTag("");
                imageButtons[i][j].setBackgroundResource(R.drawable.fort_512px_white);
            }
        }
        moveCount = 0;
        playerOneTurn = true;
        setPlayerTurnIcon("PlayerTwo");
    }

    private void configureButtonResetScore(){ //Reset the score, works when the reset game is pressed
        setEndGameWindowVisibility(false);
        configureButtonResetBoard();
        roundCountPlayerOne = 0;
        roundCountPlayerTwo = 0;
        playerOnePoints = 0;
        playerTwoPoints = 0;
        updatePointsText();
    }

    private void updatePointsText(){ // Update textView with the present score
        String playerOneScore = "Player 1\n" + playerOnePoints, playerTwoScore = "Player 2\n" + playerTwoPoints;
        textViewPlayerOne.setText(playerOneScore);
        textViewPlayerTwo.setText(playerTwoScore);
    }

    private void setEndGameWindowVisibility(boolean value){
        RelativeLayout linearLayoutEndGamePopUp = findViewById(R.id.relativeLayoutEndGamePopUp);
        if(value){
            popUpVisible = true;
            linearLayoutEndGamePopUp.setVisibility(View.VISIBLE);
        } else {
            popUpVisible = false;
            linearLayoutEndGamePopUp.setVisibility(View.INVISIBLE);
        }
    }
}
