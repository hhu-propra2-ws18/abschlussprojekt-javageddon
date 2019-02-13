package hhu.propra2.javageddon.teils.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Embeddable
public class Adresse {

    private String hausnummer;

    private String strasse;

    private String ort;

    private int plz;
}
