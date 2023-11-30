package com.chess.Chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//import java.util.Arrays;

public class BoardTest {
    Board board;
    Figure[][] position;

    @BeforeEach
    void setUp() {
        board = new Board();
        position = board.getFiguresOnBoard();
    }

    @Test
    void getFiguresOfColorTest() {
        position[1][4].move(position, 3, 4);
        position[0][5].move(position, 5, 0);

        Figure[] whitePieces = board.getFiguresOfColor(true);
        for (Figure piece : whitePieces) {
            Assertions.assertTrue(piece.is_white());
        }
        Assertions.assertEquals(16, whitePieces.length);
        // uncomment to see how it works

        // for (Figure figure : whitePieces) {
        // System.out.println("-------------------");
        // System.out.println(figure.get_name());
        // System.out.println(figure.is_white());
        // if (figure.get_possible_moves(position).isEmpty()) {
        // System.out.println("No possible moves");
        // }
        // for (int[] arr : figure.get_possible_moves(position)) {
        // System.out.println(Arrays.toString(arr));
        // }
        // System.out.println("-------------------");
        // }
    }

    @Test
    public void check_reseting_en_passant() {
        position[1][3].move(position, 3, 3);
        position[1][5].move(position, 3, 5);
        position[6][0].move(position, 4, 0);
        board.reset_en_passant(true);
        Assertions.assertFalse(((Pawn) position[3][3]).en_passant());
        Assertions.assertFalse(((Pawn) position[3][5]).en_passant());
        Assertions.assertTrue(((Pawn) position[4][0]).en_passant());
    }

    @Test
    void isAnyMovePossibleTest() {
        position[1][1].move(position, 3, 1);
        position[6][3].move(position, 4, 3);
        position[1][2].move(position, 2, 2);
        position[7][4].move(position, 3, 0);

        Assertions.assertFalse(board.isAnyMovePossible(true));
        Assertions.assertTrue(board.isAnyMovePossible(false));
    }

    @Test
    void checkmateTest() {
        position[1][1].move(position, 3, 1);
        position[6][3].move(position, 4, 3);
        position[1][2].move(position, 2, 2);
        position[7][4].move(position, 3, 0);

        Assertions.assertTrue(board.isCheckmate(true));
    }
}
