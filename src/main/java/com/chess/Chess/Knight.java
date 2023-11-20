package com.chess.Chess;

import java.util.ArrayList;

public class Knight extends Figure {
  public Knight(int row, int column, boolean is_white, boolean not_moved) {
    try {
      if (row >= 0 & row < 8 & column >= 0 & column < 8) {
        this.set_coordinates(row, column);
      } else {
        throw new IncorrectPositionOfFigure();
      }
    } catch (IncorrectPositionOfFigure e) {
      System.out.println(e.getMessage());
    }

    this.set_is_white(is_white);
    this.set_name("Knight");
    this.set_not_moved(not_moved);
  }

  @Override
  public boolean check_if_move_possible(Figure[][] curr_position, int new_row, int new_column) {
    int row = this.return_coordinates()[0];
    int column = this.return_coordinates()[1];
    boolean colour = this.is_white();

    try {
      if (new_row > 7 | new_row < 0 | new_column > 7 | new_column < 0) {
        throw new ImpossibleMove();
      }

      if (Math.abs(new_column - column) * Math.abs(new_row - row) == 2) {
        if (curr_position[new_row][new_column] != null) {
          if (colour == curr_position[new_row][new_column].is_white()) {
            throw new ImpossibleMove();
          }
        }
        return true;
      } else {
        throw new ImpossibleMove();
      }
    } catch (ImpossibleMove e) {
      return false;
    }
  }

  @Override
  public void move(Figure[][] curr_position, int new_row, int new_column) {
    if (this.check_if_move_possible(curr_position, new_row, new_column)) {
      int row = this.return_coordinates()[0];
      int column = this.return_coordinates()[1];
      curr_position[new_row][new_column] = this;
      curr_position[row][column] = null;
      curr_position[new_row][new_column].set_coordinates(new_row, new_column);
      curr_position[new_row][new_column].set_not_moved(false);
    } else {
      try {
        throw new ImpossibleMove();
      } catch (ImpossibleMove e) {
      }
    }
  }

  @Override
  public ArrayList<int[]> get_possible_moves(Figure[][] curr_position) {
    ArrayList<int[]> possible_moves = new ArrayList<>();
    int row = this.return_coordinates()[0];
    int column = this.return_coordinates()[1];

    int[][] positions = { { row - 1, column - 2 }, { row - 2, column - 1 }, { row - 2, column + 1 },
        { row - 1, column + 2 }, { row + 1, column - 2 }, { row + 2, column - 1 }, { row + 2, column + 1 },
        { row + 1, column + 2 } };

    for (int i = 0; i < positions.length; i++) {
      if (this.check_if_move_possible(curr_position, positions[i][0],
          positions[i][1])) {
        possible_moves.add(new int[] { positions[i][0], positions[i][1] });
      }
    }

    return possible_moves;
  }
}