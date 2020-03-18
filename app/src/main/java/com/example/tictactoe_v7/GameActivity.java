package com.example.tictactoe_v7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private Player playerOne = new Player("Player 1","roman_helmet_512px_white");
    private Player playerTwo = new Player("Player 2","viking_helmet_512px_grey");
    private String standardIcons = "fort_512px_white", unoccupiedString = "unoccupied", playerOneTag = "playerOneHere", playerTwoTag = "playerTwoHere", drawMessage = "The match was a Draw !";
    private int boardLength = 3, moveCount;
    private Double gameType;
    private boolean playerOneTurn = true, freezeUI = false, freezeGameBoard = false;
    private ImageButton[][] imageButtons = new ImageButton[boardLength][boardLength];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);

        getGameType();
        initializePlayerScoreTextView();
        configureButtons();
    }


    // BIG FAT CENTRAL METHOD
    // use findViewById & setOnClickListener's for the buttons
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

    // BIG FAT CENTRAL METHOD
    // call each button method when button is pressed
    @Override
    public void onClick(View v){
        if(!freezeUI) { // Buttons only accessible if the Game won window is not visible
            switch (v.getId()) {
                case R.id.buttonMainMenu:
                    finish();
                    break;
                case R.id.buttonResetGame:
                    configureButtonResetMatch();
                    break;
                case R.id.buttonResetBoard:
                    configureButtonResetBoard();
                    break;
                default:
                    if (!(v).getTag().toString().equals(unoccupiedString)) {
                        break;
                    } else if(!freezeGameBoard) {
                        if (playerOneTurn) {
                            updateOccupiedPositions(v, playerOne, playerOneTag);
                            setPlayerTurnIcon(playerTwo);
                        } else {
                            updateOccupiedPositions(v, playerTwo, playerTwoTag);
                            setPlayerTurnIcon(playerOne);
                        }
                        moveCount++;
                        checkGameState();
                    }
                    break;
            }
        } else {// Buttons only accessible if the Game won window is visible
            if (v.getId() == R.id.buttonGoMainMenuEnd)
                finish();
            if(v.getId() == R.id.buttonPlayAgainEnd)
                configureButtonResetMatch();
        }
    }

    // BIG FAT CENTRAL METHOD
    // Check how the move made by a player affects the game
    private void checkGameState(){
        if (checkForWin()) {
            if (playerOneTurn) {
                updatePlayerStats(playerOne);
                updatePointsText(playerOne);
                if(!checkMatchWinConditions(playerOne))
                    timedReset();
            } else {
                updatePlayerStats(playerTwo);
                updatePointsText(playerTwo);
                if(!checkMatchWinConditions(playerTwo))
                    timedReset();
            }
        } else if (moveCount == 9) {
            displayToastMessage(drawMessage);
            timedReset();
        } else {
            playerOneTurn = !playerOneTurn;// if the game does not end switch player turn
        }
    }


    //Match (contains more games) & make the end game window visible
    private boolean checkMatchWinConditions(Player player){
        if(gameType != 0 && player.getNrOfWins() >= Math.ceil(gameType / 2.0)) { // Who won the most of "gameType" matches
            setEndMatchWindowVisibility(true);
            setMatchWinningPlayerName(player.getName());
            return true;
        }
        return false;
    }
    // Update the end game TextView with the winning player
    private void setMatchWinningPlayerName(String player){
        TextView textViewEndGame = findViewById(R.id.textViewEndGame);
        String message = player + " won!";
        textViewEndGame.setText(message);
    }
    // Make the end Match window visible or not
    private void setEndMatchWindowVisibility(boolean value){
        RelativeLayout linearLayoutEndGamePopUp = findViewById(R.id.relativeLayoutEndGamePopUp);
        if(value){
            freezeUI = true;
            linearLayoutEndGamePopUp.setVisibility(View.VISIBLE);
        } else {
            freezeUI = false;
            linearLayoutEndGamePopUp.setVisibility(View.INVISIBLE);
        }
    }


    // If a position is chosen update its Icon and Tag
    private void updateOccupiedPositions(View v, Player player, String tag){
        int resID = getResources().getIdentifier(player.getIcon() , "drawable", getPackageName());// to see
        (v).setBackgroundResource(resID);
        (v).setTag(tag);
    }
    //save each player Object with its according TextView (for score)
    private void initializePlayerScoreTextView(){
        TextView textView = findViewById(R.id.textViewPlayerOne);
        playerOne.setTextViewScoreBoard(textView);
        textView = findViewById(R.id.textViewPlayerTwo);
        playerTwo.setTextViewScoreBoard(textView);
    }
    // Increment Nr of wins and the Nr of moves
    private void updatePlayerStats(Player player){
        player.incrementNrOfWins();
        displayToastMessage(player.getName() + " Wins !");
    }
    // Update textView with the present score
    private void updatePointsText(Player player){
        String playerScore = player.getName() + "\n" + player.getNrOfWins();
        player.getTextViewScoreBoard().setText(playerScore);
    }
    // Show the Icon for which players turn it is
    private void setPlayerTurnIcon(Player player){
        ImageView imageViewPlayerTurn = findViewById(R.id.imageViewPlayerTurn);
        int resID = getResources().getIdentifier(player.getIcon() , "drawable", getPackageName());
        imageViewPlayerTurn.setBackgroundResource(resID);
    }


    // BIG FAT CENTRAL METHOD
    // check if there is an uninterrupted line of three identical symbols (excepting the standard one), use appropriate methods
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
            if(field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals(unoccupiedString)){
                return true;
            }
        }
        return false;
    }
    private boolean checkForColumnWin(String[][] field){
        for (int i = 0; i < 3;i++){
            if(field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals(unoccupiedString)){
                return true;
            }
        }
        return false;
    }
    private boolean checkForPrimaryDiagonalWin(String[][] field) {
        if(field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals(unoccupiedString)){
            return true;
        }
        return false;
    }
    private boolean checkForSecondaryDiagonalWin(String[][] field) {
        if(field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals(unoccupiedString)){
            return true;
        }
        return false;
    }


    // Best of 3, of 5, or countless
    private void getGameType(){
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            try {
                gameType = Double.parseDouble(extras.getString("Extra_Game_Type")); //The key argument here must match the one used in the other activity
            } catch (NullPointerException e) {
                Log.i("Error", "NullPointerException");
            }
    }

    // Show a Toast
    private void displayToastMessage(String message){ Toast.makeText(this,message,Toast.LENGTH_SHORT).show(); }


    // Call the resetBoard method after a delay
    private void timedReset(){
        freezeGameBoard = true;
        new CountDownTimer(3000, 1000) {
            public void onFinish() {
                freezeGameBoard = false;
                configureButtonResetBoard();
            }

            public void onTick(long millisUntilFinished) {
            }
        }.start();
    }
    // Reset the playing board
    private void configureButtonResetBoard(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                imageButtons[i][j].setTag(unoccupiedString);
                int resID = getResources().getIdentifier(standardIcons , "drawable", getPackageName());
                imageButtons[i][j].setBackgroundResource(resID);
            }
        }
        moveCount = 0;
        playerOneTurn = true;
        setPlayerTurnIcon(playerOne);
    }
    //Reset the score & the board, works when the reset game is pressed
    private void configureButtonResetMatch(){
        setEndMatchWindowVisibility(false);
        configureButtonResetBoard();
        playerOne.resetNrOfWins();
        playerTwo.resetNrOfWins();
        updatePointsText(playerOne);
        updatePointsText(playerTwo);
    }
}