package com.chess.Chess;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@EnableScheduling
public class BoardContoller {
    Board board = new Board();

    @MessageMapping("/game.start")
    @SendTo("/topic/public")
    public UserMessage register(@Payload UserMessage message) {
        board.setPlayer(message.getSender());
        message.setBoard(board.getFiguresOnBoard());
        message.setPlayers(board.getPlayers());
        message.setNotation(board.getNotation());
        if (!board.getGameEnded()) {
            message.setType("BoardLoad");
        } else {
            message.setType(board.getWinInfo());
        }
        return message;
    }

    @MessageMapping("/game.getMoves")
    @SendToUser("/topic/private")
    public UserMessage SendPosMoves(@Payload UserMessage message) {
        String[] pos = message.getContent().split("_");
        int[] result = { Integer.parseInt(pos[0]), Integer.parseInt(pos[1]) };
        if (!board.getGameEnded()) {
            message.setType("PosibleMoves");
            message.setPosibleMoves(
                    board.getFigure(result[0], result[1]).get_possible_moves(board.getFiguresOnBoard()));
        } else {
            message.setType("None");
        }
        return message;
    }

    @MessageMapping("/game.Move")
    @SendTo("/topic/public")
    public UserMessage MoveFigure(@Payload UserMessage message) {
        message.setPlayers(board.getPlayers());
        String[] pos = message.getContent().split("_");
        int[] result = { Integer.parseInt(pos[0]), Integer.parseInt(pos[1]), Integer.parseInt(pos[2]),
                Integer.parseInt(pos[3]) };
//        if(result[0] == null){
//            message.setType("None");
//            return message;
//        }
        if (!board.getGameEnded() && board.getPlayers()[0] != null && board.getPlayers()[1] != null
                && board.Move(message.getSender(), result[0], result[1], result[2], result[3])) {
            if ((result[2] == 0 || result[2] == 7) && board.getFigure(result[2], result[3]).get_name().equals("Pawn")) {
                if(pos.length == 5) {
                    Pawn temp = (Pawn) board.getFigure(result[2], result[3]);
                    Figure figure = board.position[result[2]][result[3]];
                    temp.promote(board.getFiguresOnBoard(), result[2], result[3], pos[4]);
                    board.notate_promotion(result[0], result[1], result[2], result[3], pos[4], figure);
                } else {
                    message.setType("None");
                    return message;
                }
            }
            message.setBoard(board.getFiguresOnBoard());
            message.setNotation(board.getNotation());
            if (!board.isAnyMovePossible(board.playerWhiteTurn)) {
                board.saveGameToFile("game.txt");
                message.setType(board.SetGameEnded("NoAnyMovePossible", message.getSender()));
                board = new Board();
            } else {
                message.setType("BoardLoad");
            }
        } else {
            message.setType("None");
        }
        return message;
    }

    @MessageMapping("/game.EndGame")
    @SendTo("/topic/public")
    public UserMessage EndGame(@Payload UserMessage message) {
        if (!board.getGameEnded()) {
            if (message.getContent().equals("Resign")) {
                message.setType(board.SetGameEnded("Resign", message.getSender()));
            } else if (message.getContent().equals("Draw")) {
                message.setType(board.SetGameEnded("Draw", message.getSender()));
            }
        }
        message.setPlayers(board.getPlayers());
        message.setNotation(board.getNotation());
        message.setBoard(board.getFiguresOnBoard());
        if (board.getGameEnded()) {
            board = new Board();
        }
        return message;
    }

    private final SimpMessagingTemplate messagingTemplate;

    public BoardContoller(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Scheduled(fixedRate = 1000)
    public void SendPeriodicMessages() {
        UserMessage message = new UserMessage();
        message.setType("Timer");

        if (board.getWhiteClock() != null && board.getBlackClock() != null && !board.getGameEnded()) {
            int timeWhite = board.getWhiteClock().getTimeLeft();
            int timeBlack = board.getBlackClock().getTimeLeft();
            if (timeWhite == 0 || timeBlack == 0) {
                if (timeWhite == 0) {
                    message.setType(board.SetGameEnded("Time", board.getPlayers()[0]));
                } else {
                    message.setType(board.SetGameEnded("Time", board.getPlayers()[1]));
                }
                message.setPlayers(board.getPlayers());
                message.setNotation(board.getNotation());
                message.setBoard(board.getFiguresOnBoard());
                board = new Board();
            }
        }

        message.setContent(String.valueOf(board.getTimesLeft()[0] - 1) + " " + String.valueOf(board.getTimesLeft()[1] - 1));
        messagingTemplate.convertAndSend("/topic/public", message);
    }
}