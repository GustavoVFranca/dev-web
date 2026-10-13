package br.com.cadastropets.rest;

import br.com.cadastropets.dto.UsuarioDTO;
import br.com.cadastropets.model.Empresa;
import br.com.cadastropets.model.Usuario;
import br.com.cadastropets.util.Banco;
import br.com.cadastropets.util.Erro;
import br.com.cadastropets.util.Senha;
import br.com.cadastropets.util.Sessao;
import java.util.ArrayList;
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

@Path("usuarios")
public class UsuarioResource {

    @Context
    private HttpServletRequest request;

    // Empresas que o administrador logado administra (para o formulario)
    @GET
    @Path("empresas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> minhasEmpresas() {
        Integer adminId = Sessao.exigirAdmin(request);
        EntityManager em = Banco.EMF.createEntityManager();
        try {
            List<String> nomes = new ArrayList<>();
            for (Empresa e : em.find(Usuario.class, adminId).getEmpresas()) {
                nomes.add(e.getNome());
            }
            return nomes;
        } finally {
            em.close();
        }
    }

    // Lista so os usuarios das empresas que o administrador administra
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UsuarioDTO> listar() {
        Integer adminId = Sessao.exigirAdmin(request);
        EntityManager em = Banco.EMF.createEntityManager();
        try {
            List<Integer> ids = new ArrayList<>();
            for (Empresa e : em.find(Usuario.class, adminId).getEmpresas()) {
                ids.add(e.getId());
            }

            List<Usuario> usuarios = em.createQuery(
                    "SELECT DISTINCT u FROM Usuario u JOIN u.empresas e WHERE e.id IN :ids ORDER BY u.id",
                    Usuario.class).setParameter("ids", ids).getResultList();

            List<UsuarioDTO> resultado = new ArrayList<>();
            for (Usuario u : usuarios) {
                resultado.add(paraDTO(u));
            }
            return resultado;
        } finally {
            em.close();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UsuarioDTO cadastrar(UsuarioDTO dto) {
        Integer adminId = Sessao.exigirAdmin(request);

        if (dto.getLogin() == null || dto.getLogin().trim().isEmpty()
                || dto.getSenha() == null || dto.getSenha().isEmpty()) {
            throw Erro.de(Response.Status.BAD_REQUEST, "Login e senha sao obrigatorios");
        }
        if (dto.getEmpresas() == null || dto.getEmpresas().isEmpty()) {
            throw Erro.de(Response.Status.BAD_REQUEST, "Escolha pelo menos uma empresa");
        }

        EntityManager em = Banco.EMF.createEntityManager();
        try {
            Usuario admin = em.find(Usuario.class, adminId);

            // 1) Cada empresa escolhida precisa ser uma das que o admin administra
            List<Empresa> escolhidas = new ArrayList<>();
            for (String nome : dto.getEmpresas()) {
                Empresa achada = null;
                for (Empresa e : admin.getEmpresas()) {
                    if (e.getNome().equals(nome)) {
                        achada = e;
                    }
                }
                if (achada == null) {
                    throw Erro.de(Response.Status.FORBIDDEN, "Voce nao administra a empresa " + nome);
                }
                if (!escolhidas.contains(achada)) {
                    escolhidas.add(achada);
                }
            }

            // 2) Usuario comum so pode ter uma empresa
            if (!dto.isAdmin() && escolhidas.size() > 1) {
                throw Erro.de(Response.Status.BAD_REQUEST, "Usuario comum pertence a apenas uma empresa");
            }

            // 3) Politica da muralha da China: empresas em conflito nao podem ficar com o mesmo usuario
            for (int i = 0; i < escolhidas.size(); i++) {
                for (int j = i + 1; j < escolhidas.size(); j++) {
                    if (escolhidas.get(i).temConflitoCom(escolhidas.get(j))) {
                        throw Erro.de(Response.Status.BAD_REQUEST, "Conflito de interesses entre "
                                + escolhidas.get(i).getNome() + " e " + escolhidas.get(j).getNome());
                    }
                }
            }

            // 4) Login unico no sistema
            Long repetidos = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.login = :login", Long.class)
                    .setParameter("login", dto.getLogin().trim()).getSingleResult();
            if (repetidos > 0) {
                throw Erro.de(Response.Status.CONFLICT, "Esse login ja existe");
            }

            Usuario novo = new Usuario();
            novo.setLogin(dto.getLogin().trim());
            novo.setSenha(Senha.hash(dto.getSenha()));
            novo.setAdmin(dto.isAdmin());
            novo.setEmpresas(escolhidas);

            em.getTransaction().begin();
            em.persist(novo);
            em.getTransaction().commit();
            return paraDTO(novo);
        } finally {
            em.close();
        }
    }

    private UsuarioDTO paraDTO(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(u.getId());
        dto.setLogin(u.getLogin());
        dto.setAdmin(u.isAdmin());
        for (Empresa e : u.getEmpresas()) {
            dto.getEmpresas().add(e.getNome());
        }
        return dto;
    }
}
