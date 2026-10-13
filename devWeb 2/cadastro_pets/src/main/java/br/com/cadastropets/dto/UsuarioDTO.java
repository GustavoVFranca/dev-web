package br.com.cadastropets.dto;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDTO {

    private Integer id;
    private String login;
    private String senha;
    private boolean admin;
    private List<String> empresas = new ArrayList<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }

    public List<String> getEmpresas() { return empresas; }
    public void setEmpresas(List<String> empresas) { this.empresas = empresas; }
}
