package br.com.dforlani.mercearia.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import br.com.dforlani.mercearia.models.Produto;

@Repository
public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {
    @Query("select d from Produto d where lower(d.nome) like lower(concat(:termo, '%'))")
	List<Produto> searchByNome(@Param("termo") String termo);
}

