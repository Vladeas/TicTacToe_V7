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

    private Player playerOne = new Player("Player 1","roman_helmet_512px_white");
    private Player playerTwo = new Player("Player 2","viking_helmet_512px_grey");
    private String standardIcons = "fort_512px_white";
    private int boardLength = 3;
    private Double gameType;
    private int moveCount;
    private boolean playerOneTurn = true;
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
        setPlayerScoreTextView();
        configureButtons();
    }

    private void getGameType(){ // Best of 3, of 5, or countless
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            try {
                gameType = Double.parseDouble(extras.getString("Extra_Game_Type")); //The key argument here must match the one used in the other activity
            } catch (NullPointerException e) {
                Log.i("Error", "NullPointerException");
            }
    }

    private void setPlayerScoreTextView(){ //save each player Object with its according TextView (for score)
        TextView textView = findViewById(R.id.textViewPlayerOne);
        playerOne.setTextViewScoreBoard(textView);
        textView = findViewById(R.id.textViewPlayerTwo);
        playerTwo.setTextViewScoreBoard(textView);
    }

    private void configureButtons(){ // use findViewById & setOnClickListener's for the buttons
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
    public void onClick(View v){ // call each button method when button is pressed
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
                        updateOccupiedPositions(v,playerOne,"x");
                        setPlayerTurnIcon(playerTwo);
                    } else {
                        updateOccupiedPositions(v,playerTwo,"o");
                        setPlayerTurnIcon(playerOne);
                    }
                    moveCount++;
                    playerMoveCheck();
                    break;
            }
        } else {// Buttons only accessible if the Game won window is visible
            if (v.getId() == R.id.buttonGoMainMenuEnd)
                finish();
            if(v.getId() == R.id.buttonPlayAgainEnd)
                configureButtonResetScore();
        }
    }

    private void updateOccupiedPositions(View v, Player player, String tag){ // If a position is chosen update its Icon and Tag
        int resID = getResources().getIdentifier(player.getIcon() , "drawable", getPackageName());// to see
        (v).setBackgroundResource(resID);
        (v).setTag(tag);
    }

    private void setPlayerTurnIcon(Player player){ // Show the Icon for which players turn it is
        ImageView imageViewPlayerTurn = findViewById(R.id.imageViewPlayerTurn);
        int resID = getResources().getIdentifier(player.getIcon() , "drawable", getPackageName());
        imageViewPlayerTurn.setBackgroundResource(resID);
    }

    private void displayToastMessage(String message){ Toast.makeText(this,message,Toast.LENGTH_SHORT).show(); }// Show a Toast


    private void playerMoveCheck(){// Check the move a player made & check for win
        if (checkForWin()) {
            if (playerOneTurn) {
                updatePlayerStats(playerOne);
                if(!checkMatchWinConditions(playerOne))
                    configureButtonResetBoard();
            } else {
                updatePlayerStats(playerTwo);
                if(!checkMatchWinConditions(playerTwo))
                    configureButtonResetBoard();
            }
        } else if (moveCount == 9) {
            displayToastMessage("The match was a Draw !");
            configureButtonResetBoard();
        } else {
            playerOneTurn = !playerOneTurn;// if the game does not end switch player turn
        }
        //configureButtonResetBoard();
        updatePointsText(playerOne);
        updatePointsText(playerTwo);
    }

    private void updatePlayerStats(Player player){ // Increment Nr of wins and the Nr of moves
        player.incrementNrOfWins();
        displayToastMessage(player.getName() + " Wins !");
    }


    private boolean checkMatchWinConditions(Player player){ //Match (contains more games) & make the end game window visible
        if(gameType != 0 && player.getNrOfWins() >= Math.ceil(gameType / 2.0)) { // Who won the most of "gameType" matches
            setEndMatchWindowVisibility(true);
            setMatchWinningPlayerName(player.getName());
            return true;
        }
        return false;
    }


    private void setMatchWinningPlayerName(String player){ // Update the end game TextView with the winning player
        TextView textViewEndGame = findViewById(R.id.textViewEndGame);
        String message = player + " won!";
        textViewEndGame.setText(message);
    }

    private void configureButtonResetBoard(){ // Reset the playing board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                imageButtons[i][j].setTag("");
                int resID = getResources().getIdentifier(standardIcons , "drawable", getPackageName());
                imageButtons[i][j].setBackgroundResource(resID);
            }
        }
        moveCount = 0;
        playerOneTurn = true;
        setPlayerTurnIcon(playerOne);
    }

    private void configureButtonResetScore(){ //Reset the score & the board, works when the reset game is pressed
        setEndMatchWindowVisibility(false);
        configureButtonResetBoard();
        playerOne.resetNrOfWins();
        playerTwo.resetNrOfWins();
        updatePointsText(playerOne);
        updatePointsText(playerTwo);
    }

    private void updatePointsText(Player player){ // Update textView with the present score
        String playerScore = player.getName() + "\n" + player.getNrOfWins();
        player.getTextViewScoreBoard().setText(playerScore);
    }

    private void setEndMatchWindowVisibility(boolean value){ // Make the end Match window visible or not
        RelativeLayout linearLayoutEndGamePopUp = findViewById(R.id.relativeLayoutEndGamePopUp);
        if(value){
            popUpVisible = true;
            linearLayoutEndGamePopUp.setVisibility(View.VISIBLE);
        } else {
            popUpVisible = false;
            linearLayoutEndGamePopUp.setVisibility(View.INVISIBLE);
        }
    }




    public boolean checkForWin(){ // check if there is an uninterrupted line of three identical symbols (excepting the standard one)
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
}
