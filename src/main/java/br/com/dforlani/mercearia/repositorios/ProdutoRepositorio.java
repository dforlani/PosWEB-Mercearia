package br.com.dforlani.mercearia.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.dforlani.mercearia.models.Produto;

@Repository
public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {

}
