package com.chess.Chess;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class BoardContoller {
    Board board = new Board();
    @MessageMapping("/game.start")
    @SendTo("/topic/public")
    public UserMessage register(@Payload UserMessage message) {
        board.setPlayer(message.getSender());
        message.setBoard(board.getFiguresOnBoard());
        message.setPlayers(board.getPlayers());
        message.setNotation(board.getNotation());
        if(!board.getGameEnded()) {
            message.setType("BoardLoad");
        }else {
            message.setType(board.getWinInfo());
        }
        return message;
    }
    @MessageMapping("/game.getMoves")
    @SendToUser("/topic/private")
    public UserMessage sendPosMoves(@Payload UserMessage message, final Principal principal) {
        String[] pos = message.getContent().split("_");
        int[] result = {Integer.parseInt(pos[0]),Integer.parseInt(pos[1])};
        if(!board.getGameEnded() ) {
            message.setType("PosibleMoves");
            message.setPosibleMoves(board.getFigure(result[0], result[1]).get_possible_moves(board.getFiguresOnBoard()));
        }else {
            message.setType("None");
        }
        return message;
    }

    @MessageMapping("/game.Move")
    @SendTo("/topic/public")
    public UserMessage MoveFigure(@Payload UserMessage message) {
        message.setPlayers(board.getPlayers());
        String[] pos = message.getContent().split("_");
        int[] result = {Integer.parseInt(pos[0]),Integer.parseInt(pos[1]),Integer.parseInt(pos[2]),Integer.parseInt(pos[3])};
        if(!board.getGameEnded() && board.getPlayers()[0] != null && board.getPlayers()[1] != null && board.Move(message.getSender(), result[0], result[1], result[2], result[3])){
                if((result[2] == 0 || result[2] == 7) && board.getFigure(result[2],result[3]).get_name().equals("Pawn") ){
                    Pawn temp = (Pawn) board.getFigure(result[2], result[3]);
                    Figure figure = board.position[result[2]][result[3]];
                    temp.promote(board.getFiguresOnBoard(), result[2], result[3], pos[4]);
                    board.notate_promotion(result[0], result[1], result[2], result[3], pos[4], figure);
                }
                message.setBoard(board.getFiguresOnBoard());
                message.setNotation(board.getNotation());
                if(!board.isAnyMovePossible(board.playerWhiteTurn)){
                    board.saveGameToFile("game.txt");
                    message.setType(board.SetGameEnded("NoAnyMovePossible", message.getSender()));
                }else {
                    message.setType("BoardLoad");
                }
        }else {
            message.setType("None");
        }
        return message;
    }

    @MessageMapping("/game.EndGame")
    @SendTo("/topic/public")
    public UserMessage EndGame(@Payload UserMessage message) {
        if(message.getContent().equals("Resign")){
            message.setType(board.SetGameEnded("Resign", message.getSender()));
        }else if(message.getContent().equals("Draw")){
            message.setType(board.SetGameEnded("Draw", message.getSender()));
        }
        message.setBoard(board.getFiguresOnBoard());
        return message;
    }
}