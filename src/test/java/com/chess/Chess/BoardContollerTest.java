package com.chess.Chess;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardContollerTest {

    @Test
    public void RegisterTestThanResign() {
        UserMessage messagePlayer1 = new UserMessage();
        messagePlayer1.setSender("player1");

        UserMessage messagePlayer2 = new UserMessage();
        messagePlayer2.setSender("player2");

        BoardContoller boardContoller = new BoardContoller(null);

        UserMessage resultPlayer1 = boardContoller.register(messagePlayer1);
        assertArrayEquals(new String[]{"player1", null}, resultPlayer1.getPlayers());

        UserMessage resultPlayer2 = boardContoller.register(messagePlayer2);
        assertArrayEquals(new String[]{"player1", "player2"}, resultPlayer2.getPlayers());

        assertArrayEquals(resultPlayer1.getBoard(), resultPlayer2.getBoard());
        assertArrayEquals(resultPlayer1.getNotation().toArray(), resultPlayer2.getNotation().toArray());
        assertEquals(resultPlayer1.getType(), resultPlayer2.getType());

        messagePlayer2.setContent("Resign");
        resultPlayer2 = boardContoller.EndGame(messagePlayer2);
        assertEquals(resultPlayer2.getType(), "Winner: White\nType: Resign");
    }
    @Test
    public void EndGameTest() {
        UserMessage messagePlayer1 = new UserMessage();
        messagePlayer1.setSender("player1");

        UserMessage messagePlayer2 = new UserMessage();
        messagePlayer2.setSender("player2");

        BoardContoller boardContoller = new BoardContoller(null);

        boardContoller.register(messagePlayer1);
        boardContoller.register(messagePlayer2);

        messagePlayer1.setContent("Draw");
        UserMessage resultPlayer1 = boardContoller.EndGame(messagePlayer1);
        assertEquals(resultPlayer1.getType(), "player1 offers a draw");
    }
    @Test
    public void SendPosTestWithPromote() {
        UserMessage messagePlayer1 = new UserMessage();
        messagePlayer1.setSender("player1");
        BoardContoller boardContoller = new BoardContoller(null);
        //Get possible moves
        boardContoller.register(messagePlayer1);
        messagePlayer1.setContent("1_7");
        UserMessage resultPlayer1 = boardContoller.SendPosMoves(messagePlayer1);
        assertArrayEquals(resultPlayer1.getPosibleMoves().toArray(), new ArrayList<int[]>(Arrays.asList(new int[] {2, 7},new int[] {3,7})).toArray());
        //Move Figure type:None
        messagePlayer1.setContent("1_7_2_7");
        resultPlayer1 = boardContoller.MoveFigure(messagePlayer1);
        assertEquals(resultPlayer1.getType(), "None");
        //Move Figure type:BoardLoad
        UserMessage messagePlayer2 = new UserMessage();
        messagePlayer2.setSender("player2");

        UserMessage resultPlayer2 = boardContoller.register(messagePlayer2);
        String[] moves = {"1_6_3_6","6_5_4_5","3_6_4_5","6_6_4_6","4_5_5_6","7_6_5_5","5_6_6_6","6_4_5_4","6_6_7_6_Queen"};
        for (int i = 0; i < moves.length; i++) {
            if(i % 2 == 0){
                messagePlayer1.setContent(moves[i]);
                resultPlayer1 = boardContoller.MoveFigure(messagePlayer1);
                assertEquals(resultPlayer1.getType(), "BoardLoad");
            }
            else{
                messagePlayer2.setContent(moves[i]);
                resultPlayer2 = boardContoller.MoveFigure(messagePlayer2);
                assertEquals(resultPlayer2.getType(), "BoardLoad");
            }
        }
    }
    @Test
    public void CheckmatedTest() {
        BoardContoller boardContoller = new BoardContoller(null);

        UserMessage messagePlayer1 = new UserMessage();
        messagePlayer1.setSender("player1");
        UserMessage resultPlayer1 = boardContoller.register(messagePlayer1);

        UserMessage messagePlayer2 = new UserMessage();
        messagePlayer2.setSender("player2");
        UserMessage resultPlayer2 = boardContoller.register(messagePlayer2);

        String[] moves = {"1_2_2_2","6_3_4_3","1_1_3_1","7_4_3_0"};
        for (int i = 0; i < moves.length; i++) {
            if(i != moves.length - 1) {
                if(i % 2 == 0){
                    messagePlayer1.setContent(moves[i]);
                    resultPlayer1 = boardContoller.MoveFigure(messagePlayer1);
                    assertEquals(resultPlayer1.getType(), "BoardLoad");
                }
                else{
                    messagePlayer2.setContent(moves[i]);
                    resultPlayer2 = boardContoller.MoveFigure(messagePlayer2);
                    assertEquals(resultPlayer2.getType(), "BoardLoad");
                }
            }else{
                messagePlayer2.setContent(moves[i]);
                resultPlayer2 = boardContoller.MoveFigure(messagePlayer2);
                assertEquals(resultPlayer2.getType(), "Winner: Black\nType: checkmated");
            }
        }
    }
}
