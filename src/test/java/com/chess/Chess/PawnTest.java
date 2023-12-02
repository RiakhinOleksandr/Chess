package com.chess.Chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;

public class PawnTest {
    Figure[][] position;

    @BeforeEach
    public void setUp() {
        position = new Figure[8][8];
        position[0][0] = new King(0, 0, true, false);
        position[7][7] = new King(7, 7, false, false);
    }

    @Test
    public void move() {
        position[1][2] = new Pawn(1, 2, true, true);
        position[4][5] = new Pawn(4, 5, true, false);
        position[3][3] = new Pawn(3, 3, false, false);
        position[1][2].move(position, 3, 2);
        position[4][5].move(position, 7, 5);
        Assertions.assertEquals(position[3][2].get_name(), "Pawn");
        Assertions.assertEquals(position[3][2].is_white(), true);
        Assertions.assertEquals(position[3][2].not_moved(), false);
        Assertions.assertEquals(position[4][5].get_name(), "Pawn");
    }

    @Test
    public void capture() {
        position[1][2] = new Pawn(1, 2, true, true);
        position[2][2] = new Pawn(2, 2, true, false);
        position[3][3] = new Pawn(3, 3, false, false);
        position[2][2].move(position, 3, 3);
        Assertions.assertEquals(position[3][3].get_name(), "Pawn");
        Assertions.assertEquals(position[2][2], null);
    }

    @Test
    public void en_passant() {
        position[1][2] = new Pawn(1, 2, true, true);
        position[1][1] = new Pawn(1, 1, true, true);
        position[3][3] = new Pawn(3, 3, false, false);
        position[1][2].move(position, 3, 2);
        position[3][3].move(position, 2, 2);
        Assertions.assertEquals(position[2][2].get_name(), "Pawn");
        Assertions.assertEquals(position[3][2], null);
        Assertions.assertEquals(position[3][3], null);
        Assertions.assertEquals(position[2][2].is_white(), false);
    }

    @Test
    public void check_promotion(){
        position[6][3] = new Pawn(6,3,true,false);
        position[6][2] = new Pawn(6,2,true,false);
        position[1][0] = new Pawn(1,0,false,false);
        ((Pawn)position[6][3]).promote(position, 7, 3, "Bishop");
        ((Pawn)position[6][2]).promote(position, 7, 2, "Queen");
        ((Pawn)position[1][0]).promote(position, 0, 0, "Knight");
        Assertions.assertEquals(position[7][2].get_name(), "Queen");
        Assertions.assertEquals(position[7][3].get_name(), "Bishop");
        Assertions.assertEquals(position[0][0].get_name(), "Knight");
    }

    @Test
    public void check_move_possible() {
        position[1][1] = new Pawn(1, 1, true, true);
        position[1][2] = new Pawn(1, 2, true, true);
        position[5][5] = new Bishop(5, 5, false);
        Assertions.assertFalse(position[1][1].move_is_possible(position, 3, 1));
        position[1][2].move(position, 2, 2);
        Assertions.assertTrue(position[1][1].move_is_possible(position, 3, 1));
    }

    @Test
    public void check_illegal_moves(){
        position[3][3] = new Pawn(3,3,true,false);
        position[5][1] = new Pawn(5,1,false,false);
        position[4][1] = new Pawn(4,1,true,false);
        position[3][3].move(position, 5, 3);
        position[5][1].move(position, 4, 2);
        position[5][1].move(position, 4, 1);
        Assertions.assertNull(position[4][2]);
        Assertions.assertNull(position[5][3]);
        Assertions.assertEquals(position[5][1].get_name(), "Pawn");
        Assertions.assertEquals(position[4][1].is_white(), true);
    }

    @Test
    public void check_for_possible_moves() {
        position[1][2] = new Pawn(1, 2, true, true);
        position[2][4] = new Pawn(1, 1, true, false);
        position[3][3] = new Pawn(3, 3, false, false);
        ArrayList<int[]> possible_moves = position[3][3].get_possible_moves(position);
        int[][] expected = { { 2, 3 }, { 2, 4 } };
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertArrayEquals(possible_moves.get(i), expected[i]);
        }
        position[1][2].move(position, 3, 2);
        possible_moves = position[3][3].get_possible_moves(position);
        int[][] expected1 = { { 2, 3 }, { 2, 4 }, { 2, 2 } };
        for (int i = 0; i < expected1.length; i++) {
            Assertions.assertArrayEquals(possible_moves.get(i), expected1[i]);
        }
    }
}
