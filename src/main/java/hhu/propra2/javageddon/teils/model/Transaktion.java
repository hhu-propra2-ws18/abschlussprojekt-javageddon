package hhu.propra2.javageddon.teils.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Transaktion {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Benutzer kontoinhaber;

    private LocalDate datum;

    private String verwendungszweck;

    private int betrag;
}
