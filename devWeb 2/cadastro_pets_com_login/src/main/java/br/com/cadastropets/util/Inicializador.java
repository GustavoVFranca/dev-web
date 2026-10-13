package br.com.cadastropets.util;

import br.com.cadastropets.model.Empresa;
import br.com.cadastropets.model.Usuario;
import javax.persistence.EntityManager;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

// Roda quando o sistema sobe: cria as tabelas e, se estiver vazio, insere dados de teste.
// (As empresas nao tem tela de cadastro: sao mantidas direto no banco.)
@WebListener
public class Inicializador implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        EntityManager em = Banco.EMF.createEntityManager();
        try {
            long total = em.createQuery("SELECT COUNT(e) FROM Empresa e", Long.class).getSingleResult();
            if (total > 0) {
                return;
            }

            Empresa a = empresa("Empresa A");
            Empresa b = empresa("Empresa B");
            Empresa c = empresa("Empresa C");
            a.getConflitos().add(b); // A e B tem conflito de interesses

            Usuario admin = usuario("admin", true, a, c); // administra A e C (sem conflito)
            Usuario joao = usuario("joao", false, a);
            Usuario maria = usuario("maria", false, b);

            em.getTransaction().begin();
            em.persist(a);
            em.persist(b);
            em.persist(c);
            em.persist(admin);
            em.persist(joao);
            em.persist(maria);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private Empresa empresa(String nome) {
        Empresa e = new Empresa();
        e.setNome(nome);
        return e;
    }

    private Usuario usuario(String login, boolean admin, Empresa... empresas) {
        Usuario u = new Usuario();
        u.setLogin(login);
        u.setSenha(Senha.hash("123"));
        u.setAdmin(admin);
        for (Empresa e : empresas) {
            u.getEmpresas().add(e);
        }
        return u;
    }
}
