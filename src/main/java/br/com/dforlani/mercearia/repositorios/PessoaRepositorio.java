package br.com.dforlani.mercearia.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.dforlani.mercearia.models.Pessoa;

@Repository
public interface PessoaRepositorio extends JpaRepository<Pessoa, Long> {

    @Query("select d from Pessoa d where lower(d.nome) like lower(concat(:termo, '%'))")
	List<Pessoa> searchByNome(@Param("termo") String termo);
}
