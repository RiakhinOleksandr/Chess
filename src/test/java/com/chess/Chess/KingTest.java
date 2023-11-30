package com.chess.Chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;

public class KingTest {

  Figure[][] position;

  @BeforeEach
  public void setUp() {
    position = new Figure[8][8];
  }

  @Test
  public void check_if_move_possible() {
    boolean actual;
    position[4][3] = new King(4, 3, true, false);
    position[3][4] = new King(3, 4, true, false);
    position[4][4] = new Knight(4, 4, false, false);

    actual = position[4][3].check_if_move_possible(position, -1, 9);
    Assertions.assertEquals(false, actual);

    actual = position[4][3].check_if_move_possible(position, 3, 4);
    Assertions.assertEquals(false, actual);

    actual = position[4][3].check_if_move_possible(position, 4, 4);
    Assertions.assertEquals(true, actual);
  }

  @Test
  public void check_move_possible() {
    position[0][1] = new King(0, 1, true, false);
    position[6][2] = new Rook(6, 2, false, false);
    position[7][7] = new Bishop(7, 7, false);
    Assertions.assertFalse(position[0][1].move_is_possible(position, 0, 2));
    Assertions.assertFalse(position[0][1].move_is_possible(position, 0, 0));
    Assertions.assertTrue(position[0][1].move_is_possible(position, 1, 0));
  }

  @Test
  public void check_castle_move() {
    position[0][3] = new King(0, 3, false, true);
    position[0][0] = new Rook(0, 0, false, true);

    position[0][3].move(position, 0, 1);
    Assertions.assertEquals("King", position[0][1].get_name());
    Assertions.assertEquals("Rook", position[0][2].get_name());
    Assertions.assertEquals(null, position[0][0]);

    position[7][3] = new King(7, 3, true, true);
    position[7][7] = new Rook(7, 7, true, true);
    position[5][2] = new Knight(5, 2, false, false);

    Assertions.assertEquals(false, ((King) position[7][3]).can_castle(position, 7, 5));

    position[5][2] = null;
    position[7][3].move(position, 7, 5);
    Assertions.assertEquals("King", position[7][5].get_name());
    Assertions.assertEquals("Rook", position[7][4].get_name());
    Assertions.assertEquals(null, position[7][3]);

  }

  @Test
  public void check_move() {
    position[4][3] = new King(4, 3, true, false);
    position[4][4] = new Knight(4, 4, false, false);

    position[4][3].move(position, 4, 4);
    Assertions.assertEquals("King", position[4][4].get_name());
    Assertions.assertEquals(true, position[4][4].is_white());
    Assertions.assertEquals(null, position[4][3]);

    position[0][3] = new King(0, 3, true, true);
    position[0][7] = new Rook(0, 7, true, true);
    position[0][4] = new King(0, 4, false, false);

    position[0][3].move(position, 0, 5);
    Assertions.assertEquals(null, position[0][5]);

  }

  @Test
  public void get_possible_moves() {
    ArrayList<int[]> possible_moves;
    int[][] expected;

    position[7][3] = new King(7, 3, true, true);
    position[7][0] = new Rook(7, 0, true, true);

    possible_moves = position[7][3].get_possible_moves(position);
    expected = new int[][] { { 6, 2 }, { 6, 3 }, { 6, 4 }, { 7, 2 }, { 7, 4 }, { 7, 1 } };
    for (int i = 0; i < expected.length; i++) {
      Assertions.assertArrayEquals(expected[i], possible_moves.get(i));
    }

    position[0][3] = new King(0, 3, true, true);
    position[0][7] = new Rook(0, 7, true, true);

    possible_moves = position[0][3].get_possible_moves(position);
    expected = new int[][] { { 0, 2 }, { 0, 4 }, { 1, 2 }, { 1, 3 }, { 1, 4 }, { 0, 5 } };
    for (int i = 0; i < expected.length; i++) {
      Assertions.assertArrayEquals(expected[i], possible_moves.get(i));
    }
  }

  @Test
  public void check_for_check() {
    position[1][1] = new King(1, 1, true, false);
    position[6][6] = new King(6, 6, false, false);
    position[6][1] = new Rook(6, 1, true, false);
    position[4][4] = new Knight(4, 4, true, false);
    position[5][5] = new Bishop(5, 5, false);
    position[1][2] = new Knight(1, 2, false, false);
    Assertions.assertEquals(((King) position[1][1]).check(position), false);
    Assertions.assertEquals(((King) position[6][6]).check(position), true);
  }
}
