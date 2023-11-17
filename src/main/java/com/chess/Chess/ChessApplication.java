package com.chess.Chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
@SpringBootApplication
public class ChessApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChessApplication.class, args);
	}

	@GetMapping("/")
	public String index(Model model) {
		// Коли добавляєте фіури то змініть назви фігур в дерикторії
		// src/main/resources/static/img на
		// name_true - біла name_false - чорна

		Figure[][] position = new Figure[8][8];
		position[7][7] = new Rook(7, 7, true, false);
		position[2][7] = new Rook(2, 7, false, false);
		position[0][7] = new Rook(0, 7, true, true);

		position[2][7].move(position, 0, 7);
		position[0][7].move(position, 3, 7);

		model.addAttribute("board", position);
		return "index";
	}

}
