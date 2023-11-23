package com.chess.Chess;

import java.util.ArrayList;

public class Board {
    Figure[][] position = new Figure[8][8];

    String player1;
    String player2;

    public Board() {
        this.position[0][0] = new Rook(0, 0, true, true);
        this.position[0][1] = new Knight(0, 1, true, false);
        this.position[0][2] = new Bishop(0, 2, true);
        this.position[0][4] = new Queen(0, 4, true);
        this.position[0][3] = new King(0, 3, true, true);
        this.position[0][5] = new Bishop(0, 5, true);
        this.position[0][6] = new Knight(0, 6, true, false);
        this.position[0][7] = new Rook(0, 7, true, true);
        for (int i = 0; i < 8; i++) {
            this.position[1][i] = new Pawn(1, i, true, true);
        }

        this.position[7][0] = new Rook(7, 0, false, true);
        this.position[7][1] = new Knight(7, 1, false, false);
        this.position[7][2] = new Bishop(7, 2, false);
        this.position[7][4] = new Queen(7, 4, false);
        this.position[7][3] = new King(7, 3, false, true);
        this.position[7][5] = new Bishop(7, 5, false);
        this.position[7][6] = new Knight(7, 6, false, false);
        this.position[7][7] = new Rook(7, 7, false, true);
        for (int i = 0; i < 8; i++) {
            this.position[6][i] = new Pawn(6, i, false, true);
        }
    }

    public Figure getFigure(int row, int column) {
        return position[row][column];
    }

    public void Move(int figureRow, int figureColumn, int posRow, int posColumn) {
        try {
            position[figureRow][figureColumn].move(position, posRow, posColumn);
        } catch (Exception e) {
            // Block of code to handle errors
        }
    }

    public Figure[][] getFiguresOnBoard() {
        return position;
    }

}
