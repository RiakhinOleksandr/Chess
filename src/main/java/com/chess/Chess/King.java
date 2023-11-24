package com.chess.Chess;

import java.util.ArrayList;
import java.util.Arrays;

public class King extends Figure {

  public King(int row, int column, boolean is_white, boolean not_moved) {
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
    this.set_name("King");
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

      if (can_castle(curr_position, new_row, new_column)) {
        return true;
      }

      if (Math.abs((new_column - column) * (new_row - row)) == 1
          || Math.abs(new_column - column) + Math.abs(new_row - row) == 1) {
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

  public boolean check(Figure[][] curr_position) {
    int row = this.return_coordinates()[0];
    int column = this.return_coordinates()[1];
    boolean colour = this.is_white();
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        if (curr_position[i][j] != null) {
          if (curr_position[i][j].is_white() != colour) {
            if (curr_position[i][j].check_if_move_possible(curr_position, row, column)) {
              return true;
            }
          }
        }
      }
    }
    return false;
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
      if (can_castle(position_after_move, new_row, new_column)) {
        if (new_column == 1) { // short castling
          position_after_move[new_row][2] = new Rook(new_row, 2, colour, false);
          position_after_move[new_row][0] = null;
        } else { // long castling
          position_after_move[new_row][4] = new Rook(new_row, 4, colour, false);
          position_after_move[new_row][7] = null;
        }
      }
      position_after_move[new_row][new_column] = new King(new_row, new_column, colour, false);
      position_after_move[row][column] = null;
      return (!((King) position_after_move[new_row][new_column]).check(position_after_move));
    }
    return false;
  }

  public boolean can_castle(Figure[][] curr_position, int new_row, int new_column) {
    int row = this.return_coordinates()[0];

    if (!this.not_moved()) {
      return false;
    }

    // if king is in check return false

    if (new_row == row) {
      if (new_column == 1) { // short castling
        if (curr_position[row][0] != null && curr_position[row][0].not_moved()) {
          if (curr_position[row][2] == null && curr_position[row][1] == null) {
            // check if positions (row, 2) (row, 1) puts king in check
            return true;
          }

        }

      } else if (new_column == 5) { // long castling
        if (curr_position[row][7] != null && curr_position[row][7].not_moved()) {
          if (curr_position[row][4] == null && curr_position[row][5] == null && curr_position[row][6] == null) {
            // check if positions (row, 4) (row, 5) (row, 6) puts king in check
            return true;
          }
        }
      }
    }

    return false;
  }

  @Override
  public void move(Figure[][] curr_position, int new_row, int new_column) {
    if (this.move_is_possible(curr_position, new_row, new_column)) {

      if (can_castle(curr_position, new_row, new_column)) {
        if (new_column == 1) { // short castling
          // moving king
          curr_position[new_row][1] = this;
          curr_position[new_row][3] = null;
          curr_position[new_row][1].set_coordinates(new_row, 1);
          curr_position[new_row][1].set_not_moved(false);

          // moving rook
          curr_position[new_row][2] = curr_position[new_row][0];
          curr_position[new_row][0] = null;
          curr_position[new_row][2].set_coordinates(new_row, 2);
          curr_position[new_row][2].set_not_moved(false);
        } else { // long castling
          // moving king
          curr_position[new_row][5] = this;
          curr_position[new_row][3] = null;
          curr_position[new_row][5].set_coordinates(new_row, 5);
          curr_position[new_row][5].set_not_moved(false);

          // moving rook
          curr_position[new_row][4] = curr_position[new_row][7];
          curr_position[new_row][7] = null;
          curr_position[new_row][4].set_coordinates(new_row, 4);
          curr_position[new_row][4].set_not_moved(false);
        }

      } else {
        int row = this.return_coordinates()[0];
        int column = this.return_coordinates()[1];

        curr_position[new_row][new_column] = this;
        curr_position[row][column] = null;
        curr_position[new_row][new_column].set_coordinates(new_row, new_column);
        curr_position[new_row][new_column].set_not_moved(false);
      }
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

    for (int i = row - 1; i < row + 2; i++) {
      for (int j = column - 1; j < column + 2; j++) {
        if (this.move_is_possible(curr_position, i, j)) {
          possible_moves.add(new int[] { i, j });
        }
      }
    }

    if (row == 0 || row == 7) {
      if (this.can_castle(curr_position, row, 1)) {
        possible_moves.add(new int[] { row, 1 });
      }
      if (this.can_castle(curr_position, row, 5)) {
        possible_moves.add(new int[] { row, 5 });
      }
    }

    return possible_moves;
  }

}