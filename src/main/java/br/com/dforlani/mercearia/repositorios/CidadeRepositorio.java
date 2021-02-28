package br.com.dforlani.mercearia.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.dforlani.mercearia.models.Cidade;

public interface CidadeRepositorio extends JpaRepository<Cidade, Long> {

	@Query("select c from Cidade c where lower(c.nome) like lower(concat(:termo, '%'))")
	List<Cidade> searchByNome(@Param("termo") String termo);
}
