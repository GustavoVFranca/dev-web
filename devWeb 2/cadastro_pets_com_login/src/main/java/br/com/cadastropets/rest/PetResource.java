package br.com.cadastropets.rest;

import br.com.cadastropets.dto.PetDTO;
import br.com.cadastropets.model.Cachorro;
import br.com.cadastropets.model.Coelho;
import br.com.cadastropets.model.Empresa;
import br.com.cadastropets.model.Gato;
import br.com.cadastropets.model.Pet;
import br.com.cadastropets.util.Banco;
import br.com.cadastropets.util.Erro;
import br.com.cadastropets.util.Sessao;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("pets")
public class PetResource {

    @Context
    private HttpServletRequest request;

    @POST
    @Path("{tipo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PetDTO cadastrar(@PathParam("tipo") String tipo, PetDTO dto) {
        Integer empresaId = Sessao.empresaId(request); // exige login

        Pet pet = criarPetPorTipo(tipo);

        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new WebApplicationException("O nome do pet e obrigatorio", Response.Status.BAD_REQUEST);
        }

        pet.setNome(dto.getNome());

        EntityManager em = Banco.EMF.createEntityManager();
        try {
            // A empresa vem da sessao, nunca do que o navegador enviou
            pet.setEmpresa(em.find(Empresa.class, empresaId));

            em.getTransaction().begin();
            em.persist(pet);
            em.getTransaction().commit();
            return paraDTO(pet);
        } finally {
            em.close();
        }
    }

    // Lista so os pets da empresa do usuario logado
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PetDTO> listar() {
        Integer empresaId = Sessao.empresaId(request);

        EntityManager em = Banco.EMF.createEntityManager();
        try {
            List<Pet> pets = em.createQuery("SELECT p FROM Pet p WHERE p.empresa.id = :empresa ORDER BY p.id", Pet.class)
                    .setParameter("empresa", empresaId)
                    .getResultList();
            List<PetDTO> resultado = new ArrayList<>();
            for (Pet pet : pets) {
                resultado.add(paraDTO(pet));
            }
            return resultado;
        } finally {
            em.close();
        }
    }

    // Pagina publica: todos os pets de todas as empresas, sem exigir login
    @GET
    @Path("publico")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PetDTO> listarPublico() {
        EntityManager em = Banco.EMF.createEntityManager();
        try {
            List<Pet> pets = em.createQuery("SELECT p FROM Pet p ORDER BY p.id", Pet.class).getResultList();
            List<PetDTO> resultado = new ArrayList<>();
            for (Pet pet : pets) {
                resultado.add(paraDTO(pet));
            }
            return resultado;
        } finally {
            em.close();
        }
    }

    @DELETE
    @Path("{id}")
    public void remover(@PathParam("id") Integer id) {
        Integer empresaId = Sessao.empresaId(request);

        EntityManager em = Banco.EMF.createEntityManager();
        try {
            Pet pet = em.find(Pet.class, id);
            if (pet != null) {
                // So pode remover pet da propria empresa
                if (pet.getEmpresa() == null || !pet.getEmpresa().getId().equals(empresaId)) {
                    throw Erro.de(Response.Status.FORBIDDEN, "Esse pet nao e da sua empresa");
                }
                em.getTransaction().begin();
                em.remove(pet);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }
    }

    private Pet criarPetPorTipo(String tipo) {
        switch (tipo.toLowerCase()) {
            case "cachorro":
                return new Cachorro();
            case "gato":
                return new Gato();
            case "coelho":
                return new Coelho();
            default:
                throw new WebApplicationException(
                        "Tipo invalido: " + tipo + " (use cachorro, gato ou coelho)",
                        Response.Status.BAD_REQUEST);
        }
    }

    private PetDTO paraDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setNome(pet.getNome());
        dto.setTipo(pet.getClass().getSimpleName());
        dto.setSom(pet.emitirSom());
        if (pet.getEmpresa() != null) {
            dto.setEmpresa(pet.getEmpresa().getNome());
        }
        return dto;
    }
}
