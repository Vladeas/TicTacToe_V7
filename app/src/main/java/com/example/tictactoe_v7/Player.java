package com.example.tictactoe_v7;

public class Player {
    private String icon;
    private boolean playerTurn;
    private int moveCount, nrOfWins;

    public Player(){ /* no arg constr */}

    public Player(String icon, boolean playerTurn){
        this.icon = icon;
        this.playerTurn = playerTurn;
        this.moveCount = 0;
        this.nrOfWins = 0;
    }

    public String getIcon(){
        return this.icon;
    }

    public boolean getPlayerTurn(){
        return this.playerTurn;
    }

    public int getMoveCount(){
        return this.moveCount;
    }

    public int getNrOfWins(){
        return this.nrOfWins;
    }
}
