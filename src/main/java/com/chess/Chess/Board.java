package com.chess.Chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Board {
    Figure[][] position = new Figure[8][8];

    private boolean gameEnded = false;
    private String playerWhite = null;
    private String playerBlack = null;
    private final ArrayList<String> notation = new ArrayList<>();
    boolean playerWhiteTurn = true;
    private final char[] letters = { 'h', 'g', 'f', 'e', 'd', 'c', 'b', 'a' };

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

    public Figure[] getFiguresOfColor(boolean color) {
        Figure[][] currPosition = this.position;
        Figure[] result = new Figure[16];
        int index = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Figure currFigure = currPosition[i][j];

                if (currFigure != null && currFigure.is_white() == color) {
                    result[index] = currFigure;
                    index++;
                }
            }
        }
        return Arrays.copyOf(result, index);
    }

    public boolean isAnyMovePossible(boolean color) {
        Figure[][] currPosition = this.position;
        Figure[] playerFigures = getFiguresOfColor(color);
        for (Figure piece : playerFigures) {
            if (!piece.get_possible_moves(currPosition).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Returns true if a player, whose color is the parameter is checkmated, false
    // if stalemated.
    // Should only be called if (isAnyMovePossible == false)
    public boolean isCheckmate(boolean color) {
        Figure[][] currPosition = this.position;
        Figure[] playerFigures = getFiguresOfColor(color);
        for (Figure piece : playerFigures) {
            if (Objects.equals(piece.get_name(), "King")) {
                King king = (King) piece;
                return king.check(currPosition);
            }
        }
        return false;
    }

    public void reset_en_passant(boolean colour) {
        for (int i = 3; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.position[i][j] != null) {
                    if (Objects.equals(this.position[i][j].get_name(), "Pawn")) {
                        if (colour == this.position[i][j].is_white()) {
                            ((Pawn) this.position[i][j]).set_en_passant(false);
                        }
                    }
                }
            }
        }
    }

    public boolean Move(String player, int figureRow, int figureColumn, int posRow, int posColumn) {
        if ((Objects.equals(player, this.playerWhite) && this.playerWhiteTurn
                && this.position[figureRow][figureColumn].is_white()) ||
                (Objects.equals(player, this.playerBlack) && !this.playerWhiteTurn
                        && !this.position[figureRow][figureColumn].is_white())) {
            try {
                this.position[figureRow][figureColumn].move(this.position, posRow, posColumn);
                this.playerWhiteTurn = !this.playerWhiteTurn;
                this.reset_en_passant(this.playerWhiteTurn);
                this.notation.add("" + this.position[posRow][posColumn].get_name().charAt(0) + letters[figureColumn]
                        + (figureRow + 1) + "-" + letters[posColumn] + (posRow + 1));
                // Time Update
                return true;
            } catch (Exception e) {
                // Block of code to handle errors
            }
        }
        return false;
    }

    public Figure[][] getFiguresOnBoard() {
        return this.position;
    }

    public void setPlayer(String player) {
        if (this.playerWhite == null) {
            this.playerWhite = player;
        } else if (this.playerBlack == null && !Objects.equals(player, this.playerWhite)) {
            this.playerBlack = player;
            // Time Start
        }
    }

    public ArrayList<String> getNotation() {
        return this.notation;
    }

    public String[] getPlayers() {
        return new String[] { this.playerWhite, this.playerBlack };
    }

    public void SetGameEnded() {
        this.gameEnded = true;
    }

    public boolean getGameEnded() {
        return this.gameEnded;
    }
}
