package com.chess.Chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;

public class KnightTest {

  Figure[][] position;

  @BeforeEach
  public void setUp() {
    position = new Figure[8][8];
  }

  @Test
  public void check_if_move_possible() {
    boolean actual;
    position[0][0] = new King(0, 0, true, false);
    position[7][7] = new King(7, 7, false, false);
    position[4][3] = new Knight(4, 3, true, false);
    position[3][5] = new Knight(3, 5, true, false);
    position[5][5] = new Knight(5, 5, false, false);

    actual = position[4][3].check_if_move_possible(position, 1, 1);
    Assertions.assertEquals(false, actual);

    actual = position[4][3].check_if_move_possible(position, -1, 9);
    Assertions.assertEquals(false, actual);

    actual = position[4][3].check_if_move_possible(position, 3, 5);
    Assertions.assertEquals(false, actual);

    actual = position[4][3].check_if_move_possible(position, 5, 5);
    Assertions.assertEquals(true, actual);

  }

  @Test
  public void check_move_possible() {
    position[0][1] = new King(0, 1, true, false);
    position[2][1] = new Knight(6, 2, true, false);
    position[7][1] = new Rook(7, 7, false, false);
    Assertions.assertFalse(position[2][1].move_is_possible(position, 4, 0));
    Assertions.assertFalse(position[2][1].move_is_possible(position, 1, 3));
  }

  @Test
  public void check_move() {
    position[0][0] = new King(0, 0, true, false);
    position[7][7] = new King(7, 7, false, false);
    position[4][3] = new Knight(4, 3, true, false);
    position[3][5] = new Knight(3, 5, true, false);
    position[5][5] = new Knight(5, 5, false, false);

    position[4][3].move(position, 3, 5);
    Assertions.assertEquals("Knight", position[4][3].get_name());
    Assertions.assertEquals(true, position[4][3].is_white());

    position[4][3].move(position, 5, 5);
    Assertions.assertEquals("Knight", position[5][5].get_name());
    Assertions.assertEquals(true, position[5][5].is_white());
    Assertions.assertEquals(null, position[4][3]);

  }

  @Test
  public void get_possible_moves() {
    ArrayList<int[]> possible_moves;
    int[][] expected;

    position[0][0] = new King(0, 0, true, false);
    position[7][7] = new King(7, 7, false, false);
    position[4][3] = new Knight(4, 3, true, false);
    position[0][7] = new Knight(0, 7, true, false);

    position[3][5] = new Knight(3, 5, true, false);
    position[5][5] = new Knight(5, 5, false, false);
    position[3][3] = new Knight(3, 3, false, false);

    possible_moves = position[4][3].get_possible_moves(position);
    expected = new int[][] { { 3, 1 }, { 2, 2 }, { 2, 4 }, { 5, 1 }, { 6, 2 }, { 6, 4 }, { 5, 5 } };
    for (int i = 0; i < expected.length; i++) {
      Assertions.assertArrayEquals(expected[i], possible_moves.get(i));
    }

    possible_moves = position[0][7].get_possible_moves(position);
    expected = new int[][] { { 1, 5 }, { 2, 6 } };
    for (int i = 0; i < expected.length; i++) {
      Assertions.assertArrayEquals(expected[i], possible_moves.get(i));
    }

  }
}
