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
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String login;
    private String senha; // guarda o hash, nao a senha pura
    private boolean admin;

    // Usuario comum: 1 empresa. Administrador: varias (sem conflito entre si)
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Empresa> empresas = new ArrayList<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }

    public List<Empresa> getEmpresas() { return empresas; }
    public void setEmpresas(List<Empresa> empresas) { this.empresas = empresas; }
}
