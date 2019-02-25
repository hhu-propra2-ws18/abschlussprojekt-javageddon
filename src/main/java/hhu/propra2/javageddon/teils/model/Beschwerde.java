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
public class Beschwerde {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Reservierung reservierung;

    private String kommentar;

    @ManyToOne
    private Benutzer nutzer;

    @ManyToOne
    private Benutzer hatRecht;

    private boolean bearbeitet;

}
