package br.com.dforlani.mercearia.models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "O campo nome do produto não pode ser vazio")
    @Column(nullable = false)
    private String nome;

    // @NotBlank(message = "O campo valor não pode ser vazio")
    // @NegativeOrZero(message = "O campo valor não pode ser zero ou negativo")
    @Column(nullable = false)
    private BigDecimal valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Produto [nome=" + nome + "]";
    }

    public Produto(@NotBlank(message = "O campo nome do produto não pode ser vazio") String nome,
            @NotBlank(message = "O campo valor não pode ser vazio") BigDecimal valor) {
        this.nome = nome;
        this.valor = valor;
    }

    @Deprecated
    public Produto(){}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Produto other = (Produto) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
