package com.chess.Chess;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class UserMessageTest {

    @Test
    public void testUserMessage() {
        UserMessage userMessage = new UserMessage();
        Board board = new Board();

        userMessage.setContent("test content");
        userMessage.setSender("test sender");
        userMessage.setType("test type");
        userMessage.setBoard(board.getFiguresOnBoard());
        userMessage.setPosibleMoves(board.getFigure(1,1).get_possible_moves(board.getFiguresOnBoard()));
        userMessage.setPlayers(new String[]{"player1", "player2"});
        userMessage.setNotation(new ArrayList<>(Arrays.asList("b2-b4", "Nb8-c6", "f2-f4")));

        assertEquals("test content", userMessage.getContent());
        assertEquals("test sender", userMessage.getSender());
        assertEquals("test type", userMessage.getType());
        assertEquals(board.getFiguresOnBoard(), userMessage.getBoard());
        assertArrayEquals(board.getFigure(1,1).get_possible_moves(board.getFiguresOnBoard()).toArray(), userMessage.getPosibleMoves().toArray());
        assertArrayEquals(new String[]{"player1", "player2"}, userMessage.getPlayers());
        assertEquals(new ArrayList<>(Arrays.asList("b2-b4", "Nb8-c6", "f2-f4")), userMessage.getNotation());
    }
}