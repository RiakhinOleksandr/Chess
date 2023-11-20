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
  public void check_castle_move() {
    position[0][4] = new King(0, 4, false, true);
    position[0][0] = new Rook(0, 0, false, true);

    position[0][4].move(position, 0, 2);
    Assertions.assertEquals("King", position[0][2].get_name());
    Assertions.assertEquals("Rook", position[0][3].get_name());
    Assertions.assertEquals(null, position[0][0]);

    position[7][4] = new King(7, 4, true, true);
    position[7][7] = new Rook(7, 7, true, true);

    position[7][4].move(position, 7, 6);
    Assertions.assertEquals("King", position[7][6].get_name());
    Assertions.assertEquals("Rook", position[7][5].get_name());
    Assertions.assertEquals(null, position[7][4]);
  }

  @Test
  public void check_move() {
    position[4][3] = new King(4, 3, true, false);
    position[4][4] = new Knight(4, 4, false, false);

    position[4][3].move(position, 4, 4);
    Assertions.assertEquals("King", position[4][4].get_name());
    Assertions.assertEquals(true, position[4][4].is_white());
    Assertions.assertEquals(null, position[4][3]);

    position[0][4] = new King(0, 4, true, true);
    position[0][0] = new Rook(0, 0, true, true);
    position[0][3] = new King(0, 3, false, false);

    position[0][4].move(position, 0, 2);
    Assertions.assertEquals("King", position[0][4].get_name());
    Assertions.assertEquals("Rook", position[0][0].get_name());

  }

  @Test
  public void get_possible_moves() {
    ArrayList<int[]> possible_moves;
    int[][] expected;

    position[7][4] = new King(7, 4, true, true);
    position[7][7] = new Rook(7, 7, true, true);

    possible_moves = position[7][4].get_possible_moves(position);
    expected = new int[][] { { 6, 3 }, { 6, 4 }, { 6, 5 }, { 7, 3 }, { 7, 5 }, { 7, 6 } };
    for (int i = 0; i < expected.length; i++) {
      Assertions.assertArrayEquals(expected[i], possible_moves.get(i));
    }

    position[0][4] = new King(0, 4, true, true);
    position[0][0] = new Rook(0, 0, true, true);

    possible_moves = position[0][4].get_possible_moves(position);
    expected = new int[][] { { 0, 3 }, { 0, 5 }, { 1, 3 }, { 1, 4 }, { 1, 5 }, { 0, 2 } };
    for (int i = 0; i < expected.length; i++) {
      Assertions.assertArrayEquals(expected[i], possible_moves.get(i));
    }
  }

}
