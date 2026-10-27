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
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String login;
    private String senha;
    private boolean admin;

    @ManyToMany
    private List<Empresa> empresas;

    public Integer getId() {
        return id; 
    }
    public void setId(Integer id) {
        this.id = id; 
    }

    public String getLogin() {
        return login; 
    }
    public void setLogin(String login) {
        this.login = login; 
    }

    public String getSenha() { return senha; }
    public void setSenha(String senha) {
        this.senha = senha; 
    }

    public boolean isAdmin() {
        return admin; 
    }
    public void setAdmin(boolean admin) {
        this.admin = admin; 
    }

    public List<Empresa> getEmpresas() { return empresas; }
    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas; 
    }
}
