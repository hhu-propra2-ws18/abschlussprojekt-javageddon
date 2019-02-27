package hhu.propra2.javageddon.teils.model;

import lombok.Data;

@Data
public class Ueberweisung {
    private double betrag;
    private ProPayUser kontoinhaber;
    private ProPayUser zielKonto;
}


