package com.chess.Chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import java.util.Arrays;

@Controller
@SpringBootApplication
public class ChessApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChessApplication.class, args);
	}

	@GetMapping("/")
	public String index(Model model) {

		Figure[][] position = new Figure[8][8];
		position[7][7] = new Rook(7, 7, true);
		position[2][7] = new Rook(2, 7, false);
		position[0][7] = new Rook(0, 7, true);

		position[2][7].move(position, 0, 7);

		String[][] board = getNames(position);

		model.addAttribute("board", board);
		return "index";
	}

	public static String[][] getNames(Figure[][] position) {
		String[][] names = new String[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (position[i][j] != null) {
					if (position[i][j].is_white()) {
						names[i][j] = "W " + position[i][j].get_name() + "   ";
					} else {
						names[i][j] = "B " + position[i][j].get_name() + "   ";
					}
				} else {
					names[i][j] = "";
				}
			}
		}
		return names;
	}
}
