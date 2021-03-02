package br.com.dforlani.mercearia.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dforlani.mercearia.models.PedidoItem;

public interface PedidoItemRepositorio extends JpaRepository<PedidoItem, Long> {

}
