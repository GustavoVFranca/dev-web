package br.com.cadastropets.rest;

import br.com.cadastropets.dto.PetDTO;
import br.com.cadastropets.model.Cachorro;
import br.com.cadastropets.model.Coelho;
import br.com.cadastropets.model.Gato;
import br.com.cadastropets.model.Pet;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("pets")
public class PetResource {

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("petPU");

    @POST
    @Path("{tipo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PetDTO cadastrar(@PathParam("tipo") String tipo, PetDTO dto) {
        Pet pet = criarPetPorTipo(tipo);

        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new WebApplicationException("O nome do pet e obrigatorio", Response.Status.BAD_REQUEST);
        }

        pet.setNome(dto.getNome());

        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(pet);
            em.getTransaction().commit();
            return paraDTO(pet);
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PetDTO> listar() {
        EntityManager em = EMF.createEntityManager();
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
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            Pet pet = em.find(Pet.class, id);
            if (pet != null) {
                em.remove(pet);
            }
            em.getTransaction().commit();
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
        return dto;
    }
}
