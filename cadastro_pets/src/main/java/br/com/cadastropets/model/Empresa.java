/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.cadastropets.model;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
/**
 *
 * @author gutavo
 */
@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String nome;

    @ManyToMany
    private List<Empresa> conflitos;

    public Integer getId() { return id; }
    public void setId(Integer id) {
        this.id = id; 
    }

    public String getNome() { return nome; }
    public void setNome(String nome) {
        this.nome = nome; 
    }

    public List<Empresa> getConflitos() {
        return conflitos; 
    }
    public void setConflitos(List<Empresa> conflitos) {
        this.conflitos = conflitos; 
    }
}
