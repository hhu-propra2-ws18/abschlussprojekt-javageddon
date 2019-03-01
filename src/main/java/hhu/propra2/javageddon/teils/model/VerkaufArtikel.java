package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class VerkaufArtikel {
    @Id
    @GeneratedValue
    private long id;

    @Size(max = 255, message = "Der Titel darf maximal 255 Zeichen enthalten")
    @NotEmpty(message = "Der Artikel muss einen Artikel haben")
    private String titel;

    @Size(max = 255, message = "Die Beschreibung darf maximal 255 Zeichen enthalten")
    @NotEmpty(message = "Der Artikel muss eine Beschreibung haben")
    private String beschreibung;

    @Positive(message = "Der Artikel muss einen positiven Preis besitzen")
    private int verkaufsPreis;

    private boolean verfuegbar;

    private ArrayList<String> fotos;

    @ManyToOne
    private Benutzer eigentuemer;

    @Embedded
    private Adresse standort;
}
