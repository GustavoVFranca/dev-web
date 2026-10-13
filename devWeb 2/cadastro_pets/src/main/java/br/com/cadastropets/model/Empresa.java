package br.com.cadastropets.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String nome;

    // Empresas com as quais esta tem conflito de interesses
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Empresa> conflitos = new ArrayList<>();

    // O conflito vale nos dois sentidos: se A lista B, B tambem conflita com A
    public boolean temConflitoCom(Empresa outra) {
        return conflitos.contains(outra) || outra.getConflitos().contains(this);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Empresa> getConflitos() { return conflitos; }
    public void setConflitos(List<Empresa> conflitos) { this.conflitos = conflitos; }
}
