package br.com.cadastropets.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Banco {

    // Uma unica fabrica para o sistema todo
    public static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("petPU");
}
