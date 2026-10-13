package br.com.cadastropets.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Erro {

    // Cria um erro HTTP com a mensagem em texto, para a tela mostrar
    public static WebApplicationException de(Response.Status status, String mensagem) {
        Response r = Response.status(status).entity(mensagem).type(MediaType.TEXT_PLAIN).build();
        return new WebApplicationException(r);
    }
}
