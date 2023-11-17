package com.chess.Chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

public class RookTest {
    Figure[][] position;

    @BeforeEach
    public void setUp() {
        position = new Figure[8][8];
    }

    @Test
    public void rook_move_horizontal() {
        position[2][3] = new Rook(2, 3, true);
        position[2][7] = new Rook(2, 7, false);
        position[2][3].move(position, 2, 7);
        Assertions.assertEquals(position[2][7].get_name(), "Rook");
        Assertions.assertEquals(position[2][7].is_white(), true);
        Assertions.assertEquals(position[2][3], null);
        position[2][7].move(position, 2, 1);
        Assertions.assertEquals(position[2][1].get_name(), "Rook");
        Assertions.assertEquals(position[2][7], null);
    }

    @Test
    public void rook_move_vertical() {
        position[1][1] = new Rook(1, 1, true);
        position[7][1] = new Rook(7, 1, false);
        position[1][1].move(position, 5, 1);
        Assertions.assertEquals(position[5][1].get_name(), "Rook");
        Assertions.assertEquals(position[5][1].is_white(), true);
        Assertions.assertEquals(position[1][1], null);
        position[7][1].move(position, 5, 1);
        Assertions.assertEquals(position[5][1].get_name(), "Rook");
        Assertions.assertEquals(position[7][1], null);
    }
}