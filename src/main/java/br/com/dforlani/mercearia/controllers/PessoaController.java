package br.com.dforlani.mercearia.controllers;

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
import br.com.dforlani.mercearia.models.Cidade;
import br.com.dforlani.mercearia.models.Departamento;
import br.com.dforlani.mercearia.models.Pessoa;
import br.com.dforlani.mercearia.repositorios.CidadeRepositorio;
import br.com.dforlani.mercearia.repositorios.DepartamentoRepositorio;
import br.com.dforlani.mercearia.repositorios.PessoaRepositorio;

@Controller
public class PessoaController {
	
	private PessoaRepositorio pessoaRepo;
	private CidadeRepositorio cidadeRepo;
	private DepartamentoRepositorio departamentoRepo;
	
	private List<Departamento> departamentosFiltrados = new ArrayList<>();
	private List<Cidade> cidadesFiltradas = new ArrayList<>();
	
	public PessoaController(PessoaRepositorio pessoaRepo, CidadeRepositorio cidadeRepo, DepartamentoRepositorio departamentoRepo) {
		this.pessoaRepo = pessoaRepo;
		this.cidadeRepo = cidadeRepo;
		this.departamentoRepo = departamentoRepo;
	}
	
	@GetMapping("/rh/pessoas")
	public String pessoas(Model model) {
		model.addAttribute("listaPessoas", pessoaRepo.findAll());
		return "rh/pessoas/index";
	}
	
	@GetMapping("/rh/pessoas/nova")
	public String novaPessoa(Model model) {
		
		model.addAttribute("pessoa", new Pessoa(""));
		
		return "rh/pessoas/form";
	}
	
	@GetMapping("/rh/pessoas/{id}")
	public String alterarPessoa(@PathVariable("id") long id, Model model) {
		Optional<Pessoa> pessoaOpt = pessoaRepo.findById(id);
		if (pessoaOpt.isEmpty()) {
			throw new IllegalArgumentException("Pessoa inválida.");
		}
		
		model.addAttribute("pessoa", pessoaOpt.get());
		
		return "rh/pessoas/form";
	}
	
	@PostMapping("/rh/pessoas/salvar")
	public String salvarPessoa(@Valid @ModelAttribute("pessoa") Pessoa pessoa, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "rh/pessoas/form";
		}
		
		pessoaRepo.save(pessoa);
		return "redirect:/rh/pessoas";
	}
	
	@GetMapping("/rh/pessoas/excluir/{id}")
	public String excluirPessoa(@PathVariable("id") long id) {
		Optional<Pessoa> pessoaOpt = pessoaRepo.findById(id);
		if (pessoaOpt.isEmpty()) {
			throw new IllegalArgumentException("Pessoa inválida.");
		}
		
		pessoaRepo.delete(pessoaOpt.get());
		return "redirect:/rh/pessoas";
	}
	
	@RequestMapping("/rh/pessoas/cidadesNomeAutoComplete")
	@ResponseBody
	public List<AutoCompleteDTO> cidadesNomeAutoComplete(@RequestParam(value="term", required = false, defaultValue = "") String term) {
		List<AutoCompleteDTO> sugestoes = new ArrayList<>();
		
		try {
			if(term.length() >= 3) {
				cidadesFiltradas = cidadeRepo.searchByNome(term);
			}
			
			for (Cidade cidade : cidadesFiltradas) {
				if (cidade.getNome().toLowerCase().contains(term.toLowerCase())) {
					AutoCompleteDTO dto = new AutoCompleteDTO(cidade.getNomeUF(), Long.toString(cidade.getId()));
					sugestoes.add(dto);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sugestoes;
	}
	
	@RequestMapping("/rh/pessoas/departamentosNomeAutoComplete")
	@ResponseBody
	public List<AutoCompleteDTO> departamentosNomeAutoComplete(@RequestParam(value="term", required = false, defaultValue="") String term) {
		List<AutoCompleteDTO> sugestoes = new ArrayList<>();
		try {
			if (term.length() >= 3) {
				departamentosFiltrados = departamentoRepo.searchByNome(term);
			}
			
			for (Departamento departamento : departamentosFiltrados) {
				if (departamento.getNome().toLowerCase().contains(term.toLowerCase())) {
					sugestoes.add(new AutoCompleteDTO(departamento.getNome(), Long.toString(departamento.getId())));	
				}
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return sugestoes;
	}
}
