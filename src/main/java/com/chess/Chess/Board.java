package com.chess.Chess;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Board {
    Figure[][] position = new Figure[8][8];

    private boolean gameEnded = false;
    private String playerWhite = null;
    private String playerBlack = null;
    private String winInfo = "";
    private final ArrayList<String> votedForDraw = new ArrayList<>();
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

    // R̶e̶t̶u̶r̶n̶s̶ ̶t̶r̶u̶e̶ ̶i̶f̶ ̶a̶ ̶p̶l̶a̶y̶e̶r̶,̶ ̶w̶h̶o̶s̶e̶ ̶c̶o̶l̶o̶r̶ ̶i̶s̶ ̶t̶h̶e̶ ̶p̶a̶r̶a̶m̶e̶t̶e̶r̶,̶ ̶i̶s̶ ̶c̶h̶e̶c̶k̶m̶a̶t̶e̶d̶,̶ ̶f̶a̶l̶s̶e̶
    // i̶f̶ ̶s̶t̶a̶l̶e̶m̶a̶t̶e̶d̶.̶
    // S̶h̶o̶u̶l̶d̶ ̶o̶n̶l̶y̶ ̶b̶e̶ ̶c̶a̶l̶l̶e̶d̶ ̶i̶f̶ ̶(̶i̶s̶A̶n̶y̶M̶o̶v̶e̶P̶o̶s̶s̶i̶b̶l̶e̶ ̶=̶=̶ ̶f̶a̶l̶s̶e̶)̶
    // UPDATE: renamed isCheckmate(bool color) -> isKingInCheck(bool color), new is more clear about what method does;
    // Returns true, if the King piece of a player, whose color is the parameter is in check, false otherwise.
    // When called under the condition of (isAnyMovePossible == false), indicates checkmate if true,
    // stalemate if false.
    public boolean isKingInCheck(boolean color) {
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

    public void notate_promotion(int row, int column, int new_row, int new_column, String name, Figure figure){
        String moveNotation = "";
        moveNotation += "" + letters[column]+(row+1);
        if (figure != null) {
            moveNotation += "x";
        } else {
            moveNotation += "-";
        }
        moveNotation += "" + letters[new_column] + (new_row + 1);
        if(Objects.equals(name, "Knight")){
             moveNotation += 'N';
        } else{
            moveNotation += name.charAt(0);
        }
        if (isKingInCheck(playerWhiteTurn)) {
            if (!isAnyMovePossible(playerWhiteTurn)) { // Handling checkmate
                moveNotation += "#";
            } else { // Handling check
                moveNotation += "+";
            }
        }
        this.notation.add(moveNotation);
    }

    public boolean Move(String player, int figureRow, int figureColumn, int posRow, int posColumn) {
        if ((Objects.equals(player, this.playerWhite) && this.playerWhiteTurn
                && this.position[figureRow][figureColumn].is_white()) ||
                (Objects.equals(player, this.playerBlack) && !this.playerWhiteTurn
                        && !this.position[figureRow][figureColumn].is_white())) {
            try {
                Figure movedFigure = this.position[figureRow][figureColumn];
                Figure squareToBeOccupied = this.position[posRow][posColumn];
                movedFigure.move(this.position, posRow, posColumn);
                this.playerWhiteTurn = !this.playerWhiteTurn;
                this.reset_en_passant(this.playerWhiteTurn);

                String moveNotation = "";

                if(!(Objects.equals(movedFigure.get_name(), "Pawn") && (posRow == 0 || posRow == 7))){
                    // Handling castling
                    if (Objects.equals(movedFigure.get_name(), "King") && Math.abs(posColumn - figureColumn) == 2) {
                        if (posColumn > figureColumn) {
                            moveNotation += "O-O-O";
                        } else {
                            moveNotation += "O-O";
                        }
                    } else {

                        if (Objects.equals(movedFigure.get_name(), "Pawn")) {
                            moveNotation += "" + letters[figureColumn]+(figureRow+1);
                        } else if (Objects.equals(movedFigure.get_name(), "Knight")) {
                            moveNotation += "N"+letters[figureColumn]+(figureRow+1);
                        } else {
                            moveNotation += "" + movedFigure.get_name().charAt(0) + letters[figureColumn] +
                                    (figureRow+1);
                        }

                        if (squareToBeOccupied != null) {
                            moveNotation += "x";
                        } else if (Objects.equals(movedFigure.get_name(), "Pawn") && posColumn != figureColumn) {
                            // Handling en passant
                            moveNotation += "x";
                        } else {
                            moveNotation += "-";
                        }
                        moveNotation += "" + letters[posColumn] + (posRow + 1);
                    }
                    if (isKingInCheck(playerWhiteTurn)) {
                        if (!isAnyMovePossible(playerWhiteTurn)) { // Handling checkmate
                            moveNotation += "#";
                        } else { // Handling check
                            moveNotation += "+";
                        }
                    }
                    this.notation.add(moveNotation);
                }
                // Time Update
                return true;
            } catch (Exception e) {
                // Block of code to handle errors
            }
        }
        return false;
    }

    public void saveGameToFile(String fileName) {
        fileName = getUniqueFileName("games", fileName);
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName);

            writer.write("White player: " + this.playerWhite + '\n');
            writer.write("Black player: " + this.playerBlack + '\n');
            writer.write("----------------------\n");

            int i = 0;
            for (String move : this.notation) {
                if(i < 18){
                    move = ' ' + move;
                }
                if (i % 2 == 0) {
                    writer.write(Integer.toString(i / 2 + 1) + ") ");
                    while (move.length() < 11) {
                        move = move + ' ';
                    }
                } else {
                    move = move + '\n';
                }
                writer.write(move);
                i++;
            }
            if (i % 2 == 1){
                writer.write("\n");
            }
            writer.write("----------------------\n");
            writer.write(this.winInfo);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong when closing FileWriter", e);
            }
        }
    }

    private String getUniqueFileName(String folderName, String fileName) {
        createDirectoryIfNotExists(folderName);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timeStamp = dateFormat.format(new Date());
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1) {
            return folderName + File.separator + fileName.substring(0, dotIndex) +
                    "_" + timeStamp + fileName.substring(dotIndex);
        } else {
            return folderName + File.separator + fileName + "_" + timeStamp;
        }
    }

    private void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
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

    public String SetGameEnded(String type, String player) {
        if(type.equals("Draw")){
            if(votedForDraw.toArray().length == 0){
                votedForDraw.add(player);
                return "Opponent offers a draw";
            }else if(votedForDraw.toArray().length == 1 && !votedForDraw.get(0).equals(player)) {
                votedForDraw.add(player);
                this.gameEnded = true;
                this.winInfo = "Draw";
                saveGameToFile("game.txt");
                return "Draw";
            }
        } else {
            this.gameEnded = true;
            String color = "White";
            if(type.equals("NoAnyMovePossible")){
                if(this.playerWhiteTurn){
                    color = "Black";
                }
                if(this.isKingInCheck(this.playerWhiteTurn)) {
                    this.winInfo = "Winner: " + color + " Type: checkmated";
                }else {
                    this.winInfo = "Winner: " + color + " Type: stalemated";
                }
            }else if(type.equals("Resign")){
                if(player.equals(playerWhite)){
                    color = "Black";
                }
                this.winInfo = "Winner: " + color +" Type: Resign";
            }
            saveGameToFile("game.txt");
            return winInfo;
        }
        return "None";
    }

    public boolean getGameEnded() {
        return this.gameEnded;
    }
    public String getWinInfo() {
        return this.winInfo;
    }
}
