package com.chess.Chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class BishopTest {
    Figure[][] position;

    @BeforeEach
    void setUp() {
        position = new Figure[8][8];
        position[0][2] = new Bishop(0, 2, true);
    }

    @Test
    void legalMoveTests() {
        position[0][2].move(position, 2, 4);
        Assertions.assertNull(position[0][2]);
        Assertions.assertEquals("Bishop", position[2][4].get_name());
        position[2][4].move(position, 5, 1);
        Assertions.assertNull(position[2][4]);
        Assertions.assertEquals("Bishop", position[5][1].get_name());
    }

    @Test
    void illegalMoveTest() {
        position[0][2].move(position, 2, 3);
        Assertions.assertEquals("Bishop", position[0][2].get_name());
    }

    @Test
    void takeEnemyPieceTest() {
        position[5][7] = new Bishop(5, 7, false);
        position[0][2].move(position, 5,7);
        Assertions.assertTrue(position[5][7].is_white());
        Assertions.assertNull(position[0][2]);
    }

    @Test
    void illegalStationaryMoveTest() {
        Assertions.assertFalse(position[0][2].check_if_move_possible(position, 0, 2));
    }

    @Test
    void illegalJumpOverAllyPieceTest() {
        position[2][4] = new Bishop(2, 4, true);
        Assertions.assertFalse(position[0][2].check_if_move_possible(position, 4, 6));
    }

    @Test
    void getPossibleMovesTest() {
        position[3][5] = new Bishop(3, 5, false);
        ArrayList<int[]> possibleMoves = position[0][2].get_possible_moves(position);
        int[][] expected = new int[][] { { 1, 1 }, { 1, 3 }, { 2, 0 }, { 2, 4 }, { 3, 5 } };
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertArrayEquals(expected[i], possibleMoves.get(i));
        }
    }
}
