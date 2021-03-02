package br.com.dforlani.mercearia.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.dforlani.mercearia.dtos.AutoCompleteDTO;
import br.com.dforlani.mercearia.models.Pedido;
import br.com.dforlani.mercearia.models.PedidoItem;
import br.com.dforlani.mercearia.models.Pessoa;
import br.com.dforlani.mercearia.repositorios.PedidoRepositorio;
import br.com.dforlani.mercearia.repositorios.PessoaRepositorio;


@Controller
public class PedidosController {
	
	private PedidoRepositorio pedidoRepositorio;
	private PessoaRepositorio pessoaRepositorio;

	private List<Pessoa> pessoasFiltrados = new ArrayList<>();

		
	public PedidosController(PedidoRepositorio pedidoRepositorio, PessoaRepositorio pessoaRepositorio) {
        this.pedidoRepositorio = pedidoRepositorio;
		this.pessoaRepositorio = pessoaRepositorio;
		
	}
	
	@GetMapping("/pedidos")
	public String pedidos(Model model) {
		model.addAttribute("listaPedidos", pedidoRepositorio.findAll());
		model.addAttribute("pedido", new Pedido());
		return "pedidos/index";
	}
	
	@GetMapping("/pedidos/novo")
	public String novo(Model model) {
		
		model.addAttribute("pedido", new Pedido(LocalDate.now()));
		
		
		return "pedidos/form";
	}
	
	@GetMapping("/pedidos/{id}")
	public String alterar(@PathVariable("id") long id, Model model) {
		Optional<Pedido> pedidoOpt = pedidoRepositorio.findById(id);
		if (pedidoOpt.isEmpty()) {
			throw new IllegalArgumentException("Pedido inválido.");
		}
		
		model.addAttribute("pedido", pedidoOpt.get());
		model.addAttribute("pedidoItem", new PedidoItem());
		
		return "pedidos/form";
	}
	
	@PostMapping("/pedidos/salvar")
	public String salvar(@Valid @ModelAttribute("pedido") Pedido pedido, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "pedidos/index";
		}
		
		pedidoRepositorio.save(pedido);
		model.addAttribute("pedido", pedido);
		model.addAttribute("pedidoItem", new PedidoItem(pedido));
		return "pedidos/form";
	}
	
	@GetMapping("/pedidos/excluir/{id}")
	public String excluir(@PathVariable("id") long id) {
		Optional<Pedido> pedidoOpt = pedidoRepositorio.findById(id);
		if (pedidoOpt.isEmpty()) {
			throw new IllegalArgumentException("Pedido inválido.");
		}
		
		pedidoRepositorio.delete(pedidoOpt.get());
		return "redirect:/pedidos";
	}

	@RequestMapping("/pedidos/pessoasNomeAutoComplete")
	@ResponseBody
	public List<AutoCompleteDTO> pessoasNomeAutoComplete(@RequestParam(value="term", required = false, defaultValue="") String term) {
		List<AutoCompleteDTO> sugestoes = new ArrayList<>();
		try {
			if (term.length() >= 2) {
				pessoasFiltrados = pessoaRepositorio.searchByNome(term);
			}
			
			for (Pessoa pessoa : pessoasFiltrados) {
				if (pessoa.getNome().toLowerCase().contains(term.toLowerCase())) {
					sugestoes.add(new AutoCompleteDTO(pessoa.getNome(), Long.toString(pessoa.getId())));	
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return sugestoes;
	}
	
}
