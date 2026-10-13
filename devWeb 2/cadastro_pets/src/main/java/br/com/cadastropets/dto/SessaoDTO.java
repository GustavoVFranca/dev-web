package br.com.cadastropets.dto;

public class SessaoDTO {

    private String login;
    private String empresa;
    private boolean admin;

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
}
