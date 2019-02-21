package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Beschwerde {
    @Id
    @GeneratedValue
    private long id;

    private Reservierung reservierung;

    private String kommentar;

    private Benutzer nutzer;

    private Benutzer hatRecht;

    private boolean bearbeitet;
}
