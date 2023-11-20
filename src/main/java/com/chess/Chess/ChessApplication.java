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

		//Дошка находиться в Board.java
		//Те що знизу просто щоб відмалювати дошку
		Figure[][] position = new Figure[8][8];

		model.addAttribute("board", position);
		return "index";
	}

}
