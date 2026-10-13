package br.com.cadastropets.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

public class Sessao {

    // Guarda na sessao quem logou e em qual empresa
    public static void iniciar(HttpServletRequest req, Integer usuarioId, Integer empresaId, boolean admin) {
        HttpSession s = req.getSession(true);
        s.setAttribute("usuarioId", usuarioId);
        s.setAttribute("empresaId", empresaId);
        s.setAttribute("admin", admin);
    }

    public static void encerrar(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s != null) {
            s.invalidate();
        }
    }

    // Exige login. Devolve o id da empresa em que o usuario logou
    public static Integer empresaId(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("empresaId") == null) {
            throw Erro.de(Response.Status.UNAUTHORIZED, "Faca login primeiro");
        }
        return (Integer) s.getAttribute("empresaId");
    }

    public static Integer usuarioId(HttpServletRequest req) {
        empresaId(req); // garante que esta logado
        return (Integer) req.getSession(false).getAttribute("usuarioId");
    }

    // Exige login E que o usuario seja administrador
    public static Integer exigirAdmin(HttpServletRequest req) {
        Integer id = usuarioId(req);
        Boolean admin = (Boolean) req.getSession(false).getAttribute("admin");
        if (admin == null || !admin) {
            throw Erro.de(Response.Status.FORBIDDEN, "Apenas administradores");
        }
        return id;
    }
}
