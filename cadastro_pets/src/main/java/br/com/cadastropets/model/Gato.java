package br.com.cadastropets.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("GATO")
public class Gato extends Pet {

    @Override
    public String emitirSom() {
        return "Miau!";
    }
}
