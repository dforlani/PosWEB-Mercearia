package br.com.dforlani.mercearia;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
import br.com.dforlani.mercearia.models.PedidoItem;
import br.com.dforlani.mercearia.models.Pessoa;
import br.com.dforlani.mercearia.models.Produto;
import br.com.dforlani.mercearia.repositorios.CidadeRepositorio;
import br.com.dforlani.mercearia.repositorios.DepartamentoRepositorio;
import br.com.dforlani.mercearia.repositorios.PedidoItemRepositorio;
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
	@Autowired
	private PedidoItemRepositorio pedidoItemRep;
	
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
		Produto prodAux = new Produto("Coca-Cola", new BigDecimal(1.89));
		produtoRep.save(prodAux);

		prodAux = new Produto("Farinha Anaconda", new BigDecimal(6.98));
		produtoRep.save(prodAux);

		prodAux = new Produto("Barra de Chocolate Nestlê",new BigDecimal( 7.02));
		produtoRep.save(prodAux);

		prodAux = new Produto("Sabonete", new BigDecimal(2.35));
		produtoRep.save(prodAux);
		produtoRep.flush();
		

	
		//PEDIDO 1
		Pedido pedido = new Pedido(LocalDateTime.of(1990, 12, 1, 9, 10, 55));	
		pedido.setPessoa(p2);			
		pedidoRep.save(pedido);
		//INCLUSÃO DE ITEM
		PedidoItem item1 = new PedidoItem();
		item1.setPedido(pedido);
		item1.setProduto(produtoRep.findAll().get(0));
		item1.setQuantidade(4);
		pedidoItemRep.save(item1);

		//PEDIDO 2
		pedido = new Pedido(LocalDateTime.of(2020, 2, 4, 15, 6, 7));	
		pedido.setPessoa(p1);		
		pedidoRep.save(pedido);
		//INCLUSÃO DE ITEM 2
		PedidoItem item2 = new PedidoItem();
		item2.setPedido(pedido);
		item2.setProduto(produtoRep.findAll().get(0));
		item2.setQuantidade(10);
		pedidoItemRep.save(item2);

		//PEDIDO 3
		pedido = new Pedido(LocalDateTime.of(2018, 4, 5, 5, 8, 9));	
		pedido.setPessoa(p1);			
		pedidoRep.save(pedido);
		//INCLUSÃO DE ITEM 3
		PedidoItem item3 = new PedidoItem();
		item3.setPedido(pedido);
		item3.setProduto(produtoRep.findAll().get(0));
		item3.setQuantidade(10);		
		pedidoItemRep.save(item3);



	}
}
