package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Artikel {
    @Id
    @GeneratedValue
    private long id;

    @NotEmpty(message = "Der Artikel muss einen Artikel haben")
    private String titel;

    @NotEmpty(message = "Der Artikel muss eine Beschreibung haben")
    private String beschreibung;

    @Min(value=1, message = "Der Artikel muss einen Preis pro Tag haben")
    private int kostenTag;

    @Min(value=1, message = "Der Artikel muss eine Kaution haben")
    private int kaution;

    private boolean aktiv;

    private boolean verfuegbar;

    private ArrayList<String> fotos;

    @ManyToOne
    private Benutzer eigentuemer;

    @Embedded
    private Adresse standort;


}
