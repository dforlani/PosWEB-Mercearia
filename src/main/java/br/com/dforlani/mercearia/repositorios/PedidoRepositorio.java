package br.com.dforlani.mercearia.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.dforlani.mercearia.models.Pedido;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {

}
