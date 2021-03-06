package br.com.dforlani.mercearia.controllers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import br.com.dforlani.mercearia.models.Produto;
import br.com.dforlani.mercearia.repositorios.PedidoItemRepositorio;
import br.com.dforlani.mercearia.repositorios.PedidoRepositorio;
import br.com.dforlani.mercearia.repositorios.PessoaRepositorio;
import br.com.dforlani.mercearia.repositorios.ProdutoRepositorio;

@Controller
public class PedidosController {

	private PedidoRepositorio pedidoRepositorio;
	private PedidoItemRepositorio pedidoItemRepositorio;
	private PessoaRepositorio pessoaRepositorio;
	private ProdutoRepositorio produtoRepositorio;

	private List<Pessoa> pessoasFiltrados = new ArrayList<>();
	private List<Produto> produtosFiltrados = new ArrayList<>();

	public PedidosController(PedidoRepositorio pedidoRepositorio, PedidoItemRepositorio pedidoItemRepositorio,
			PessoaRepositorio pessoaRepositorio, ProdutoRepositorio produtoRepositorio) {
		this.pedidoRepositorio = pedidoRepositorio;
		this.pessoaRepositorio = pessoaRepositorio;
		this.produtoRepositorio = produtoRepositorio;
		this.pedidoItemRepositorio = pedidoItemRepositorio;

	}

	@GetMapping("/pedidos")
	public String pedidos(Model model) {
		List<Pedido> pedidos = pedidoRepositorio.findAll();		
		model.addAttribute("listaPedidos", pedidos);
		model.addAttribute("pedido", new Pedido());
		return "pedidos/index";
	}

	@GetMapping("/pedidos/novo")
	public String novo(Model model) {

		model.addAttribute("pedido", new Pedido(LocalDateTime.now()));

		return "pedidos/form";
	}

	@GetMapping("/pedidos/{id}")
	public String alterar(@PathVariable("id") long id, Model model) {
		Optional<Pedido> pedidoOpt = pedidoRepositorio.findById(id);
		if (pedidoOpt.isEmpty()) {
			throw new IllegalArgumentException("Pedido inválido.");
		}

		BigDecimal total = new BigDecimal(0);
		for (PedidoItem item : pedidoOpt.get().getItens()) {
			total = total.add(item.getProduto().getValor().multiply(new BigDecimal(item.getQuantidade())));			
			
		}
		

		model.addAttribute("total", total);
		model.addAttribute("pedido", pedidoOpt.get());
		model.addAttribute("pedidoItem", new PedidoItem(1));

		return "pedidos/form";
	}

	@PostMapping("/pedidos/salvar")
	public String salvar(@Valid @ModelAttribute("pedido") Pedido pedido, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "pedidos/index";
		}
		pedido.setData(LocalDateTime.now());
		pedidoRepositorio.save(pedido);
		model.addAttribute("pedido", pedido);
		model.addAttribute("pedidoItem", new PedidoItem(pedido));
		return "redirect:/pedidos/"+pedido.getId();
	}

	@PostMapping("/pedidos/{pedidoId}/adicionarItem")
	public String adicionarItem(@PathVariable("pedidoId") Long pedidoId, @RequestParam("produtoId") Long produtoId,
			@ModelAttribute("pedidoItem") PedidoItem pedidoItem, Model model) {
		// if (bindingResult.hasErrors()) {
		// return "pedidos/form";
		// }

		Optional<Pedido> pedidoOpt = pedidoRepositorio.findById(pedidoId);
		Optional<Produto> produtoOpt = produtoRepositorio.findById(produtoId);

		if (!(pedidoOpt.isEmpty()) && !produtoOpt.isEmpty()) {
			pedidoItem.setProduto(produtoOpt.get());
			pedidoItem.setPedido(pedidoOpt.get());

			pedidoItemRepositorio.save(pedidoItem);
		} else {
			pedidoItem = new PedidoItem();
		}

		return "redirect:/pedidos/" + pedidoId;
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

	@GetMapping("/pedidos/{idPedido}/excluirItem/{idPedidoItem}")
	public String excluirItem(@PathVariable("idPedido") long idPedido,
			@PathVariable("idPedidoItem") Long idPedidoItem) {
		Optional<PedidoItem> peditoItemOpt = pedidoItemRepositorio.findById(idPedidoItem);
		if (peditoItemOpt.isEmpty()) {
			throw new IllegalArgumentException("Pedido Item inválido.");
		}

		pedidoItemRepositorio.delete(peditoItemOpt.get());
		return "redirect:/pedidos/" + idPedido;
	}

	@RequestMapping("/pedidos/pessoasNomeAutoComplete")
	@ResponseBody
	public List<AutoCompleteDTO> pessoasNomeAutoComplete(
			@RequestParam(value = "term", required = false, defaultValue = "") String term) {
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

	@RequestMapping("/pedidos/produtosNomeAutoComplete")
	@ResponseBody
	public List<AutoCompleteDTO> produtosNomeAutoComplete(
			@RequestParam(value = "term", required = false, defaultValue = "") String term) {
		List<AutoCompleteDTO> sugestoes = new ArrayList<>();
		try {
			if (term.length() >= 2) {
				produtosFiltrados = produtoRepositorio.searchByNome(term);
			}

			for (Produto produto : produtosFiltrados) {
				if (produto.getNome().toLowerCase().contains(term.toLowerCase())) {
					sugestoes.add(new AutoCompleteDTO(produto.getNome(), Long.toString(produto.getId())));
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return sugestoes;
	}

}
