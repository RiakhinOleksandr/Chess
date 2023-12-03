package com.chess.Chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;


public class BoardTest {
    Board board;
    Figure[][] position;

    @BeforeEach
    void setUp() {
        board = new Board();
        position = board.getFiguresOnBoard();
        board.colour = 0;
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
        position[1][6] = new Pawn(1,6,false,false);
        position[0][6] = null;
        Assertions.assertTrue(position[6][1].is_white());
        Assertions.assertTrue(board.Move("player1", 6, 1, 7, 0));
        board.notate_promotion(6, 1, 7, 0, "Rook", false);
        Assertions.assertEquals(board.getNotation().size(), 1);
        Assertions.assertEquals((board.getNotation()).get(0), "g7xh8R");
        board.notate_promotion(1, 6, 0, 6, "Knight", true);
        Assertions.assertEquals((board.getNotation()).get(1), "b2-b1N");
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
    public void check_getting_players(){
        String[] players = board.getPlayers();
        Assertions.assertEquals(players[0], "player1");
        Assertions.assertEquals(players[1], "player2");
    }

    @Test
    void setGameEndedTest() {
        board.SetGameEnded("NoAnyMovePossible", "player1");
        Assertions.assertTrue(board.getGameEnded());
    }

    @Test
    void setGameEndedDrawTest() {
        Assertions.assertEquals("player1 offers a draw", board.SetGameEnded("Draw", "player1"));
        Assertions.assertEquals("Draw", board.SetGameEnded("Draw", "player2"));
    }

    @Test
    void setPlayersRandomTest() {
        board = new Board();
        position = board.getFiguresOnBoard();
        board.colour = 1;
        board.setPlayer("firstShouldBeBlack");
        board.setPlayer("secondShouldBeWhite");
        Assertions.assertEquals("firstShouldBeBlack", board.getPlayers()[1]);
        Assertions.assertEquals("secondShouldBeWhite", board.getPlayers()[0]);
    }

    @Test
    void setPlayersInvalidNameTest() {
        board = new Board();
        position = board.getFiguresOnBoard();
        board.colour = 0;
        board.setPlayer("playerWhite");
        board.setPlayer("playerWhite");
        Assertions.assertEquals("playerWhite", board.getPlayers()[0]);
        Assertions.assertNull(board.getPlayers()[1]);

        board = new Board();
        position = board.getFiguresOnBoard();
        board.colour = 1;
        board.setPlayer("playerBlack");
        board.setPlayer("playerBlack");
        Assertions.assertEquals("playerBlack", board.getPlayers()[1]);
        Assertions.assertNull(board.getPlayers()[0]);
    }

    @Test
    void setPlayersOverrideNamesTest() {
        board.setPlayer("playerWhite");
        board.setPlayer("playerBlack");
        Assertions.assertEquals("player1", board.getPlayers()[0]);
        Assertions.assertEquals("player2", board.getPlayers()[1]);

        board = new Board();
        position = board.getFiguresOnBoard();
        board.colour = 1;
        board.setPlayer("playerBlack");
        board.setPlayer("playerWhite");
        board.setPlayer("someOtherName");
        Assertions.assertEquals("playerWhite", board.getPlayers()[0]);
        Assertions.assertEquals("playerBlack", board.getPlayers()[1]);
    }

    @Test
    void setGameEndedWhiteTimeoutTest() {
        board.SetGameEnded("Time", "player1");
        Assertions.assertEquals("Winner: Black\nType: time is over", board.getWinInfo());
    }

    @Test
    void setGameEndedBlackTimeoutTest() {
        board.SetGameEnded("Time", "player2");
        Assertions.assertEquals("Winner: White\nType: time is over", board.getWinInfo());
    }

    @Test
    void setGameEndedIncorrectNameTest() {
        board.SetGameEnded("Time", "player3");
        Assertions.assertEquals("None", board.SetGameEnded("Time", "player3"));
        Assertions.assertEquals("", board.getWinInfo());
    }

    @Test
    void setGameEndedResignWhiteTest() {
        board.SetGameEnded("Resign", "player1");
        Assertions.assertEquals("Winner: Black\nType: Resign", board.getWinInfo());
    }

    @Test
    void setGameEndedResignBlackTest() {
        board.SetGameEnded("Resign", "player2");
        Assertions.assertEquals("Winner: White\nType: Resign", board.getWinInfo());
    }
}
