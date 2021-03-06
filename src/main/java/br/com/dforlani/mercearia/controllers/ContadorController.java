package br.com.dforlani.mercearia.controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContadorController {

	@GetMapping("/contador")
	public String pessoas(HttpServletRequest req, Model model) {
		Integer contador = 0;

		// inicializa a sessão do usuário
		HttpSession session = req.getSession(true);
		if (session != null) {

			contador = (Integer) session.getAttribute("contador");

			//se o contador não foi iniciado ainda, inicia ele com 0
			if (contador == null) {
				contador = 0;
			}

			contador = contador + 1;

			//salva variável de sessão
			session.setAttribute("contador", contador);

			model.addAttribute("contador", contador);
		} else {
			//em caso de erro, emite mensagem de erro
			model.addAttribute("erro", "<Contador não pôde ser inicializado>");
		}
		
		return "contador/index";
	}
}