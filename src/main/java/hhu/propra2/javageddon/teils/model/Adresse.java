package hhu.propra2.javageddon.teils.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Adresse {

    private String hausnummer;

    private String strasse;

    private String ort;

    private int plz;
}
