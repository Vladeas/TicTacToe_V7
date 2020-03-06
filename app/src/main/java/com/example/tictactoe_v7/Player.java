package com.example.tictactoe_v7;

public class Player {
    private String name, icon;
    private boolean playerTurn;
    private int moveCount, nrOfWins;

    public Player(){ /* no arg constr */}

    public Player(String name, String icon, boolean playerTurn){
        this.name = name;
        this.icon = icon;
        this.playerTurn = playerTurn;
        this.moveCount = 0;
        this.nrOfWins = 0;
    }

    public String getName() { return this.name; }

    public String getIcon(){
        return this.icon;
    }

    public boolean getPlayerTurn(){
        return this.playerTurn;
    }

    public int getMoveCount(){
        return this.moveCount;
    }

    public void incrementMoveCount() { this.moveCount++; }

    public int getNrOfWins(){
        return this.nrOfWins;
    }

    public void incrementNrOfWins() { this.nrOfWins++; }
}
