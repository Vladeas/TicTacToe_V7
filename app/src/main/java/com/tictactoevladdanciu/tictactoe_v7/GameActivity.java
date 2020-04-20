package com.tictactoevladdanciu.tictactoe_v7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
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
    private String areYouCertainString = "Are you certain that you want to ", lastCommand;
    private int boardLength = 3, moveCount, delayTimeMS = 1500, freezeUI = 1;
    private Double gameType;
    private boolean playerOneTurn = true, freezeGameBoard = false,  playerOneWonLastTurn, switchOnOffTruth;
    private ImageButton[][] imageButtons = new ImageButton[boardLength][boardLength];

    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String SHARED_PREFS_MORE= "sharedPrefsMore";
    public static final String SWITCHSOUND = "switchsoundonoff";
    public static final String PLAYERONESCORE = "playerOneScore";
    public static final String PLAYERTWOSCORE = "playerTwoScore";
    public static final String PLAYERTURN = "playerTurn";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_game);

        getGameType();
        loadPlayerScoreData();//load the previous game score(gametype check in the method)
        loadSoundPreference();
        initializePlayerScoreTextView();
        configureButtons();
        updatePointsText(playerOne);
        updatePointsText(playerTwo);
        setPlayerTurnIcon();
    }


    // BIG FAT CENTRAL METHOD
    // use findViewById & setOnClickListener's for the buttons
    private void configureButtons(){
        Button buttonMainMenu = findViewById(R.id.buttonMainMenu);
        Button buttonResetGame = findViewById(R.id.buttonResetGame);
        Button buttonResetBoard = findViewById(R.id.buttonResetBoard);
        Button buttonMainMenuEnd = findViewById(R.id.buttonGoMainMenuEnd);
        Button buttonPlayAgainEnd = findViewById(R.id.buttonPlayAgainEnd);
        Button buttonYes = findViewById(R.id.buttonYes);
        Button buttonNo = findViewById(R.id.buttonNo);


        buttonMainMenu.setOnClickListener(this);
        buttonResetGame.setOnClickListener(this);
        buttonResetBoard.setOnClickListener(this);
        buttonMainMenuEnd.setOnClickListener(this);
        buttonPlayAgainEnd.setOnClickListener(this);
        buttonYes.setOnClickListener(this);
        buttonNo.setOnClickListener(this);

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
        switch(freezeUI){
            case 1:// Buttons only accessible if the overlay windows are not visible
                switch (v.getId()) {
                    case R.id.buttonMainMenu:
                        buttonSoundMethod();
                        configureButtonGoMainMenu();
                        break;
                    case R.id.buttonResetGame:
                        buttonSoundMethod();
                        configureButtonResetMatch();
                        break;
                    case R.id.buttonResetBoard:
                        buttonSoundMethod();
                        configureButtonResetBoard();
                        break;
                    default:
                        if (!(v).getTag().toString().equals(unoccupiedString)) {
                            break;
                        } else if(!freezeGameBoard) {
                            gameBoardSoundMethod();
                            setPlayerTurnIcon();
                            if (playerOneTurn) {
                                updateOccupiedPositions(v, playerOne, playerOneTag);

                            } else {
                                updateOccupiedPositions(v, playerTwo, playerTwoTag);
                            }
                            moveCount++;
                            checkGameState();
                        }
                        break;
                }
                break;
            case 2 :// Buttons only accessible if the Game won window is visible
                if (v.getId() == R.id.buttonGoMainMenuEnd) {
                    buttonSoundMethod();
                    finish();
                }
                if(v.getId() == R.id.buttonPlayAgainEnd) {
                    buttonSoundMethod();
                    resetMatch();
                }
                break;
            case 3: // Buttons only accessible if the Are You Certain window is visible
                if(v.getId() == R.id.buttonYes) { // continue with the intended action
                    buttonSoundMethod();
                    switch (lastCommand) {
                        case "resetBoard":
                            configureAreYouCertainView(false, "");
                            resetBoard();
                            break;
                        case "resetMatch":
                            configureAreYouCertainView(false, "");
                            resetMatch();
                            break;
                        case "goMainMenu":
                            configureAreYouCertainView(false, "");
                            finish();
                            break;
                        default:
                            break;
                    }
                }
                else if(v.getId() == R.id.buttonNo) { // do not continue
                    buttonSoundMethod();
                    configureAreYouCertainView(false, "");
                }
                break;
            default:
                break;
        }
    }

    // BIG FAT CENTRAL METHOD
    // Check how the move made by a player affects the game
    private void checkGameState(){
        if (checkForWin()) {// check if the new move won the game
            if (playerOneTurn) { // player One Won
                updatePlayerStats(playerOne);
                updatePointsText(playerOne);
                if(!checkMatchWinConditions(playerOne)) { // if the match is not over play a new game, if it is end the match
                    gameVictorySoundMethod();
                    playerOneWonLastTurn = true;
                    timedReset();
                }
                savePlayerScoreData();//save the score of the players after every win(game type check in the method)
            } else { // player Two Won
                updatePlayerStats(playerTwo);
                updatePointsText(playerTwo);
                savePlayerScoreData();//save the score of the players after every win(game type check in the method)
                if(!checkMatchWinConditions(playerTwo)) { // if the match is not over play a new game, if it is end the match
                    gameVictorySoundMethod();
                    playerOneWonLastTurn = false;
                    timedReset();
                }
                savePlayerScoreData();//save the score of the players after every win(game type check in the method)
            }
        } else if (moveCount == 9) { // The game is a draw
            gameDrawSoundMethod();
            displayToastMessage(drawMessage);
            timedReset();
        } else {
            playerOneTurn = !playerOneTurn;// if the game does not end switch player turn
        }
    }


    //Match (contains more games) & make the end game window visible
    private boolean checkMatchWinConditions(Player player){
        if(gameType != 0 && player.getNrOfWins() >= Math.ceil(gameType / 2.0)) { // Who won the most of "gameType" matches
            matchVictorySoundMethod();
            setEndMatchWindowVisibility(true);
            setMatchWinningPlayerName(player.getName());
            return true;
        }
        return false;
    }
    // Update the end game TextView with the winning player
    private void setMatchWinningPlayerName(String player){
        TextView textViewEndGame = findViewById(R.id.textViewEndGame);
        String message = player + "\nwon!";
        textViewEndGame.setText(message);
    }
    // Make the end Match window visible or not also use a delay
    private void setEndMatchWindowVisibility(boolean value){
        final RelativeLayout linearLayoutEndGamePopUp = findViewById(R.id.relativeLayoutEndGamePopUp);
        if(value){
            freezeUI = 2;
            new CountDownTimer(delayTimeMS, 1000) {
                public void onFinish() {
                    linearLayoutEndGamePopUp.setVisibility(View.VISIBLE);
                }

                public void onTick(long millisUntilFinished) {
                }
            }.start();
        } else {
            freezeUI = 1;
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
    private void setPlayerTurnIcon(){
        if(playerOneTurn) {
            ImageView imageViewPlayerTurn = findViewById(R.id.imageViewPlayerTurn);
            int resID = getResources().getIdentifier(playerOne.getIcon(), "drawable", getPackageName());
            imageViewPlayerTurn.setBackgroundResource(resID);
        }else{
            ImageView imageViewPlayerTurn = findViewById(R.id.imageViewPlayerTurn);
            int resID = getResources().getIdentifier(playerTwo.getIcon(), "drawable", getPackageName());
            imageViewPlayerTurn.setBackgroundResource(resID);
        }
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
                winningFormationShowcase(i,0, i,1, i,2);
                return true;
            }
        }
        return false;
    }
    private boolean checkForColumnWin(String[][] field){
        for (int i = 0; i < 3;i++){
            if(field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals(unoccupiedString)){
                winningFormationShowcase(0, i, 1, i, 2, i);
                return true;
            }
        }
        return false;
    }
    private boolean checkForPrimaryDiagonalWin(String[][] field) {
        if(field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals(unoccupiedString)){
            winningFormationShowcase(0, 0, 1, 1, 2, 2);
            return true;
        }
        return false;
    }
    private boolean checkForSecondaryDiagonalWin(String[][] field) {
        if(field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals(unoccupiedString)){
            winningFormationShowcase(0, 2, 1, 1, 2, 0);
            return true;
        }
        return false;
    }

    //the winning formation icons will turn to flames
    private void winningFormationShowcase(int lineOne, int columnOne, int lineTwo, int columnTwo, int lineThree, int columnThree){
        imageButtons[lineOne][columnOne].setBackgroundResource(R.drawable.flame_512px_red);
        imageButtons[lineTwo][columnTwo].setBackgroundResource(R.drawable.flame_512px_red);
        imageButtons[lineThree][columnThree].setBackgroundResource(R.drawable.flame_512px_red);
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

    // Play the sound effect for choosing a position, also check if the sound is enabled or not
    private void gameBoardSoundMethod(){
        loadSoundPreference();
        if(switchOnOffTruth) {
            MediaPlayer positionChoiceSound = MediaPlayer.create(this, R.raw.sword_scrape);
            positionChoiceSound.start();
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
    // Play the sound effect for winning a game, also check if the sound is enabled or not
    private void gameVictorySoundMethod(){
        loadSoundPreference();
        if(switchOnOffTruth) {
            MediaPlayer gameVictorySound = MediaPlayer.create(this, R.raw.victory_shout);
            gameVictorySound.start();
        }
    }
    // Play the sound effect for a draw, also check if the sound is enabled or not
    private void gameDrawSoundMethod(){
        loadSoundPreference();
        if(switchOnOffTruth) {
            MediaPlayer gameVictorySound = MediaPlayer.create(this, R.raw.draw);
            gameVictorySound.start();
        }
    }
    // Play the sound effect for winning a match, also check if the sound is enabled or not
    private void matchVictorySoundMethod(){
        loadSoundPreference();
        if(switchOnOffTruth) {
            MediaPlayer matchVictorySound = MediaPlayer.create(this, R.raw.victory_music);
            matchVictorySound.start();
        }
    }

    // Show a Toast
    private void displayToastMessage(String message){ Toast.makeText(this,message,Toast.LENGTH_SHORT).show(); }

    // Save the functions purpose in a LastCommand String & show the areYouCertain window
    private void configureButtonResetBoard(){
        configureAreYouCertainView(true, "reset the board ?");
        lastCommand = "resetBoard";
    }
    // Save the functions purpose in a LastCommand String & show the areYouCertain window
    private void configureButtonResetMatch(){
        configureAreYouCertainView(true, "reset the game ?");
        lastCommand = "resetMatch";
    }
    // Save the functions purpose in a LastCommand String & show the areYouCertain window
    private void configureButtonGoMainMenu(){
        configureAreYouCertainView(true, "go back to the menu ?");
        lastCommand = "goMainMenu";
    }

    // Call the resetBoard method after a delay
    private void timedReset(){
        freezeGameBoard = true;
        new CountDownTimer(delayTimeMS, 1000) {
            public void onFinish() {
                freezeGameBoard = false;
                resetBoard();
            }

            public void onTick(long millisUntilFinished) {
            }
        }.start();
    }
    // Reset the playing board
    private void resetBoard(){
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                imageButtons[i][j].setTag(unoccupiedString);
                int resID = getResources().getIdentifier(standardIcons , "drawable", getPackageName());
                imageButtons[i][j].setBackgroundResource(resID);
            }
        }
        moveCount = 0;
        if(playerOneWonLastTurn) {
            playerOneTurn = true;
            setPlayerTurnIcon();
        }
        else{
            playerOneTurn = false;
            setPlayerTurnIcon();
        }
    }
    //Reset the score & the board, works when the reset game is pressed
    private void resetMatch(){
        setEndMatchWindowVisibility(false);
        playerOneWonLastTurn = true;//player one is the first to start
        resetBoard();
        playerOne.resetNrOfWins();
        playerTwo.resetNrOfWins();
        updatePointsText(playerOne);
        updatePointsText(playerTwo);
        savePlayerScoreData();

    }

    //Ask if the user wants to continue with the action (when pressing buttons)
    private void configureAreYouCertainView(boolean value, String message){
        TextView textViewAreYouCertain = findViewById(R.id.textViewAreYouCertain);
        textViewAreYouCertain.setText(areYouCertainString + message);

        RelativeLayout linearLayoutAreYouCertainPopUp= findViewById(R.id.relativeLayoutAreYouCertain);
        if(value){
            freezeUI = 3;
            linearLayoutAreYouCertainPopUp.setVisibility(View.VISIBLE);
        } else {
            freezeUI = 1;
            linearLayoutAreYouCertainPopUp.setVisibility(View.INVISIBLE);
        }
    }

    //Use SharedPreferences to save the score for the player(only in just play mode)
    private void savePlayerScoreData(){
        if(gameType == 0) {
            int scorePlayerOne = playerOne.getNrOfWins();
            int scorePlayerTwo = playerTwo.getNrOfWins();
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_MORE, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putInt(PLAYERONESCORE, scorePlayerOne);
            editor.putInt(PLAYERTWOSCORE, scorePlayerTwo);
            editor.putBoolean(PLAYERTURN,playerOneTurn);
            editor.apply();
        }
    }

    // Get the saved preference for players score in the game (saved in the settings activity)
    private void loadPlayerScoreData() {
        if(gameType == 0) {
            int scorePlayerOne, scorePlayerTwo;
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_MORE, MODE_PRIVATE);
            scorePlayerOne = sharedPreferences.getInt(PLAYERONESCORE, 0);
            scorePlayerTwo = sharedPreferences.getInt(PLAYERTWOSCORE, 0);
            playerOneTurn = sharedPreferences.getBoolean(PLAYERTURN, true);
            playerOne.setNrOfWins(scorePlayerOne);
            playerTwo.setNrOfWins(scorePlayerTwo);
        }
    }
    // Get the saved preference for sound in the game (saved in the settings activity)
    private void loadSoundPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        switchOnOffTruth = sharedPreferences.getBoolean(SWITCHSOUND, true);
    }
}