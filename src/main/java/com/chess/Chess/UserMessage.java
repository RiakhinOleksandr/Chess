package com.chess.Chess;

import java.util.ArrayList;

public class UserMessage {
    private String content;
    private String sender;

    private String type;
    private Figure[][] board;

    private ArrayList<int[]> posibleMoves;

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

    public ArrayList<int[]> getposibleMoves() {
        return posibleMoves;
    }

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
}
