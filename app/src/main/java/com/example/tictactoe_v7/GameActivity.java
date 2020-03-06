package com.example.tictactoe_v7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private Player playerOne = new Player("Player One","roman_helmet_512px_white", true);
    private Player playerTwo = new Player("Player Two","viking_helmet_512px_grey", false);
    private String standardIcons = "fort_512px_white";
    private int boardLength = 3;
    private Double gameType;
    private int moveCount, roundCountPlayerOne, roundCountPlayerTwo, playerOnePoints, playerTwoPoints;
    private boolean playerOneTurn = true;
    private TextView textViewPlayerOne;
    private TextView textViewPlayerTwo;
    private ImageButton[][] imageButtons = new ImageButton[boardLength][boardLength];
    private boolean popUpVisible = false;

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

        getGameType();
        configureTextViewsScore();
        configureButtons();
    }

    private void getGameType(){
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            try {
                gameType = Double.parseDouble(extras.getString("Extra_Game_Type")); //The key argument here must match the one used in the other activity
            } catch (NullPointerException e) {
                Log.i("Error", "NullPointerException");
            }
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
                        updateMoveOnGameActivity(v,playerOne,"x");
                        setPlayerTurnIcon(playerOne);
                    } else {
                        updateMoveOnGameActivity(v,playerTwo,"o");
                        setPlayerTurnIcon(playerTwo);
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

    private void updateMoveOnGameActivity(View v, Player player, String tag){
        int resID = getResources().getIdentifier(player.getIcon() , "drawable", getPackageName());// to see
        (v).setBackgroundResource(resID);
        (v).setTag(tag);
    }

    private void setPlayerTurnIcon(Player player){
        ImageView imageViewPlayerTurn = findViewById(R.id.imageViewPlayerTurn);
        int resID = getResources().getIdentifier(player.getIcon() , "drawable", getPackageName());
        imageViewPlayerTurn.setBackgroundResource(resID);
    }

    private void displayToastMessage(String message){ toastMessage(message);}


    private void winConditionsConfiguration(){
        if (checkForWin()) {
            if (playerOneTurn) {
                updatePlayerStats(playerOne);
            } else {
                updatePlayerStats(playerTwo);
            }
        } else if (moveCount == 9) {
            displayToastMessage("The match was a Draw !");
            configureButtonResetBoard();
        } else {
            playerOneTurn = !playerOneTurn;
        }
        checkEndMatchConditions(playerOne);
        checkEndMatchConditions(playerTwo);
        updatePointsText();
    }

    private void updatePlayerStats(Player player){
        player.incrementNrOfWins();
        player.incrementMoveCount();
        displayToastMessage(player.getName() + " Wins !");
    }


    private void checkEndMatchConditions(Player player){
        if(gameType != 0 && player.getNrOfWins() >= Math.ceil(gameType / 2.0)) {
            setEndGameWindowVisibility(true);
            playerEndGameWin(player.getName());
        }
    }


    private void playerEndGameWin(String player){
        TextView textViewEndGame = findViewById(R.id.textViewEndGame);
        String message = player + " won!";
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
    }

    private void configureButtonResetBoard(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                imageButtons[i][j].setTag("");
                int resID = getResources().getIdentifier(standardIcons , "drawable", getPackageName());
                imageButtons[i][j].setBackgroundResource(resID);
            }
        }
        moveCount = 0;
        playerOneTurn = true;
        setPlayerTurnIcon(playerTwo);
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
