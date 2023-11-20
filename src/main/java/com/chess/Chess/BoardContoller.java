package com.chess.Chess;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
public class BoardContoller {
    Board board = new Board();
    @MessageMapping("/game.start")
    @SendTo("/topic/public")
    public UserMessage register(@Payload UserMessage message) {
        message.setBoard(board.getFiguresOnBoard());
        message.setType("BoardLoad");
        return message;
    }
    @MessageMapping("/game.getMoves")
    @SendTo("/topic/public")
    public UserMessage sendPosMoves(@Payload UserMessage message) {

        String[] pos = message.getContent().split("_");
        int[] result = {Integer.parseInt(pos[0]),Integer.parseInt(pos[1])};

        message.setType("PosibleMoves");
        message.setPosibleMoves(board.getFigure(result[0], result[1]).get_possible_moves(board.getFiguresOnBoard()));
        return message;
    }

    @MessageMapping("/game.Move")
    @SendTo("/topic/public")
    public UserMessage MoveFigure(@Payload UserMessage message) {

        String[] pos = message.getContent().split("_");
        int[] result = {Integer.parseInt(pos[0]),Integer.parseInt(pos[1]),Integer.parseInt(pos[2]),Integer.parseInt(pos[3])};

        //        this.position[2][7].move(position, 0, 7);
//        this.position[0][7].move(position, 3, 7);

        board.Move(result[0],result[1],result[2],result[3]);
        message.setBoard(board.getFiguresOnBoard());
        message.setType("BoardLoad");
        return message;
    }
}