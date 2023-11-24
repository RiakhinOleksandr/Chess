package com.chess.Chess;

import java.util.ArrayList;

public class Figure {
    private int row, column;
    private boolean is_white, not_moved;
    private String name;

    public int[] return_coordinates() {
        return new int[] { this.row, this.column };
    }

    public void set_coordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public String get_name() {
        return this.name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public void set_not_moved(boolean not_moved) {
        this.not_moved = not_moved;
    }

    public boolean not_moved() {
        return this.not_moved;
    }

    public boolean is_white() {
        return this.is_white;
    }

    public void set_is_white(boolean is_white) {
        this.is_white = is_white;
    }

    public boolean check_if_move_possible(Figure[][] curr_position, int new_row, int new_column) {
        return false;
    }

    public boolean move_is_possible(Figure[][] curr_position, int new_row, int new_column) {
        return false;
    }

    public void move(Figure[][] curr_position, int new_row, int new_column) {
    }

    public ArrayList<int[]> get_possible_moves(Figure[][] curr_position) {
        return new ArrayList<int[]>();
    }
}

class ImpossibleMove extends Exception {
    public ImpossibleMove() {
        super("You can`t make this move");
    }
}

class IncorrectPositionOfFigure extends Exception {
    public IncorrectPositionOfFigure() {
        super("You can`t place figure here");
    }
}