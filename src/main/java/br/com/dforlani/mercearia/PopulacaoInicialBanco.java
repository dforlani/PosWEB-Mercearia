package br.com.dforlani.mercearia;

import java.io.File;
import java.time.LocalDate;

import javax.transaction.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import br.com.dforlani.mercearia.models.Cidade;
import br.com.dforlani.mercearia.models.Departamento;
import br.com.dforlani.mercearia.models.Pedido;
import br.com.dforlani.mercearia.models.Pessoa;
import br.com.dforlani.mercearia.models.Produto;
import br.com.dforlani.mercearia.repositorios.CidadeRepositorio;
import br.com.dforlani.mercearia.repositorios.DepartamentoRepositorio;
import br.com.dforlani.mercearia.repositorios.PedidoRepositorio;
import br.com.dforlani.mercearia.repositorios.PessoaRepositorio;
import br.com.dforlani.mercearia.repositorios.ProdutoRepositorio;


@Component
@Transactional
public class PopulacaoInicialBanco implements CommandLineRunner {

	@Autowired
	private PessoaRepositorio pessoaRepo;
	@Autowired
	private CidadeRepositorio cidadeRepo;
	@Autowired
	private DepartamentoRepositorio departamentoRepo;
	@Autowired
	private ProdutoRepositorio produtoRep;
	@Autowired
	private PedidoRepositorio pedidoRep;
	
	@Override
	public void run(String... args) throws Exception {
		
		File jsonFile = ResourceUtils.getFile("classpath:municipios.json");
		ObjectMapper cidadeMapper = new ObjectMapper();
		JsonNode dataNode = cidadeMapper.readTree(jsonFile).get("data");
		
		dataNode.forEach((cidadeNode) -> {
			Cidade cidade = new Cidade();
			cidade.setCodigo(cidadeNode.get("Codigo").asText());
			cidade.setNome(cidadeNode.get("Nome").asText());
			cidade.setUf(cidadeNode.get("Uf").asText());
			cidadeRepo.save(cidade);
		});
		
		cidadeRepo.flush();

		Departamento departamento1 = new Departamento("Tecnologia da Informação", "TI");
		Departamento departamento2 = new Departamento("Recursos Humanos", "RH");
		Departamento departamento3 = new Departamento("Produção", "PROD");
		departamentoRepo.save(departamento1);
		departamentoRepo.save(departamento2);
		departamentoRepo.save(departamento3);
		
		departamentoRepo.flush();
		
		Cidade cidade1 = cidadeRepo.findById(1L).get();
		
		Pessoa p1 = new Pessoa("Joao");
		p1.setDataNascimento(LocalDate.of(1990, 4, 1));
		p1.setEmail("joao@gmail.com");
		p1.setCpf("10518516962");
		p1.setCidade(cidade1);
		p1.setDepartamento(departamento1);
		
		Pessoa p2 = new Pessoa("Maria");
		p2.setDataNascimento(LocalDate.of(1900, 1, 1));
		p2.setEmail("maria@gmail.com");
		p2.setCpf("10518516962");
		p2.setCidade(cidade1);
		p2.setDepartamento(departamento1);
		
		pessoaRepo.save(p1);
		pessoaRepo.save(p2);

		//INCLUSÃO AUTOMÁTICA DE PRODUTOS 
		Produto prodAux = new Produto("Coca-Cola", 1.89);
		produtoRep.save(prodAux);

		prodAux = new Produto("Farinha Anaconda", 6.98);
		produtoRep.save(prodAux);

		prodAux = new Produto("Barra de Chocolate Nestlê", 7.02);
		produtoRep.save(prodAux);

		prodAux = new Produto("Sabonete", 2.35);
		produtoRep.save(prodAux);
		produtoRep.flush();
		

		//INCLUSÃO AUTOMÁTICA DE PEDIDOS 
		Pedido pedido = new Pedido(LocalDate.of(1990, 12, 1));
		// pedido.setItens(produtoRep.findAll());
		pedidoRep.save(pedido);

		pedido = new Pedido(LocalDate.of(2020, 2, 4));
		// pedido.setItens(produtoRep.findAll());
		pedidoRep.save(pedido);

		pedido = new Pedido(LocalDate.of(2018, 4, 5));
		// pedido.setItens(produtoRep.findAll());
		pedidoRep.save(pedido);

		// pedidoRep.flush();



	}
}
