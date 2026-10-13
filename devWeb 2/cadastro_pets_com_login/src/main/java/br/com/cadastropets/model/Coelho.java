package br.com.cadastropets.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("COELHO")
public class Coelho extends Pet {

    @Override
    public String emitirSom() {
        return "Miii!";
    }
}
