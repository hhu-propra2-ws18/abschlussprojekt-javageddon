package hhu.propra2.javageddon.teils.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Artikel {
    @Id
    @GeneratedValue
    private Long id;

    private String titel;

    private String beschreibung;

    private int kostenTag;

    private int kaution;

    private boolean aktiv;

    private boolean verfügbar;

    /*
    TODO:
    - Liste an Strings für die Links zu den Bildern einfügen
    - Feld für den Besitzer vom Typ Benutzer
    - Und eine Artikel Adresse
     */






}
