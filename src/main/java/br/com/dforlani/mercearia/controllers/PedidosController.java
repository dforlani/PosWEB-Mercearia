package br.com.dforlani.mercearia.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.dforlani.mercearia.models.Produto;
import br.com.dforlani.mercearia.repositorios.PedidoRepositorio;


@Controller
public class PedidosController {
	
	private PedidoRepositorio pedidoRepositorio;

		
	public PedidosController(PedidoRepositorio pedidoRepositorio) {
        this.pedidoRepositorio = pedidoRepositorio;
		
	}
	
	@GetMapping("/produtos")
	public String produtos(Model model) {
		model.addAttribute("listaProdutos", pedidoRepositorio.findAll());
		return "produtos/index";
	}
	
	@GetMapping("/produtos/novo")
	public String novo(Model model) {
		
		model.addAttribute("produto", new Produto());
		
		return "produtos/form";
	}
	
	@GetMapping("/produtos/{id}")
	public String alterar(@PathVariable("id") long id, Model model) {
		Optional<Produto> produtoOpt = pedidoRepositorio.findById(id);
		if (produtoOpt.isEmpty()) {
			throw new IllegalArgumentException("Produto inválido.");
		}
		
		model.addAttribute("produto", produtoOpt.get());
		
		return "produtos/form";
	}
	
	@PostMapping("/produtos/salvar")
	public String salvar(@Valid @ModelAttribute("produto") Produto produto, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "produtos/form";
		}
		
		pedidoRepositorio.save(produto);
		return "redirect:/produtos";
	}
	
	@GetMapping("/produtos/excluir/{id}")
	public String excluir(@PathVariable("id") long id) {
		Optional<Produto> produtoOpt = pedidoRepositorio.findById(id);
		if (produtoOpt.isEmpty()) {
			throw new IllegalArgumentException("Produto inválido.");
		}
		
		pedidoRepositorio.delete(produtoOpt.get());
		return "redirect:/produtos";
	}
	
}
