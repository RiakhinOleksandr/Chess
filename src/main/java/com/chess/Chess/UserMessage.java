package com.chess.Chess;

import java.util.ArrayList;

public class UserMessage {
    private String content;
    private String sender;

    private String type;
    private Figure[][] board;

    private ArrayList<int[]> posibleMoves;

    private String[] players = new String[2];

    private ArrayList<String> notation = new ArrayList<String>();

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public Figure[][] getBoard() {
        return board;
    }

    public String getType() {
        return type;
    }

    public ArrayList<int[]> getPosibleMoves() {
        return posibleMoves;
    }

    public String[] getPlayers(){return players;}

    public ArrayList<String> getNotation(){return notation;}

    public void setContent(String content) {
        this.content =  content;
    }

    public void setSender(String sender) {
        this.sender =  sender;
    }

    public void setBoard(Figure[][] board) {
        this.board =  board;
    }

    public void setType(String type) {
        this.type =  type;
    }

    public void setPosibleMoves(ArrayList<int[]> posibleMoves) {
        this.posibleMoves =  posibleMoves;
    }

    public void setPlayers(String[] players) {
        this.players =  players;
    }
    public void setNotation(ArrayList<String> notation) {
        this.notation =  notation;
    }
}
