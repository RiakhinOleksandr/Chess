package com.chess.Chess;

import java.util.ArrayList;

public class Rook extends Figure {
    public Rook(int row, int column, boolean is_white, boolean not_moved) {
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
        this.set_name("Rook");
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
            if (row == new_row) {
                if (column == new_column) {
                    throw new ImpossibleMove();
                } else {
                    if (new_column > column) { // If rook moves right
                        for (int i = column + 1; i < new_column; i++) {
                            if (curr_position[row][i] == null) {
                                curr_position[row][i] = curr_position[row][i - 1];
                                curr_position[row][i - 1] = null;
                            } else {
                                if (i != column + 1) {
                                    curr_position[row][column] = curr_position[row][i - 1];
                                    curr_position[row][i - 1] = null;
                                }
                                throw new ImpossibleMove();
                            }
                        }
                        if (curr_position[row][new_column] != null) {
                            if (colour != curr_position[row][new_column].is_white()) {
                                curr_position[row][column] = curr_position[row][new_column - 1];
                                curr_position[row][new_column - 1] = null;
                            } else {
                                if (new_column - 1 != column) {
                                    curr_position[row][column] = curr_position[row][new_column - 1];
                                    curr_position[row][new_column - 1] = null;
                                }
                                throw new ImpossibleMove();
                            }
                        } else {
                            curr_position[row][column] = curr_position[row][new_column - 1];
                            curr_position[row][new_column - 1] = null;
                        }
                    } else { // If rook moves left
                        for (int i = column - 1; i > new_column; i--) {
                            if (curr_position[row][i] == null) {
                                curr_position[row][i] = curr_position[row][i + 1];
                                curr_position[row][i + 1] = null;
                            } else {
                                if (i != column - 1) {
                                    curr_position[row][column] = curr_position[row][i + 1];
                                    curr_position[row][i + 1] = null;
                                }
                                throw new ImpossibleMove();
                            }
                        }
                        if (curr_position[row][new_column] != null) {
                            if (colour != curr_position[row][new_column].is_white()) {
                                curr_position[row][column] = curr_position[row][new_column + 1];
                                curr_position[row][new_column + 1] = null;
                            } else {
                                if (new_column + 1 != column) {
                                    curr_position[row][column] = curr_position[row][new_column + 1];
                                    curr_position[row][new_column + 1] = null;
                                }
                                throw new ImpossibleMove();
                            }
                        } else {
                            curr_position[row][column] = curr_position[row][new_column + 1];
                            curr_position[row][new_column + 1] = null;
                        }
                    }
                }
            } else if (column == new_column) {
                if (row == new_row) {
                    throw new ImpossibleMove();
                } else {
                    if (new_row > row) { // If rook moves down
                        for (int i = row + 1; i < new_row; i++) {
                            if (curr_position[i][column] == null) {
                                curr_position[i][column] = curr_position[i - 1][column];
                                curr_position[i - 1][column] = null;
                            } else {
                                if (i != row + 1) {
                                    curr_position[row][column] = curr_position[i - 1][column];
                                    curr_position[i - 1][column] = null;
                                }
                                throw new ImpossibleMove();
                            }
                        }
                        if (curr_position[new_row][new_column] != null) {
                            if (colour != curr_position[new_row][new_column].is_white()) {
                                curr_position[row][new_column] = curr_position[new_row - 1][new_column];
                                curr_position[new_row - 1][new_column] = null;
                            } else {
                                if (new_row - 1 != row) {
                                    curr_position[row][column] = curr_position[new_row - 1][new_column];
                                    curr_position[new_row - 1][new_column] = null;
                                }
                                throw new ImpossibleMove();
                            }
                        } else {
                            curr_position[row][new_column] = curr_position[new_row - 1][new_column];
                            curr_position[new_row - 1][new_column] = null;
                        }
                    } else { // If rook moves up
                        for (int i = row - 1; i > new_row; i--) {
                            if (curr_position[i][column] == null) {
                                curr_position[i][column] = curr_position[i + 1][column];
                                curr_position[i + 1][column] = null;
                            } else {
                                if (i != row - 1) {
                                    curr_position[row][column] = curr_position[i + 1][column];
                                    curr_position[i + 1][column] = null;
                                }
                                throw new ImpossibleMove();
                            }
                        }
                        if (curr_position[new_row][new_column] != null) {
                            if (colour != curr_position[new_row][new_column].is_white()) {
                                curr_position[row][new_column] = curr_position[new_row + 1][new_column];
                                curr_position[new_row + 1][new_column] = null;
                            } else {
                                if (new_row + 1 != row) {
                                    curr_position[row][column] = curr_position[new_row + 1][new_column];
                                    curr_position[new_row + 1][new_column] = null;
                                }
                                throw new ImpossibleMove();
                            }
                        } else {
                            curr_position[row][new_column] = curr_position[new_row + 1][new_column];
                            curr_position[new_row + 1][new_column] = null;
                        }
                    }
                }
            } else {
                throw new ImpossibleMove();
            }
            return true;
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
        for (int i = 0; i < 8; i++) {
            if (this.check_if_move_possible(curr_position, i, column)) {
                possible_moves.add(new int[] { i, column });
            }
        }
        for (int i = 0; i < 8; i++) {
            if (this.check_if_move_possible(curr_position, row, i)) {
                possible_moves.add(new int[] { row, i });
            }
        }
        return possible_moves;
    }
}