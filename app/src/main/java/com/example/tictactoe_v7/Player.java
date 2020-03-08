package com.example.tictactoe_v7;

import android.widget.TextView;

public class Player {
    private String name, icon;
    private int nrOfWins;
    private TextView textViewScoreBoard;

    public Player(){ /* no arg constructor */}

    public Player(String name, String icon){
        this.name = name;
        this.icon = icon;
        this.nrOfWins = 0;
    }

    public TextView getTextViewScoreBoard() {return this.textViewScoreBoard; }

    public void setTextViewScoreBoard(TextView textViewScoreBoard) {this.textViewScoreBoard = textViewScoreBoard; }

    public String getName() { return this.name; }

    public String getIcon(){
        return this.icon;
    }

    public int getNrOfWins(){
        return this.nrOfWins;
    }

    public void incrementNrOfWins() { this.nrOfWins++; }

    public void resetNrOfWins() { this.nrOfWins = 0; }
}
