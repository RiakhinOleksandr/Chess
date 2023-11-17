package com.chess.Chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;

public class RookTest {
    Figure[][] position;

    @BeforeEach
    public void setUp() {
        position = new Figure[8][8];
    }

    @Test
    public void move_horizontal() {
        position[2][3] = new Rook(2, 3, true, false);
        position[2][7] = new Rook(2, 7, false, false);
        position[2][3].move(position, 2, 7);
        Assertions.assertEquals(position[2][7].get_name(), "Rook");
        Assertions.assertEquals(position[2][7].is_white(), true);
        Assertions.assertEquals(position[2][3], null);
        position[2][7].move(position, 2, 1);
        Assertions.assertEquals(position[2][1].get_name(), "Rook");
        Assertions.assertEquals(position[2][7], null);
    }

    @Test
    public void move_vertical() {
        position[1][1] = new Rook(1, 1, true, false);
        position[7][1] = new Rook(7, 1, false, false);
        position[1][1].move(position, 5, 1);
        Assertions.assertEquals(position[5][1].get_name(), "Rook");
        Assertions.assertEquals(position[5][1].is_white(), true);
        Assertions.assertEquals(position[1][1], null);
        position[7][1].move(position, 5, 1);
        Assertions.assertEquals(position[5][1].get_name(), "Rook");
        Assertions.assertEquals(position[7][1], null);
    }

    @Test
    public void getting_possible_moves() {
        position[2][2] = new Rook(2, 2, true, false);
        position[2][4] = new Rook(2, 4, true, false);
        position[6][2] = new Rook(6, 2, false, false);
        ArrayList<int[]> possible_moves = position[2][2].get_possible_moves(position);
        int[][] expected = new int[][] { { 0, 2 }, { 1, 2 }, { 3, 2 }, { 4, 2 }, { 5, 2 }, { 6, 2 }, { 2, 0 }, { 2, 1 },
                { 2, 3 } };
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertArrayEquals(possible_moves.get(i), expected[i]);
        }
    }

    @Test
    public void getting_possible_moves2() {
        position[0][2] = new Rook(0, 2, true, false);
        position[0][3] = new Rook(0, 3, true, false);
        position[2][2] = new Rook(2, 2, true, false);
        position[2][4] = new Rook(2, 4, true, false);
        ArrayList<int[]> possible_moves = position[0][2].get_possible_moves(position);
        int[][] expected = new int[][] { { 1, 2 }, { 0, 0 }, { 0, 1 } };
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertArrayEquals(possible_moves.get(i), expected[i]);
        }
    }
}