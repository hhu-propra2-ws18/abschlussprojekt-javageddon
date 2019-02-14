package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private boolean verfuegbar;

    @ElementCollection
    private List<String> foto;

    @ManyToOne
    private Benutzer eigentuemer;

    @Embedded
    private Adresse adresse;

}
