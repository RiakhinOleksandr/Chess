package com.chess.Chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class QueenTest {
    Figure[][] position;

    @BeforeEach
    void setUp() {
        position = new Figure[8][8];
        position[3][4] = new Queen(3, 4, true);
    }

    @Test
    void legalMovesTest() {
        position[3][4].move(position, 3, 1);
        Assertions.assertEquals("Queen", position[3][1].get_name());
        Assertions.assertNull(position[3][4]);
        position[3][1].move(position, 3, 5);
        Assertions.assertEquals("Queen", position[3][5].get_name());
        Assertions.assertNull(position[3][1]);
        position[3][5].move(position, 7, 1);
        Assertions.assertEquals("Queen", position[7][1].get_name());
        Assertions.assertNull(position[3][5]);
    }

    @Test
    void illegalMoveTest() {
        position[3][4].move(position, 5, 3);
        Assertions.assertNull(position[5][3]);
        Assertions.assertEquals("Queen", position[3][4].get_name());
    }

    @Test
    void takeEnemyPieceTest() {
        position[3][2] = new Queen(3, 2, false);
        position[3][4].move(position, 3, 2);
        Assertions.assertTrue(position[3][2].is_white());
    }

    @Test
    void illegalStationaryMoveTest() {
        Assertions.assertFalse(position[3][4].check_if_move_possible(position, 3, 4));
    }

    @Test
    void jumpOverPieceTest() {
        position[3][2] = new Queen(3, 2, false);
        position[3][4].move(position, 3, 1);
        Assertions.assertNull(position[3][1]);
        Assertions.assertEquals("Queen", position[3][4].get_name());
        Assertions.assertFalse(position[3][2].is_white());
    }

    @Test
    void outOfBoardMoveTest() {
        position[3][4].move(position, 3, 8);
        Assertions.assertEquals("Queen", position[3][4].get_name());
    }

    @Test
    void getPossibleMovesTest() {
        position[3][2] = new Queen(3, 2, false);
        position[6][1] = new Bishop(6, 1, true);
        position[2][3] = new Bishop(2, 3, false);
        ArrayList<int[]> possibleMoves = position[3][4].get_possible_moves(position);
        int[][] expected = new int[][] { { 0, 4 }, { 0, 7 }, { 1, 4 }, { 1, 6 }, { 2, 3 }, { 2, 4 }, { 2, 5 }, { 3, 2 },
                { 3, 3 }, { 3, 5 }, { 3, 6 }, { 3, 7 }, { 4, 3 }, { 4, 4 }, { 4, 5 }, { 5, 2 }, { 5, 4 }, { 5, 6 },
                { 6, 4 }, { 6, 7 }, { 7, 4 } };
        Assertions.assertEquals(expected.length, possibleMoves.size());
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertArrayEquals(expected[i], possibleMoves.get(i));
        }
    }
}
