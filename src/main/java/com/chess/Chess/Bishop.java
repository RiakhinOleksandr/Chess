package com.chess.Chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Bishop extends Figure {
    public Bishop(int row, int column, boolean isWhite) {
        this.set_coordinates(row, column);
        this.set_is_white(isWhite);
        this.set_name("Bishop");
        this.set_not_moved(false);
    }

    private boolean checkDiagonalMove(Figure[][] currPosition, int newRow, int newCol) {
        int currRow = this.return_coordinates()[0];
        int currCol = this.return_coordinates()[1];
        boolean color = this.is_white();

        int rowDirection = Integer.compare(newRow, currRow); // Equals 1 when piece moves up, -1 when down, used as a
                                                             // multiplier
        int colDirection = Integer.compare(newCol, currCol); // Equals 1 when piece moves right, -1 when left, used as a
                                                             // multiplier

        for (int i = 1; i < Math.abs(newRow - currRow); i++) {
            if (currPosition[currRow + i * rowDirection][currCol + i * colDirection] != null) {
                return false;
            }
        }
        return currPosition[newRow][newCol] == null || color != currPosition[newRow][newCol].is_white();
    }

    @Override
    public boolean check_if_move_possible(Figure[][] currPosition, int newRow, int newCol) {
        int currRow = this.return_coordinates()[0];
        int currCol = this.return_coordinates()[1];

        if (newRow > 7 | newRow < 0 | newCol > 7 | newCol < 0) {
            return false;
        }

        if (Math.abs(newRow - currRow) != Math.abs(newCol - currCol)) { // Any but diagonal moves
            return false;
        }
        return checkDiagonalMove(currPosition, newRow, newCol);
    }

    @Override
    public boolean move_is_possible(Figure[][] curr_position, int new_row, int new_column) {
        int row = this.return_coordinates()[0];
        int column = this.return_coordinates()[1];
        boolean colour = this.is_white();
        if (this.check_if_move_possible(curr_position, new_row, new_column)) {
            Figure[][] position_after_move = new Figure[8][8];
            for (int i = 0; i < 8; i++) {
                position_after_move[i] = Arrays.copyOf(curr_position[i], 8);
            }
            position_after_move[new_row][new_column] = new Bishop(new_row, new_column, colour);
            position_after_move[row][column] = null;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (position_after_move[i][j] != null) {
                        if (Objects.equals(position_after_move[i][j].get_name(), "King")) {
                            if (colour == position_after_move[i][j].is_white()) {
                                return (!((King) position_after_move[i][j]).check(position_after_move));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void move(Figure[][] currPosition, int newRow, int newCol) {
        if (move_is_possible(currPosition, newRow, newCol)) {
            int currRow = this.return_coordinates()[0];
            int currCol = this.return_coordinates()[1];
            currPosition[newRow][newCol] = this;
            currPosition[currRow][currCol] = null;
            currPosition[newRow][newCol].set_coordinates(newRow, newCol);
        }
    }

    @Override
    public ArrayList<int[]> get_possible_moves(Figure[][] currPos) {
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.move_is_possible(currPos, i, j)) {
                    possibleMoves.add(new int[] { i, j });
                }
            }
        }
        return possibleMoves;
    }
}
