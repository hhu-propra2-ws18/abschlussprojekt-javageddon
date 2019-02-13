package hhu.propra2.javageddon.teils.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

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

    private List<String> foto;

    private Benutzer eigentuemer;

    /*
    TODO:
    - Und eine Artikel Adresse
     */






}
