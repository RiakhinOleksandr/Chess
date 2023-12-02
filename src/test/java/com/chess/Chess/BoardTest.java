package com.chess.Chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//import java.util.Arrays;

public class BoardTest {
    Board board;
    Figure[][] position;

    @BeforeEach
    void setUp() {
        board = new Board();
        position = board.getFiguresOnBoard();
        board.setPlayer("player1");
        board.setPlayer("player2");
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
    public void check_getting_figure(){
        Assertions.assertEquals(board.getFigure(0,2).get_name(), "Bishop");
        Assertions.assertNull(board.getFigure(2,2));
    }

    @Test
    public void check_notating_promotion(){
        position[6][1] = new Pawn(6,1,true,false);
        Assertions.assertTrue(position[6][1].is_white());
        Figure figure = position[7][2];
        Assertions.assertTrue(board.Move("player1", 6, 1, 7, 0));
        board.notate_promotion(6, 1, 7, 0, "Rook", figure);
        Assertions.assertEquals(board.getNotation().size(), 1);
        Assertions.assertEquals((board.getNotation()).get(0), "g7xh8R");
    }

    @Test
    public void check_move(){
        Assertions.assertTrue(board.Move("player1", 1, 1, 3, 1));
        Assertions.assertFalse(board.Move("player1", 1, 5, 3, 5));
        Assertions.assertTrue(board.Move("player2", 6, 1, 5, 1));
        Assertions.assertTrue(board.Move("player1", 0, 1, 2, 2));
        Assertions.assertTrue(board.Move("player2", 7, 2, 5, 0));
        Assertions.assertTrue(board.Move("player1", 2, 2, 4, 3));
        Assertions.assertTrue(board.Move("player2", 7, 3, 7, 2));
        Assertions.assertTrue(board.Move("player1", 4, 3, 6, 4));
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

        Assertions.assertTrue(board.isKingInCheck(true));
    }

    @Test
    void saveGameToFileTest() {
        board.Move("player1", 1, 1, 3, 1);
        board.Move("player2", 6, 3, 4, 3);
        board.Move("player1", 1, 2, 2, 2);
        board.Move("player2", 7, 4, 3, 0);

        String fileName = "test_game.txt";
        board.saveGameToFile(fileName);
        fileName = board.getUniqueFileName("games", fileName);
        Assertions.assertTrue(Files.exists(Paths.get(fileName)));
        try {
            Files.deleteIfExists(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void setGameEndedTest() {
        board.SetGameEnded("NoAnyMovePossible", "player1");
        Assertions.assertTrue(board.getGameEnded());
    }

    @Test
    void setGameEndedDrawTest() {
        Assertions.assertEquals("Opponent offers a draw", board.SetGameEnded("Draw", "player1"));
        Assertions.assertEquals("Draw", board.SetGameEnded("Draw", "player2"));
    }
}
