package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Verkauf {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "VERKAUF_BENUTZER_ID")
    private Benutzer kaeufer;

    @ManyToOne
    @JoinColumn(name = "VERKAUF_ARTIKEL_ID")
    private VerkaufArtikel artikel;

    @Builder.Default
    private Boolean bearbeitet = false;

    @Builder.Default
    private Boolean akzeptiert = false;

    public int ermittleStatus(){
        if (!bearbeitet) return 1;           // Anfrage in Bearbeitung
        if (bearbeitet && !akzeptiert) return 2;                              // Anfrage abgelehnt
        if (akzeptiert ) return 3;   // Anfrage akzeptiert
        return -1; // Zustand tritt nie ein
    }
}
