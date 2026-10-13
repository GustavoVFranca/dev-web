package br.com.cadastropets.rest;

import br.com.cadastropets.dto.LoginDTO;
import br.com.cadastropets.dto.SessaoDTO;
import br.com.cadastropets.model.Empresa;
import br.com.cadastropets.model.Usuario;
import br.com.cadastropets.util.Banco;
import br.com.cadastropets.util.Erro;
import br.com.cadastropets.util.Senha;
import br.com.cadastropets.util.Sessao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("auth")
public class AuthResource {

    @Context
    private HttpServletRequest request;

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SessaoDTO login(LoginDTO dto) {
        if (dto.getEmpresa() == null || dto.getLogin() == null || dto.getSenha() == null) {
            throw Erro.de(Response.Status.BAD_REQUEST, "Informe empresa, usuario e senha");
        }

        EntityManager em = Banco.EMF.createEntityManager();
        try {
            // O usuario precisa existir, ter essa senha e pertencer a empresa informada
            List<Usuario> achados = em.createQuery(
                    "SELECT u FROM Usuario u JOIN u.empresas e "
                    + "WHERE u.login = :login AND u.senha = :senha AND e.nome = :empresa", Usuario.class)
                    .setParameter("login", dto.getLogin())
                    .setParameter("senha", Senha.hash(dto.getSenha()))
                    .setParameter("empresa", dto.getEmpresa())
                    .getResultList();

            if (achados.isEmpty()) {
                throw Erro.de(Response.Status.UNAUTHORIZED, "Empresa, usuario ou senha invalidos");
            }

            Usuario usuario = achados.get(0);
            Empresa empresa = null;
            for (Empresa e : usuario.getEmpresas()) {
                if (e.getNome().equals(dto.getEmpresa())) {
                    empresa = e;
                }
            }

            // CWP: administrador nao pode administrar empresas com conflito de interesses
            if (usuario.isAdmin()) {
                List<Empresa> todas = usuario.getEmpresas();
                for (int i = 0; i < todas.size(); i++) {
                    for (int j = i + 1; j < todas.size(); j++) {
                        if (todas.get(i).temConflitoCom(todas.get(j))) {
                            throw Erro.de(Response.Status.FORBIDDEN, "Conflito de interesses entre "
                                    + todas.get(i).getNome() + " e " + todas.get(j).getNome());
                        }
                    }
                }
            }

            Sessao.iniciar(request, usuario.getId(), empresa.getId(), usuario.isAdmin());
            return montar(usuario.getLogin(), empresa.getNome(), usuario.isAdmin());
        } finally {
            em.close();
        }
    }

    // A tela usa isso para saber se ja existe alguem logado
    @GET
    @Path("eu")
    @Produces(MediaType.APPLICATION_JSON)
    public SessaoDTO eu() {
        Integer usuarioId = Sessao.usuarioId(request);
        Integer empresaId = Sessao.empresaId(request);

        EntityManager em = Banco.EMF.createEntityManager();
        try {
            Usuario u = em.find(Usuario.class, usuarioId);
            Empresa e = em.find(Empresa.class, empresaId);
            return montar(u.getLogin(), e.getNome(), u.isAdmin());
        } finally {
            em.close();
        }
    }

    @POST
    @Path("logout")
    public void logout() {
        Sessao.encerrar(request);
    }

    private SessaoDTO montar(String login, String empresa, boolean admin) {
        SessaoDTO s = new SessaoDTO();
        s.setLogin(login);
        s.setEmpresa(empresa);
        s.setAdmin(admin);
        return s;
    }
}
