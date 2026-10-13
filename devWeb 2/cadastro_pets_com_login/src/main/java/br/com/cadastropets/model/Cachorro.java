package br.com.cadastropets.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CACHORRO")
public class Cachorro extends Pet {

    @Override
    public String emitirSom() {
        return "Au au!";
    }
}
